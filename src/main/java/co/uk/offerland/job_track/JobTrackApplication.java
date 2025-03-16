package co.uk.offerland.job_track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.blockhound.BlockHound;
import reactor.core.scheduler.ReactorBlockHoundIntegration;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class JobTrackApplication {
//    static {
//        BlockHound.install();
//    }

    public static void main(String[] args) {
        SpringApplication.run(JobTrackApplication.class, args);
    }

}
