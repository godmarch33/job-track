package co.uk.offerland.job_track.infrastructure.persistence.listener;

import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserModelListener extends AbstractMongoEventListener<User> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<User> event) {
        User user = event.getSource();
        user.setUpdatedAt(Instant.now());

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now());
        }
    }
}
