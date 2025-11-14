<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 자동완성 CSS & JS -->
<link rel="stylesheet" href="/css/auto_search.css">
<script src="/js/auto_search.js"></script>

<!-- 플로팅 아이콘 -->
<div class="floating-icons">
    <a href="https://www.instagram.com/khieiorkr/" target="_blank" class="icon-instagram">
        <img src="https://img.icons8.com/fluent/48/000000/instagram-new.png" alt="인스타그램"/>
    </a>
    <a href="https://www.youtube.com/@KH_academy" target="_blank" class="icon-youtube">
        <img src="https://img.icons8.com/color/48/youtube-play.png" alt="유튜브"/>
    </a>
    <a href="/map" class="icon-map">
        <img src="https://img.icons8.com/color/48/000000/map-marker.png" alt="오시는길"/>
    </a>
</div>

<header>
    <div class="inner">

        <!-- 로고 -->
        <h1 class="logo">
            <a href="/mainpage">
                <img src="/img/mypet.png">MY PET 동물병원
            </a>
        </h1>

        <!-- ⭐ 중앙 검색창 영역 ⭐ -->
        <div class="search-center">
            <div class="search-area">
                <input type="text" id="query" placeholder="검색어 입력..." oninput="searchKeyword()" autocomplete="off">
                <button type="button" id="sendBtn">검색</button>
                <div id="box" class="autocomplete-box"></div>
            </div>
        </div>

        <!-- 오른쪽 계정 메뉴 -->
        <ul class="util">
            <c:choose>
                <c:when test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'USER'}">
                    <li><a href="/mypage_userinfo">마이페이지</a></li>
                    <li><a href="/logout">로그아웃</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="/login">로그인</a></li>
                    <li><a href="/register">회원가입</a></li>
                </c:otherwise>
            </c:choose>
        </ul>

        <!-- 메뉴 -->
        <ul id="gnb">
            <li><a href="/hospital_info">병원소개</a></li>

            <li class="dropdown-parent">
                <a href="#">게시판</a>
                <ul class="submenu">
                    <li><a href="/notices_list">공지사항</a></li>
                    <li><a href="#">자유게시판</a></li>
                    <li><a href="/qna_page">Q&A</a></li>
                </ul>
            </li>

            <c:choose>
                <c:when test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'USER'}">
                    <li><a href="/reservation">예약</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="/login">예약</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</header>

<!-- 검색 결과 전송용 히든 폼 -->
<form id="searchForm" action="/search_results" method="get" style="display:none;">
    <input type="hidden" name="siteurl" id="siteurl">
    <input type="hidden" name="keyword" id="keyword">
</form>
