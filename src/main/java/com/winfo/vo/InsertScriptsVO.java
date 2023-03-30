package com.winfo.vo;

import java.util.List;

import lombok.Data;

@Data
public class InsertScriptsVO {

	private int testSetId;

	private List<Integer> listOfLineIds;

	private Integer incrementalValue;

}
