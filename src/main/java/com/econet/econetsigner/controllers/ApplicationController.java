package com.econet.econetsigner.controllers;

import com.econet.econetsigner.dtos.Bill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ApplicationController {

    @PostMapping("/sign")
    public ResponseEntity<Bill> sign(@RequestParam String callBackUrl, @Valid @RequestBody Bill billRequest){
        //TODO call logic
       return new ResponseEntity<>(billRequest, HttpStatus.OK);
    }


}
