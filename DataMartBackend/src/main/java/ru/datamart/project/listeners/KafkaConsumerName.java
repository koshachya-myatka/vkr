package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerName {
    private final NameService nameService;
    private final ObjectMapper objectMapper;

    @KafkaListener(concurrency = "2", topics = "name-topic", groupId = "name-group")
    private void addName(String data) {
        try {
            CommentDto commentDto = objectMapper.readValue(data, CommentDto.class);
            Optional<Comment> commentOptional = commentService.addComment(commentDto);
            if (commentOptional.isPresent()) {
                log.info(commentOptional.get());
            } else {
                log.info("Comment не создан. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}