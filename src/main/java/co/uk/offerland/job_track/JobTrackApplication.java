package co.uk.offerland.job_track;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.blockhound.BlockHound;

import java.security.Security;

@EnableReactiveMongoRepositories
@SpringBootApplication
@EnableScheduling
public class JobTrackApplication {
//    static {
//        BlockHound.install();
//    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication.run(JobTrackApplication.class, args);
    }
}
