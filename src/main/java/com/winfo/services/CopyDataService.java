package com.winfo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyDataDao;
import com.winfo.vo.CopyDataDetails;
@Service
public class CopyDataService {
	@Autowired
	CopyDataDao dao;

	 
	
	@Transactional
	public String copyData(CopyDataDetails copyDataDetails ) throws ParseException {
		return dao.copyData( copyDataDetails);

	}
}
