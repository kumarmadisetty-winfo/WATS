package com.winfo.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiValidationDto {

	private List<LookUpCodeVO> lookupCodes = new ArrayList<>();

	@JsonProperty("flag")
	private boolean flag;

}
