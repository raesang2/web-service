package com.namestats.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.namestats.dto.PostpartumSearchDto;
import com.namestats.service.PostpartumCenterService;
import com.namestats.vo.PostpartumCenter;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostpartumViewController {

    private final PostpartumCenterService postpartumCenterService;

    @GetMapping("/searchPostpartumPage")
    public String searchPostpartumPage(Model model) {
        model.addAttribute("regionList", postpartumCenterService.selectPostpartumRegions());
        return "searchPostpartum";
    }

    @GetMapping("/searchPostpartumPage/{city}")
    public String searchPostpartumByCity(@PathVariable String city, Model model) {
        model.addAttribute("regionList", postpartumCenterService.selectPostpartumRegions());
        model.addAttribute("city", city);
        return "searchPostpartum";
    }

    @GetMapping("/searchPostpartumPage/{city}/{district}")
    public String searchPostpartumByRegion(
            @PathVariable String city,
            @PathVariable String district,
            Model model) {

        model.addAttribute("regionList", postpartumCenterService.selectPostpartumRegions());
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        return "searchPostpartum";
    }

    @GetMapping("/selectPostpartumPage")
    public String selectPostpartumPage(@RequestParam(required = false) String city,
                                       @RequestParam(required = false) String district,
                                       @RequestParam(required = false) String name,
                                       Model model) {

    	PostpartumSearchDto dto = new PostpartumSearchDto();
    	dto.setCity(city);
    	dto.setDistrict(district);
    	dto.setName(name);    	
    	
        List<PostpartumCenter> centers = postpartumCenterService.searchCenters(dto);
        model.addAttribute("postpartumCenters", centers);
        return "postpartumTableFragment";
    }
}
