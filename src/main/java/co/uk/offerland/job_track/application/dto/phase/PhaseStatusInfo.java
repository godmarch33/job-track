package co.uk.offerland.job_track.application.dto.phase;

import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import lombok.Data;

@Data
public class PhaseStatusInfo {

    private String status;
    private String subStatus;
    private String nextStageButtonName ;
    private String msgTooltip ;
}
