package co.uk.offerland.job_track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class JobTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobTrackApplication.class, args);
    }

}
