package com.winfo.service;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.winfo.vo.DeleteScriptsVO;
import com.winfo.vo.ResponseDto;

@Service
public interface DeleteScriptsService {
	
	public  ResponseDto deleteScriptsFromLibrary(@RequestBody DeleteScriptsVO deletescriptsdata) throws ParseException;
}
