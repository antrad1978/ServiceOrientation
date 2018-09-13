package pharmacare.tasks.domain;

import org.activiti.engine.runtime.ProcessInstance;

public class WorkflowResult {
	private int result;
	String message;
	private ProcessInstance processInstance;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
}
