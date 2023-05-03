package com.winfo.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMetaData;

@Repository
public interface ScriptMetaDataRepository extends JpaRepository<ScriptMetaData, Integer> {

}
