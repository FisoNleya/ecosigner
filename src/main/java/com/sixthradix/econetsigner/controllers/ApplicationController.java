package com.sixthradix.econetsigner.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixthradix.econetsigner.dtos.Bill;
import com.sixthradix.econetsigner.dtos.InvoiceSubmissionResponse;
import com.sixthradix.econetsigner.utils.DirWatcher;
import com.sixthradix.econetsigner.utils.FileManager;
import com.sixthradix.econetsigner.utils.JSON2Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ApplicationController {

    @Value("${app.ESDOutputFolder}")
    private String ESDOutputFolder;

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final FileManager fileManager = new FileManager();

    @PostMapping("/sign")
    public ResponseEntity<InvoiceSubmissionResponse> sign(@RequestParam String callBackUrl, @Valid @RequestBody Bill billRequest){
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
                logger.error("File write successful");
            }else {
                logger.error("ESD output folder either not set or mis-configured");
            }
        } catch (JsonProcessingException e) {
           logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage().concat(""));
        }
        InvoiceSubmissionResponse response = new InvoiceSubmissionResponse();
        response.setMessage("Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
