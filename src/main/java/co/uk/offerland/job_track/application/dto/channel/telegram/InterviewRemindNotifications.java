package co.uk.offerland.job_track.application.dto.channel.telegram;


import lombok.Data;

import java.util.List;

@Data
public class InterviewRemindNotifications {

    private List<InterviewRemindNotification> interviewRemindNotifications;
}
