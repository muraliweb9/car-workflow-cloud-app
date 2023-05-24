package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.RestApiResult;
import com.interview.carworkflowcloud.process.FinaliseBooking;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Validated
@Slf4j
@AllArgsConstructor
public class TestController {

    private final FinaliseBooking finaliseBooking;

    @GetMapping("/testDiscovery")
    public RestApiResult testDiscovery() {

        finaliseBooking.log();
        return RestApiResult.COMPLETED_OK;
    }
}
