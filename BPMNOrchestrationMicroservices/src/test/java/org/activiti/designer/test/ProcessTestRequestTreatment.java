package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestRequestTreatment {

	private String filename = "/Users/admin/eclipse-workspace/shared1/src/main/resources/diagrams/TreatmentRequestVersion2.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("requestTreatment.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("requestTreatment", variableMap);
		Map<String, Object> blockVariables = new HashMap<String, Object>();
		blockVariables.put("name", "Activiti");
		 
		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
		
		assertNotSame(executions.size(), 0);
		
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).messageEventSubscriptionName("blockRequestMessage").singleResult();
		runtimeService.messageEventReceived("blockRequestMessage", execution.getId());
		
		executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
		
		List<Execution> otherExecutions = runtimeService.createExecutionQuery()
				.processInstanceId(processInstance.getId())
			      .messageEventSubscriptionName("acceptRequestMessage")
			      .list();
		
		assertEquals(otherExecutions.size(), 1);
		
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).messageEventSubscriptionName("acceptRequestMessage").singleResult();
		runtimeService.messageEventReceived("acceptRequestMessage", execution.getId());
		
		List<Execution> ended = runtimeService.createExecutionQuery()
				.processInstanceId(processInstance.getId())
			      .list();
		
		/*List<HistoricTaskInstance> taskList = activitiRule.getHistoryService()
				  .createHistoricTaskInstanceQuery()
				  .processInstanceId(processInstance.getId()).list();*/
		
		assertNotNull(processInstance.getId());
		assertEquals(ended.size(), 0);
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	@Test
	public void endProcessWithTrigger() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("requestTreatment.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("requestTreatment", variableMap);
		
		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
		
		assertNotSame(executions.size(), 0);
		
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).signalEventSubscriptionName("setRequestExpired").singleResult();
		runtimeService.messageEventReceived("setRequestExpired", execution.getId());
		
		List<Execution> otherExecutions = runtimeService.createExecutionQuery()
				.processInstanceId(processInstance.getId())
			      .list();
		
		assertEquals(otherExecutions.size(), 1);
	}
}