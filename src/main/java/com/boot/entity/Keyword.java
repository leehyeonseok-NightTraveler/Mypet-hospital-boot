package com.boot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SEARCH_KEYWORDS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword {

 	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "KEYWORD", nullable = false, length = 200)
    private String keyword;

    @Column(name = "SEARCH_COUNT")
    private Long searchCount;
    
    @Column(name = "SITE_URL", length = 500)
    private String siteUrl;
}
