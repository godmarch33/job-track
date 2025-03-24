package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.ContactPerson;
import co.uk.offerland.job_track.domain.entity.PhaseName;
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
    private InterviewNote notes;
    @Field("uses_phase")
    private List<Phase> phases = new ArrayList<>();
    @Field("available_phase")
    private List<String> availablePhases = new ArrayList<>();
    private List<ContactPerson> contactPersons = new ArrayList<>();
    private String description;
    private String jobSalary;
    private String desiredSalary;
    private String currency;
    private String addType;
    private Instant createdAt;
    private Instant updatedAt;

    public Job() {
    }

    public void initializeAvailablePhases() {
        this.availablePhases.addAll(Arrays.stream(PhaseName.values())
                .map(PhaseName::getLabel)
                .toList());
    }

    public Phase currentPhase() {
        return phases.stream()
                .filter(e -> e.getStatusInfoEntity().getStatus()  == PhaseStatus.IN_PROGRESS)
                .findAny()
                .orElse(phases.getLast());
    }

    public void addPhase(Phase phase) {
        this.phases.add(phase);
        removeAvailablePhase(phase.getPhaseName());
    }

    public void removePhase(Phase phase) {
        this.phases.remove(phase);
        addAvailablePhase(phase.getPhaseName());
    }

    public void addAvailablePhase(String phaseName) {
        availablePhases.add(phaseName);
        log.info("after add available phases {}", availablePhases);
    }

    public void removeAvailablePhase(String phaseName) {
        availablePhases.remove(phaseName);
        log.info("after available phases {}", availablePhases);
    }

    public Phase nextPhase() {
        int orderIndex = currentPhase().getOrderIndex();

        return phases.stream()
                .filter(e -> e.getOrderIndex() == (orderIndex + 1))
                .findFirst()
                .orElse(phases.getLast());
    }
}
