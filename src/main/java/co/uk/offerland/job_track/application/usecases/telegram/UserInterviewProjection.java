package co.uk.offerland.job_track.application.usecases.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInterviewProjection {
    private UUID userId;
    private UUID jobId;
    private UUID phaseId;
    private String company;
    private String title;
    private String phaseName;
    private Instant interviewTime;
    private String telegramUserId;
    private List<Integer> notificationReminders;


}
