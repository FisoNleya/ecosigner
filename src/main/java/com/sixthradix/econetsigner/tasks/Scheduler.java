package com.sixthradix.econetsigner.tasks;

import com.sixthradix.econetsigner.entities.Report;
import com.sixthradix.econetsigner.repositories.ReportRepository;
import com.sixthradix.econetsigner.utils.FileManager;
import com.sixthradix.econetsigner.utils.JSON2Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.sixthradix.econetsigner.services.SigningService.FAIL_STATUS;
import static com.sixthradix.econetsigner.services.SigningService.SUCCESS_STATUS;


@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    @Value("${app.sourceFolder}")
    private String sourceFolder;

    @Value("${app.ESDOutputFolder}")
    private String ESDOutputFolder;

    @Value("${app.failedFolder}")
    private String ESDErrorFolder;

    private final ReportRepository reportRepository;

    private final FileManager fileManager;
    private final JSON2Text converter;

    @Scheduled(cron = "${app.status-cron}")
    public void updateFailedBillStatus() {
        log.info("Checking for failed bills status");

        //get all failed bills
        var failedInvoice = reportRepository.findAllByStatus(FAIL_STATUS);

        //check if each bill is now in output folder
        for(final var invoice: failedInvoice){
            var file = getFile(invoice.getInvoiceNumber());
            if(file.exists()){
                String signature = getInvoiceSignature(file);
                invoice.setSignature(signature);
                updateInvoiceStatus(invoice);
            }
        }
    }

    private void updateInvoiceStatus(Report invoice){
        invoice.setStatus(SUCCESS_STATUS);
        reportRepository.save(invoice);
    }

    private File getFile(String invoiceNumber){
        var unSignedFilesDir = new File(sourceFolder);
        var signedFilesDir = new File(ESDOutputFolder);
        var errorFilesDir = new File(ESDErrorFolder);
        var filepath = String.format("%s%s%s.txt", signedFilesDir, File.separator, invoiceNumber);
        return new File(filepath);
    }

    private String getInvoiceSignature(File file){
        List<String> signedInvoiceData = null;
        try {
            signedInvoiceData = fileManager.readTextFile(file, false);
        } catch (FileNotFoundException e) {
           log.error(e.getMessage());
           return null;
        }
        JSONObject jsonObject = converter.toJSON2(signedInvoiceData);
        return jsonObject.getString(JSON2Text.SIGNATURE);
    }
}
