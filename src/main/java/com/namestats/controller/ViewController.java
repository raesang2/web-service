package com.namestats.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.namestats.common.Base64Util;
import com.namestats.dto.PollRequestDTO;
import com.namestats.service.NameStatsService;
import com.namestats.service.PollService;
import com.namestats.vo.PollDetailVO;
import com.namestats.vo.PollMasterVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.YearNameStatsVO;

@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    private NameStatsService nameStatsService;

    @Autowired
    private PollService pollService;
    
    @GetMapping("/searchNamePage")
    public String searchNamePage(SearchVO searchVO, Model model) {
        model.addAttribute("searchVO", searchVO);  // 검색 조건을 모델에 추가
        model.addAttribute("yearList", nameStatsService.getTargetYear());
        model.addAttribute("recentDataDate", nameStatsService.getRecentDataDate());
    	return "searchName";
    }
    
    @GetMapping("/selectNameStatsPage")
    public String selectNameStatsPage(SearchVO searchVO, Model model) {
    	List<YearNameStatsVO> result = nameStatsService.getAllNameStatsByYear(searchVO);
        // 결과를 모델에 추가
        model.addAttribute("results", result);

        // 결과만 포함하는 테이블 부분을 반환 (HTML 코드로)
        return "nameStatsTableFragment"; // nameStatsTableFragment는 HTML 파일에 있는 부분 이름    	
    }

    @GetMapping("/createPollPage")
    public String showCreatePollPage() {
        return "createPoll";  // resources/templates/createPoll.html을 반환
    }
    
    @GetMapping("/doPollPage")
    public String showDoPollPage(String pollMasterId, Model model) {
        // base64로 된 데이터가 넘어옴.
        String decodePollMasterId = Base64Util.decodeBase64(pollMasterId);
        
        // 디코딩된 masterid 로 조회
        // 1. poll_master 조회
        PollMasterVO pollMasterVO = pollService.getPollMasterByPollMasterId(decodePollMasterId);        
        // 2. poll detail 조회
        List<PollDetailVO> pollDetailList = pollService.getPollDetailByPollMasterId(decodePollMasterId);
        
        
        // masterid, detailid base64로 인코딩해서 화면에 전달
        // PollRequestDTO
        PollRequestDTO pollRequestDTO =  new PollRequestDTO();
        // 1. poll master 세팅
        // pollMasterID는 이미 인코딩되어 넘어온 값으로 그대로 사용
        pollMasterVO.setPollMasterId(pollMasterId);
        pollRequestDTO.setPollMasterVO(pollMasterVO);
        
        // 2. poll detail 세팅 
        String encodePollDetailId = "";
        for(PollDetailVO pollDetailVO : pollDetailList) {
        	// detailId base64로 인코딩해서 전달
        	encodePollDetailId = Base64Util.encodeBase64(pollDetailVO.getPollDetailId());
        	pollDetailVO.setPollDetailId(encodePollDetailId);
        }
        pollRequestDTO.setPollDetailList(pollDetailList);

        // 3. dto 로 만들어서 전달
        model.addAttribute("results", pollRequestDTO);
    	
    	return "doPoll";
    }
    
    @GetMapping("/viewPollPage")
    public String showViewPollPage(String pollMasterId, Model model) {
        // base64로 된 데이터가 넘어옴.
        String decodePollMasterId = Base64Util.decodeBase64(pollMasterId);
        
        // 디코딩된 masterid 로 조회
        // 1. poll_master 조회
        PollMasterVO pollMasterVO = pollService.getPollMasterByPollMasterId(decodePollMasterId);        
        // 2. poll detail 조회
        List<PollDetailVO> pollDetailList = pollService.getPollDetailByPollMasterId(decodePollMasterId);
        
        
        // masterid, detailid base64로 인코딩해서 화면에 전달
        // PollRequestDTO
        PollRequestDTO pollRequestDTO =  new PollRequestDTO();
        // 1. poll master 세팅
        // pollMasterID는 이미 인코딩되어 넘어온 값으로 그대로 사용
        pollMasterVO.setPollMasterId(pollMasterId);
        pollRequestDTO.setPollMasterVO(pollMasterVO);
        
        // 2. poll detail 세팅 
        String encodePollDetailId = "";
        for(PollDetailVO pollDetailVO : pollDetailList) {
        	// detailId base64로 인코딩해서 전달
        	encodePollDetailId = Base64Util.encodeBase64(pollDetailVO.getPollDetailId());
        	pollDetailVO.setPollDetailId(encodePollDetailId);
        }
        pollRequestDTO.setPollDetailList(pollDetailList);

        // 3. dto 로 만들어서 전달
        model.addAttribute("results", pollRequestDTO);
    	
    	return "viewPoll";
    }
}
