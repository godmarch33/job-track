package co.uk.offerland.job_track.application.dto;

public record CompanyVisaSponsorshipDto(String company,
                                        String location,
                                        String position,
                                        String visaSponsorship,
                                        String salary,
                                        String bonus) {

    public static final CompanyVisaSponsorshipDto DEFAULT_VALUE = new CompanyVisaSponsorshipDto("unknown","unknown", "unknown", "unknown", "unknown", "unknown");
}
