package com.winfo.interceptor;



import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

/**
 * Copyright (c) 2016, 2022, Oracle and/or its affiliates.  All rights reserved.
 * This software is dual-licensed to you under the Universal Permissive License (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License 2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose either license.
 */
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;

public class OciListRequest {
	
	static Logger log = Logger.getLogger("Logger");

    public static void main(String[] args) throws Exception {

		/*
		 * Configuring the AuthenticationDetailsProvider. It's assuming there is a
		 * default OCI config file "~/.oci/config", and a profile in that config with
		 * the name "DEFAULT". Make changes to the following line if needed and use
		 * ConfigFileReader.parse(configurationFilePath, profile);
		 */
        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(new ClassPathResource("oci/config").getInputStream(),
        		"WATS_WINFOERP");

        final AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);

        ObjectStorage client = new ObjectStorageClient(provider);
		/*
		 * ObjectStorage client =
		 * "https://objectstorage.uk-london-1.oraclecloud.com/p/lRnqxvET-QbtTNYtWerWsDueIvA_J6h54tvfTrIFT53mNWpfTeM9HZ7alf_mfetD/n/nrch2emfoqis/b/obj-watsdev01-standard/o/";
		 */
        
        client.setRegion(Region.UK_LONDON_1);
        String namespaceName = "nrch2emfoqis";
        String bucketName = "obj-watsdev01-standard";
        List<String> objNames = null;
        
		/*
		 * Builder listBucketsBuilder = ListBucketsRequest.builder()
		 * .namespaceName(namespaceName) .compartmentId(provider.getTenantId());
		 * 
		 * String nextToken = null; do { listBucketsBuilder.page(nextToken);
		 * ListBucketsResponse listBucketsResponse =
		 * client.listBuckets(listBucketsBuilder.build()); for (BucketSummary bucket :
		 * listBucketsResponse.getItems()) { System.out.println("Found bucket: " +
		 * bucket.getName()); } nextToken = listBucketsResponse.getOpcNextPage(); }
		 * while (nextToken != null);
		 * 
		 * fetch the file from the object storage String bucketName =
		 * "obj-watsdev01-standard"; String objectName = "ebs/WATS/EBSP2PScenario";
		 * GetObjectResponse getResponse = client.getObject( GetObjectRequest.builder()
		 * .namespaceName(namespaceName) .bucketName(bucketName) .objectName(objectName)
		 * .build());
		 * 
		 * stream contents should match the file uploaded try (final InputStream
		 * fileStream = getResponse.getInputStream()) { // use fileStream
		 * System.out.println(fileStream.toString()); } // try-with-resources
		 * automatically closes fileStream try (final InputStream stream =
		 * getResponse.getInputStream(); final OutputStream outputStream = new
		 * FileOutputStream("C:\\WATS")) { // use fileStream byte[] buf = new
		 * byte[8192]; int bytesRead; while ((bytesRead = stream.read(buf)) > 0) {
		 * outputStream.write(buf, 0, bytesRead); } } // try-with-resources
		 * automatically closes streams System.out.println("File size: " + new
		 * File(outputFileName).length() + " bytes");
		 */
        
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
        		.namespaceName(namespaceName)
        		.bucketName(bucketName)
        		.prefix("ebs/WATS/EBS2030")
        		.build();

                /* Send request to the Client */
                ListObjectsResponse response = client.listObjects(listObjectsRequest);
               
               objNames = response.getListObjects().getObjects().stream()
                        .map(objSummary -> objSummary.getName())
                        .collect(Collectors.toList());
             ListIterator<String> listIt= objNames.listIterator();
             while (listIt.hasNext())
             {
            	 log.info(listIt.next());
             }
        client.close();
    }

}