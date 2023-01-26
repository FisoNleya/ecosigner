package com.sixthradix.econetsigner.controllers;

import com.sixthradix.econetsigner.dtos.auth.AuthenticatedUserDto;
import com.sixthradix.econetsigner.dtos.reports.UserReportDto;
import com.sixthradix.econetsigner.entities.Report;
import com.sixthradix.econetsigner.security.authentication.AuthenticatedUser;
import com.sixthradix.econetsigner.services.ReportsService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/reports")
    public ResponseEntity<Page<Report>> fetchReports(
            AuthenticatedUser user,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String invoiceNumber,
            @RequestParam(required = false) Boolean statusFailed,
            @Parameter(example = "2022-10-10") @RequestParam(required = false) String startDate,
            @Parameter(example = "2022-11-10") @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size

    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        return new ResponseEntity<>(reportsService
                .fetchReports(userName, invoiceNumber, statusFailed, pageable, startDate, endDate), HttpStatus.OK);

    }

    @GetMapping("/reports/users/{userName}")
    public ResponseEntity<UserReportDto> fetchUserReport(
            @PathVariable String userName,
            @Parameter(example = "2022-10-10") @RequestParam(required = false) String startDate,
            @Parameter(example = "2022-11-10") @RequestParam(required = false) String endDate){
        return new ResponseEntity<>(reportsService.fetchUserReport(userName, startDate, endDate), HttpStatus.OK);
    }
}















