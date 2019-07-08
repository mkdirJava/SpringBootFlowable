package com.flowable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assert;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FlowableApplicationTests {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	@Test
	public void contextLoads() {
		
	}
	
	@Test
    @Deployment(resources = { "processes/article-workflow.bpmn20.xml" })
    public void articleApprovalTest() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("author", "test@baeldung.com");
        variables.put("url", "http://baeldung.com/dummy");
  
        runtimeService.startProcessInstanceByKey("articleReview", variables);
        Task task = taskService.createTaskQuery().singleResult();
  
        assertEquals("Review the submitted tutorial", task.getName());
  
        variables.put("approved", true);
        taskService.complete(task.getId(), variables);
  
        
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }

}
