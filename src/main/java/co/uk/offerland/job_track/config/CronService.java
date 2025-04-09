package co.uk.offerland.job_track.config;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CronService {

    private final InterviewReminderService interviewReminderService;

//    @Scheduled(cron = "0 0 6 * * *") // every day at 6 AM
    @Scheduled(cron = "1 * * * * *") // every minute
    public void sendInterviewReminders() {
        interviewReminderService.sendDailyReminders()
                .subscribe();
    }
}
