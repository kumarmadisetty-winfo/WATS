package com.winfo.config;

import java.io.IOException;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class GraphqlSchemaReaderUtil {

	public static String getSchemaFromFileName(final String filename) throws IOException {
		
//		return new String(
//				GraphqlSchemaReaderUtil.class.getResourceAsStream("/graphQl/" + filename + ".graphql").readAllBytes());
		return "";
	}

}
