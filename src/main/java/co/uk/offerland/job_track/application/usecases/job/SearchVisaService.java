package co.uk.offerland.job_track.application.usecases.job;

import co.uk.offerland.job_track.application.dto.SearchVisaRequest;
import co.uk.offerland.job_track.application.dto.SearchVisaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SearchVisaService {

    private final WebClient webClient;

    @Value("${search.service.url}")
    private String searchServiceUrl;

    public SearchVisaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(searchServiceUrl).build();
    }

    public Mono<SearchVisaResponse> search(String query) {
        return webClient.post()
                .uri("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new SearchVisaRequest(query)), SearchVisaRequest.class)
                .retrieve()
                .bodyToMono(SearchVisaResponse.class);
    }
}