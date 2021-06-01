//package com.winfo.services;
//
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.transaction.Transactional;
//import javax.transaction.Transactional.TxType;
//
//import java.util.Map.Entry;
//import com.oracle.bmc.Region;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import com.oracle.bmc.ConfigFileReader;
//import com.oracle.bmc.ConfigFileReader.ConfigFile;
//import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
//import com.oracle.bmc.core.ComputeClient;
//import com.oracle.bmc.core.ComputeManagementClient;
//import com.oracle.bmc.core.ComputeWaiters;
//import com.oracle.bmc.core.model.Instance;
//import com.oracle.bmc.core.model.InstancePoolSummary;
//import com.oracle.bmc.core.model.InstanceSummary;
//import com.oracle.bmc.core.model.UpdateInstancePoolDetails;
//import com.oracle.bmc.core.requests.GetInstancePoolRequest;
//import com.oracle.bmc.core.requests.GetInstanceRequest;
//import com.oracle.bmc.core.requests.InstanceActionRequest;
//import com.oracle.bmc.core.requests.ListInstancePoolInstancesRequest;
//import com.oracle.bmc.core.requests.ListInstancePoolsRequest;
//import com.oracle.bmc.core.requests.UpdateInstancePoolRequest;
//import com.oracle.bmc.core.responses.GetInstancePoolResponse;
//import com.oracle.bmc.core.responses.GetInstanceResponse;
//import com.oracle.bmc.core.responses.InstanceActionResponse;
//import com.oracle.bmc.core.responses.ListInstancePoolInstancesResponse;
//import com.oracle.bmc.core.responses.ListInstancePoolsResponse;
//import com.oracle.bmc.core.responses.UpdateInstancePoolResponse;
//import com.winfo.dao.AuditLogsDao;
//import com.winfo.dao.VmInstanceDAO;
//import com.winfo.vo.TestScriptDto;
//
//@Service
//@RefreshScope
//public class VMDetailesService {
//	Logger log = Logger.getLogger("Logger");
//	
//	@Autowired
//	private VmInstanceDAO vmInstanceDao;
//	
//	@Value("${instancePool.compementId}")
//    private String compementId;
//	
//	@Value("${instancePool.configLocation}")
//    private String configLocation;
//	
//	@Value("${instance.displayName}")
//    private String displayName;
//	
//	@Value("${instance.maxNoOfBrowsersForVm}")
//    private int maxNoOfBrowsersForVm;
//	
//	@Value("${instance.instanceConfigProfileName}")
//    private String configProfileName;
//	
//    @Transactional
//	public void startInstance(String testRunId) throws IOException {
//
//		ListInstancePoolInstancesResponse listresponse = null;
//		ConfigFileAuthenticationDetailsProvider provider = null;
//		try {
//			
//			ConfigFile configFile = ConfigFileReader.parse(new ClassPathResource(configLocation).getInputStream(), configProfileName);
//
//			provider = new ConfigFileAuthenticationDetailsProvider(configFile);
//			ComputeManagementClient client = new ComputeManagementClient(provider);
//		    ListInstancePoolsRequest listInstancePoolsRequest = ListInstancePoolsRequest.builder()
//		    		.compartmentId(compementId)
//		    		.displayName(displayName)
//		    		.limit(719)
////		    		.page("EXAMPLE-page-Value")
//		    		.sortBy(ListInstancePoolsRequest.SortBy.Displayname)
//		    		.sortOrder(ListInstancePoolsRequest.SortOrder.Desc)
//		    		.lifecycleState(InstancePoolSummary.LifecycleState.Running)
//		    		.build();
//		    ListInstancePoolsResponse response = client.listInstancePools(listInstancePoolsRequest);
//
//			listresponse=getListOfInstancesResponse(response,client,compementId);
//			int numberOfVms = 0;
//			
//			for (InstanceSummary instanceSummary : listresponse.getItems()) {
//				if ("Running".equalsIgnoreCase(instanceSummary.getState())) {
//					numberOfVms++;
//				}
//			}
//			int inprogressandInqueueCount = vmInstanceDao.getInprogressAndInqueueCount();
//			int testrunScriptsCount = vmInstanceDao.getNumberOfTestscriptsforTestRunId(testRunId);
//			int total = inprogressandInqueueCount + testrunScriptsCount - maxNoOfBrowsersForVm * numberOfVms;
//            int maxNoOfBrowsersForConfig=vmInstanceDao.getMaxNoOfBrowsersForConfiguration(testRunId);
//            int increageBrowserscount=0;
//					while (total > 0 && maxNoOfBrowsersForConfig>increageBrowserscount) {
//						numberOfVms++;
//						total = total - maxNoOfBrowsersForVm;
//						increageBrowserscount=increageBrowserscount+maxNoOfBrowsersForVm;
//			         }
//					 UpdateInstancePoolDetails updateInstancePoolDetails =
//				                UpdateInstancePoolDetails.builder().size(numberOfVms).build();
//
//				        UpdateInstancePoolRequest updateRequest =
//				                UpdateInstancePoolRequest.builder()
//				                        .instancePoolId(response.getItems().get(0).getId())
//				                        .updateInstancePoolDetails(updateInstancePoolDetails)
//				                        .build();
//
//				        UpdateInstancePoolResponse updateResponse = client.updateInstancePool(updateRequest);
//				        System.out.println("no of vms lanched and wait 2mits"+updateResponse.getInstancePool().getSize());
//						String fistVmStatus = null;
//						
//						while(!"Running".equalsIgnoreCase(fistVmStatus)) {
//							Thread.sleep(5000);
//							listresponse=getListOfInstancesResponse(response,client,compementId);	
//						for (InstanceSummary instanceSummary : listresponse.getItems()) {
//							if("Running".equalsIgnoreCase(instanceSummary.getState())) {
//								fistVmStatus="Running";
//							}
//						}
//						}
//				        log.info("no of vms lanched"+updateResponse.getInstancePool().getSize());
//
//		} catch (Exception e) {
//			log.error("failed at sart the vms" + e);
//			e.printStackTrace();
//		}
//
//	}
//    @Transactional
//	public void stopInstance() throws Exception {
//		ConfigFileAuthenticationDetailsProvider provider=null;
//		ListInstancePoolInstancesResponse listresponse = null;
//		try {
//			ConfigFile configFile = ConfigFileReader.parse(configLocation, configProfileName);
//			provider = new ConfigFileAuthenticationDetailsProvider(configFile);
//			ComputeManagementClient client = new ComputeManagementClient(provider);
//		    ListInstancePoolsRequest listInstancePoolsRequest = ListInstancePoolsRequest.builder()
//		    		.compartmentId(compementId)
//		    		.displayName(displayName)
//		    		.limit(719)
//		    		.sortBy(ListInstancePoolsRequest.SortBy.Displayname)
//		    		.sortOrder(ListInstancePoolsRequest.SortOrder.Desc)
//		    		.lifecycleState(InstancePoolSummary.LifecycleState.Running)
//		    		.build();
//		    ListInstancePoolsResponse response = client.listInstancePools(listInstancePoolsRequest);
//
//			listresponse=getListOfInstancesResponse(response,client,compementId);
//
//			for (InstanceSummary instanceSummary : listresponse.getItems()) {
//				if ("Running".equalsIgnoreCase(instanceSummary.getState())) {					
//			UpdateInstancePoolDetails updateInstancePoolDetails =
//				                UpdateInstancePoolDetails.builder().size(0).build();
//
//				        UpdateInstancePoolRequest updateRequest =
//				                UpdateInstancePoolRequest.builder()
//				                        .instancePoolId(response.getItems().get(0).getId())
//				                        .updateInstancePoolDetails(updateInstancePoolDetails)
//				                        .build();
//				        UpdateInstancePoolResponse updateResponse = client.updateInstancePool(updateRequest);
//                    
//                      
//  						listresponse=getListOfInstancesResponse(response,client,compementId);
//                    	  while(!"Terminated".equalsIgnoreCase(listresponse.getItems().get(0).getState())) {
//                    		  Thread.sleep(7000);
//  							listresponse=getListOfInstancesResponse(response,client,compementId);
//                    	  }
//
//				        System.out.println("all vms are stoped and wait 2mits"+updateResponse.getInstancePool().getSize());
//				        log.info("all vms are stoped"+updateResponse.getInstancePool().getSize());
//				}
//			}
//		} catch (Exception e) {
//			log.error("failed at stop the vms" + e);
//			System.out.println("Exception"+e);
//		}
//	}
//	public ListInstancePoolInstancesResponse getListOfInstancesResponse(ListInstancePoolsResponse response, ComputeManagementClient client, String instancePoolId) {
//		try {
//
//			System.out.println("instanceSummary");
//			ListInstancePoolInstancesRequest listInstancePoolInstancesRequest = ListInstancePoolInstancesRequest
//					.builder().compartmentId(response.getItems().get(0).getCompartmentId())
//					.instancePoolId(response.getItems().get(0).getId())
//					.displayName(response.getItems().get(0).getDisplayName()).limit(122)
//					.sortBy(ListInstancePoolInstancesRequest.SortBy.Timecreated)
//					.sortOrder(ListInstancePoolInstancesRequest.SortOrder.Desc).build();
//
//			
//			    return client.listInstancePoolInstances(listInstancePoolInstancesRequest);
//		} catch (Exception e) {
//			System.out.println("Exception"+e);
//			log.error("failed at configuration setup" + e);
//		}
//		return null;
//	}
//
//
//}
