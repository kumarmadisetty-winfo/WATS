package com.winfo.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class GraphqlSchemaReaderUtil {

	public static String getSchemaFromFileName(final String filename) throws IOException {
		InputStream inputStream = GraphqlSchemaReaderUtil.class.getResourceAsStream("/graphQl/" + filename + ".graphql");
				 ByteArrayOutputStream result = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
				 }
				String schema = result.toString(StandardCharsets.UTF_8.name());
				return schema;
//		return new String(
//				GraphqlSchemaReaderUtil.class.getResourceAsStream("/graphQl/" + filename + ".graphql").readAllBytes());
	}

}
