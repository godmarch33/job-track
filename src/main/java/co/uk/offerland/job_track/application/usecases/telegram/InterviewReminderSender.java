package co.uk.offerland.job_track.application.usecases.telegram;

import co.uk.offerland.job_track.application.dto.channel.telegram.InterviewRemindNotification;
import co.uk.offerland.job_track.config.TelegramInterviewRemindProducer;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import co.uk.offerland.job_track.infrastructure.persistence.RemindersQueueRepository;
import co.uk.offerland.job_track.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewReminderSender {

    private final UserRepository userRepository;
    private final RemindersQueueRepository remindersQueueRepository;
    private final TelegramInterviewRemindProducer telegramProducer;

    @Scheduled(cron = "0 * * * * *") // every minute
    public void sendDueReminders() {
        log.info("Start cron job:sendDueReminders");
        Instant now = Instant.now();
        Instant windowStart = now.minus(1, ChronoUnit.MINUTES);
        Instant windowEnd = now.plus(48, ChronoUnit.HOURS);

        remindersQueueRepository.findRemindersBetween(windowStart, windowEnd)
                .filter(reminder -> shouldSendReminder(reminder, now))
                .flatMap(this::processReminder, 5)
                .subscribe();
    }

    private boolean shouldSendReminder(InterviewRemindNotification reminder, Instant now) {
        long minutesBefore = ChronoUnit.MINUTES.between(now, reminder.getInterviewTime());
        log.info("minutesBefore: {}, reminder.getMinutesBefore(): {}", minutesBefore, reminder.getMinutesBefore());
        return minutesBefore <= reminder.getMinutesBefore();
    }

    private Mono<Void> processReminder(InterviewRemindNotification reminder) {
        log.info("Sending message to Kafka!!!!!!!");
        return telegramProducer.send(reminder)
                .then(remindersQueueRepository.delete(reminder))
                .then(checkIfLastReminderAndUpdate(reminder))
                .doOnSuccess(unused -> log.info("Reminder sent and processed for phaseId={}, telegramUserId={}", reminder.getPhaseId(), reminder.getTelegramUserId()))
                .doOnError(error -> log.error("Error processing reminder: phaseId={}, telegramUserId={}", reminder.getPhaseId(), reminder.getTelegramUserId(), error));
    }

    private Mono<Void> checkIfLastReminderAndUpdate(InterviewRemindNotification reminder) {
        return remindersQueueRepository.countByUserIdAndJobIdAndPhaseId(
                        reminder.getUserId(),
                        reminder.getJobId(),
                        reminder.getPhaseId()
                )
                .flatMap(count -> {
                    if (count == 0) {
                        log.info("userId: {}, jobId: {}, phaseId: {}", reminder.getUserId(), reminder.getJobId(), reminder.getPhaseId());
                        return userRepository.updateNotificationSend(reminder.getUserId(), reminder.getJobId(), reminder.getPhaseId())
                                .doOnSuccess(unused -> log.info("Updated notificationSend=true for phaseId={}", reminder.getPhaseId()));
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
