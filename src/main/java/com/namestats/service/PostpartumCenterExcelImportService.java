package com.namestats.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.namestats.common.CoordConverter;
import com.namestats.mapper.PostpartumCenterMapper;
import com.namestats.vo.LocalDataPostpartumCenter;
import com.namestats.vo.MohwPostpartumCenter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostpartumCenterExcelImportService {

    private final PostpartumCenterMapper mapper;
    
    private static final String MOHW_SEARCH_URL = "https://www.mohw.go.kr/board.es?mid=a10412000000&bid=0020";
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void importLocalDataExcelData() {
        String fileUrl = "http://www.localdata.go.kr/data/dataDownload.do?opnSvcIdEx=01_01_04_P&srhStatusEx=01&fileType=xlsx";
        String localPath = "localdata_postpartum.xlsx";

        try {
            // 1. 파일 다운로드 및 저장
            try (InputStream in = new URL(fileUrl).openStream();
                 FileOutputStream out = new FileOutputStream(localPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("📥 다운로드 완료: " + localPath);

            // 2. 저장된 엑셀 파일 읽어서 처리
            importFromFile(localPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }


	private void importFromFile(String filePath) {
		try (InputStream inputStream = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() > 0) {
                mapper.deleteAllLocalDataPostpartumCenters();
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                LocalDataPostpartumCenter localDataPostpartumCenter = new LocalDataPostpartumCenter();
                localDataPostpartumCenter.setLocalGovCode(getCellValue(row.getCell(3)));         // 개방자치단체코드
                localDataPostpartumCenter.setManageNo(getCellValue(row.getCell(4)));             // 관리번호
                localDataPostpartumCenter.setLicenseDate(getCellValue(row.getCell(5)));          // 인허가일자
                localDataPostpartumCenter.setStatus(getCellValue(row.getCell(8)));          // 인허가일자
                localDataPostpartumCenter.setPhoneNumber(getCellValue(row.getCell(15)));         // 소재지전화
                localDataPostpartumCenter.setRoadAddress(getCellValue(row.getCell(19)));         // 도로명전체주소
                localDataPostpartumCenter.setBusinessName(getCellValue(row.getCell(21)));        // 사업장명
                localDataPostpartumCenter.setCoordX5174(parseDouble(getCellValue(row.getCell(26)))); // X좌표
                localDataPostpartumCenter.setCoordY5174(parseDouble(getCellValue(row.getCell(27)))); // Y좌표
                localDataPostpartumCenter.setPregnantCapacity(parseInt(getCellValue(row.getCell(28)))); // 임산부정원수
                localDataPostpartumCenter.setInfantCapacity(parseInt(getCellValue(row.getCell(29))));   // 영유아정원수
                localDataPostpartumCenter.setPregnantRoomArea(parseDouble(getCellValue(row.getCell(30)))); // 임산부실면적
                localDataPostpartumCenter.setInfantRoomArea(parseDouble(getCellValue(row.getCell(31))));   // 영유아실면적
                localDataPostpartumCenter.setTotalFloors(parseInt(getCellValue(row.getCell(44))));         // 건물층수
                localDataPostpartumCenter.setAboveGroundFloors(parseInt(getCellValue(row.getCell(45))));   // 지상층수
                localDataPostpartumCenter.setBasementFloors(parseInt(getCellValue(row.getCell(46))));       // 지하층수

                // ✅ 위경도 자동 변환
                if (localDataPostpartumCenter.getCoordX5174() != null && localDataPostpartumCenter.getCoordY5174() != null) {
                    double[] latLng = CoordConverter.convert(localDataPostpartumCenter.getCoordX5174(), localDataPostpartumCenter.getCoordY5174());
                    localDataPostpartumCenter.setLatitude(latLng[0]);
                    localDataPostpartumCenter.setLongitude(latLng[1]);
                }

                mapper.insertLocalDataPostpartumCenter(localDataPostpartumCenter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	
    @Transactional
    public void importMohwExcelData(){
    	try {
			Map<String, String> result = fetchLatestPostNoBySearch();
            String excelUrl = result.get("excelUrl");

            Path excelPath = downloadMohwExcelFile(excelUrl);
            
            if(excelPath != null) {
            	importMohwExcelToDB(excelPath);
            }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }	
	
    private Map<String, String> fetchLatestPostNoBySearch() throws IOException {
        // POST form data
        Map<String, String> formData = new HashMap<>();
        formData.put("keyField", "TITLE");
        formData.put("keyWord", "산후조리원 현황");

        // Jsoup POST 요청 (쿠키도 설정 가능)
        Connection connection = Jsoup.connect(MOHW_SEARCH_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                // 필요하면 쿠키 추가, 일반적으론 없어도 됨
                //.cookie("JSESSIONID", "xhfVrwHPe6rkWtX1BeTKRzLNXfDEgOW2oxpkEY1c2GooSg4Lla8lzfwosKGLheho.mohwwas1_servlet_engine20")
                .data(formData)
                .method(Connection.Method.POST)
                .timeout(10_000);

        Document doc = connection.execute().parse();

        // 검색 결과에서 첫번째 게시물의 href 추출
        Elements links = doc.select("tr td.txt_left a.txt_title");
        if (links.isEmpty()) {
            throw new RuntimeException("검색 결과가 없습니다.");
        }

        Element firstLink = links.get(0);
        String href = firstLink.attr("href"); // ex) /board.es?mid=a10412000000&bid=0020&act=view&list_no=1485162&tag=&nPage=1

        // list_no 파라미터 추출
        String listNo = Arrays.stream(href.split("&"))
                .filter(param -> param.startsWith("list_no="))
                .map(param -> param.substring("list_no=".length()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("list_no 파라미터가 없습니다."));

        // 게시물 URL과 엑셀 다운로드 URL 구성
        String postUrl = "https://www.mohw.go.kr" + href;
        String excelUrl = "https://www.mohw.go.kr/boardDownload.es?bid=0020&list_no=" + listNo + "&seq=1";

        Map<String, String> result = new HashMap<>();
        result.put("postUrl", postUrl);
        result.put("excelUrl", excelUrl);

        return result;
    }	
	
    // 엑셀 다운로드 및 임시파일 저장
    private Path downloadMohwExcelFile(String excelUrl) throws IOException {
        ResponseEntity<Resource> response = restTemplate.getForEntity(excelUrl, Resource.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Resource resource = response.getBody();

            // 임시 디렉터리에 파일 저장 (예: system temp)
            Path tempFile = Files.createTempFile("mohw_postpartum_", ".xlsx");
            try (InputStream is = resource.getInputStream()) {
                Files.copy(is, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            return tempFile;
        } else {
            throw new IOException("엑셀 파일 다운로드 실패: " + response.getStatusCode());
        }
    }

    public void importMohwExcelToDB(Path excelPath) throws Exception {
        try (InputStream is = Files.newInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            int startRowIndex = 5; // 엑셀 6번째 행 (index는 0부터 시작)

            int rowIndex = 0;
            if (sheet.getLastRowNum() < 8) {
                throw new IllegalStateException("엑셀에 데이터가 6행 이상 존재하지 않습니다.");
            }
            
            // 기존 데이터 삭제
            mapper.deleteAllMohwPostpartumCenters();
            
            for (Row row : sheet) {
                if (rowIndex++ < startRowIndex) continue;
                if ("".equals(getCellValue(row.getCell(0)))) continue;
                                
                MohwPostpartumCenter center = new MohwPostpartumCenter();
                
                center.setCity(getCellValue(row.getCell(1)));         // 시도
                center.setDistrict(getCellValue(row.getCell(2)));     // 시군구
                center.setOperatorType(getCellValue(row.getCell(3))); // 운영주체
                center.setName(getCellValue(row.getCell(4)));         // 산후조리원명
                center.setAddress(getCellValue(row.getCell(5)));      // 주소
                center.setPhone(getCellValue(row.getCell(6)));        // 전화번호
                center.setPriceStandard(parseInt(getCellValue(row.getCell(7))));   // 일반
                center.setPriceSpecial(parseInt(getCellValue(row.getCell(8))));    // 특실

                mapper.insertMohwPostpartumCenter(center);
            }
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

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void deleteAllLocalDataPostpartumCenters() {
    	mapper.deleteAllLocalDataPostpartumCenters();
    }
}