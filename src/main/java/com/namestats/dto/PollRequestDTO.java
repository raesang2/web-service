package com.namestats.dto;

import java.util.List;

import com.namestats.vo.PollDetailVO;
import com.namestats.vo.PollMasterVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollRequestDTO {

    private PollMasterVO pollMasterVO;
    private List<PollDetailVO> pollDetailList;
}
