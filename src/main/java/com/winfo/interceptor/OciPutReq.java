package com.winfo.interceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.jfree.util.Log;
import org.springframework.core.io.ClassPathResource;

/**
 * Copyright (c) 2016, 2022, Oracle and/or its affiliates.  All rights reserved.
 * This software is dual-licensed to you under the Universal Permissive License (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License 2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose either license.
 */
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;

public class OciPutReq {

	/*
	 * // public static void main(String[] args) throws Exception { // // String
	 * configurationFilePath = "~/.oci/config"; // String profile = "DEFAULT"; // //
	 * // Configuring the AuthenticationDetailsProvider. It's assuming there is a
	 * default OCI config file // // "~/.oci/config", and a profile in that config
	 * with the name "DEFAULT". Make changes to the following // // line if needed
	 * and use ConfigFileReader.parse(configurationFilePath, profile); // // final
	 * ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(new
	 * ClassPathResource("oci/config").getInputStream(), // "WATS_WINFOSOLUTIONS");
	 * // // final AuthenticationDetailsProvider provider = // new
	 * ConfigFileAuthenticationDetailsProvider(configFile); // // ObjectStorage
	 * client = new ObjectStorageClient(provider); // // ObjectStorage client =
	 * "https://objectstorage.uk-london-1.oraclecloud.com/p/lRnqxvET-QbtTNYtWerWsDueIvA_J6h54tvfTrIFT53mNWpfTeM9HZ7alf_mfetD/n/nrch2emfoqis/b/obj-watsdev01-standard/o/";
	 * // client.setRegion(Region.UK_LONDON_1); // // GetNamespaceResponse
	 * namespaceResponse = //
	 * client.getNamespace(GetNamespaceRequest.builder().build()); // String
	 * namespaceName = namespaceResponse.getValue(); //
	 * System.out.println("Using namespace: " + namespaceName); // // Builder
	 * listBucketsBuilder = // ListBucketsRequest.builder() //
	 * .namespaceName(namespaceName) // .compartmentId(provider.getTenantId()); //
	 * // String nextToken = null; // do { // listBucketsBuilder.page(nextToken); //
	 * ListBucketsResponse listBucketsResponse = //
	 * client.listBuckets(listBucketsBuilder.build()); // for (BucketSummary bucket
	 * : listBucketsResponse.getItems()) { // System.out.println("Found bucket: " +
	 * bucket.getName()); // } // nextToken = listBucketsResponse.getOpcNextPage();
	 * // } while (nextToken != null); // // // fetch the file from the object
	 * storage // String bucketName = null; // String objectName = null; //
	 * GetObjectResponse getResponse = // client.getObject( //
	 * GetObjectRequest.builder() // .namespaceName(namespaceName) //
	 * .bucketName(bucketName) // .objectName(objectName) // .build()); // // //
	 * stream contents should match the file uploaded // try (final InputStream
	 * fileStream = getResponse.getInputStream()) { // // use fileStream //
	 * System.out.println(fileStream.toString()); // } // try-with-resources
	 * automatically closes fileStream // // client.close(); // }
	 */

	public static void main(String[] args) throws Exception {

		/**
		 * Create a default authentication provider that uses the DEFAULT profile in the
		 * configuration file. Refer to <see
		 * href="https://docs.cloud.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm#SDK_and_CLI_Configuration_File>the
		 * public documentation</see> on how to prepare a configuration file.
		 */
		final ConfigFileReader.ConfigFile configFile = ConfigFileReader
				.parse(new ClassPathResource("oci/config").getInputStream(), "WATS_WINFOERP");
		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
		final String FILE_NAME = "C:\\Users\\Winfo solutions\\Priya\\softwares\\wats\\jars\\padf\\WATS\\TestJiraP1\\Detailed_Report.pdf";
		File file = new File(FILE_NAME);
		long fileSize = FileUtils.sizeOf(file);
		InputStream is = new FileInputStream(file);

		/* Create a service client */
		try (ObjectStorageClient client = new ObjectStorageClient(provider);) {

			/* Create a request and dependent object(s). */

			PutObjectRequest putObjectRequest = PutObjectRequest.builder().namespaceName("nrch2emfoqis")
					.bucketName("obj-watsdev01-standard").objectName("ebs/Detailed_Report.pdf").contentLength(fileSize)// Create
																														// a
																														// Stream,
																														// for
																														// example,
																														// by
																														// calling
																														// a
																														// helper
																														// function
																														// like
																														// below.

					.putObjectBody(is)/*
										 * // .ifMatch("EXAMPLE-ifMatch-Value") // .ifNoneMatch("B") //
										 * .opcClientRequestId(
										 * "ocid1.test.oc1..<unique_ID>EXAMPLE-opcClientRequestId-Value") //
										 * .expect("EXAMPLE-expect-Value") //
										 * .contentMD5("900150983cd24fb0d6963f7d28e17f72") //
										 * .contentType("EXAMPLE-contentType-Value") //
										 * .contentLanguage("EXAMPLE-contentLanguage-Value") //
										 * .contentEncoding("EXAMPLE-contentEncoding-Value") //
										 * .contentDisposition("EXAMPLE-contentDisposition-Value") //
										 * .cacheControl("EXAMPLE-cacheControl-Value") //
										 * .opcSseCustomerAlgorithm("EXAMPLE-opcSseCustomerAlgorithm-Value") //
										 * .opcSseCustomerKey("EXAMPLE-opcSseCustomerKey-Value") //
										 * .opcSseCustomerKeySha256("EXAMPLE-opcSseCustomerKeySha256-Value")
										 * //.opcSseKmsKeyId("ocid1.test.oc1..<unique_ID>EXAMPLE-opcSseKmsKeyId-Value")
										 * //.storageTier(StorageTier.Standard)
										 */ .build();

			/* Send request to the Client */
			PutObjectResponse response = client.putObject(putObjectRequest);
			Log.info(response.toString());
		}

	}

}