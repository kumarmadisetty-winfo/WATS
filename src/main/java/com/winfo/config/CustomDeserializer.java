package com.winfo.config;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.vo.MessageQueueDto;

public class CustomDeserializer implements Deserializer<MessageQueueDto> {
	public final Logger logger = LogManager.getLogger(CustomDeserializer.class);
	
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MessageQueueDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
            	logger.info("Null received at deserializing");
                return null;
            }
            logger.info("Deserializing...");
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), MessageQueueDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }

    @Override
    public void close() {
      // Empty Field
    }

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// Empty Field
		
	}
}
