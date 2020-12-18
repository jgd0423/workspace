<%@page import="model.member.MemberExample"%>
<%@page import="model.member.MemberDTO"%>
<%@page import="model.member.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id="dto" class="model.member.MemberDTO" scope="page"></jsp:useBean>
<jsp:setProperty property="*" name="dto"/>

<%

MemberExample dao = new MemberExample();
MemberDTO dbDto = dao.getSelectOne(dto.getId());
String pwd = dto.getPwd();
String dbPwd = dbDto.getPwd();

if (!pwd.equals(dbPwd)) {
	out.println("<script>");
	out.println("alert('비밀번호가 다릅니다.')");
	out.println("history.back();");
	out.println("</script>");
	return;
}
int result = dao.setUpdate(dto);
if (result > 0) {
	out.println("<script>");
	out.println("alert('정상적으로 수정되었습니다.')");
	out.println("location.href='view.jsp?id=" + dto.getId() + "';");
	out.println("</script>");
} else {
	out.println("<script>");
	out.println("alert('DB처리 중 오류가 발생했습니다.')");
	out.println("history.back();");
	out.println("</script>");
}


%>