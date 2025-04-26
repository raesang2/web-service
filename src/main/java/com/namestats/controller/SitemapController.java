package com.namestats.controller;

import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.common.Base64Util;
import com.namestats.service.NameStatsService;
import com.namestats.service.PollService;
import com.namestats.vo.SitemapUrl;

@RestController
public class SitemapController {

    @Autowired
    private NameStatsService nameStatsService;

    @Autowired
    private PollService pollService;


    private final List<String> DOMAINS = List.of(
            "https://kkidsname.com",
            "https://www.kkidsname.com",
            "https://namestats.duckdns.org"
    );

    // sitemap.xml 생성 및 Google, Bing, Naver에 ping 요청
    @GetMapping("/sitemap/generate")
    public String generateSitemap() {
        try {
            // 현재 날짜
            LocalDate lastmod = LocalDate.now();
            List<SitemapUrl> urls = new ArrayList<>();

            // 기본 URL들 추가
            for (String domain : DOMAINS) {
                urls.add(new SitemapUrl(domain + "/searchYearPage", lastmod, "daily", 1.0));
                urls.add(new SitemapUrl(domain + "/searchNamePage", lastmod, "daily", 0.8));
                urls.add(new SitemapUrl(domain + "/createPollPage", lastmod, "daily", 0.5));
            }

            // 연도별 URL 추가
            List<Integer> years = nameStatsService.getTargetYear();
            for (Integer year : years) {
                for (String domain : DOMAINS) {
                    urls.add(new SitemapUrl(domain + "/report/" + year, lastmod, "weekly", 0.6));
                }
            }
            
            // pollMasterId 추가
            String bas64pollMasterId = "";
            List<String> pollMasterIds = pollService.getPollMasterId();
            for (String pollMasterId : pollMasterIds) {
                for (String domain : DOMAINS) {
                	bas64pollMasterId = Base64Util.encodeBase64(pollMasterId);
                    urls.add(new SitemapUrl(domain + "/doPoll/" + bas64pollMasterId, lastmod, "weekly", 0.6));
                    urls.add(new SitemapUrl(domain + "/viewPoll/" + bas64pollMasterId, lastmod, "weekly", 0.6));
                }
            }           

            // sitemap.xml 파일 저장 경로
            String filePath = "src/main/resources/static/sitemap.xml";
            FileWriter writer = new FileWriter(filePath);

            // XML 헤더 작성
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

            // URL 목록 작성
            for (SitemapUrl url : urls) {
                writer.write("  <url>\n");
                writer.write("    <loc>" + url.getLoc() + "</loc>\n");
                writer.write("    <lastmod>" + url.getLastmod() + "</lastmod>\n");
                writer.write("    <changefreq>" + url.getChangefreq() + "</changefreq>\n");
                writer.write("    <priority>" + url.getPriority() + "</priority>\n");
                writer.write("  </url>\n");
            }

            // XML 끝 태그 작성
            writer.write("</urlset>");
            writer.close();

            // 생성된 sitemap.xml 파일 경로 출력
            System.out.println("✅ sitemap.xml 파일 생성 완료: " + filePath);

            // 검색 엔진에 ping 요청
            pingSearchEngines();

            return "Sitemap generated and pinged to search engines!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating sitemap!";
        }
    }

    // Google, Naver에 sitemap ping 요청
    private void pingSearchEngines() {
        pingGoogle();
        pingNaver();
    }

    // Google에 ping 요청
    private void pingGoogle() {
        try {
            String sitemapUrl = "https://kkidsname.com/sitemap.xml";
            String pingUrl = "https://www.google.com/ping?sitemap=" + sitemapUrl;
            HttpURLConnection connection = (HttpURLConnection) new URL(pingUrl).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("📡 Google ping 응답 코드: " + responseCode);
        } catch (Exception e) {
            System.out.println("❌ Google ping 실패");
            e.printStackTrace();
        }
    }

    // Naver에 ping 요청
    private void pingNaver() {
        try {
            String sitemapUrl = "https://kkidsname.com/sitemap.xml";
            String pingUrl = "https://sitemap.naver.com/ping?sitemap=" + sitemapUrl;  // Naver ping URL 형식
            HttpURLConnection connection = (HttpURLConnection) new URL(pingUrl).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("📡 Naver ping 응답 코드: " + responseCode);
        } catch (Exception e) {
            System.out.println("❌ Naver ping 실패");
            e.printStackTrace();
        }
    }
}
