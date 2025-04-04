package co.uk.offerland.job_track.application.usecases.analytics;

import co.uk.offerland.job_track.application.dto.analytics.AnalyticsResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.channels.FileChannel;

@Service
public class AnalyticsCase {
    public Mono<AnalyticsResponse> getAllAnalytics() {
        return Mono.empty();
    }
}
