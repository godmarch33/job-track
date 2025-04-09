package co.uk.offerland.job_track.config;

import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MailjetService {

    private final WebClient webClient;

    public MailjetService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.mailjet.com/v3")
                .defaultHeaders(http -> http.setBasicAuth("df9b7be4be7af756c44fbd269b457519", "a7175be51a2569dbda4f316291144c7d"))
                .build();
    }

    public Mono<Void> sendInterviewReminderEmail(User user, List<Phase> phases) {
        // Log the input data for debugging purposes
        log.debug("Sending interview reminder email to user: {}", user.getEmail());

        // Generate the email body
        String body = generateEmailBody(user, phases);
        log.debug("Generated email body: {}", body);

        Map<String, Object> message = Map.of(
                "Messages", List.of(Map.of(
                        "From", Map.of("Email", "noreply@xpatjob.co.uk", "Name", "Career Assistant"),
                        "To", List.of(Map.of("Email", user.getEmail(), "Name", user.getFullName())),
                        "Subject", "Interview Reminder",
                        "HTMLPart", body
                ))
        );

        // Log the message content for debugging
        log.debug("Prepared message for email sending: {}", message);

        return webClient.post()
                .uri("/send")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.debug("Email sent to user: {}", user.getEmail()))  // Log after completion
                .onErrorResume(e -> {
                    log.error("Failed to send email to user: {}", user.getEmail(), e);
                    return Mono.empty();  // Handle error gracefully
                })
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
