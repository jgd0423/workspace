<%@page import="member.model.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id="dto" class="member.model.MemberDTO" scope="page"></jsp:useBean>
<jsp:setProperty property="*" name="dto"/>

<%

MemberDAO dao = new MemberDAO();
int result = dao.getLogin(dto);
int cookNo = 0;

if(result > 0) {
	session.setAttribute("cookNo", result); // 세션 등록
	out.println("<script>");
	out.println("alert('정상적으로 로그인되었습니다.');");
	out.println("location.href='list.jsp';");
	out.println("</script>");
} else {
	out.println("<script>");
	out.println("alert('비밀번호가 맞지 않습니다. 다시 입력하세요.');");
	out.println("location.href='login.jsp';");
	out.println("</script>");
}

%>