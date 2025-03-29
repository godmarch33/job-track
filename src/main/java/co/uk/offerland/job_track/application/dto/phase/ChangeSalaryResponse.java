package co.uk.offerland.job_track.application.dto.phase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSalaryResponse {

    private String amount;
    private String currency;
}
