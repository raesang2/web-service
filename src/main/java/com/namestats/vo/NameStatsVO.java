package com.namestats.vo;

import java.time.LocalDate;
import java.time.Year;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NameStatsVO {

	private String targetName;
	private LocalDate targetDate;
	private Year targetYear;
	private int targetCount = 0;
	private String targetGender;
	private String targetSido;
}
