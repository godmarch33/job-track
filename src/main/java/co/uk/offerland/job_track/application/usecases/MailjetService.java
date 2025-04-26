package co.uk.offerland.job_track.application.usecases;

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
        this.webClient = builder.baseUrl("https://api.mailjet.com/v3.1")
                .defaultHeaders(http -> http.setBasicAuth("df9b7be4be7af756c44fbd269b457519", "fa7b45780e90b456b4f074d40ad04f80"))
                .build();
    }

    public Mono<Void> sendInterviewReminderEmail(User user, List<Phase> phases) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Cannot send interview reminder: user or user email is null/empty.");
            return Mono.empty();
        }

        log.debug("Sending interview reminder email to user: {}", user.getEmail());

        var body = generateEmailBody(phases);
        log.debug("Generated email body: {}", body);

        Map<String, Object> message = Map.of(
                "Messages", List.of(Map.of(
                        "From", Map.of("Email", "noreply@xpatjob.co.uk", "Name", "Offerland Career Assistant"),
                        "To", List.of(Map.of("Email", user.getEmail(), "Name", user.getFullName())),
                        "Subject", "Interview Reminder",
                        "HTMLPart", body
                ))
        );

        log.debug("Prepared message for email sending: {}", message);

        return webClient.post()
                .uri("/send")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.debug("Email sent successfully to user: {}. Response: {}", user.getEmail(), response))
                .doOnError(e -> log.error("Failed to send email to user: {}", user.getEmail(), e))
                .onErrorResume(e -> Mono.empty())
                .then();
    }

    private String generateEmailBody(List<Phase> phases) {
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
