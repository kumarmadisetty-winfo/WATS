package com.winfo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyDataCustomerDao;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;
@Service
public class CopyDataCustomerService {
	@Autowired
	CopyDataCustomerDao dao;

	 
	
	@Transactional
	public List<DomGenericResponseBean> copyData(CopyDataDetails copyDataDetails ) throws ParseException {
		return dao.copyData( copyDataDetails);

	}
}
