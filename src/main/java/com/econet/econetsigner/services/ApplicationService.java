package com.econet.econetsigner.services;

import com.econet.econetsigner.dtos.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final RestTemplate restTemplate;

    public Object sendResponse(String callBackUrl, Response recordResponse) {
        return restTemplate.postForEntity(callBackUrl, recordResponse, Object.class);
    }
}
