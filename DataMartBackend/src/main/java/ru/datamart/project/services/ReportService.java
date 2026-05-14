package ru.datamart.project.services;

import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.InvalidRequestException;
import ru.datamart.project.dto.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    private static BaseFont REGULAR_FONT;
    private static BaseFont BOLD_FONT;
    private static final String REGULAR_FONT_PATH = "fonts/Montserrat-Regular.ttf";
    private static final String BOLD_FONT_PATH = "fonts/Montserrat-Bold.ttf";

    private final BatchService batchService;
    private final MesService mesService;
    private final LimsService limsService;
    private final ScadaService scadaService;
    private final NotificationService notificationService;
    private final UserProfileService userProfileService;

    static {
        try {
            REGULAR_FONT = BaseFont.createFont(REGULAR_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BOLD_FONT = BaseFont.createFont(BOLD_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            log.info("Шрифты Montserrat успешно загружены");
        } catch (Exception e) {
            log.error("Ошибка загрузки шрифтов Montserrat", e);
        }
    }

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
            Document document = new Document(PageSize.A4, 45, 45, 45, 45);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            DecimalFormat df = new DecimalFormat("#.###");
            BaseColor primary = new BaseColor(16, 19, 23);
            BaseColor accent = new BaseColor(37, 99, 235);
            BaseColor white = BaseColor.WHITE;

            document.addTitle("Отчет по партии " + data.getBatch().getBatchId());
            document.addAuthor(data.getReportInfo().getAuthor());
            document.addCreationDate();
            document.open();

            Paragraph title = new Paragraph("ОТЧЁТ ПО ПАРТИИ", getTitleFont());
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(5);
            document.add(title);
            Paragraph batchIdTitle = new Paragraph(data.getBatch().getBatchId(), new Font(BOLD_FONT, 16, Font.BOLD, accent));
            batchIdTitle.setAlignment(Element.ALIGN_CENTER);
            batchIdTitle.setSpacingAfter(10);
            document.add(batchIdTitle);
            drawSeparator(document, accent, 2f);

            addSectionTitle(document, "ИНФОРМАЦИЯ О ПАРТИИ");
            PdfPTable batchTable = createStyledTable(2);
            batchTable.setWidths(new float[]{3, 5});
            addTableRow(batchTable, "Тип металла:", data.getBatch().getMetalTypeName(),
                    white, white, true);
            addTableRow(batchTable, "Поступление:", format(data.getBatch().getStartTime()),
                    white, white, true);
            addTableRow(batchTable, "Окончание анализов:", format(data.getBatch().getEndTime()),
                    white, white, true);
            addTableRow(batchTable, "Статус:", data.getBatch().getStatusName(),
                    white, white, true);
            addTableRow(batchTable, "Выход годного:",
                    data.getBatch().getOutputYield() != null ? df.format(data.getBatch().getOutputYield()) + "%" : "—",
                    white, white, true);
            document.add(batchTable);

            addSectionTitle(document, "ПРОИЗВОДСТВЕННЫЕ ДАННЫЕ (MES)");
            if (data.getMes() != null) {
                PdfPTable mesTable = createStyledTable(2);
                mesTable.setWidths(new float[]{3, 5});
                addTableRow(mesTable, "Оборудование:", data.getMes().getEquipmentId(), white, white, true);
                addTableRow(mesTable, "Оператор:", data.getMes().getOperatorId(), white, white, true);
                addTableRow(mesTable, "Температура:",
                        data.getMes().getTemperature() != null ? df.format(data.getMes().getTemperature()) + " °C" : "—",
                        white, white, true);
                addTableRow(mesTable, "Давление:",
                        data.getMes().getPressure() != null ? df.format(data.getMes().getPressure()) + " Па" : "—",
                        white, white, true);
                addTableRow(mesTable, "Длительность:",
                        data.getMes().getDurationSec() != null ? data.getMes().getDurationSec() + " сек" : "—",
                        white, white, true);
                addTableRow(mesTable, "Энергопотребление:",
                        data.getMes().getEnergyConsumption() != null ? df.format(data.getMes().getEnergyConsumption()) + " кВт·ч" : "—",
                        white, white, true);
                addTableRow(mesTable, "Статус:", data.getMes().getStatusName(), white, white, true);
                document.add(mesTable);
            } else {
                addEmptyDataMessage(document);
            }

            addSectionTitle(document, "ЛАБОРАТОРНЫЕ АНАЛИЗЫ (LIMS)");
            int index = 1;
            if (!data.getLims().isEmpty()) {
                for (BatchLimsDto lims : data.getLims()) {
                    addAnalysisTitle(document, "Анализ " + index);
                    PdfPTable sampleInfo = createStyledTable(2);
                    sampleInfo.setWidths(new float[]{2, 5});
                    sampleInfo.setSpacingAfter(5);
                    addTableRow(sampleInfo, "Проба:", lims.getSampleId(),
                            white, white, true);
                    addTableRow(sampleInfo, "Метод:", lims.getAnalysisMethod(),
                            white, white, true);
                    addTableRow(sampleInfo, "Дата:", format(lims.getTestDate()),
                            white, white, true);
                    addTableRow(sampleInfo, "Статус:", lims.getStatusName(),
                            white, white, true);
                    document.add(sampleInfo);

                    if (lims.getResults() != null && !lims.getResults().isEmpty()) {
                        PdfPTable resultsTable = new PdfPTable(4);
                        resultsTable.setWidthPercentage(100);
                        resultsTable.setWidths(new float[]{3, 2, 2, 2});
                        resultsTable.setSpacingBefore(5);
                        resultsTable.setSpacingAfter(10);
                        Font headerFont = new Font(BOLD_FONT, 10, Font.BOLD, white);
                        addHeaderCell(resultsTable, "Параметр", headerFont, primary);
                        addHeaderCell(resultsTable, "Значение", headerFont, primary);
                        addHeaderCell(resultsTable, "Ед. изм.", headerFont, primary);
                        addHeaderCell(resultsTable, "Норма", headerFont, primary);
                        for (BatchLimsResultDto r : lims.getResults()) {
                            addCell(resultsTable, r.getParameterName(), getBodyFont(), white);
                            String value = r.getValue();
                            try {
                                double valueDouble = Double.parseDouble(value);
                                value = df.format(valueDouble);
                            } catch (NumberFormatException e) {
                                value = r.getValue();
                            }
                            addCell(resultsTable, value, getBodyFont(), white);
                            addCell(resultsTable, r.getUnit(), getBodyFont(), white);
                            String normalText = r.getNormal() ? "Да" : "Нет";
                            addCell(resultsTable, normalText, new Font(REGULAR_FONT, 10, Font.NORMAL, primary), white);
                        }
                        document.add(resultsTable);
                    } else {
                        addEmptyDataMessage(document);
                    }
                    index++;
                }
            } else {
                addEmptyDataMessage(document);
            }

            addSectionTitle(document, "ПОКАЗАТЕЛИ ОБОРУДОВАНИЯ (SCADA)");
            if (!data.getScada().isEmpty()) {
                PdfPTable scadaTable = new PdfPTable(6);
                scadaTable.setWidthPercentage(100);
                scadaTable.setWidths(new float[]{2, 2, 1.5f, 1.5f, 1.5f, 1.5f});
                scadaTable.setSpacingBefore(5);
                scadaTable.setSpacingAfter(10);
                Font headerFont = new Font(BOLD_FONT, 8, Font.BOLD, white);
                addHeaderCell(scadaTable, "Оборудование", headerFont, primary);
                addHeaderCell(scadaTable, "Параметр", headerFont, primary);
                addHeaderCell(scadaTable, "Среднее", headerFont, primary);
                addHeaderCell(scadaTable, "Мин.", headerFont, primary);
                addHeaderCell(scadaTable, "Макс.", headerFont, primary);
                addHeaderCell(scadaTable, "Измерений", headerFont, primary);
                for (BatchScadaAvgDto s : data.getScada()) {
                    addCell(scadaTable, s.getEquipmentId(), getBodyFont(), white);
                    addCell(scadaTable, s.getParameter(), getBodyFont(), white);
                    Font valueFont = getBodyFont();
                    addCell(scadaTable, df.format(s.getAvgValue()), valueFont, white);
                    addCell(scadaTable, df.format(s.getMinValue()), valueFont, white);
                    addCell(scadaTable, df.format(s.getMaxValue()), valueFont, white);
                    addCell(scadaTable, String.valueOf(s.getValuesCount()), valueFont, white);
                }
                document.add(scadaTable);
            } else {
                addEmptyDataMessage(document);
            }

            addSectionTitle(document, "ОТКЛОНЕНИЯ И АВАРИИ");
            PdfPTable analyticsTable = createStyledTable(2);
            analyticsTable.setWidths(new float[]{4, 3});
            Font alarmFont = new Font(REGULAR_FONT, 11, Font.NORMAL, primary);
            addTableRow(analyticsTable, "Аварийные уведомления:",
                    String.valueOf(data.getReportInfo().getAlarmCount()),
                    white, white, alarmFont);
            Font devFont = new Font(REGULAR_FONT, 11, Font.NORMAL, primary);
            addTableRow(analyticsTable, "Отклонения от норм (LIMS):",
                    String.valueOf(data.getReportInfo().getDeviationCount()),
                    white, white, devFont);
            document.add(analyticsTable);

            addSectionTitle(document, "ИНФОРМАЦИЯ ОБ ОТЧЁТЕ");
            PdfPTable metaTable = createStyledTable(2);
            metaTable.setWidths(new float[]{3, 5});
            addTableRow(metaTable, "Автор:", data.getReportInfo().getAuthor(),
                    white, white, true);
            addTableRow(metaTable, "Дата создания:",
                    format(data.getReportInfo().getCreatedAt()),
                    white, white, true);
            document.add(metaTable);

            drawSeparator(document, accent, 2f);
            Paragraph footer = new Paragraph(
                    "Документ сгенерирован автоматически • " +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                    getSmallFont()
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Ошибка при создании PDF-отчёта", e);
            throw new InvalidRequestException("Ошибка при создании PDF-отчета");
        }
    }

    private void addAnalysisTitle(Document document, String title) throws DocumentException {
        Font sectionFont = new Font(BOLD_FONT, 12, Font.BOLD, new BaseColor(41, 51, 71));
        Paragraph section = new Paragraph(title, sectionFont);
        section.setSpacingAfter(4);
        document.add(section);
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Font sectionFont = getSectionFont();
        Paragraph section = new Paragraph(title, sectionFont);
        section.setSpacingBefore(10);
        section.setSpacingAfter(8);
        Chunk bullet = new Chunk("▸ ", new Font(BOLD_FONT, 12, Font.BOLD,
                new BaseColor(41, 128, 185)));
        section.add(0, bullet);
        document.add(section);
    }

    private PdfPTable createStyledTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);
        table.setSpacingAfter(10);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(new BaseColor(189, 195, 199));
        table.getDefaultCell().setPadding(6);
        return table;
    }

    private void addTableRow(PdfPTable table, String label, String value,
                             BaseColor labelBg, BaseColor valueBg, boolean useBoldLabel) {
        Font labelFont = useBoldLabel ? getSubsectionFont() : getBodyFont();
        Font valueFont = getBodyFont();
        addCell(table, label, labelFont, labelBg);
        addCell(table, value, valueFont, valueBg);
    }

    private void addTableRow(PdfPTable table, String label, String value,
                             BaseColor labelBg, BaseColor valueBg, Font valueFont) {
        addCell(table, label, getSubsectionFont(), labelBg);
        addCell(table, value, valueFont, valueBg);
    }

    private void addCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(new BaseColor(189, 195, 199));
        cell.setPadding(6);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(new BaseColor(149, 165, 166));
        cell.setPadding(7);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void drawSeparator(Document document, BaseColor color, float width)
            throws DocumentException {
        LineSeparator line = new LineSeparator(width, 100, color, Element.ALIGN_CENTER, -2);
        document.add(line);
    }

    private void addEmptyDataMessage(Document document) throws DocumentException {
        Paragraph empty = new Paragraph("Нет данных",
                new Font(REGULAR_FONT, 10, Font.ITALIC, new BaseColor(149, 165, 166)));
        empty.setSpacingBefore(5);
        empty.setSpacingAfter(5);
        document.add(empty);
    }

    private String format(LocalDateTime dt) {
        if (dt == null) return "—";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dt.format(formatter);
    }

    private Font getTitleFont() {
        return new Font(BOLD_FONT, 22, Font.BOLD, new BaseColor(16, 19, 23));
    }

    private Font getSectionFont() {
        return new Font(BOLD_FONT, 14, Font.BOLD, new BaseColor(16, 19, 23));
    }

    private Font getSubsectionFont() {
        return new Font(BOLD_FONT, 12, Font.BOLD, new BaseColor(41, 51, 71));
    }

    private Font getBodyFont() {
        return new Font(REGULAR_FONT, 10.5f, Font.NORMAL, new BaseColor(16, 19, 23));
    }

    private Font getSmallFont() {
        return new Font(REGULAR_FONT, 9, Font.NORMAL, new BaseColor(16, 19, 23));
    }
}