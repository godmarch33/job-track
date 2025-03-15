package co.uk.offerland.job_track.domain.entity.nosql;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Data
public class DailyProgressEntity {

    public static final int MAX_DAILY_CV = 10;
    private int currentCVAmount = 0;
    private LocalDate lastUpdatedDate = LocalDate.now();

    public void updateCounter() {
        LocalDate today = LocalDate.now();

        if (!today.isEqual(lastUpdatedDate)) {
            currentCVAmount = 1;
            lastUpdatedDate = today;
        } else {
            currentCVAmount++;
        }
        log.info("Updated CV Count:[{}] ", currentCVAmount);
    }

    public void releaseNextDayCounter() {
        LocalDate today = LocalDate.now();

        if (!today.isEqual(lastUpdatedDate)) {
            currentCVAmount = 0;
            lastUpdatedDate = today;
            log.info("Reset next day counder [{}] ", currentCVAmount);
        }

    }
}
