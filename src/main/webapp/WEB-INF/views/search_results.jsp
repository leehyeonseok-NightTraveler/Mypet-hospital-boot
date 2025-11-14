<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>검색 결과</title>
</head>
<body>
  <h2>🔍 검색 결과 목록</h2>

  <table>
    <tr>
      <th>키워드</th>
    </tr>

    <c:forEach var="keyword" items="${keywordList}" varStatus="status">
      <tr>
        <td>
          <a href="${siteurlList[status.index]}" target="_blank">
            ${keyword}
          </a>
        </td>
      </tr>
    </c:forEach>
  </table>

</body>
</html>
