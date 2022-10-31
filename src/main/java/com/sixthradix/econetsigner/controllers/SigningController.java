package com.sixthradix.econetsigner.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixthradix.econetsigner.dtos.Bill;
import com.sixthradix.econetsigner.dtos.MessageResponse;
import com.sixthradix.econetsigner.dtos.auth.AuthenticatedUser;
import com.sixthradix.econetsigner.services.SigningService;
import com.sixthradix.econetsigner.utils.FileManager;
import com.sixthradix.econetsigner.utils.JSON2Text;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
public class SigningController {

    @Value("${app.ESDOutputFolder}")
    private String ESDOutputFolder;

    @Value("${app.sourceFolder}")
    private String sourceFolder;

    private final ObjectMapper mapper;

    private final FileManager fileManager;


    private final SigningService signingService;

    @Hidden
    @PostMapping("/sign_async")
    public ResponseEntity<MessageResponse> sign(@RequestParam String callBackUrl, @Valid @RequestBody Bill billRequest){
        //Convert payload to .txt and set to ESD folder
        try {

            String jsonStr = mapper.writeValueAsString(billRequest);
            JSONObject jsonObj = new JSONObject(jsonStr);
            jsonObj.put(JSON2Text.CALLBACK_URL, callBackUrl); //append callback to file to later processing
            List<String> invoiceData = new JSON2Text().convert(jsonObj);
            File file = new File(ESDOutputFolder);
            if(file.exists() && file.isDirectory()){
                String filename = jsonObj.getString("InvoiceNumber");
                String filepath = String.format("%s%s%s.txt", ESDOutputFolder, File.separator, filename);
                fileManager.writeToTextFile(invoiceData, filepath);
                log.error("File write successful");
            }else {
                log.error("ESD output folder either not set or mis-configured");
            }
        } catch (JsonProcessingException e) {
           log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage().concat(""));
        }
        MessageResponse response = new MessageResponse();
        response.setMessage("Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/sign")
    public ResponseEntity<Object> sign(@Valid @RequestBody Bill billRequest, AuthenticatedUser user){
        return signingService.signBill(billRequest, user);
    }




}
