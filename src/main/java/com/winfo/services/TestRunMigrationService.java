package com.winfo.services;

import javax.transaction.Transactional;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winfo.dao.TestRunMigrationDao;
import com.winfo.vo.TestRunDetails;

@Service
public class TestRunMigrationService {

	@Autowired
	TestRunMigrationDao dao;

	@Transactional
	public String testRunMigration(TestRunDetails testRunDetails) throws ParseException, JsonProcessingException {
		return dao.testRunMigration(testRunDetails);

	}

}
