package ru.datamart.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DataMartBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataMartBackendApplication.class, args);
	}

	@Bean
	public static ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		//todo вот тут разобраться с датами
		return mapper;
	}
}