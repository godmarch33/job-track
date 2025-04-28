package co.uk.offerland.job_track.application.usecases.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class SearchJobService {

    private final WebClient webClient;

    private  static String apiKey = "scp-live-fc4dc4169e1e4a7cbfef4eae69d4cf11";

    public Mono<String> search(String searchUri) {
       log.info("Searching... {}", searchUri);
        return Mono.fromCallable(() ->
                        Jsoup.connect(searchUri)
                                .timeout(3000)
                                .get()
                                .text()
                )
                .subscribeOn(Schedulers.boundedElastic()) // Run blocking call on a separate thread
                .doOnError(error -> log.error("Failed to fetch HTML: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.info("Falling back to Scrapfly for: {}", searchUri);
                    return fallbackToScrapfly(searchUri);
                });
    }

    private Mono<String> fallbackToScrapfly(String searchUri) {
        try {
            String encodedUrl = URLEncoder.encode(searchUri, StandardCharsets.UTF_8);
            URL url = new URL("https://api.scrapfly.io/scrape?url=" + encodedUrl + "&key=" + apiKey + "&render_js=true&asp=true");

            return webClient.get()
                    .uri(url.toURI())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(15))
                    .map(this::processScrapflyResponse)
                    .onErrorResume(e -> {
                        log.error("Error during Scrapfly request: {}", e.getMessage(), e);
                        return Mono.just("Error during Scrapfly request");
                    });
        } catch (Exception e) {
            log.error("Failed to construct Scrapfly URL: {}", e.getMessage(), e);
            return Mono.just("Failed to construct Scrapfly URL");
        }
    }

    private String processScrapflyResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject resultObject = jsonResponse.getJSONObject("result");
            String html = resultObject.getString("content");
            Document doc = Jsoup.parse(html);
            String plainText = doc.text();
            log.info("Scrapfly fetched plainText: {}", plainText);
            return plainText;
        } catch (Exception e) {
            log.error("Error processing Scrapfly response: {}", e.getMessage(), e);
            return "Error processing response";
        }
    }

//    public Mono<String> search(String searchUri) {
//        return Mono.fromCallable(() ->
//                        Jsoup.connect(searchUri)
//                                .timeout(3000)
//                                .get()
//                                .text()
//                )
//                .subscribeOn(Schedulers.boundedElastic()) // Run blocking call on a separate thread
//                .doOnError(error -> log.error("Failed to fetch HTML: {}", error.getMessage()))
//                .onErrorResume(error -> {
//                    log.info("Falling back to Scrapfly for: {}", searchUri);
//                    try {
//                        String encodedUrl = URLEncoder.encode(searchUri, StandardCharsets.UTF_8);
//                        URL url = new URL("https://api.scrapfly.io/scrape?url=" + encodedUrl + "&key=" + apiKey + "&render_js=true&asp=true");
//
//                        return webClient.get()
//                                .uri(url.toURI())
//                                .accept(MediaType.APPLICATION_JSON)
//                                .retrieve()
//                                .bodyToMono(String.class)
//                                .map(response -> {
//                                    try {
//                                        JSONObject jsonResponse = new JSONObject(response);
//                                        JSONObject resultObject = jsonResponse.getJSONObject("result");
//                                        String html = resultObject.getString("content");
//                                        Document doc = Jsoup.parse(html);
//                                        String plainText = doc.text();
//                                        log.info("Scrapfly fetched plainText: {}", plainText);
//                                        return plainText;
//                                    } catch (Exception e) {
//                                        log.error("Error processing Scrapfly response: {}", e.getMessage(), e);
//                                        return "Error processing response";
//                                    }
//                                })
//                                .onErrorResume(e -> {
//                                    log.error("Error during Scrapfly request: {}", e.getMessage(), e);
//                                    return Mono.just("Error during Scrapfly request");
//                                });
//                    } catch (Exception e) {
//                        log.error("Failed to construct Scrapfly URL: {}", e.getMessage(), e);
//                        return Mono.just("Failed to construct Scrapfly URL");
//                    }
//                });

}
