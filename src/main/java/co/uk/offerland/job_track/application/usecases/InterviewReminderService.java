package co.uk.offerland.job_track.application.usecases;

import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewReminderService {

    private final UserRepository userRepository;
    private final MailjetService mailjetService;

    public Mono<Void> sendDailyReminders() {
        var today = LocalDate.now();
        var start = today.atStartOfDay(ZoneOffset.UTC).toInstant();
        var end = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return userRepository.findUsersWithInterviewsBetween(start, end)
                .flatMap(user -> {
                    List<Phase> todayPhases = user.getJobs().stream()
                            .flatMap(job -> job.getPhases().stream())
                            .filter(phase -> {
                                Instant interviewTime = phase.getInterviewScheduleTime();
                                return interviewTime != null &&
                                        !interviewTime.isBefore(start) &&
                                        interviewTime.isBefore(end);
                            })
                            .toList();
                    log.info("user: [{}], phases: [{}]", user.getEmail(), todayPhases);
                    if (!todayPhases.isEmpty()) {
                        return mailjetService.sendInterviewReminderEmail(user, todayPhases);
                    }
                    return Mono.empty();
                })
                .then();
    }


}
