package fr.cridf.babylone14166.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

    public byte[] exportToExcel(List<List<String>> lines) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("export");
        int i = 0;
        for (List<String> line : lines) {
            Row row = sheet.createRow(i);
            i++;
            int j = 0;
            for (String cell : line) {
                row.createCell(j).setCellValue(cell);
                j++;
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

}
