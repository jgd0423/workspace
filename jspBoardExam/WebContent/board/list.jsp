<%@page import="java.util.Optional"%>
<%@page import="java.net.Inet4Address"%>
<%@page import="board.model.dto.BoardDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="board.model.dao.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%

String referer = request.getHeader("REFERER");
String ip = Inet4Address.getLocalHost().getHostAddress();

BoardDAO dao = new BoardDAO();
String searchField = request.getParameter("searchField");
String searchData = request.getParameter("searchData");

if ((searchField == null || searchField.length() <= 0) || (searchData == null || searchData.length() <= 0)) {
	searchField = "";
	searchData = "";
}

// 전체 게시글의 개수
int count = dao.getTotalRecordCount(searchField, searchData);

// 화면에 보여질 개수
final int PAGE_SIZE = 20;

// 페이징 바 길이
final int PAGE_BLOCK = 15;

// 현재 페이지 번호
String pageNum = Optional.ofNullable(request.getParameter("pageNum")).orElse("1"); 
int currentPage = Integer.parseInt(pageNum);

// 현재 페이지에 보여질 시작번호, 끝번호
int startRow = (currentPage - 1) * PAGE_SIZE + 1;
int endRow = currentPage * PAGE_SIZE;

// 테이블에 표시할 번호
int number = count - (currentPage - 1) * PAGE_SIZE;

// 전체 페이지 수
double totalPageDou = Math.ceil(count / (double)PAGE_SIZE);
int totalPage = (int)totalPageDou;

int pageCount = 0;
int startPage = 1;
int endPage = 1;
if (count > 0) {
	pageCount = count / PAGE_SIZE + (count % PAGE_SIZE == 0 ? 0 : 1);
	if (currentPage % PAGE_BLOCK != 0) {
		startPage = (int)(currentPage / PAGE_BLOCK) * PAGE_BLOCK + 1;
	} else {
		startPage = ((int)(currentPage / PAGE_BLOCK) - 1) * PAGE_BLOCK + 1;
	}
	
// 	int pageBlock = 10;
	endPage = startPage + PAGE_BLOCK - 1;
	if (endPage > pageCount) {
		endPage = pageCount;
	}
}



ArrayList<BoardDTO> boardList = dao.getList(startRow, endRow, searchField, searchData);


out.println("count : " + count + "<br>");
out.println("currentPage : " + currentPage + "<br>");
out.println("startRow : " + startRow + "<br>");
out.println("endRow : " + endRow + "<br>");
out.println("number : " + number + "<br>");
out.println("pageCount : " + pageCount + "<br>");
out.println("startPage : " + startPage + "<br>");
out.println("endPage : " + endPage + "<br>");
out.println("totalPage : " + totalPage + "<br>");

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<body>

referer: <%=referer %><br>
클라이언트ip: <%=request.getRemoteAddr() %><br>
요청URI: <%=request.getRequestURI() %><br>
컨텍스트경로: <%=request.getContextPath() %><br>
서버이름: <%=request.getServerName() %><br>
서버포트: <%=request.getServerPort() %><br><br>
requested URL: <%=request.getRequestURL() %><br>
requested Info: <%=request.getRequestURI() %><br>

<h2 align="center">게시글 목록</h2>


<div style="height:300px;">
	<table border="1" width="1500" align="center">
		<tr>
			<td colspan="5">
				<form name="searchForm">
					<select name="searchField">
						<option value="">-선택-</option>
						<option value="writer">작성자</option>
						<option value="subject">제목</option>
						<option value="content">내용</option>
						<option value="all">제목+내용</option>
					</select>
				
					<input type="text" name="searchData">
					<button type="button" onclick="search();">검색하기</button>
				</form>
			</td>
		</tr>
		<tr align="center">
			<td>순번</td>
			<td>제목</td>
			<td>작성자</td>
			<td>등록일</td>
			<td>조회수</td>
		</tr>
		<% for (int i = 0; i < boardList.size(); i++) { %>
			<% BoardDTO dto = boardList.get(i); %>
			
			<tr>
				<td><%=number-- %></td>
				<% if (dto.getRe_step() > 1) { %>
					<% String padding = (20 * (dto.getRe_step() - 1)) + "px"; %>
					<% String re = ""; %>
					<% for (int j = 0; j < dto.getRe_step() - 1; j++) { %>
						<% re += "Re:"; %>
					<% } %>
					<td>
						<a href="#" onclick="view('<%=dto.getNo()%>');" style="padding-left:<%=padding%>">
							<%=re %> <%=dto.getSubject() %>
						</a>
					</td>
				<% } else { %>
					<td>
						<a href="#" onclick="view('<%=dto.getNo()%>');"><%=dto.getSubject() %></a>
					</td>
				<% } %>
				<td><%=dto.getWriter() %></td>
				<td><%=dto.getRegi_date() %></td>	
				<td><%=dto.getHit() %></td>
			</tr>
		<% } %>
		<% if (boardList.size() == 0) { %>
			<tr>
				<td colspan="5" height="250px" align="center">글이 없습니다.</td>
			</tr>
		<% } %>
		<tr>
			<td colspan="5" align="center">
				<% if (currentPage <= PAGE_BLOCK) { %>
					[첫페이지]
					[이전10개]
				<% } else { %>
					<a href="list.jsp?pageNum=1&searchField=<%=searchField%>&searchData=<%=searchData%>">[첫페이지]</a>
					<a href="list.jsp?pageNum=<%=startPage - PAGE_BLOCK%>&searchField=<%=searchField%>&searchData=<%=searchData%>">[이전10개]</a>
				<% } %>
				&nbsp;&nbsp;&nbsp;
				<% for (int n = startPage; n <= endPage; n++) { %>
					<% if (n == currentPage) { %>
						<span style="color:red; font-weight: bold;">[<%=n %>]</span>
					<% } else { %>
						<a href="list.jsp?pageNum=<%=n%>&searchField=<%=searchField%>&searchData=<%=searchData%>"><%=n %></a>
					<% } %>
					&nbsp;&nbsp;
				<% } %>
				&nbsp;
				<% if (currentPage >= totalPage - PAGE_BLOCK) { %>
					[다음10개]
					[끝페이지]
				<% } else { %>
					<a href="list.jsp?pageNum=<%=startPage + PAGE_BLOCK%>&searchField=<%=searchField%>&searchData=<%=searchData%>">[다음10개]</a>
					<a href="list.jsp?pageNum=<%=totalPage %>&searchField=<%=searchField%>&searchData=<%=searchData%>">[끝페이지]</a>
				<% } %>				
			</td>
		</tr>
	</table>
<br>
<br>
<div align="center">
	<button type="button" onclick="goToWrite();">글쓰기</button>
</div>
</div>


<script>
function view(no) {
	location.href='view.jsp?no=' + no;
}

function goToWrite() {
	location.href='write.jsp';
}

function search() {
	searchForm.method = "post";
	searchForm.action = "listSearch.jsp";
	searchForm.submit();
}
</script>

</body>
</html>