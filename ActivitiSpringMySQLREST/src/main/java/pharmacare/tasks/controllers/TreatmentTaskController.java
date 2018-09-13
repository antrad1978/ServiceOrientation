package pharmacare.tasks.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pharmacare.tasks.domain.WorkflowResult;

@RestController
public class TreatmentTaskController {

	@Autowired
    private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;

    @RequestMapping("/start")
    public WorkflowResult StartTreatmentWorklofw(@RequestParam(value="token", defaultValue="") String token, String treatmentId) {
    	//RepositoryServiceImpl repositoryService = new RepositoryServiceImpl();
    	Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applicantName", "John Doe");
        variables.put("email", "john.doe@activiti.com");
        variables.put("phoneNumber", "123456789");
        List<ProcessDefinition> results = repositoryService.createProcessDefinitionQuery().active().list();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variables);
        
        WorkflowResult result=new WorkflowResult();
        result.setResult(1);
        result.setProcessInstance(processInstance);
        return result;
    }
}
