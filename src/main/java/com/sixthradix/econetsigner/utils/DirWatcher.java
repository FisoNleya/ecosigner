package com.sixthradix.econetsigner.utils;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DirWatcher implements FileAlterationListener {
    Logger logger = LoggerFactory.getLogger(DirWatcher.class);
    private final FileManager fileManager = new FileManager();

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
            for(String x: signedInvoiceData){
                System.out.println(x);
            }
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
