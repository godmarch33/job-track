package co.uk.offerland.job_track.application.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobNoteResponse {
    private String interviewName;
    private String date;
    private String notes;
}
