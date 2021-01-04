package board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import board.model.dto.BoardDTO;
import db.Db;
import db.DbImplOracle;

public class BoardDAO {
	// Field
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Db db = new DbImplOracle();
	
	public int setInsert(BoardDTO dto) {
		conn = db.dbConn();
		int result = 0;
		try {
			String sql = "insert into board values (seq_board.nextval, "
					+ "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "sysdate)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getWriter());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getEmail());
			pstmt.setString(6, dto.getPasswd());
			pstmt.setInt(7, dto.getRef());
			pstmt.setInt(8, dto.getRe_step());
			pstmt.setInt(9, dto.getRe_level());
			pstmt.setInt(10, dto.getHit());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return result;
	}
	
	public int getMaxValue(String columnName) {
		conn = db.dbConn();
		int result = 0;
		try {
			String sql = "select nvl(max(" + columnName + "), 0) from board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1); // 컬럼의 순서 입력해도 된다.
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return result;
	}
	
	public ArrayList<BoardDTO> getSelectAll() {
		conn = db.dbConn();
		ArrayList<BoardDTO> arrayList = new ArrayList<>();
		try {
			String sql = "select * from board order by ref desc, re_level asc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNo(rs.getInt("no"));
				dto.setNum(rs.getInt("num"));
				dto.setWriter(rs.getString("writer"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setEmail(rs.getString("email"));
				dto.setPasswd(rs.getString("passwd"));
				dto.setRef(rs.getInt("ref"));
				dto.setRe_step(rs.getInt("re_step"));
				dto.setRe_level(rs.getInt("re_level"));
				dto.setHit(rs.getInt("hit"));
				dto.setRegi_date(rs.getString("regi_date"));
				arrayList.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return arrayList;
	}
	
	public BoardDTO getSelectOne(int no) {
		conn = db.dbConn();
		BoardDTO dto = new BoardDTO();
		try {
			String sql = "select * from board where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setNo(rs.getInt("no"));
				dto.setNum(rs.getInt("num"));
				dto.setWriter(rs.getString("writer"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setEmail(rs.getString("email"));
				dto.setPasswd(rs.getString("passwd"));
				dto.setRef(rs.getInt("ref"));
				dto.setRe_step(rs.getInt("re_step"));
				dto.setRe_level(rs.getInt("re_level"));
				dto.setHit(rs.getInt("hit"));
				dto.setRegi_date(rs.getString("regi_date"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return dto;
	}
	
	public int getRowsCount() {
		conn = db.dbConn();
		int count = 0;
		try {
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return count;
	}
	
	public void setUpdateHit(int no) {
		conn = db.dbConn();
		try {
			String sql = "update board set hit = (hit + 1) where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
	}
	
	public void setUpdateReLevel(BoardDTO dto) {
		conn = db.dbConn();
		try {
			String sql = "update board set re_level = (re_level + 1) where ref = ? and re_level > ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getRef());
			pstmt.setInt(2, dto.getRe_level());
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
	}
	
	public ArrayList<BoardDTO> getPagingList(int ONE_PAGE_ROWS, int pageNum) {
		conn = db.dbConn();
		
		int endNum = pageNum * ONE_PAGE_ROWS;
		int startNum = endNum - ONE_PAGE_ROWS + 1;
		
		ArrayList<BoardDTO> boardList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM "
					+ "(SELECT ROW_NUMBER() OVER (ORDER BY ref desc, re_level asc) ORDER_NUM, board.* FROM board) "
					+ "WHERE ORDER_NUM BETWEEN ? AND ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startNum);
			pstmt.setInt(2, endNum);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNo(rs.getInt("no"));
				dto.setNum(rs.getInt("num"));
				dto.setWriter(rs.getString("writer"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setEmail(rs.getString("email"));
				dto.setPasswd(rs.getString("passwd"));
				dto.setRef(rs.getInt("ref"));
				dto.setRe_step(rs.getInt("re_step"));
				dto.setRe_level(rs.getInt("re_level"));
				dto.setHit(rs.getInt("hit"));
				dto.setRegi_date(rs.getString("regi_date"));
				boardList.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return boardList;
	}
	
	public int getAllRowsCount() {
		conn = db.dbConn();
		int allRowsCount = 0;
		try {
			String sql = "SELECT COUNT(*) FROM board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				allRowsCount = rs.getInt(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		
		return allRowsCount;
	}
	
	public int setUpdate(BoardDTO dto) {
		conn = db.dbConn();
		int result = 0;
		try {
			String sql = "update board set subject = ?, email = ?, content = ? where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getNo());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		
		return result;
	}
	
	public int deletePost(BoardDTO dto) {
		conn = db.dbConn();
		int result = 0;
		try {
			String sql = "delete from board where no = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNo());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return result;
	}
	
	public int findSameRefMaxRe_step(BoardDTO dto) {
		conn = db.dbConn();
		int result = 0;
		int ref = dto.getRef();
		try {
			String sql = "select max(re_step) from board where ref = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ref);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		return result;
	}
	
	public ArrayList<Integer> getLevelMinusStep() {
		ArrayList<Integer> levelMinusStepList = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT re_level - re_step a FROM board ORDER BY a";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				levelMinusStepList.add(rs.getInt("a"));
			}			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return levelMinusStepList;
	}
	
	public boolean isLastChild(BoardDTO dto) {
		conn = db.dbConn();
		boolean result = false;
		ArrayList<Integer> levelMinusStepList = getLevelMinusStep();
		ArrayList<Integer> lastChildNoList = new ArrayList<>();
		try {
			for (int i = 0; i < levelMinusStepList.size(); i++) {
				String sql = "SELECT no from board where re_level = (SELECT MAX(re_level) FROM board WHERE re_level - re_step = ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, levelMinusStepList.get(i));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					lastChildNoList.add(rs.getInt(1));
				}
			}
			
			if (lastChildNoList.contains(dto.getNo()))  {
				result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return result;
	}
}
