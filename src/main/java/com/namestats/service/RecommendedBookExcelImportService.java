package com.namestats.service;

import java.io.InputStream;
import java.net.URL;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.namestats.mapper.RecommendedBookMapper;
import com.namestats.vo.RecommendedBook;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendedBookExcelImportService {

    private final RecommendedBookMapper mapper;

    public void importExcelData() {
        try {
            String fileUrl = "https://nlcy.go.kr/NLCY/board/C10600000000_suggestBookListExcel.do?schOpt2=-&bcid=nlcy_normal08";
            try (InputStream inputStream = new URL(fileUrl).openStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);
                if(sheet.getLastRowNum() > 0) {
                	mapper.deleteAllRecommendedBooks();
                }
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    RecommendedBook book = new RecommendedBook();
                    book.setRecommendationMonth(getCellValue(row.getCell(1)));
                    book.setTheme(getCellValue(row.getCell(2)));
                    book.setTargetAudience(getCellValue(row.getCell(3)));
                    book.setTitle(getCellValue(row.getCell(4)));
                    book.setSubjectCategory(getCellValue(row.getCell(5)));
                    book.setAuthor(getCellValue(row.getCell(6)));
                    book.setPublisher(getCellValue(row.getCell(7)));
                    book.setPublicationYear(parseIntSafe(getCellValue(row.getCell(8))));
                    book.setIsbn(getCellValue(row.getCell(9)));
                    book.setCallNumber(getCellValue(row.getCell(10)));

                    mapper.insertRecommendedBook(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> "";
        };
    }

    private Integer parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void deleteAllRecommendedBooks() {
    	mapper.deleteAllRecommendedBooks();
    }
}