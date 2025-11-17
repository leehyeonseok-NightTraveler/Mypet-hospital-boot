package com.boot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.boot.dto.KeywordDTO;
import com.boot.entity.Keyword;
import com.boot.repository.KeywordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutoCompleteService {

    private final KeywordRepository repo;

    public List<KeywordDTO> getSuggestions(String q) {
        if (q == null || q.isBlank()) return Collections.emptyList();

        // prefix 검색 + 인덱스 사용 → 아주 빠름
        List<Keyword> prefix = repo
                .findTop10ByKeywordStartingWithIgnoreCaseOrderBySearchCountDesc(q);

        if (prefix.size() >= 10) {
            return convert(prefix);
        }
        
        // contain 검색 (선택)
        List<Keyword> contain = repo
                .findTop10ByKeywordContainingIgnoreCaseOrderBySearchCountDesc(q);

        LinkedHashSet<Keyword> set = new LinkedHashSet<>();
        set.addAll(prefix);
        set.addAll(contain);

        return convert(new ArrayList<>(set));
    }

    private List<KeywordDTO> convert(List<Keyword> list) {
        return list.stream()
                .limit(10)
                .map(k -> new KeywordDTO(k.getKeyword(), k.getSiteUrl()))
                .toList();
    }
}
