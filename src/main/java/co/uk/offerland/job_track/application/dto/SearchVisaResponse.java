package co.uk.offerland.job_track.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SearchVisaResponse(@JsonProperty("matched_text") String matchedText,
                                 double distance) {
}
