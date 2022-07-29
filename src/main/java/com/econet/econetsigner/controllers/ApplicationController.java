package com.econet.econetsigner.controllers;

import com.econet.econetsigner.dtos.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ApplicationController {

    @PostMapping("/sign")
    public ResponseEntity<Stock> sign(@RequestParam String callBackUrl, @RequestBody Stock stockRequest){

        //TODO call logic
       return new ResponseEntity<>(stockRequest, HttpStatus.OK);
    }


}
