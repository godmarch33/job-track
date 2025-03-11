package co.uk.offerland.job_track.application.usecases;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GlassdoorScraper {

    public static void main(String[] args) {
        String apiKey = "YOUR_SCRAPFLY_API_KEY";
        String targetUrl = "https://www.glassdoor.com/Salaries/index.htm"; // Example endpoint
        String encodedUrl = URLEncoder.encode(targetUrl, StandardCharsets.UTF_8);

        String scrapflyUrl = String.format("https://api.scrapfly.io/scrape?url=%s&key=%s&render_js=true", encodedUrl, apiKey);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(scrapflyUrl))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Response:");
                System.out.println(response.body());
            } else {
                System.out.println("Failed to fetch data: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
