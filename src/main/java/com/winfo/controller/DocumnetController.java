package com.winfo.controller;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.serviceImpl.DocumnetService;
import com.winfo.vo.DocumentsVo;
import com.winfo.vo.ResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumnetController {
	
	public static final Logger logger = Logger.getLogger(DocumnetController.class);

	final DocumnetService documnetsService;
	
	@PostMapping(value = "/downloadDocument" , produces= MediaType.APPLICATION_PDF_VALUE)
	@ApiOperation( value="Download Test Run/Script/Release Note PDF",notes = "<B> FileName and FilePath </B> should be provided to download PDF <br>"
			+ "will get file from object store and download PDF")
	@ApiResponses( value = { @ApiResponse( code=200,message="PDF downloaded successfully")})
	public ResponseEntity<StreamingResponseBody>  downloadDocument(@Valid @RequestBody DocumentsVo documentsVo) throws Exception {

			return documnetsService.retrieveDocumentsFromObjectStore(documentsVo);
	}
	
	@PostMapping(value = "/uploadDocument")
	@ApiOperation( value="Upload any file into object store",notes = "<B> FileName and FilePath </B> should be provided to download PDF <br>"
			+ "will get file from object store and download PDF. Also pass which object store want to upload")
	@ApiResponses( value = { @ApiResponse( code=200,message="PDF uploaded successfully")})
	public  ResponseEntity<ResponseDto>  uploadDocument(@RequestParam("file") MultipartFile request,@RequestParam("jsonBody") String documentsVoString) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentsVo documentsVo =objectMapper.readValue(documentsVoString, DocumentsVo.class);
		byte[] fileBytes = request.getInputStream().readAllBytes();
		ResponseDto responseDto =documnetsService.uploadDocumentsInObjectStore(documentsVo,fileBytes);
		if (responseDto.getStatusCode() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(responseDto);
		} else {
			return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
