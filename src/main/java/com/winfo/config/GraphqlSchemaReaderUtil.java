package com.winfo.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class GraphqlSchemaReaderUtil {

  public static String getSchemaFromFileName(final String filename) throws IOException, URISyntaxException {
//	  ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//	  InputStream is = classloader.getResourceAsStream("graphQl/" + filename + ".graphql");
//	    InputStream in = GraphqlSchemaReaderUtil.class.getClassLoader().getResource("graphQl/" + filename + ".graphql").openStream();
//	    in.toString();
//	  InputStream is = GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream("C:\\Users\\UdayPratapSingh\\Documents\\WATS\\src\\main\\resources\\graphQl\\"+ filename + ".graphql");
//	  URL url = GraphqlSchemaReaderUtil.class.getClassLoader().getResource("graphQl1"+File.separator + filename + ".graphql");
//	  Path path = Paths.get(url.toURI());
//	  List<String> str = Files.readAllLines(path);
//	  GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream("graphQl/" + filename + ".graphql");
//	    String text = new BufferedReader(
//	    	      new InputStreamReader(is, StandardCharsets.UTF_8))
//	    	        .lines()
//	    	        .collect(Collectors.joining("\n"));
//	    System.out.println("he");
//	  return new BufferedReader(
//    	      new InputStreamReader(in, StandardCharsets.UTF_8))
//  	        .lines()
//  	        .collect(Collectors.joining("\n"));
//	  		InputStream in = new ClassPathResource("graphQl\\create-script\\create-script-schema.graphql").getInputStream();
			List<String> t = Files.readAllLines(Paths.get("graphQl/"+ filename + ".graphql"));
			String text = String.join("\n", t);
			//        GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream("graphQl/" + filename + ".graphql").toString());
//    InputStream in = GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream("graphQl/" + filename + ".graphql");
	    return text;
  }

}
