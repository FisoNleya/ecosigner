package com.sixthradix.econetsigner;

import com.sixthradix.econetsigner.utils.DirWatcher;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class EconetSignerApplication {
	Logger logger = LoggerFactory.getLogger(EconetSignerApplication.class);

	@Value("${app.sourceFolder}")
	private String sourceFolder;

	public static void main(String[] args) {
		SpringApplication.run(EconetSignerApplication.class, args);
	}

	@Bean
	public void startDirWatcher(){
		File file = new File(sourceFolder);
		logger.info("Source folder " + file.getAbsolutePath());

		if(file.exists() && file.isDirectory()){
			FileFilter fileFilter = file1 -> file1.getName().endsWith(".txt"); //process .txt files only
			FileAlterationObserver observer = new FileAlterationObserver(file, fileFilter);

			observer.addListener(new DirWatcher());

			long interval = TimeUnit.SECONDS.toMillis(3);
			FileAlterationMonitor monitor = new FileAlterationMonitor(interval);
			monitor.addObserver(observer);

			try {
				monitor.start();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}else {
			logger.error("Source folder either not set or mis-configured");
		}
	}
}
