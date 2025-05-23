package com.namestats.controller;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.namestats.common.Base64Util;
import com.namestats.dto.PollRequestDTO;
import com.namestats.service.FacilityService;
import com.namestats.service.NameStatsService;
import com.namestats.service.PollService;
import com.namestats.vo.Facility;
import com.namestats.vo.PollDetailVO;
import com.namestats.vo.PollMasterVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.YearNameStatsVO;

@Controller
@RequestMapping("/")
public class InfoViewController {

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
