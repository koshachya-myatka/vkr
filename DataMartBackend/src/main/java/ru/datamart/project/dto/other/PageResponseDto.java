package ru.datamart.project.dto.other;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PageResponseDto<T> {
    private List<T> items;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}