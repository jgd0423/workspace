package jspInterfaceTest.model.member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MemberDAOImplMySQL implements MemberDAO {
	// Field
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	// Method
	@Override
	public void getConn() {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String dbUrl = "jdbc:mysql://localhost:3306/jspInterfaceTest";
			String dbId = "jspInterfaceTest";
			String dbPasswd = "1234";
			
			Class.forName(driver);
			conn = DriverManager.getConnection(dbUrl, dbId, dbPasswd);
			System.out.println("-- MySQL 접속 성공 --");					
		} catch(Exception e) {
			System.out.println("-- MySQL 접속 실패 --");
			e.printStackTrace();
		}
	}
	
	public void getConnClose() {
		try {
			if (rs != null) { rs.close(); }
			if (pstmt != null) { pstmt.close(); }
			if (conn != null) { conn.close(); }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int setInsert(MemberDTO dto) {
		getConn();
		int result = 0;
		try {
			String sql = "insert into member values ("
					+ "?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getPhone());
			pstmt.setString(5, dto.getJob());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose();
		}
		return result;
	}

	@Override
	public ArrayList<MemberDTO> getAllMembers() {
		getConn();
		ArrayList<MemberDTO> resumeList = new ArrayList<>();
		try {
			String sql = "select * from member order by id";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setJob(rs.getString("job"));
				resumeList.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose();
		}
		return resumeList;
	}

	@Override
	public MemberDTO getOneMember(String id) {
		getConn();
		MemberDTO dto = new MemberDTO();
		try {
			String sql = "select * from member where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setJob(rs.getString("job"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose();
		}
		return dto;
	}

	@Override
	public int setUpdate(MemberDTO dto) {
		getConn();
		int result = 0;
		try {
			String sql = "update member set phone = ?, job = ? where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPhone());
			pstmt.setString(2, dto.getJob());
			pstmt.setString(3, dto.getId());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose();
		}
		return result;
	}

	@Override
	public int setDelete(MemberDTO dto) {
		getConn();
		int result = 0;
		try {
			String sql = "delete from member where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose();
		}
		return result;
	}

}
