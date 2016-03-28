import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class TestBeanJUnit {
    @Autowired
    RuntimeService runtimeService;

    @Autowired

    DeploymentBuilder deploymentBuilder;

    @Test
    public void runSimple01Bpmn() {
        deploy();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testBean");
        Assert.assertNotNull(processInstance);
    }

    private void deploy(){
        deploymentBuilder.addClasspathResource("bpmn/Simple01.bpmn").deploy();
    }
}
