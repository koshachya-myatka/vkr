package ru.datamart.project.dto.notifications;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationUpdateDto {
    private String status;
    private String comment;
}