package de.local.energycharts.infrastructure.solarcity.writer;

import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.core.solarcity.model.SolarSystem;
import de.local.energycharts.core.solarcity.ports.out.SolarSystemsXlsWriter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparing;

@SecondaryAdapter
@Component
public class ApachePoiSolarSystemsXlsWriter implements SolarSystemsXlsWriter {

    private Workbook workbook;
    private Sheet sheet;
    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final Short fontSize = 12;
    private final String fontName = "Arial";

    @SneakyThrows
    public File write(SolarCity solarCity) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("PV-Anlagen in " + solarCity.getName());

        createHeader();
        createRows(solarCity.getAllSolarSystems());
        autoSizeAllColumns();

        File tmpFile = new File("/tmp/" + solarCity.getName() + ".xlsx");
        workbook.write(new FileOutputStream(tmpFile));
        workbook.close();

        return tmpFile;
    }

    private void createHeader() {
        var headerStyle = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(true);
        headerStyle.setFont(font);
        var header = sheet.createRow(0);

        var headerCell = header.createCell(0);
        headerCell.setCellValue("Betreiber");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Leistung [kWp]");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Inbetriebnahme");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Status");
        headerCell.setCellStyle(headerStyle);
    }

    private void createRows(Set<SolarSystem> allSolarSystems) {
        AtomicInteger rowNum = new AtomicInteger(1);
        allSolarSystems.stream()
                .sorted(comparing(SolarSystem::getInstalledNetPowerkWp).reversed())
                .forEach(solarSystem -> createRow(solarSystem, rowNum.getAndIncrement()));
    }

    private void createRow(SolarSystem solarSystem, int rowNum) {
        var style = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);

        var row = sheet.createRow(rowNum);

        var cell = row.createCell(0);
        cell.setCellValue(solarSystem.getOperatorName());
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(solarSystem.getName());
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue(numberFormat.format(solarSystem.getInstalledNetPowerkWp()));
        cell.setCellStyle(style);

        cell = row.createCell(3);
        if (solarSystem.getCommissioning().isEqual(LocalDate.EPOCH)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(dateTimeFormatter.format(solarSystem.getCommissioning()));
        }
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue(getStatus(solarSystem));
        cell.setCellStyle(style);
    }

    private String getStatus(SolarSystem solarSystem) {
        if (solarSystem.isActive()) {
            return "Aktiv";
        }
        if (solarSystem.isNotActive()) {
            return "Stillgelegt";
        }
        return "";
    }

    private void autoSizeAllColumns() {
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
    }
}
