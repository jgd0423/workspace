
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@page import="java.sql.*"%>

<%
	String id = request.getParameter("id");
	String pw = request.getParameter("pw");
	String checkPw = request.getParameter("checkPw");
	String name = request.getParameter("name");
	String phone = request.getParameter("phone");
	String email = request.getParameter("email");
	String birthYear_ = request.getParameter("birthYear");
	int birthYear = Integer.parseInt(birthYear_);
	

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	int result = 0;

	try {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String dbUrl = "jdbc:oracle:thin:@localhost:1521/xe";
		String dbId = "jspTest";
		String dbPasswd = "1234";
		
		Class.forName(driver);
		conn = DriverManager.getConnection(dbUrl, dbId, dbPasswd);
		System.out.println("-- 오라클 접속 성공 --");
		


		String sql = "insert into joinTBL01 values (?, ?, ?, ?, ?, ?, ?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		pstmt.setString(3, checkPw);
		pstmt.setString(4, name);
		pstmt.setString(5, phone);
		pstmt.setString(6, email);
		pstmt.setInt(7, birthYear);
		// executeUpdate() 하기전에 값을 체크해야한다. 그래야 시퀀스 번호 안꼬인다.
		result = pstmt.executeUpdate();

		if (result > 0) { // 성공
			// response.sendRedirect("list.jsp");
		
			out.println("<script>");
			out.println("alert('정상 처리 되었습니다.');");
			out.println("location.href='list.jsp';");
			out.println("</script>");
		} else { // 실패
			out.println("<script>");
			out.println("alert('처리중 오류가 발생했습니다.');");
			out.println("location.href='joinForm.jsp';");
			out.println("</script>");
		}
		
	} catch(Exception e) {
		System.out.println("-- 오라클 접속 실패 --");
		e.printStackTrace();
	} finally {
		try {
			if (pstmt != null) { pstmt.close(); }
			if (conn != null) { conn.close(); }
		} catch(Exception e) {
			System.out.println("-- 오라클 접속 실패 --");
			e.printStackTrace();
		}
	}

%>


