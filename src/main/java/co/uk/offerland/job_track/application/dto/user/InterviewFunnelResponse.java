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
  private int appliedCount;
  private int screeningСallCount;
  private int assessmentTestCount;
  private int firstRoundCount;
  private int secondRoundCount;
  private int thirdRoundCount;
  private int finalRoundCount;
  private int negotiationCount;
  private int acceptedCount;
}
