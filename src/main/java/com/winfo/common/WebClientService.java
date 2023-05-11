package com.winfo.common;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.vo.ApiValidationVO;

import reactor.core.publisher.Mono;

@Component
public class WebClientService {
	private final Logger logger = LogManager.getLogger(WebClientService.class);

	public void executeWebClientApi(ApiValidationVO apiValidationData, String userName, String password) {
		logger.info(
				"Received details as input in executeSmartBearApi: URL - {}, HTTP Type - {}, Request Header - {}, Request Body - {}, Response - {}",
				apiValidationData.getUrl(), apiValidationData.getHttpType(), apiValidationData.getRequestHeader(),
				apiValidationData.getRequestBody(), apiValidationData.getResponse());

		try {
			WebClient client = WebClient.create();

			// Setting headers
			HttpHeaders headers = new HttpHeaders();
			MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
			if (apiValidationData.getRequestHeader() != null) {
				requestHeaders.setAll(apiValidationData.getRequestHeader());
				headers.addAll(requestHeaders);
			}
			headers.setBasicAuth(userName, password);

			// Fetching HttpMethod
			HttpMethod httpMethod = HttpMethod.valueOf(apiValidationData.getHttpType());

			Mono<String> bodyToMono;

			// Checking if body required or not for API.
			if (apiValidationData.getRequestBody() != null
					&& !StringUtils.isEmpty(apiValidationData.getRequestBody())) {
				bodyToMono = client.method(httpMethod).uri(apiValidationData.getUrl())
						.headers(httpHeaders -> httpHeaders.addAll(headers)).contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(apiValidationData.getRequestBody())).retrieve()
						.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
								clientResponse -> Mono.error(new NotFoundException("Endpoint not found")))
						.onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
								clientResponse -> Mono.error(new WatsEBSCustomException(401, "Authentication Error")))
						.onStatus(httpStatus -> HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus),
								clientResponse -> Mono.error(new InternalServerErrorException("Internal server error")))
						.onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus),
								clientResponse -> Mono.error(new WatsEBSCustomException(400,"Bad Request")))
						.onStatus(httpStatus -> HttpStatus.SERVICE_UNAVAILABLE.equals(httpStatus),
								clientResponse -> Mono.error(new WatsEBSCustomException(503,"Service Unavailable")))
						.bodyToMono(String.class);
			} else {
				bodyToMono = client.method(httpMethod).uri(apiValidationData.getUrl())
						.headers(httpHeaders -> httpHeaders.addAll(headers)).accept(MediaType.APPLICATION_JSON)
						.retrieve()
						.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
								clientResponse -> Mono.error(new NotFoundException("Endpoint not found")))
						.onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
								clientResponse -> Mono.error(new WatsEBSCustomException(401, "Authentication Error")))
						.onStatus(httpStatus -> HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus),
								clientResponse -> Mono.error(new InternalServerErrorException("Internal server error")))
						.onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus),
								clientResponse -> Mono.error(new WatsEBSCustomException(400,"Bad Request")))
						.onStatus(httpStatus -> HttpStatus.SERVICE_UNAVAILABLE.equals(httpStatus),
								clientResponse -> Mono.error(new WatsEBSCustomException(503,"Service Unavailable")))
						.bodyToMono(String.class);
			}

			// Getting the response.
			String result = bodyToMono.block();
			apiValidationData.setResponse(result);
		} catch (NotFoundException ex) {
			logger.error("{}", ex.getMessage());
			throw new WatsEBSCustomException(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		} catch (WatsEBSCustomException ex) {
			logger.error("{}", ex.getMessage());
			throw new WatsEBSCustomException(ex.getErrorCode(), ex.getMessage());
		} catch (InternalServerErrorException ex) {
			logger.error("{}", ex.getMessage());
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		} catch (Exception ex) {
			logger.error("Error while calling API: {}", ex.getMessage());
			throw new WatsEBSCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		}
	}

}
