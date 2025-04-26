package com.namestats.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SitemapUrl {

    private String loc;
    private LocalDate lastmod;
    private String changefreq;
    private double priority;
}
