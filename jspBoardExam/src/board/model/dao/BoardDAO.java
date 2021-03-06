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
			String sql = "INSERT INTO BOARD VALUES (seq_board.NEXTVAL, "
					+ "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, "
					+ "SYSDATE)";
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
			pstmt.setInt(10, dto.getRe_parent());
			pstmt.setInt(11, dto.getHit());
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
			String sql = "SELECT NVL(MAX(" + columnName + "), 0) FROM board";
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
			String sql = "SELECT * FROM board ORDER BY ref DESC, re_level ASC";
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
				dto.setRe_parent(rs.getInt("re_parent"));
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
	
	public ArrayList<BoardDTO> getList(int startRow, int endRow, String searchField, String searchData) {
		conn = db.dbConn();
		ArrayList<BoardDTO> arrayList = new ArrayList<>();
		try {
			String sql = "";
			String basic_sql = "SELECT * FROM board WHERE no > 0";
					
			if (searchField.equals("all") && searchData.length() > 0) {
				basic_sql += " AND subject LIKE ? OR content LIKE ?";
			} else if (searchField.length() > 0 && searchData.length() > 0) {
				basic_sql += " AND " + searchField + " LIKE ?";
			}
			
			basic_sql += " ORDER BY ref DESC, re_level ASC";
			sql += "SELECT * FROM ";
			sql += "(SELECT ROWNUM Rnum, a.* FROM ";
			sql += "(" + basic_sql + ") a) ";
			sql += "WHERE Rnum >= ? AND Rnum <= ?";

			pstmt = conn.prepareStatement(sql);

			if (searchField.equals("all") && searchData.length() > 0) {
				pstmt.setString(1, "%" + searchData + "%");
				pstmt.setString(2, "%" + searchData + "%");
				pstmt.setInt(3, startRow);
				pstmt.setInt(4, endRow);
			} else if (searchField.length() > 0 && searchData.length() > 0) {
				pstmt.setString(1, "%" + searchData + "%");
				pstmt.setInt(2, startRow);
				pstmt.setInt(3, endRow);
			} else if (searchField.length() == 0 && searchData.length() == 0) {
				pstmt.setInt(1, startRow);
				pstmt.setInt(2, endRow);
			}
			
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
	
//	public ArrayList<BoardDTO> getList(int startRow, int endRow) {
//		conn = db.dbConn();
//		ArrayList<BoardDTO> arrayList = new ArrayList<>();
//		try {
//			String basic_sql = "SELECT * FROM board ORDER BY ref desc, re_level asc";
//			String sql = "";
//			sql += "SELECT * FROM ";
//			sql += "(SELECT ROWNUM Rnum, a.* FROM ";
//			sql += "(" + basic_sql + ") a) ";
//			sql += "WHERE Rnum >= ? AND Rnum <= ?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, startRow);
//			pstmt.setInt(2, endRow);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				BoardDTO dto = new BoardDTO();
//				dto.setNo(rs.getInt("no"));
//				dto.setNum(rs.getInt("num"));
//				dto.setWriter(rs.getString("writer"));
//				dto.setSubject(rs.getString("subject"));
//				dto.setContent(rs.getString("content"));
//				dto.setEmail(rs.getString("email"));
//				dto.setPasswd(rs.getString("passwd"));
//				dto.setRef(rs.getInt("ref"));
//				dto.setRe_step(rs.getInt("re_step"));
//				dto.setRe_level(rs.getInt("re_level"));
//				dto.setRe_parent(rs.getInt("re_parent"));
//				dto.setHit(rs.getInt("hit"));
//				dto.setRegi_date(rs.getString("regi_date"));
//				arrayList.add(dto);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		} finally {
//			db.dbConnClose();
//		}
//		return arrayList;
//	}
	
	public BoardDTO getSelectOne(int no) {
		conn = db.dbConn();
		BoardDTO dto = new BoardDTO();
		try {
			String sql = "SELECT * "
					+ "FROM (SELECT b.*, LAG(no) OVER(ORDER BY ref DESC, re_level ASC) preNo, "
					+ "LAG(subject) OVER (ORDER BY ref DESC, re_level ASC) preSubject, "
					+ "LEAD(no) OVER (ORDER BY ref DESC, re_level ASC) nxtNo, "
					+ "LEAD(subject) OVER (ORDER BY ref DESC, re_level ASC) nxtSubject "
					+ "FROM board b ORDER BY ref DESC, re_level ASC) WHERE no = ?";
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
				dto.setRe_parent(rs.getInt("re_parent"));
				dto.setHit(rs.getInt("hit"));
				dto.setRegi_date(rs.getString("regi_date"));
				dto.setPreNo(rs.getInt("preNo"));
				dto.setPreSubject(rs.getString("preSubject"));
				dto.setNxtNo(rs.getInt("nxtNo"));
				dto.setNxtSubject(rs.getString("nxtSubject"));
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
			String sql = "SELECT COUNT(*) FROM board";
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
			String sql = "UPDATE board SET hit = (hit + 1) WHERE no = ?";
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
			String sql = "UPDATE board SET re_level = (re_level + 1) WHERE ref = ? AND re_level > ?";
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
	
	// 만들어본거
	public ArrayList<BoardDTO> getPagingList(int ONE_PAGE_ROWS, int pageNum) {
		conn = db.dbConn();
		
		int endNum = pageNum * ONE_PAGE_ROWS;
		int startNum = endNum - ONE_PAGE_ROWS + 1;
		
		ArrayList<BoardDTO> boardList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM "
					+ "(SELECT ROW_NUMBER() OVER (ORDER BY ref DESC, re_level ASC) ORDER_NUM, board.* FROM board) "
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
				dto.setRe_parent(rs.getInt("re_parent"));
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
	
	public int getTotalRecordCount(String searchField, String searchData) {
		conn = db.dbConn();
		int allRowsCount = 0;
		try {
			String sql = "";
			sql += "SELECT COUNT(*) FROM board WHERE no > 0";
			
			if (searchField.equals("all") && searchData.length() > 0) {
				sql += " AND subject LIKE ? OR content LIKE ?";
			} else if (searchField.length() > 0 && searchData.length() > 0) {
				sql += " AND " + searchField + " LIKE ?";
			}
			
			pstmt = conn.prepareStatement(sql);

			if (searchField.equals("all") && searchData.length() > 0) {
				pstmt.setString(1, "%" + searchData + "%");
				pstmt.setString(2, "%" + searchData + "%");
			} else if (searchField.length() > 0 && searchData.length() > 0) {
				pstmt.setString(1, "%" + searchData + "%");
			}

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
	
	// 검색 메소드
//	public int getTotalRecordCount(String searchField, String searchData) {
//		conn = db.dbConn();
//		int allRowsCount = 0;
//		try {
//			String sql = "";
//			if ((searchField == null || searchField.length() <= 0) || (searchData == null || searchData.length() <= 0)) {
//				sql = "SELECT COUNT(*) FROM board";
//				pstmt = conn.prepareStatement(sql);
//			} else if (searchField.equals("writer")) {
//				sql = "SELECT COUNT(*) FROM board WHERE writer LIKE ?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, "%" + searchData + "%");
//			} else if (searchField.equals("subject")) {
//				sql = "SELECT COUNT(*) FROM board WHERE subject LIKE ?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, "%" + searchData + "%");
//			} else if (searchField.equals("content")) {
//				sql = "SELECT COUNT(*) FROM board WHERE content LIKE ?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, "%" + searchData + "%");
//			} else if (searchField.equals("all")) {
//				sql = "SELECT COUNT(*) FROM board WHERE subject LIKE ? OR content LIKE ?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, "%" + searchData + "%");
//				pstmt.setString(2, "%" + searchData + "%");
//			}
//
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				allRowsCount = rs.getInt(1);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		} finally {
//			db.dbConnClose();
//		}
//		
//		return allRowsCount;
//	}
	
	public int setUpdate(BoardDTO dto) {
		conn = db.dbConn();
		int result = 0;
		try {
			String sql = "UPDATE board SET subject = ?, email = ?, content = ? WHERE no = ?";
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
			String sql = "DELETE FROM board WHERE no = ? ";
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
	
	public ArrayList<Integer> getLevelMinusStep(int ref) {
		ArrayList<Integer> levelMinusStepList = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT re_level - re_step a FROM board WHERE ref = ? ORDER BY a";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ref);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				levelMinusStepList.add(rs.getInt("a"));
			}			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return levelMinusStepList;
	}
	
	public boolean isHaveChild(BoardDTO dto) {
		conn = db.dbConn();
		boolean result = false;
		try {
			String sql = "SELECT (SELECT COUNT(*) FROM board WHERE re_parent = b.no) pcounter "
					+ "FROM board b WHERE no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNo());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					result = true;			
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}

		return result;
	}
	
	public boolean isSoloContent(BoardDTO dto) {
		conn = db.dbConn();
		boolean result = false;
		int totalRefCount = 0;
		try {
			String sql = "SELECT COUNT(*) FROM board WHERE ref = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getRef());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalRefCount = rs.getInt(1);
			}
			
			if (totalRefCount == 1) {
				result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}

		return result;
	}
	
	public int[] getNearByCurrentPost(BoardDTO dto) {
		conn = db.dbConn();
		int[] backAndForthNo = new int[2];
		try {
			String sql = "SELECT rnum, no "
					+ "FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) "
					+ "WHERE rnum = ((SELECT rnum FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) WHERE no = ?) - 1) "
					+ "OR rnum = ((SELECT rnum FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) WHERE no = ?) + 1)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNo());
			pstmt.setInt(2, dto.getNo());
			rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				backAndForthNo[i] = rs.getInt("no");
				i += 1;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		
		return backAndForthNo;
	}
	
	public int[] getNearByCurrentPostAndRnum(BoardDTO dto) {
		conn = db.dbConn();
		int[] backAndForthNoAndRnum = new int[3];
		int rNum = 0;
		try {
			String sql = "SELECT rnum, no, (SELECT MAX(ROWNUM) FROM board) MAXRnum "
					+ "FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) "
					+ "WHERE rnum = ((SELECT rnum FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) WHERE no = ?) - 1) "
					+ "OR rnum = ((SELECT rnum FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) WHERE no = ?) + 1)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNo());
			pstmt.setInt(2, dto.getNo());
			rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				backAndForthNoAndRnum[i] = rs.getInt("no");
				rNum = rs.getInt("maxrnum");
				i += 1;
			}
			backAndForthNoAndRnum[2] = rNum;
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		
		return backAndForthNoAndRnum;
	}
	
	public int getCurrentPostRnum(BoardDTO dto) {
		conn = db.dbConn();
		int currentPostRnum = 0;
		try {
			String sql = "SELECT rnum, no FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) WHERE rnum = ((SELECT rnum FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a) WHERE no = ?))";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNo());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				currentPostRnum = rs.getInt("rnum");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		
		return currentPostRnum;
	}
	
	public int getMaxRnum() {
		conn = db.dbConn();
		int maxRnum = 0;
		try {
			String sql = "SELECT MAX(rnum) FROM (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM board ORDER BY ref DESC, re_level ASC) a)";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxRnum = rs.getInt(1);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		
		return maxRnum;
	}
	
	public BoardDTO getViewPreNxt001(int no, String gubun) {
		BoardDTO dto = new BoardDTO();
		conn = db.dbConn();
		try {
			String sql_1 = "select Rnum from (select A.*, Rownum Rnum from (select no from board order by ref desc, re_level asc) A) where no = ?";
			String sql_2;
			if (gubun.equals("pre")) {
				sql_2 = "select max(Rnum) from (select A.*, Rownum Rnum from (select no from board order by ref desc, re_level asc) A) where Rnum < (" + sql_1 + ")";			
			} else {
				sql_2 = "select min(Rnum) from (select A.*, Rownum Rnum from (select no from board order by ref desc, re_level asc) A) where Rnum > (" + sql_1 + ")";
			}
			
			String sql = "select * from (select A.*, Rownum Rnum from (select * from board order by ref desc, re_level asc) A) where Rnum = (" + sql_2 + ")";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setNo(rs.getInt("no"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return dto;
	}
	
	public BoardDTO getViewPreNxt002(int no, String gubun) {
		BoardDTO dto = new BoardDTO();
		conn = db.dbConn();
		try {
			String sql;
			if (gubun.equals("pre")) {
				sql = "select * from (select no, subject, LAG(no) OVER (ORDER BY ref desc, re_level asc) newNo, LAG(subject) OVER (ORDER BY ref desc, re_level asc) newSubject from board order by ref desc, re_level asc) where no = ?";
			} else {
				sql = "select * from (select no, subject, LEAD(no) OVER (ORDER BY ref desc, re_level asc) newNo, LEAD(subject) OVER (ORDER BY ref desc, re_level asc) newSubject from board order by ref desc, re_level asc) where no = ?";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setNo(rs.getInt("no"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.dbConnClose();
		}
		return dto;
	}
}
