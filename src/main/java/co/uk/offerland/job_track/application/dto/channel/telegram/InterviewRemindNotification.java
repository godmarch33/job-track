package co.uk.offerland.job_track.application.dto.channel.telegram;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Document(collection = "interviewreminders")
public class InterviewRemindNotification {

    @Id
    private String id;
    @Field("remindId")
    @Indexed(unique = true)
    @Builder.Default
    private UUID remindId = UUID.randomUUID();
    private UUID userId;
    private UUID jobId;
    private UUID phaseId;

    private String company;
    private String title;
    private String phaseName;
    private Instant interviewTime;
    private Instant triggerTime;
    private Integer minutesBefore;
    private String messageTitle;
    private String messageBody;
    private String telegramUserId;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
