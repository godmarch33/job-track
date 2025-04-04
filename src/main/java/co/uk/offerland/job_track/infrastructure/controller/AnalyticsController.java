package co.uk.offerland.job_track.infrastructure.controller;

import co.uk.offerland.job_track.application.dto.analytics.AnalyticsResponse;
import co.uk.offerland.job_track.application.usecases.analytics.AnalyticsCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsCase analyticsCase;

    @GetMapping
    public Mono<ResponseEntity<AnalyticsResponse>> getAnalitics() {
        log.info("Income analytics request");
        return analyticsCase.getAllAnalytics().map(response -> ResponseEntity.ok(response));
    }
}
