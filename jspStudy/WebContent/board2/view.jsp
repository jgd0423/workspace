<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/inc_header.jsp" %>

<form name="viewForm">
	<c:choose>
		<c:when test="${tempPage == 'viewPasswdPage' }">
			<table border="1" align="center" width="80%">
				<tr>
					<td colspan="2">
						<h2>글상세보기 (비밀글입니다.)</h2>
					</td>
				</tr>
				<tr>
					<td width="150">비밀번호</td>
					<td>
						<input type="password" name="view_passwd" id="view_passwd" />
						<button type="button" id="btnViewPasswd" onclick="goView(${dto.no})">확인</button>
					</td>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
			<table border="1" align="center" width="80%">
				<tr>
					<td colspan="2">
						<h1>글상세보기</h1>
						<input type="text" name="no" value="${dto.no }" />
					</td>
				</tr>
				<tr>
					<td width="150px">작성자 : </td>
					<td>${dto.writer}</td>
				</tr>
				<tr>
					<td>제목 : </td>
					<td>${dto.subject}</td>
				</tr>
				<tr>
					<td>내용 : </td>
					<td id="content">${dto.content}</td>
				</tr>
				<tr>
					<td>이메일 : </td>
					<td>${dto.email}</td>
				</tr>
				<tr>
					<td>조회수 : </td>
					<td>${dto.hit}</td>
				</tr>
				<tr>
					<td>현재글의 답변글 : </td>
					<td>${dto.child_counter}</td>
				</tr>
				<tr>
					<td colspan="2">
						<button type="button" onclick="goWrite()">글쓰기</button>
						<c:if test="${dto.noticeNo == 0 }">
							<button type="button" onclick="goReply(${dto.no})">답변쓰기</button>
						</c:if>
						<button type="button" onclick="goModify(${dto.no})">수정하기</button>
						<c:if test="${dto.child_counter == 0 }">
							<button type="button" onclick="goDelete(${dto.no})">삭제하기</button>
						</c:if>
						<button type="button" onclick="goList()">목록으로</button>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="50px">
						<table border="1" width="100%" align="center">
							<tr>
								<td width="100px">이전글 : </td>
								<td>
									<c:if test="${dto.preSubject == null }">
										이전글이 없습니다.
									</c:if>
									<c:if test="${dto.preSubject != null }">
										<a href="#" onclick="goNearPage(${dto.preNo})">${dto.preSubject }</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td width="100px">다음글 : </td>
								<td>
									<c:if test="${dto.nxtSubject == null }">
										다음글이 없습니다.
									</c:if>
									<c:if test="${dto.nxtSubject != null }">
										<a href="#" onclick="goNearPage(${dto.nxtNo})">${dto.nxtSubject }</a>
									</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="50px" style="padding: 20 0 0 0;">
						<a name="comment"></a>
						<jsp:include page="./comment_list.jsp" />
					</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>
</form>


<script>

function goNearPage(no) {
	location.href = `${path}/board2_servlet/view.do?no=\${no}`;
}

function goList() {
	location.href = '${path}/board2_servlet/list.do';
}

function goWrite() {
	location.href = '${path}/board2_servlet/write.do';
}

function goView(no) {
	document.viewForm.method = 'post';
	document.viewForm.action = `${path}/board2_servlet/view.do?no=\${no}`;
	document.viewForm.submit();
}

function goReply(no) {
	location.href = `${path}/board2_servlet/reply.do?no=\${no}`;
}

function goModify(no) {
	location.href = `${path}/board2_servlet/modify.do?no=\${no}`;
}

function goDelete(no) {
	location.href = `${path}/board2_servlet/delete.do?no=\${no}`;
}

</script>