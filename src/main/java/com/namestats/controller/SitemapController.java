package com.namestats.controller;

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
            "https://www.kkidsname.com"
    );

    @GetMapping(value = "/sitemap.xml", produces = "application/xml; charset=UTF-8")
    public String getSitemapXml() {
        try {
            LocalDate lastmod = LocalDate.now();
            List<SitemapUrl> urls = new ArrayList<>();

            for (String domain : DOMAINS) {
            	urls.add(new SitemapUrl(domain + "/searchFacility", lastmod, "daily", 1.0));
            	urls.add(new SitemapUrl(domain + "/searchYearPage", lastmod, "daily", 1.0));
                urls.add(new SitemapUrl(domain + "/searchNamePage", lastmod, "daily", 0.8));
                urls.add(new SitemapUrl(domain + "/createPollPage", lastmod, "daily", 0.5));
            }

            List<Integer> years = nameStatsService.getTargetYear();
            for (Integer year : years) {
                for (String domain : DOMAINS) {
                    urls.add(new SitemapUrl(domain + "/report/" + year, lastmod, "weekly", 0.6));
                }
            }

            List<String> pollMasterIds = pollService.getPollMasterId();
            for (String pollMasterId : pollMasterIds) {
                String base64Id = Base64Util.encodeBase64(pollMasterId);
                for (String domain : DOMAINS) {
                    urls.add(new SitemapUrl(domain + "/doPollPage/" + base64Id, lastmod, "weekly", 0.6));
                    urls.add(new SitemapUrl(domain + "/viewPollPage/" + base64Id, lastmod, "weekly", 0.6));
                }
            }

            // XML 문자열 생성
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlBuilder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

            for (SitemapUrl url : urls) {
                xmlBuilder.append("  <url>\n");
                xmlBuilder.append("    <loc>").append(url.getLoc()).append("</loc>\n");
                xmlBuilder.append("    <lastmod>").append(url.getLastmod()).append("</lastmod>\n");
                xmlBuilder.append("    <changefreq>").append(url.getChangefreq()).append("</changefreq>\n");
                xmlBuilder.append("    <priority>").append(url.getPriority()).append("</priority>\n");
                xmlBuilder.append("  </url>\n");
            }

            xmlBuilder.append("</urlset>");
            return xmlBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error>Internal Server Error</error>";
        }
    }

}
