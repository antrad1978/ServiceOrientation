package pharmacare.tasks.domain;

import org.activiti.engine.runtime.ProcessInstance;

public class WorkflowResult {
	private int result;
	String message;
	private String processInstanceId;
	private long taskInstanceId;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
  		this.result = result;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public long getTaskInstanceId() {
		return taskInstanceId;
	}
	public void setTaskInstanceId(long taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}
}
