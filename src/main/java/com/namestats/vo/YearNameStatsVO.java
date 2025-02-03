package com.namestats.vo;

import java.time.Year;

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

}
