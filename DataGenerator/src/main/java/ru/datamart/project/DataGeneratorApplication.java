package ru.datamart.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.datamart.project.generators.DataGenerator;
import tools.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DataGeneratorApplication {

    public static void main(String[] args) {
        new DataGenerator().generate();
        SpringApplication.run(DataGeneratorApplication.class, args);
    }

    @Bean
    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //todo вот тут разобраться с датами
        return mapper;
    }
}