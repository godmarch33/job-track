package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import lombok.Data;

@Data
public class PhaseStatusInfoEntity {

    private PhaseStatus status;
    private PhaseSubStatus subStatus;
    private String nextStageButtonName ;
    private String msgTooltip;
}
