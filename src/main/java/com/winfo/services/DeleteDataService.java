package com.winfo.services;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.winfo.dao.DeleteDataDAO;
import com.winfo.vo.DeleteScriptsData;

@Service
public class DeleteDataService {
	@Autowired
	DeleteDataDAO dao;
	
	public String deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException {
		return dao.deleteData(deletescriptsdata);
		
	}
}
