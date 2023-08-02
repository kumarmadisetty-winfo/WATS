package com.winfo.service;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.DeleteScriptsData;

@Service
public interface DeleteDataService {
	
	public  List<DomGenericResponseBean> deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException;
}
