package co.uk.offerland.job_track.config;

import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class MailjetService {

    private final WebClient webClient;

    public MailjetService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.mailjet.com/v3.1")
                .defaultHeaders(http -> http.setBasicAuth("df9b7be4be7af756c44fbd269b457519", "a7175be51a2569dbda4f316291144c7d"))
                .build();
    }

    public Mono<Void> sendInterviewReminderEmail(User user, List<Phase> phases) {
        String body = generateEmailBody(user, phases);

        Map<String, Object> message = Map.of(
                "Messages", List.of(Map.of(
                        "From", Map.of("Email", "noreply@yourdomain.com", "Name", "Career Assistant"),
                        "To", List.of(Map.of("Email", user.getEmail(), "Name", user.getFullName())),
                        "Subject", "Interview Reminder",
                        "HTMLPart", body
                ))
        );

        return webClient.post()
                .uri("/send")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .then(); // convert response to Mono<Void>
    }

    private String generateEmailBody(User user, List<Phase> phases) {
        StringBuilder sb = new StringBuilder("<h2>Today you have the following interviews:</h2><ul>");
        for (Phase phase : phases) {
            sb.append("<li>")
                    .append(phase.getName())
                    .append(" at ")
                    .append(phase.getInterviewScheduleTime())
                    .append("</li>");
        }
        sb.append("</ul><p>Good luck!</p>");
        return sb.toString();
    }
}
