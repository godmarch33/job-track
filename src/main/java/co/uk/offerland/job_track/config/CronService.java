package co.uk.offerland.job_track.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronService {

    private final InterviewReminderService interviewReminderService;

//    @Scheduled(cron = "0 0 6 * * *") // every day at 6 AM
    @Scheduled(cron = "1 * * * * *") // every minute
    public void sendInterviewReminders() {
        log.info("Sending interview reminders");
        interviewReminderService.sendDailyReminders()
                .subscribe();
    }
}
