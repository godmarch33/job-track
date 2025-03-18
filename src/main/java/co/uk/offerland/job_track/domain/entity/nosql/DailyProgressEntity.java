package co.uk.offerland.job_track.domain.entity.nosql;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
public class DailyProgressEntity {

    public static final int MAX_DAILY_CV = 10;
    private int numberApplyCv = 0;
    private LocalDate lastUpdatedDate = LocalDate.now();

    public int updateCounter() {
        LocalDate today = LocalDate.now();

        if (!today.isEqual(lastUpdatedDate)) {
            numberApplyCv = 1;
            lastUpdatedDate = today;
        } else {
            numberApplyCv++;
        }
        log.info("Updated CV Count:[{}] ", numberApplyCv);
        return numberApplyCv;
    }

    public void releaseNextDayCounter() {
        LocalDate today = LocalDate.now();

        if (!today.isEqual(lastUpdatedDate)) {
            numberApplyCv = 0;
            lastUpdatedDate = today;
            log.info("Reset next day counter: [{}] ", numberApplyCv);
        }
    }
}
