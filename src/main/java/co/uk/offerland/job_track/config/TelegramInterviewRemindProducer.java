package co.uk.offerland.job_track.config;

import co.uk.offerland.job_track.application.dto.channel.telegram.InterviewRemindNotification;
import co.uk.offerland.job_track.application.dto.channel.telegram.InterviewRemindNotifications;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderResult;

import java.nio.charset.StandardCharsets;

import static co.uk.offerland.job_track.config.KafkaConfiguration.TELEGRAM_BOT_OUT_TOPIC;


@Slf4j
@Service
public class TelegramInterviewRemindProducer {

    private final ReactiveKafkaProducerTemplate<String, InterviewRemindNotification> kafkaTemplate;

    public TelegramInterviewRemindProducer(KafkaProperties properties) {
        this.kafkaTemplate = new ReactiveKafkaProducerTemplate<>(
                SenderOptions.create(properties.buildProducerProperties()));
    }

    public Mono<SenderResult<Void>> send(InterviewRemindNotification notification) {
        log.info("Sending reminder for telegramUserId={} to topic={}", notification.getTelegramUserId(), TELEGRAM_BOT_OUT_TOPIC);
        var newRecord = new ProducerRecord<>(
                TELEGRAM_BOT_OUT_TOPIC, notification.getTelegramUserId(), notification);
        newRecord.headers().add("Content-Type", "application/json".getBytes(StandardCharsets.UTF_8));

        return kafkaTemplate.send(newRecord);

    }
}
