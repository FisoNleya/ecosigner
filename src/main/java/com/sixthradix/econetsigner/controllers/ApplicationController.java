package com.sixthradix.econetsigner.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixthradix.econetsigner.dtos.Bill;
import com.sixthradix.econetsigner.dtos.MessageResponse;
import com.sixthradix.econetsigner.dtos.SignedInvoiceResponse;
import com.sixthradix.econetsigner.utils.FileManager;
import com.sixthradix.econetsigner.utils.JSON2Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ApplicationController {

    @Value("${app.ESDOutputFolder}")
    private String ESDOutputFolder;

    @Value("${app.sourceFolder}")
    private String sourceFolder;

    @Value("${app.wait}")
    private int tries;

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final FileManager fileManager = new FileManager();
    private final JSON2Text converter = new JSON2Text();

    @PostMapping("/sign_async")
    public ResponseEntity<MessageResponse> sign(@RequestParam String callBackUrl, @Valid @RequestBody Bill billRequest){
        //Convert payload to .txt and set to ESD folder
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(billRequest);
            JSONObject jsonObj = new JSONObject(jsonStr);
            jsonObj.put(JSON2Text.CALLBACK_URL, callBackUrl); //append callback to file to later processing
            List<String> invoiceData = new JSON2Text().convert(jsonObj);
            File file = new File(ESDOutputFolder);
            if(file.exists() && file.isDirectory()){
                String filename = jsonObj.getString("InvoiceNumber");
                String filepath = String.format("%s%s%s.txt", ESDOutputFolder, File.separator, filename);
                fileManager.writeToTextFile(invoiceData, filepath);
                logger.info("File write successful");
            }else {
                logger.error("ESD output folder either not set or mis-configured");
            }
        } catch (JsonProcessingException e) {
           logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage().concat(""));
        }
        MessageResponse response = new MessageResponse();
        response.setMessage("Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/sign")
    public ResponseEntity<Object> sign(@Valid @RequestBody Bill billRequest){
        MessageResponse messageResponse = new MessageResponse();

        //Convert payload to .txt and set to ESD folder
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(billRequest);
            JSONObject jsonObj = new JSONObject(jsonStr);
            List<String> invoiceData = new JSON2Text().convert(jsonObj);

            String invoiceNumber = jsonObj.getString("InvoiceNumber");

            File unsignedFilesDir = new File(ESDOutputFolder);
            if(unsignedFilesDir.exists() && unsignedFilesDir.isDirectory()){
                String filepath = String.format("%s%s%s.txt", ESDOutputFolder, File.separator, invoiceNumber);
                fileManager.writeToTextFile(invoiceData, filepath);
                logger.info("File write successful");
            }else {
                logger.error("ESD output folder either not set or mis-configured");
            }

            /*Wait for file to be signed*/
            int count = 0;
            File signedFilesDir = new File(sourceFolder);
            while(count < tries){
                Collection<File> files = FileUtils.listFiles(signedFilesDir, new String[] {"txt"}, false);

                File invoiceFile = files.parallelStream().filter(file -> invoiceNumber.equals(FilenameUtils.getBaseName(file.getAbsolutePath())))
                .findAny()
                .orElse(null);

                if(invoiceFile != null){
                    //Read file to json and respond
                    List<String> signedInvoiceData = fileManager.readTextFile(invoiceFile, false);

                    JSONObject jsonObject = converter.toJSON(signedInvoiceData);
                    SignedInvoiceResponse response = new SignedInvoiceResponse();
                    response.setCurrency(jsonObject.getString(JSON2Text.CURRENCY));
                    response.setInvoiceNumber(jsonObject.getString(JSON2Text.INVOICE_NUMBER));
                    response.setBPN(jsonObject.getString(JSON2Text.BPN));
                    response.setInvoiceAMT(jsonObject.getString(JSON2Text.INVOICE_AMOUNT));
                    response.setSignature(jsonObject.getString(JSON2Text.SIGNATURE));

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                //wait a bit before checking for signed file again
                Thread.sleep(1000);
                count++;
            }

        } catch (JsonProcessingException e) {
           logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage().concat(""));
        }catch (Exception e){
            logger.error(e.getMessage());
        }


        messageResponse.setMessage("Failed to sign invoice, check the ESD");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


}
