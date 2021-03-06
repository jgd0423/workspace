<%@page import="java.util.ArrayList"%>
<%@page import="java.net.Inet4Address"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/include_top_import.jsp" %>
<%@ include file="../include/include_top_common.jsp" %>
<%@ include file="../include/include_session.jsp" %>
<%@ include file="../include/include_session_check.jsp" %>
<% request.setCharacterEncoding("UTF-8"); %>

<%

MemberDAO dao = new MemberDAO();
MemberDTO dto = dao.getSelectOne(cookNo);

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수정하기</title>
</head>
<body>

<table border="1" width="100%">
	<tr>
		<td>
		<!-- 상단 메뉴 -->
		<jsp:include page="../include/include_top_menu.jsp" flush="false">
			<jsp:param name="ip" value="<%=ip %>"/>
			<jsp:param name="cookNo" value="<%=cookNo %>"/>
			<jsp:param name="cookName" value="<%=cookName %>"/>
		</jsp:include>
		<!-- 상단 메뉴 -->
		</td>
	</tr>
	<tr>
		<td style="padding: 50px 20px;">
		<!-- 중단 메뉴 -->
		<h2>수정하기</h2>
		<form name="modifyForm">
			<input type="hidden" name="no" value="<%=dto.getNo() %>" />
			<table border="1">
				<tr>
					<td>번호</td>
					<td><%=dto.getNo() %></td>
				</tr>
				<tr>
					<td>아이디</td>
					<td><%=dto.getId() %></td>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td><input type="text" name="passwd"></td>
				</tr>
				<tr>
					<td>이름</td>
					<td><%=dto.getName() %></td>
				</tr>
				<tr>
					<td>성별</td>
					<td><%=dto.getGender() %></td>
				</tr>
				<tr>
					<td>생년월일</td>
					<td><input type="text" name="bornYear" value="<%=dto.getBornYear()%>"></td>
				</tr>
				<tr>
					<td>가입일시</td>
					<td><%=dto.getRegiDate() %></td>
				</tr>
			</table>
			<br>
			<a href="#" id="btn">[수정하기]</a>
		</form>
		<!-- 중단 메뉴 -->
		</td>
	</tr>
	<tr>
		<td>
		<!-- 하단 메뉴 -->
		<jsp:include page="../include/include_bottom_menu.jsp" flush="false"></jsp:include>
		<!-- 하단 메뉴 -->
		</td>
	</tr>
</table>

<script>
const btn = document.querySelector('#btn');

function modifyInfo() {
	if (document.modifyForm.passwd.value === "") {
		alert("비밀번호를 입력하세요.");
		document.modifyForm.passwd.focus();
		return;
	}
	
	if (confirm("수정하시겠습니까?")) {
		document.modifyForm.method = "post";
		document.modifyForm.action = "modifyProc.jsp";
		document.modifyForm.submit();		
	}
}

btn.addEventListener('click', modifyInfo);
</script>

</body>
</html>