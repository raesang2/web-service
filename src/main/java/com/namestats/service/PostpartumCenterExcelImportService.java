package com.namestats.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

	/**
	 * ✅ MOHW 엑셀 데이터 import
	 */
	@Transactional
	public void importMohwExcelData() {
		try {
			Map<String, String> result = fetchLatestPostNoBySearch();
			String excelUrl = result.get("excelUrl");

			Path excelPath = downloadMohwExcelFile(excelUrl);

			if (excelPath != null) {
				importMohwExcelToDB(excelPath);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ✅ 최신 게시글 조회 + Excel URL 동적 추출
	 */
	private Map<String, String> fetchLatestPostNoBySearch() throws IOException {

		Map<String, String> formData = new HashMap<>();
		formData.put("keyField", "TITLE");
		formData.put("keyWord", "산후조리원 현황");

		Connection connection = Jsoup.connect(MOHW_SEARCH_URL)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.data(formData)
				.method(Connection.Method.POST)
				.timeout(10_000);

		Document doc = connection.execute().parse();

		Elements links = doc.select("tr td.txt_left a.txt_title");
		if (links.isEmpty()) {
			throw new RuntimeException("검색 결과가 없습니다.");
		}

		Element firstLink = links.get(0);
		String href = firstLink.attr("href");

		// list_no 추출
		String listNo = Arrays.stream(href.split("&"))
				.filter(param -> param.startsWith("list_no="))
				.map(param -> param.substring("list_no=".length()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("list_no 파라미터가 없습니다."));

		String postUrl = "https://www.mohw.go.kr" + href;

		// 🔥 핵심: seq 사용 안함 → HTML에서 Excel 찾기
		String excelUrl = findExcelDownloadUrl(postUrl);

		Map<String, String> result = new HashMap<>();
		result.put("postUrl", postUrl);
		result.put("excelUrl", excelUrl);

		return result;
	}

	/**
	 * ✅ 게시글 HTML에서 Excel 다운로드 URL 추출 (핵심 로직)
	 */
	private String findExcelDownloadUrl(String postUrl) throws IOException {
		Document doc = Jsoup.connect(postUrl).get();

		Elements fileItems = doc.select("div.file ul.list li");

		for (Element li : fileItems) {

			// 1️⃣ 파일명 기준 체크
			String text = li.ownText().trim();
			if (text.matches(".*\\.(xlsx|xls)$")) {
				Element link = li.selectFirst(".link a[href*=/boardDownload.es]");
				if (link != null) {
					return "https://www.mohw.go.kr" + link.attr("href");
				}
			}

			// 2️⃣ alt 속성 기준 체크 (보완)
			Element img = li.selectFirst("img");
			if (img != null) {
				String alt = img.attr("alt"); // ex: "xlsx 첨부파일"
				if (alt.contains("xlsx") || alt.contains("xls")) {
					Element link = li.selectFirst(".link a[href*=/boardDownload.es]");
					if (link != null) {
						return "https://www.mohw.go.kr" + link.attr("href");
					}
				}
			}
		}

		throw new RuntimeException("엑셀 파일을 찾을 수 없습니다.");
	}

	/**
	 * ✅ 엑셀 다운로드
	 */
	private Path downloadMohwExcelFile(String excelUrl) throws IOException {
		ResponseEntity<Resource> response = restTemplate.getForEntity(excelUrl, Resource.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Resource resource = response.getBody();

			Path tempFile = Files.createTempFile("mohw_postpartum_", ".xlsx");
			try (InputStream is = resource.getInputStream()) {
				Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
			}
			return tempFile;
		} else {
			throw new IOException("엑셀 파일 다운로드 실패: " + response.getStatusCode());
		}
	}

	/**
	 * ✅ 엑셀 → DB 저장
	 */
	public void importMohwExcelToDB(Path excelPath) throws Exception {
		try (InputStream is = Files.newInputStream(excelPath);
				Workbook workbook = new XSSFWorkbook(is)) {

			Sheet sheet = workbook.getSheetAt(0);

			int startRowIndex = 5;

			if (sheet.getLastRowNum() < 8) {
				throw new IllegalStateException("엑셀 데이터가 부족합니다.");
			}

			mapper.deleteAllMohwPostpartumCenters();

			int rowIndex = 0;
			for (Row row : sheet) {
				if (rowIndex++ < startRowIndex) continue;
				if ("".equals(getCellValue(row.getCell(0)))) continue;

				MohwPostpartumCenter center = new MohwPostpartumCenter();

				center.setCity(getCellValue(row.getCell(1)));
				center.setDistrict(getCellValue(row.getCell(2)));
				center.setOperatorType(getCellValue(row.getCell(3)));
				center.setName(getCellValue(row.getCell(4)));
				center.setAddress(getCellValue(row.getCell(5)));
				center.setPhone(getCellValue(row.getCell(6)));
				center.setPriceStandard(parseInt(getCellValue(row.getCell(7))));
				center.setPriceSpecial(parseInt(getCellValue(row.getCell(8))));

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