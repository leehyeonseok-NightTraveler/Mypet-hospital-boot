package com.boot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class SearchController {
	
    @GetMapping("/search")
    public String searchResult(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "searchResult";
    }
    
    @RequestMapping("/login")
    public String login() {
    	
		return "login";
	}
    
    @GetMapping("/search_results")
    public String searchResults(
            @RequestParam("siteurl") String siteurlJson,
            @RequestParam("keyword") String keywordJson,
            Model model) {

        try {

            if (siteurlJson == null || siteurlJson.trim().isEmpty() ||
                keywordJson == null || keywordJson.trim().isEmpty()) {

                model.addAttribute("siteurlList", List.of());
                model.addAttribute("keywordList", List.of());
                return "search_results";
            }

            ObjectMapper mapper = new ObjectMapper();

            List<String> siteurlList =
                mapper.readValue(siteurlJson, new TypeReference<List<String>>() {});

            List<String> keywordList =
                mapper.readValue(keywordJson, new TypeReference<List<String>>() {});

            model.addAttribute("siteurlList", siteurlList);
            model.addAttribute("keywordList", keywordList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "search_results";
    }

    
    @GetMapping("/auto_search") 
    public String search() {
        return "auto_search"; 
    }
}
