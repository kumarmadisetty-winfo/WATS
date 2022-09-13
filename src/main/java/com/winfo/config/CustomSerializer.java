package com.winfo.config;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.vo.MessageQueueDto;

public class CustomSerializer implements Serializer<MessageQueueDto> {
	public final Logger logger = LogManager.getLogger(CustomSerializer.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void close() {
		// Empty Field
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// Empty Field
	}

	@Override
	public byte[] serialize(String topic, MessageQueueDto data) {
		try {
			if (data == null) {
				logger.info("Null received at serializing");
				return new byte[0];
			}
			logger.info("Serializing...");
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error when serializing MessageDto to byte[]");
		}
	}
}