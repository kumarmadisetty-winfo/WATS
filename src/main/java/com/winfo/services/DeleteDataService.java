package com.winfo.services;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.winfo.vo.DomGenericResponseBean;



import com.winfo.dao.DeleteDataDAO;
import com.winfo.vo.DeleteScriptsData;

@Service
public class DeleteDataService {
	@Autowired
	DeleteDataDAO dao;
	
	public  List<DomGenericResponseBean> deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException {
		return dao.deleteData(deletescriptsdata);
		
	}
}
