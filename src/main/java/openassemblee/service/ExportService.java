package openassemblee.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    public static class Entry {
        private String sheetName;
        private List<List<String>> lines;

        public Entry(String sheetName, List<List<String>> lines) {
            this.sheetName = sheetName;
            this.lines = lines;
        }

        public String getSheetName() {
            return sheetName;
        }

        public List<List<String>> getLines() {
            return lines;
        }
    }

    public byte[] exportToExcel(String sheetName, List<List<String>> lines) {
        return exportToExcel(new Entry(sheetName, lines));
    }

    public byte[] exportToExcel(Entry... entries) {
        Workbook wb = new XSSFWorkbook();
        for (Entry e : entries) {
            Sheet sheet = wb.createSheet(e.getSheetName());
            int i = 0;
            for (List<String> line : e.getLines()) {
                Row row = sheet.createRow(i);
                i++;
                int j = 0;
                for (String cell : line) {
                    row.createCell(j).setCellValue(cell);
                    j++;
                }
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
