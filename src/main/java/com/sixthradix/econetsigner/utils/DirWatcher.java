package com.sixthradix.econetsigner.utils;

import com.sixthradix.econetsigner.dtos.SignedInvoiceResponse;
import com.sixthradix.econetsigner.services.SigningService;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DirWatcher implements FileAlterationListener {
    Logger logger = LoggerFactory.getLogger(DirWatcher.class);
    private final FileManager fileManager = new FileManager();

    private final SigningService applicationService;
    private final JSON2Text converter = new JSON2Text();


    public DirWatcher(SigningService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
    }

    @Override
    public void onDirectoryCreate(File file) {

    }

    @Override
    public void onDirectoryChange(File file) {

    }

    @Override
    public void onDirectoryDelete(File file) {

    }

    @Override
    public void onFileCreate(File file) {
        logger.info("File created: ".concat(file.getAbsolutePath()));
        try {
            List<String> signedInvoiceData = fileManager.readTextFile(file, false);

            JSONObject jsonObject = converter.toJSON(signedInvoiceData);
            SignedInvoiceResponse response = new SignedInvoiceResponse();
            response.setCurrency(JSON2Text.CURRENCY);
            response.setInvoiceNumber(JSON2Text.INVOICE_NUMBER);
            response.setBPN(JSON2Text.BPN);
            response.setInvoiceAMT(JSON2Text.INVOICE_AMOUNT);
            response.setSignature(JSON2Text.SIGNATURE);

            String callbackUrl = jsonObject.getString(JSON2Text.CALLBACK_URL);

//            send response to callback url
            applicationService.sendResponse(callbackUrl, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onFileChange(File file) {
        logger.info("File changed: ".concat(file.getAbsolutePath()));
    }

    @Override
    public void onFileDelete(File file) {

    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
    }
}
