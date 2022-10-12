package com.guru;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.CSVReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesFromZipTests {
    ClassLoader cl = FilesFromZipTests.class.getClassLoader();

    @DisplayName("Parse pdf from zip")
    @Test
    void parsePdfFromZipTest() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("test.zip")) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.contains("pdf")) {
                    PDF pdf = new PDF(zipInputStream);
                    assertThat(pdf.text).contains("Material");

                }
            }
        }
    }

    @DisplayName("Parse xlsx from zip")
    @Test
    void parseXlsxFromZipTest() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("test.zip")) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.contains("xlsx")) {
                    XLS xls = new XLS(zipInputStream);
                    assertThat(xls.excel.getSheetName(0)).isEqualTo("Sheet1");
                    assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).isEqualTo("Zurich Financial Services Ltd");
                }
            }
        }
    }

    @DisplayName("Parse csv from zip")
    @Test
    void parseCsvFromZipTest() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("test.zip")) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.contains("csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zipInputStream));
                    List<String[]> list = reader.readAll();
                    String[] line = list.get(1);
                    assertThat(line[0]).isEqualTo("Dina");
                    assertThat(line[1]).isEqualTo(" 33");
                }
            }
        }
    }

    @DisplayName("Parse JSON")
    @Test
    void parseJsonTest() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("User.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(inputStream, User.class);
            assertThat(user.name).isEqualTo("Di");
            assertThat(user.age).isEqualTo(34);
            assertThat(user.country).isEqualTo("Germany");
            assertThat(user.hobby[0]).isEqualTo("Tennis");
            assertThat(user.order.isCreditCard).isTrue();
            assertThat(user.order.products).isEqualTo(2);
            assertThat(user.order.productsDetail.get(1)).isEqualTo("ebook2");
        }
    }
}
