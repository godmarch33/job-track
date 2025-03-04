package co.uk.offerland.job_track.infrastructure.converter;

import co.uk.offerland.job_track.application.dto.CompanyVisaSponsorshipDto;
import co.uk.offerland.job_track.domain.entity.CompanyVisaSponsorshipEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyVisaSponsorshipEntityConverter {

    public CompanyVisaSponsorshipDto toDto(CompanyVisaSponsorshipEntity companyVisaSponsorship) {
        return new CompanyVisaSponsorshipDto(
                companyVisaSponsorship.getCompany(),
                companyVisaSponsorship.getLocation(),
                "",
                companyVisaSponsorship.getVisaSponsorship(),
                "",
                ""
        );
    }
}
