<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/inc_header.jsp" %>

<form name="modifyForm">
	<input type="hidden" name="no" value="${dto.no }" />
	<table border="1" align="center" width="80%">
		<tr>
			<td colspan="2" align="center">
				<h2>수정하기</h2>
			</td>
		</tr>
		<tr>
			<td>번호</td>
			<td>${dto.no }</td>
		</tr>
		<tr>
			<td>아이디</td>
			<td>ID : ${dto.id } / 세션ID : ${sessionScope.cookId }</td>
		</tr>
		<tr>
			<td>비밀번호</td>
			<td><input type="text" name="passwd"></td>
		</tr>
		<tr>
			<td>이름</td>
			<td>${dto.name }</td>
		</tr>
		<tr>
			<td>성별</td>
			<td>${dto.gender }</td>
		</tr>
		<tr>
			<td>출생년도</td>
			<td><input type="text" name="bornYear" value="${dto.bornYear }"></td>
		</tr>
		<tr>
			<td>가입일시</td>
			<td>${dto.regiDate }</td>
		</tr>
		<tr>
			<td colspan="2" height="50" align="center">
				<button type="button" onclick="modifyInfo('${dto.no }')">수정하기</button>
			</td>
		</tr>
	</table>
</form>

<script>

function modifyInfo(no) {
	if (confirm("수정하시겠습니까?")) {
		document.modifyForm.method = 'post';
		document.modifyForm.action = '${path}/member_servlet/modifyProc.do';
		document.modifyForm.submit();
	}
}

</script>