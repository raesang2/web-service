package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchVO {

	private String targetName;
	private String fromTargetYear;
	private String toTargetYear;
	private String targetGender;
}
