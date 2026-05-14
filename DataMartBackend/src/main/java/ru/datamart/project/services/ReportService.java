package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.InvalidRequestException;
import ru.datamart.project.dto.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    private final BatchService batchService;
    private final MesService mesService;
    private final LimsService limsService;
    private final ScadaService scadaService;
    private final NotificationService notificationService;
    private final UserProfileService userProfileService;

    public byte[] generateReport(String batchId) {
        BatchReportDto reportDto = buildReportData(batchId);
        return buildPdf(reportDto);
    }

    private BatchReportDto buildReportData(String batchId) {
        BatchDto batch = batchService.getBatchById(batchId);
        if (batch == null) {
            throw new InvalidRequestException("Партия не найдена");
        }
        BatchMesDto mes = mesService.getMesByBatchId(batchId);
        List<BatchLimsDto> lims = limsService.getLimsByBatchId(batchId);
        List<BatchScadaAvgDto> scada = scadaService.getScadaAvgByBatchId(batchId);
        BatchReportInfoDto reportInfo = new BatchReportInfoDto();
        UserProfileDto currentUser = userProfileService.getCurrentUser();
        long alarmCount = notificationService.countAlarmsByBatchId(batchId);
        long deviationCount = 0;
        if (!lims.isEmpty()) {
            for (BatchLimsDto batchLimsDto : lims) {
                for (BatchLimsResultDto batchLimsResultDto : batchLimsDto.getResults()) {
                    if (!batchLimsResultDto.getNormal()) {
                        deviationCount++;
                    }
                }
            }
        }
        reportInfo.setAlarmCount(alarmCount);
        reportInfo.setDeviationCount(deviationCount);
        reportInfo.setCreatedAt(LocalDateTime.now());
        reportInfo.setAuthor(currentUser.getSurname() + " " + currentUser.getName() + " " + currentUser.getPatronymic());
        BatchReportDto reportDto = new BatchReportDto();
        reportDto.setBatch(batch);
        reportDto.setMes(mes);
        reportDto.setLims(lims);
        reportDto.setScada(scada);
        reportDto.setReportInfo(reportInfo);
        return reportDto;
    }

    private byte[] buildPdf(BatchReportDto data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);
            document.open();
            // Заголовок
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Отчёт по партии " + data.getBatch().getBatchId(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            // Блок информации о партии
            document.add(new Paragraph("Информация о партии", boldFont()));
            document.add(new Paragraph("Тип металла: " + data.getBatch().getMetalTypeName()));
            document.add(new Paragraph("Поступление: " + format(data.getBatch().getStartTime()) +
                    " | Окончание анализов: " + format(data.getBatch().getEndTime())));
            document.add(new Paragraph("Статус: " + data.getBatch().getStatusName() +
                    " | Выход годного: " + data.getBatch().getOutputYield() + "%"));
            document.add(Chunk.NEWLINE);
            // MES
            document.add(new Paragraph("Производственные данные (MES)", boldFont()));
            if (data.getMes() != null) {
                document.add(new Paragraph("Оборудование: " + data.getMes().getEquipmentId()));
                document.add(new Paragraph("Оператор: " + data.getMes().getOperatorId()));
                document.add(new Paragraph("Температура: " + data.getMes().getTemperature() + "°C"));
                document.add(new Paragraph("Давление: " + data.getMes().getPressure() + " Па"));
                document.add(new Paragraph("Длительность: " + data.getMes().getDurationSec() + " сек"));
                document.add(new Paragraph("Энергопотребление: " + data.getMes().getEnergyConsumption() + " кВт·ч"));
                document.add(new Paragraph("Статус: " + data.getMes().getStatusName()));
                document.add(Chunk.NEWLINE);
            } else {
                document.add(new Paragraph("Нет данных"));
            }
            // LIMS
            document.add(new Paragraph("Лабораторные анализы (LIMS)", boldFont()));
            if (!data.getLims().isEmpty()) {
                for (BatchLimsDto lims : data.getLims()) {
                    document.add(new Paragraph("Проба: " + lims.getSampleId() +
                            " | Метод: " + lims.getAnalysisMethod() +
                            " | Дата: " + format(lims.getTestDate()) +
                            " | Статус: " + lims.getStatusName()));
                    if (lims.getResults() != null && !lims.getResults().isEmpty()) {
                        PdfPTable table = new PdfPTable(4);
                        table.setWidthPercentage(100);
                        table.setSpacingBefore(5);
                        table.setSpacingAfter(5);
                        addTableHeader(table, "Параметр", "Значение", "Ед.изм.", "Норма");
                        for (BatchLimsResultDto r : lims.getResults()) {
                            table.addCell(r.getParameterName());
                            table.addCell(r.getValue());
                            table.addCell(r.getUnit());
                            table.addCell(r.getNormal() ? "Да" : "Нет");
                        }
                        document.add(table);
                    } else {
                        document.add(new Paragraph("Нет данных"));
                    }
                    document.add(Chunk.NEWLINE);
                }
            } else {
                document.add(new Paragraph("Нет данных"));
            }
            // SCADA
            document.add(new Paragraph("Показатели оборудования (SCADA)", boldFont()));
            if (!data.getScada().isEmpty()) {
                PdfPTable scadaTable = new PdfPTable(6);
                scadaTable.setWidthPercentage(100);
                addTableHeader(scadaTable, "Оборудование", "Параметр", "Ср. знач.", "Мин.", "Макс.", "Кол-во измерений");
                for (BatchScadaAvgDto s : data.getScada()) {
                    scadaTable.addCell(s.getEquipmentId());
                    scadaTable.addCell(s.getParameter());
                    scadaTable.addCell(String.valueOf(s.getAvgValue()));
                    scadaTable.addCell(String.valueOf(s.getMinValue()));
                    scadaTable.addCell(String.valueOf(s.getMaxValue()));
                    scadaTable.addCell(String.valueOf(s.getValuesCount()));
                }
                document.add(scadaTable);
                document.add(Chunk.NEWLINE);
            } else {
                document.add(new Paragraph("Нет данных"));
            }
            // Метаданные отчёта
            document.add(new Paragraph("Отклонения и аварии", boldFont()));
            document.add(new Paragraph("Аварийных уведомлений: " + data.getReportInfo().getAlarmCount()));
            document.add(new Paragraph("Отклонений от норм в анализах: " + data.getReportInfo().getDeviationCount()));
            document.add(new Paragraph("Информация об отчёте", boldFont()));
            document.add(new Paragraph("Автор: " + data.getReportInfo().getAuthor()));
            document.add(new Paragraph("Дата создания: " + format(data.getReportInfo().getCreatedAt())));
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new InvalidRequestException("Ошибка при создании отчета");
        }
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private Font boldFont() {
        return new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    }

    private String format(LocalDateTime dt) {
        return dt != null ? dt.toString().replace("T", " ") : "";
    }
}