package co.uk.offerland.job_track.config;

import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewReminderService {

    private final UserRepository userRepository;
    private final MailjetService mailjetService;

    public Mono<Void> sendDailyReminders() {
        var today = LocalDate.now();
        var start = today.atStartOfDay();
        var end = today.plusDays(1).atStartOfDay();

        return userRepository.findUsersWithInterviewsBetween(start.toInstant(ZoneOffset.of("UTC")), end.toInstant(ZoneOffset.of("UTC")))
                .flatMap(user -> {
                    List<Phase> todayPhases = user.getJobs().stream()
                            .flatMap(job -> job.getPhases().stream())
                            .filter(phase -> {
                                var date = phase.getInterviewDate();
                                return date != null && !date.isBefore(start) && date.isBefore(end);
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
