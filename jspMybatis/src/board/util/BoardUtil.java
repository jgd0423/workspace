package board.util;

import java.util.ArrayList;
import java.util.Map;

import board.model.dao.BoardDAO;
import common.Util;


public class BoardUtil extends Util {
	public String tblCheck(String tbl, String defaultTbl) {
		if (tbl == null || tbl.trim().equals("")) {
			tbl = defaultTbl;
		}
		tbl = tbl.trim();
		return tbl;
	}
	
	public ArrayList<String> tblStatus(String tbl) {
		BoardDAO dao = new BoardDAO();
		Map<String, Object> tblStatusMap = dao.isUsingTable(tbl);
		
		ArrayList<String> tblStatus = new ArrayList<>();
		tblStatus.add((String)tblStatusMap.get("SERVICEGUBUN"));
		tblStatus.add((String)tblStatusMap.get("TBLNAME"));
		return tblStatus;
	}
	
	// overloading
	public String[] searchCheck(String search_option, String search_data) {
		String[] result = new String[2];
		
		if (search_option == null || search_option.trim().equals("")) {
			search_option = "";
		}
		search_option = search_option.trim();
		if (search_option.equals("")) {}
		else if (search_option.equals("writer")) {}
		else if (search_option.equals("subject")) {}
		else if (search_option.equals("content")) {}
		else if (search_option.equals("writer_subject_content")) {}
		else { search_option = ""; }
		
		if (search_data == null || search_data.trim().equals("")) {
			search_data = "";
		}
		search_data = search_data.trim();
		
		if (search_option.equals("") || search_data.equals("")) {
			search_option = "";
			search_data = "";
		}
		
		result[0] = search_option;
		result[1] = search_data;
		return result;
	}
}
