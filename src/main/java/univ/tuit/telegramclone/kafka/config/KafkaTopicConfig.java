package univ.tuit.telegramclone.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import univ.tuit.telegramclone.kafka.constants.KafkaConstants;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic kafkaChatTopics() {
        return TopicBuilder.name(KafkaConstants.KAFKA_TOPIC)
                .build();
    }

    @Bean
    public NewTopic kafkaChatJsonTopics(){
        return TopicBuilder.name(KafkaConstants.KAFKA_JSON_TOPIC)
                .build();
    }
}
