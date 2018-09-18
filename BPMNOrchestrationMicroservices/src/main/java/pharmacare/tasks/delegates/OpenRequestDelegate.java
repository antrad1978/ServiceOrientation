package pharmacare.tasks.delegates;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class OpenRequestDelegate implements JavaDelegate {
	 
		/* (non-Javadoc)
		 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
		 */
		public void execute(DelegateExecution delegateExecution) {
	 
			System.out.println("Executed process with key "+
								delegateExecution.getProcessInstanceBusinessKey()+
								" with process definition Id "+
								delegateExecution.getProcessDefinitionId()+
								" with process instance Id "+delegateExecution.getProcessInstanceId()+
								" is restarted because of a timer. ");
	 
		}
}
