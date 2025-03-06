package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.ContactPersonEntity;
import co.uk.offerland.job_track.domain.entity.JobPhase;
import co.uk.offerland.job_track.domain.entity.JobPhaseStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
public class JobEntity {

    @Field("jobId")
    @Indexed(unique = true)
    private UUID jobId = UUID.randomUUID();
    private String company;
    private String title;
    private String location;
    private String link;
    private String companyLogo;
    private String notes;
    @Field("uses_phase")
    private List<JobPhaseEntity> phases = new ArrayList<>();
    @Field("available_phase")
    private List<String> availablePhases = new ArrayList<>();
    private ContactPersonEntity contactPerson;
    private String description;
    private String salary;
    private Instant createdAt;
    private Instant updatedAt;

    public JobEntity() {
        initializeAvailablePhases();
    }

    private void initializeAvailablePhases() {
        this.availablePhases.addAll(Arrays.stream(JobPhase.values())
                .map(JobPhase::getLabel)
                .toList());
    }

    public JobPhaseEntity currentPhase() {
        return phases.stream()
                .filter(e -> e.getStatus() == JobPhaseStatus.IN_PROGRESS)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("incorrect state phases entity"));
    }

    public void removeAvailablePhase(String phaseName) {
        availablePhases.remove(phaseName);
    }

    public void addPhase(JobPhaseEntity phase) {
        this.phases.add(phase);
        removeAvailablePhase(phase.getPhaseName());
    }

    public JobPhaseEntity nextPhase() {
        int orderIndex = currentPhase().getOrderIndex();

        return phases.stream()
                .filter(e -> e.getOrderIndex() == orderIndex + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No next phase found"));
    }
}
