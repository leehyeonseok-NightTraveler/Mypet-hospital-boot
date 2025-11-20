<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지 - 멤버십 정보</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/userinfo_style.css">
    <link rel="stylesheet" href="/css/membership.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="mypage-container">

    <aside class="sidemenu">
        <h2>마이페이지 🐾</h2>
        <a href="/mypage_userinfo">내 정보</a>
        <a href="/mypage_petlist">펫 목록</a>
        <a href="/mypage_membership" class="active">멤버십</a>
        <a href="/mypage_medical">진료 내역</a>
        <a href="/mypage_grooming">미용 내역</a>
    </aside>


    <section class="content">

        <h2>멤버십 정보</h2>

        <div class="profile-box">
            <h2>
                ${loginUser.user_name}
                <span class="badge badge-${currentGrade}">
                    ${currentGrade}
                </span>
            </h2>

            <p>멤버십 만료일: <strong>${expiryDate}</strong></p>
        </div>

        <div class="section">
            <h3 class="section-title">멤버십 혜택 안내</h3>

            <table>
                <tr>
                    <th>등급</th>
                    <th>혜택 내용</th>
                </tr>

                <tr>
                    <td><span class="badge badge-플래티넘">플래티넘</span></td>
                    <td>
                        ✔ 진료 20% 할인<br>
                        ✔ 미용 20% 할인<br>
                        ✔ 연 1회 무료 종합검진<br>
                        ✔ 우선 예약
                    </td>
                </tr>

                <tr>
                    <td><span class="badge badge-골드">골드</span></td>
                    <td>
                        ✔ 진료 10% 할인<br>
                        ✔ 미용 10% 할인<br>
                        ✔ 연 1회 부분 검진 무료
                    </td>
                </tr>

                <tr>
                    <td><span class="badge badge-브론즈">브론즈</span></td>
                    <td>
                        ✔ 진료 5% 할인<br>
                        ✔ 미용 5% 할인
                    </td>
                </tr>

                <tr>
                    <td><span class="badge badge-노말">노말</span></td>
                    <td>
                        ✔ 신규 고객 기본 혜택 제공
                    </td>
                </tr>
            </table>
        </div>


        <div class="section">
            <h3 class="section-title">등급 이력</h3>

            <table>
                <tr>
                    <th>등급</th>
                    <th>시작일</th>
                    <th>종료일</th>
                    <th>기준 방문 횟수</th>
                    <th>평가일</th>
                </tr>

                <c:forEach var="g" items="${gradeHistory}">
                    <tr>
                        <td><span class="badge badge-${g.grade}">${g.grade}</span></td>
                        <td>${g.start_date}</td>
                        <td>${g.end_date}</td>
                        <td>${g.evaluation_visits}</td>
                        <td>${g.evaluation_date}</td>
                    </tr>
                </c:forEach>

                <c:if test="${empty gradeHistory}">
                    <tr>
                        <td colspan="5">등급 이력이 없습니다.</td>
                    </tr>
                </c:if>
            </table>
        </div>


        <div class="section">
            <h3 class="section-title">서비스 이용 내역</h3>

            <table>
                <tr>
                    <th>방문일</th>
                    <th>종류</th>
                    <th>항목</th>
                    <th>메모</th>
                    <th>완료 여부</th>
                </tr>

                <c:forEach var="s" items="${serviceHistory}">
                    <tr>
                        <td>${s.service_date}</td>
                        <td>${s.service_type}</td>
                        <td>${s.service_item}</td>
                        <td>${s.details_memo}</td>
                        <td>
                            <c:choose>
                                <c:when test="${s.completion_status == 'Y'}">
                                    <span style="color:green; font-weight:bold;">완료</span>
                                </c:when>
                                <c:otherwise>
                                    <span style="color:red; font-weight:bold;">대기</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty serviceHistory}">
                    <tr>
                        <td colspan="5">등록된 방문 이력이 없습니다.</td>
                    </tr>
                </c:if>
            </table>
        </div>

    </section>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>