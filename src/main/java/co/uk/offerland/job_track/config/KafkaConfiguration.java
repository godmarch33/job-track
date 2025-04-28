package co.uk.offerland.job_track.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    public static final String TELEGRAM_BOT_OUT_TOPIC = "telegramBotOutTopic";

    @Bean
    public NewTopic telegramBotOutTopic(){
        return TopicBuilder.name(TELEGRAM_BOT_OUT_TOPIC)
                .partitions(4)
                .build();
    }


}
