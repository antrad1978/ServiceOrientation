package pharmacare.tasks.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pharmacare.tasks.domain.WorkflowResult;

@RestController
public class TreatmentTaskController {
	
	private String filename = "/Users/admin/eclipse-workspace/shared1/src/main/resources/diagrams/TreatmentRequestVersion2.bpmn";

	@Autowired
    private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;

    @RequestMapping("/start")
    public WorkflowResult startTreatmentWorklofw(@RequestParam(value="token", defaultValue="") String token, String treatmentId) {
    	//RepositoryServiceImpl repositoryService = new RepositoryServiceImpl();
    	Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applicantName", "John Doe");
        variables.put("email", "john.doe@activiti.com");
        variables.put("phoneNumber", "123456789");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("requestTreatment", variables);
        
        WorkflowResult result=new WorkflowResult();
        result.setResult(1);
        result.setProcessInstanceId(processInstance.getId());
        return result;
    }
    
    @RequestMapping("/deploy")
    public boolean deployWorkflow() {
    	try {
			Deployment deployment= repositoryService.createDeployment().addInputStream("requestTreatment.bpmn20.xml",
					new FileInputStream(filename)).deploy();
			System.out.println(deployment.getKey());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
        return true;
    }
    
    @RequestMapping("/block")
    public boolean blockRequest(@RequestParam(value="executionId", defaultValue="") String processInstanceId) {
    	try {
    		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).messageEventSubscriptionName("blockRequestMessage").singleResult();
    		if(execution==null)
    			return false;
    		else
    			runtimeService.messageEventReceived("blockRequestMessage", execution.getId());
    		return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
        
    }
    
    @RequestMapping("/executions")
    public List<Execution> executions(@RequestParam(value="executionId", defaultValue="") String processInstanceId) {
    	try {
    		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
    		return executions;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    @RequestMapping("/accept")
    public boolean acceptRequest(@RequestParam(value="executionId", defaultValue="") String processInstanceId) {
    	try {
    		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).messageEventSubscriptionName("acceptRequestMessage").singleResult();
    		if(execution==null)
    			return false;
    		else
    			runtimeService.messageEventReceived("acceptRequestMessage", execution.getId());
    		return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    @RequestMapping("/other")
    public WorkflowResult startAutomatedWorklofw(@RequestParam(value="token", defaultValue="") String token, String treatmentId) {
    	Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applicantName", "John Doe");
        variables.put("email", "john.doe@activiti.com");
        variables.put("phoneNumber", "123456789");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);
        
        WorkflowResult result=new WorkflowResult();
        result.setResult(1);
        result.setProcessInstanceId(processInstance.getId());
        return result;
    }
}
 