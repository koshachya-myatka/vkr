package ru.datamart.project.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.mes.topic}")
    private String mesTopic;
    @Value("${kafka.scada.topic}")
    private String scadaTopic;
    @Value("${kafka.lims.topic}")
    private String limsTopic;

    @Bean
    public NewTopic createMesTopic() {
        return TopicBuilder.name(mesTopic).partitions(4).build();
    }

    @Bean
    public NewTopic createScadaTopic() {
        return TopicBuilder.name(scadaTopic).partitions(4).build();
    }

    @Bean
    public NewTopic createLimsTopic() {
        return TopicBuilder.name(limsTopic).partitions(4).build();
    }
}