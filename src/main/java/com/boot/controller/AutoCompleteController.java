package com.boot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.dto.KeywordDTO;
import com.boot.service.AutoCompleteService;

import lombok.RequiredArgsConstructor;

//검색 자동완성 기능
//jsp 호출용
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AutoCompleteController {

    private final AutoCompleteService service;

    @GetMapping("/api/autocomplete")
    public List<KeywordDTO> getSuggestions(@RequestParam String query) {
        return service.getSuggestions(query);
    }
    
    
}
