package co.uk.offerland.job_track.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewFunnelResponse {

  private DailyProgress dailyProgress;
  private int savedCount;
  private int appliedCount;
  private int interviewCount;
  private int rejectedCount;
  private int declinedCount;
  private int acceptedCount;
}
