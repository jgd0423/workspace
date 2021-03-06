<%@page import="board.BoardDTO"%>
<%@page import="board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%

String no_ = request.getParameter("no");
int no = Integer.parseInt(no_);
BoardDAO dao = new BoardDAO();
dao.setUpdateHit(no);
BoardDTO dto = dao.getSelectOne(no);

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 보기</title>
</head>
<body>

<h2>게시글 보기</h2>

<table border="1" width="600">
	<tr>
		<td width="100px">작성자</td>
		<td><%=dto.getWriter() %></td>
	</tr>
	<tr>
		<td>이메일 : </td>
		<td><%=dto.getEmail() %></td>
	</tr>
	<tr>
		<td>제목 : </td>
		<td><%=dto.getSubject() %></td>
	</tr>
	<tr>
		<td>내용 : </td>
		<td><%=dto.getContent() %></td>
	</tr>
	<tr>
		<td>조회수 : </td>
		<td><%=dto.getHit() %></td>
	</tr>
	<tr>
		<td>작성일 : </td>
		<td><%=dto.getRegiDate() %></td>
	</tr>
</table>

<a href="#" onclick="move('A', '<%=no%>');">[답변하기]</a>
&nbsp;&nbsp;
<a href="#" onclick="move('M', '<%=no%>');">[수정하기]</a>
&nbsp;&nbsp;
<% if (dao.isLastChild(dto)) { %>
	<a href="#" onclick="move('D', '<%=no%>');">[삭제하기]</a>
<% } %>


<script>

function move(value1, value2) {
	if (value1 === 'A') {
		location.href='write.jsp?no=' + value2;
	} else if (value1 === 'M') {
		location.href='modify.jsp?no=' + value2;
	} else if (value1 === 'D') {
		location.href='delete.jsp?no=' + value2;
	}
}

</script>

</body>
</html>