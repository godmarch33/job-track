package co.uk.offerland.job_track.infrastructure.converter;

import co.uk.offerland.job_track.application.dto.CompanyVisaSponsorshipDto;
import co.uk.offerland.job_track.application.dto.SearchVisaResponse;
import org.springframework.stereotype.Component;

@Component
public class SearchVisaResponseConverter {

    public CompanyVisaSponsorshipDto toDto(String company, String location, String visaType) {
        return new CompanyVisaSponsorshipDto(
                company,
                location,
                "",
                visaType,
                "",
                ""
        );
    }
}
