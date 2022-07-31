package com.sixthradix.econetsigner.services;

import com.sixthradix.econetsigner.dtos.SignedInvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final RestTemplate restTemplate;

    public Object sendResponse(String callBackUrl, SignedInvoiceResponse recordResponse) {
        return restTemplate.postForEntity(callBackUrl, recordResponse, Object.class);
    }
}
