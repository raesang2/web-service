package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollDetailVO {

    private String pollDetailId;
    private String pollMasterId;
    private String pollItem;
    private String pollItemDesc;
    private int pollItemCount;
    private int totalPollItemCount;
    private float pollItemCountRate;
}
