package com.winfo.config;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.vo.PyJabKafkaDto;

public class CustomSerializer implements Serializer<PyJabKafkaDto> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void close() {
	}

	@Override
	public void configure(Map configs, boolean isKey) {

	}

	@Override
	public byte[] serialize(String topic, PyJabKafkaDto data) {
		try {
			if (data == null) {
				System.out.println("Null received at serializing");
				return null;
			}
			System.out.println("Serializing...");
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error when serializing MessageDto to byte[]");
		}
	}
}