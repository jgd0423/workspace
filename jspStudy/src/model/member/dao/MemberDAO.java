package model.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.Db;
import db.DbExample;
import db.DbImplOracle;
import model.member.dto.MemberDTO;

public class MemberDAO {
	// Field
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	// Method
	public Connection getConn() {
		conn = DbExample.getConn();
		return conn;
	}
	
	public void getConnClose(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		DbExample.getConnClose(rs, pstmt, conn);
	}
	
	public int setInsert(MemberDTO dto) {
		conn = getConn();
		int result = 0;
		try {
			String sql = "INSERT INTO member VALUES (seq_member.NEXTVAL, "
					+ "?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPasswd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getGender());
			pstmt.setInt(5, dto.getBornYear());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose(rs, pstmt, conn);
		}
		return result;
	}
	
	public ArrayList<MemberDTO> getSelectAll() {
		conn = getConn();
		ArrayList<MemberDTO> arrayList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM member ORDER BY no ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNo(rs.getInt("no"));
				dto.setId(rs.getString("id"));
				dto.setPasswd(rs.getString("passwd"));
				dto.setName(rs.getString("name"));
				dto.setGender(rs.getString("gender"));
				dto.setBornYear(rs.getInt("bornYear"));
				dto.setRegiDate(rs.getTimestamp("regiDate"));
				arrayList.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose(rs, pstmt, conn);
		}
		return arrayList;
	}
	
	public MemberDTO getSelectOne(int no) {
		conn = getConn();
		MemberDTO dto = new MemberDTO();
		try {
			String sql = "SELECT * FROM member WHERE no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setNo(rs.getInt("no"));
				dto.setId(rs.getString("id"));
				dto.setPasswd(rs.getString("passwd"));
				dto.setName(rs.getString("name"));
				dto.setGender(rs.getString("gender"));
				dto.setBornYear(rs.getInt("bornYear"));
				dto.setRegiDate(rs.getTimestamp("regiDate"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose(rs, pstmt, conn);
		}
		return dto;
	}
	
	public int setUpdate(MemberDTO dto) {
		conn = getConn();
		int result = 0;
		try {
			String sql = "UPDATE member SET "
					+ "bornYear = ? "
					+ "WHERE no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBornYear());
			pstmt.setInt(2, dto.getNo());

			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose(rs, pstmt, conn);
		}
		
		return result;
	}
	
	public int setDelete(MemberDTO dto) {
		conn = getConn();
		int result = 0;
		try {
			String sql = "DELETE FROM member WHERE no = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNo());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getConnClose(rs, pstmt, conn);
		}
		return result;
	}
	
	public MemberDTO getLogin(MemberDTO dto) {
		conn = getConn();
		MemberDTO resultDto = new MemberDTO();
		try {
			String sql = "select * from member where id = ? and passwd = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPasswd());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				resultDto.setNo(rs.getInt("no"));
				resultDto.setId(rs.getString("id"));
				resultDto.setPasswd(rs.getString("passwd"));
				resultDto.setName(rs.getString("name"));
				resultDto.setGender(rs.getString("gender"));
				resultDto.setBornYear(rs.getInt("bornYear"));
				resultDto.setRegiDate(rs.getTimestamp("regiDate"));
			}
		} catch(Exception e) {
			System.out.println("-- 오라클 접속 실패 --");
			e.printStackTrace();
		} finally {
			getConnClose(rs, pstmt, conn);
		}
		return resultDto;
	}
}