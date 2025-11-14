package com.boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.entity.Keyword;


public interface KeywordRepository extends JpaRepository<Keyword, Long>{
	List<Keyword> findTop10ByKeywordStartingWithIgnoreCaseOrderBySearchCountDesc(String prefix);
	List<Keyword> findTop10ByKeywordContainingIgnoreCaseOrderBySearchCountDesc(String query);
}
