package co.uk.offerland.job_track.application.dto.phase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponse {

   private List<PhaseResponse> phases;
}
