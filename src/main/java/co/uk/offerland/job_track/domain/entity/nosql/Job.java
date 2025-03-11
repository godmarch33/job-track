package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.ContactPerson;
import co.uk.offerland.job_track.domain.entity.JobPhase;
import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Data
public class Job {

    @Field("jobId")
    @Indexed(unique = true)
    private UUID jobId = UUID.randomUUID();
    private String company;
    private String title;
    private String location;
    private String jobUrl;
    private String companyLogo;
    private String notes;
    @Field("uses_phase")
    private List<Phase> phases = new ArrayList<>();
    @Field("available_phase")
    private List<String> availablePhases = new ArrayList<>();
    private ContactPerson contactPerson;
    private String description;
    private String jobSalary;
    private String desiredSalary;
    private Instant createdAt;
    private Instant updatedAt;

    public Job() {
    }

    public void initializeAvailablePhases() {
        this.availablePhases.addAll(Arrays.stream(JobPhase.values())
                .map(JobPhase::getLabel)
                .toList());
    }

    public Phase currentPhase() {
        return phases.stream()
                .filter(e -> e.getStatus() == PhaseStatus.IN_PROGRESS)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("incorrect state phases entity"));
    }

    public void addPhase(Phase phase) {
        this.phases.add(phase);
        removeAvailablePhase(phase.getPhaseName());
    }

    public void removeAvailablePhase(String phaseName) {
        log.info("removing available phase {}", phaseName);
        log.info("before available phases {}", availablePhases);
        availablePhases.remove(phaseName);
        log.info("after available phases {}", availablePhases);
    }

    public Phase nextPhase() {
        int orderIndex = currentPhase().getOrderIndex();

        return phases.stream()
                .filter(e -> e.getOrderIndex() == orderIndex + 1)
                .findFirst()
                .orElse(null);
    }
}
