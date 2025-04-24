package com.namestats.vo;

import java.time.Year;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearNameStatsVO {

	private Year targetYear;
	private String targetName;
	private int mCount = 0;
	private int fCount = 0;
	private String targetRank;
	private float targetRate;
	private int targetCount = 0;
	private int totalCount = 0;    
    @JsonProperty("mRate")
    private int mRate = 0;
    @JsonProperty("fRate")
    private int fRate = 0;

}
