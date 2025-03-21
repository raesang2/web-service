package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollMasterVO {

    private String pollMasterId;
    private String pollName;
    private String pollStartDate;
    private String pollEndDate;
    private String pollDesc;
    private String pollLastName;
    private String pollGender;
    private String pollResultsView;
    private String pollEnable;
}
