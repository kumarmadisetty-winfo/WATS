package com.winfo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.winfo.vo.ScriptHealVo;

@Service
public interface ScriptHealService {

	public ResponseEntity<List<ScriptHealVo>> getNewInputParameters(String targetApplication, String productVersion, String module) throws IOException;
}
