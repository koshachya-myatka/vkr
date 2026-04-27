package ru.datamart.project.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.name.topic}")
    private String nameTopic;

    @Bean
    public NewTopic createNameTopic() {
        return TopicBuilder.name(nameTopic).partitions(4).build();
    }
}