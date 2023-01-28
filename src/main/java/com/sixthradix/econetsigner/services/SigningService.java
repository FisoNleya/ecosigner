package com.sixthradix.econetsigner.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixthradix.econetsigner.dtos.Bill;
import com.sixthradix.econetsigner.dtos.MessageResponse;
import com.sixthradix.econetsigner.dtos.SignedInvoiceResponse;
import com.sixthradix.econetsigner.dtos.auth.AuthenticatedUserDto;
import com.sixthradix.econetsigner.dtos.reports.ReportDto;
import com.sixthradix.econetsigner.utils.BillUtils;
import com.sixthradix.econetsigner.utils.FileManager;
import com.sixthradix.econetsigner.utils.JSON2Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigningService {


    public static final String FAILED_CHECK_ESD = "Failed to sign invoice, check the ESD";

    public static final String INVALID_SIGNATURE = "Invalid signature, check the ESD";

    public static final String FAIL_STATUS = "Failed";

    public static final String SUCCESS_STATUS = "Success";

    @Value("${app.ESDOutputFolder}")
    private String ESDOutputFolder;

    @Value("${app.sourceFolder}")
    private String sourceFolder;

    @Value("${app.wait}")
    private int tries;

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final FileManager fileManager;

    private final JSON2Text converter;

    private final ReportsService reportsService;

    public Object sendResponse(String callBackUrl, SignedInvoiceResponse recordResponse) {
        return restTemplate.postForEntity(callBackUrl, recordResponse, Object.class);
    }


    private List<String> getInvoiceData(Bill billRequest) throws JsonProcessingException {
        //shorten invoice number for ESD Device
        billRequest.setInvoiceNumber(BillUtils.shortenInvoiceNumber(billRequest.getInvoiceNumber()));

        String jsonStr = mapper.writeValueAsString(billRequest);
        JSONObject jsonObj = new JSONObject(jsonStr);
        return new JSON2Text().convert(jsonObj);
    }

    public ResponseEntity<Object> signBill(Bill billRequest, AuthenticatedUserDto user) {
        String invoiceNumber = billRequest.getInvoiceNumber();
        MessageResponse messageResponse = new MessageResponse();
        String status = FAIL_STATUS;

        //Convert payload to .txt and set to ESD folder
        try {
            List<String> invoiceData = getInvoiceData(billRequest);

            File unsignedFilesDir = new File(ESDOutputFolder);
            if (unsignedFilesDir.exists() && unsignedFilesDir.isDirectory()) {
                String filepath = String.format("%s%s%s.txt", ESDOutputFolder, File.separator, invoiceNumber);
                fileManager.writeToTextFile(invoiceData, filepath);
                log.error("File write successful");
            } else {
                log.error("ESD output folder either not set or mis-configured");
            }

            /*Wait for file to be signed*/
            int count = 0;
            File signedFilesDir = new File(sourceFolder);
            while (count < tries) {
                Collection<File> files = FileUtils.listFiles(signedFilesDir, new String[]{"txt"}, false);

                File invoiceFile = files.parallelStream().filter(file -> invoiceNumber.equals(FilenameUtils.getBaseName(file.getAbsolutePath())))
                        .findAny()
                        .orElse(null);

                if (invoiceFile != null) {
                    //Read file to json and respond
                    List<String> signedInvoiceData = fileManager.readTextFile(invoiceFile, false);

                    JSONObject jsonObject = converter.toJSON2(signedInvoiceData);
                    SignedInvoiceResponse response = new SignedInvoiceResponse();
                    response.setCurrency(jsonObject.getString(JSON2Text.CURRENCY));
                    response.setInvoiceNumber(jsonObject.getString(JSON2Text.INVOICE_NUMBER));
                    response.setBPN(jsonObject.getString(JSON2Text.BPN));
                    response.setInvoiceAMT(jsonObject.getString(JSON2Text.INVOICE_AMOUNT));
                    response.setSignature(jsonObject.getString(JSON2Text.SIGNATURE));

                    if (!response.getSignature().contains(response.getInvoiceAMT())) {
                        var report = ReportDto.builder()
                                .user(user)
                                .invoiceNumber(invoiceNumber)
                                .status(status)
                                .currency(billRequest.getCurrency())
                                .invoiceAmount(BigDecimal.valueOf(Double.parseDouble(billRequest.getInvoiceAmount())))
                                .invoiceTaxAmount(BigDecimal.valueOf(Double.parseDouble(billRequest.getInvoiceTaxAmount())))
                                .build();
                        reportsService.logReportRecord(report);
                        return new ResponseEntity<>(INVALID_SIGNATURE, HttpStatus.EXPECTATION_FAILED);
                    }

                    status = SUCCESS_STATUS;
                    var report = ReportDto.builder()
                                    .invoiceNumber(invoiceNumber)
                                    .signature(response.getSignature())
                                    .user(user)
                                    .status(status)
                                    .currency(billRequest.getCurrency())
                                    .invoiceAmount(BigDecimal.valueOf(Double.parseDouble(billRequest.getInvoiceAmount())))
                                    .invoiceTaxAmount(BigDecimal.valueOf(Double.parseDouble(billRequest.getInvoiceTaxAmount())))
                                    .build();
                    reportsService.logReportRecord(report);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                //wait a bit before checking for signed file again
                Thread.sleep(1000);
                count++;
            }


        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage().concat(""));
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        var report = ReportDto.builder()
                .user(user)
                .invoiceNumber(invoiceNumber)
                .status(status)
                .currency(billRequest.getCurrency())
                .invoiceAmount(BigDecimal.valueOf(Double.parseDouble(billRequest.getInvoiceAmount())))
                .invoiceTaxAmount(BigDecimal.valueOf(Double.parseDouble(billRequest.getInvoiceTaxAmount())))
                .build();
        reportsService.logReportRecord(report);
        messageResponse.setMessage(FAILED_CHECK_ESD);
        return new ResponseEntity<>(messageResponse, HttpStatus.EXPECTATION_FAILED);
    }


}