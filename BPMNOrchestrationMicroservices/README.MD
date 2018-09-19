# BPMN and Microservices

This is a complete example that show how to drive and monitor a process based on Microservices oriented architecture using **BPMN and Activiti**

## Description

Sample contains a running in process instance of Activiti that support MySQL, the async executor and some REST services that will simulate messages. We used in our environment a sync API based on Kafka and some consumers for run process consistently between different services and to sync our databases. 

## Timers and Service Tasks

Process is running some timers and sample service tasks. In our scenario we prefer to drive process via intermediate message catch because this enable eventual consistency and monitoring capabilities. Time based conditions are driven via Activiti async executor.

## Code

Sample contains unit tests for processes scenarios and uses H2 in test environment.

## Performance and Scaling considerations

You could scale if required on process number using muti tenancy, in our case we use geo areas. About scaling for executor you could scale it using Spring and ActiveMQ for example.


![Process Diagram](https://github.com/antrad1978/ServiceOrientation/blob/master/BPMNOrchestrationMicroservices/TreatmentRequest.png?raw=true)
