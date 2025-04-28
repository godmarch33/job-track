package co.uk.offerland.job_track.application.usecases.telegram;


import co.uk.offerland.job_track.application.dto.channel.telegram.InterviewRemindNotification;
import co.uk.offerland.job_track.config.TelegramInterviewRemindProducer;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import co.uk.offerland.job_track.infrastructure.persistence.RemindersQueueRepository;
import co.uk.offerland.job_track.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewReminderScheduler {

    private final UserRepository userRepository;
    private final RemindersQueueRepository remindersQueueRepository;

    @Scheduled(cron = "0 0 */2 * * *")
    public void prepareReminders() {
        log.info("Start cron job:prepareReminders");
        Instant from = Instant.now();
        log.info("time now: {}", from);
        Instant to = from.plus(48, ChronoUnit.HOURS);

        userRepository.findUpcomingInterviewPhasesBetweenDates(from, to)
                .flatMap(interview -> Flux.fromIterable(generateReminders(interview)))
                .collectList()
                .filter(reminders -> !reminders.isEmpty())
                .flatMap(this::bulkUpsertReminders)
                .doOnSuccess(unused -> log.info("Prepared reminders successfully"))
                .doOnError(error -> log.error("Error preparing reminders", error))
                .subscribe();
    }

    private Mono<Void> bulkUpsertReminders(List<InterviewRemindNotification> reminders) {
        return remindersQueueRepository
                .deleteAllById(reminders.stream().map(InterviewRemindNotification::getId).toList())
                .thenMany(remindersQueueRepository.saveAll(reminders))
                .then();
    }

    private List<InterviewRemindNotification> generateReminders(UserInterviewProjection interview) {
        if (interview.getNotificationReminders() == null || interview.getNotificationReminders().isEmpty()) {
            return new ArrayList<>();
        }
        List<InterviewRemindNotification> reminders = new ArrayList<>();
        for (Integer minutesBefore : interview.getNotificationReminders()) {
            Instant triggerAt = interview.getInterviewTime().minus(minutesBefore, ChronoUnit.MINUTES);
            if (triggerAt.isAfter(Instant.now())) {
                Map.Entry<String, String> messageContent = generateMessageContent(interview.getCompany(), interview.getTitle(), minutesBefore);
                InterviewRemindNotification notification = InterviewRemindNotification.builder()
                        .id(generateNotificationId(interview.getPhaseId(), minutesBefore))
                        .jobId(interview.getJobId())
                        .phaseId(interview.getPhaseId())
                        .userId(interview.getUserId())
                        .interviewTime(interview.getInterviewTime())
                        .title(interview.getTitle())
                        .company(interview.getCompany())
                        .triggerTime(triggerAt)
                        .telegramUserId(interview.getTelegramUserId())
                        .phaseName(interview.getPhaseName())
                        .messageTitle(messageContent.getKey())
                        .messageBody(messageContent.getValue())
                        .minutesBefore(minutesBefore)
                        .build();
                reminders.add(notification);
            }
        }
        return reminders;
    }

    private String generateNotificationId(UUID phaseId, int minutesBefore) {
        return phaseId.toString() + "_" + minutesBefore;
    }

    private Map.Entry<String, String> generateMessageContent(String company, String title, Integer minutesBefore) {
        if (minutesBefore >= 2880) { // больше 48 часов
            return Map.entry(
                    "Your interview at " + company + " in 2 days!",
                    "Get ready for your interview for the position of " + title + ". Review the company details and prepare questions."
            );
        } else if (minutesBefore >= 1440) { // больше 24 часов
            return Map.entry(
                    "Your interview at " + company + " is tomorrow!",
                    "Get ready for your interview for the position of " + title + ". Review the company details and prepare questions."
            );
        } else if (minutesBefore >= 120) { // больше 2 часов
            return Map.entry(
                    "Upcoming Interview in a few hours!",
                    "You have an interview for " + title + " at " + company + " soon. Stay calm and review your notes."
            );
        } else if (minutesBefore >= 60) { // 1 час
            return Map.entry(
                    "Interview starts in 1 hour!",
                    "Your interview for " + title + " at " + company + " is almost here. Check your setup and get ready."
            );
        } else if (minutesBefore >= 30) { // 30 минут
            return Map.entry(
                    "30 minutes until your interview",
                    "Final check! Your interview for " + title + " at " + company + " starts in 30 minutes."
            );
        } else if (minutesBefore >= 15) {
            return Map.entry(
                    "Almost time!",
                    "Your interview for " + title + " at " + company + " will start soon. Relax and stay confident!"
            );
        } else {
            return Map.entry(
                    "Interview about to start!",
                    "It's time! Good luck with your interview for " + title + " at " + company + "!"
            );
        }
    }
}
