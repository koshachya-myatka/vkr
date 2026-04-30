package ru.datamart.project;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.datamart.project.generators.DataGenerator;
import tools.jackson.databind.ObjectMapper;

@SpringBootApplication
@RequiredArgsConstructor
public class DataGeneratorApplication {
    private final DataGenerator dataGenerator;

    public static void main(String[] args) {
        SpringApplication.run(DataGeneratorApplication.class, args);
    }

    @PostConstruct
    public void start() {
        dataGenerator.generate();
    }

    @Bean
    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //todo вот тут разобраться с датами
        return mapper;
    }
}