package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiValidationDto {

	private List<LookUpCodeVO> lookupCodes = new ArrayList<>();
	private boolean flag;

}
