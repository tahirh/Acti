import beans.TestBean;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class TestBeanJUnitSpy {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    DeploymentBuilder deploymentBuilder;

    @Test
    public void runSimple01Bpmn() {
        TestBean testBeanSpy = (TestBean) createSpy("testBean");
        deploy();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testBean");
        Assert.assertNotNull(processInstance);

        Mockito.verify(testBeanSpy, times(1)).init();
        Mockito.verify(testBeanSpy, times(1)).test(anyString());
    }

    private void deploy() {
        deploymentBuilder.addClasspathResource("bpmn/Simple01.bpmn").deploy();
    }

    private Object createSpy(String beanName) {
        BeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        Object bean = beanFactory.getBean(beanName);
        Object spyBean = Mockito.spy(bean);
        ((BeanDefinitionRegistry) beanFactory).removeBeanDefinition(beanName);
        ((ConfigurableBeanFactory) beanFactory).registerSingleton(beanName, spyBean);
        return spyBean;
    }
}
