package com.winfo.controller;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.winfo.serviceImpl.TemplateDownloadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class TemplateDownloadController {

	@Autowired
	private TemplateDownloadService templateDownloaderService;

	@ResponseBody
	@GetMapping(value = { "/downloadTemplate/{userName}", "/downloadTemplate/{userName}/{scriptId}" })
	@ApiOperation(value = "Download Template ", notes = " Download Template ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 400, message = "Bad Request Body"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<StreamingResponseBody> generateTemplete(@PathVariable Optional<Integer> scriptId,@PathVariable String userName)
			throws Exception {
		StreamingResponseBody stream = out -> {
			templateDownloaderService.generateTemplate(scriptId,userName).write(out);
		};

		return ResponseEntity.ok()
				.header("Content-Disposition",
						"attachment; filename=\"Winfo Test Automation Metadata Template_" + new Date() + ".xlsx\"")
				.body(stream);
	}

}
