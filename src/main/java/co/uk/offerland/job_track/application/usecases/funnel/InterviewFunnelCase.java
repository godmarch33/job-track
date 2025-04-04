package co.uk.offerland.job_track.application.usecases.funnel;

import co.uk.offerland.job_track.application.dto.user.InterviewFunnelResponse;
import co.uk.offerland.job_track.domain.entity.nosql.Job;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import co.uk.offerland.job_track.infrastructure.exception.JobNotFoundException;
import co.uk.offerland.job_track.infrastructure.exception.UserNotFoundException;
import co.uk.offerland.job_track.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewFunnelCase {

    private final UserRepository userRepository;

    public Mono<InterviewFunnelResponse> getFunnelStatistics(UUID userId) {
        return findUserById(userId).map(this::mapToInterviewFunnelResponse);
    }

    private Mono<User> findUserById(UUID userId) {
        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId.toString())));
    }

    private InterviewFunnelResponse mapToInterviewFunnelResponse(User user) {
        var stats = user.getInterviewStat();
        return InterviewFunnelResponse.builder()
                .appliedCount(stats.getAppliedCount())
                .screeningСallCount(stats.getScreeningСallCount())
                .assessmentTestCount(stats.getAssessmentTestCount())
                .firstRoundCount(stats.getFirstRoundCount())
                .secondRoundCount(stats.getSecondRoundCount())
                .thirdRoundCount(stats.getThirdRoundCount())
                .finalRoundCount(stats.getFinalRoundCount())
                .negotiationCount(stats.getNegotiationCount())
                .acceptedCount(stats.getAcceptedCount())
                .build();
    }
}
