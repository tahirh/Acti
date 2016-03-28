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
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class TestBeanJUnitMock {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    DeploymentBuilder deploymentBuilder;

    TestBean testBean;

    @Test
    public void runSimple01Bpmn() {
        createTestBeanMock();
        deploy();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testBean");
        Assert.assertNotNull(processInstance);
        verify(testBean, times(1)).init();
        verify(testBean, times(1)).test(anyString());
    }

    private void createTestBeanMock(){
        testBean = Mockito.mock(TestBean.class);
        when(testBean.init()).thenReturn("Init Mock");
        when(testBean.test(anyString())).thenReturn("Test Mock");
        registerBean("testBean", testBean);
    }

    private void registerBean(String beanName, Object beanInstance) {
        BeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        if (beanFactory.containsBean(beanName)){
            ((BeanDefinitionRegistry) beanFactory).removeBeanDefinition(beanName);
        }
        ((ConfigurableBeanFactory) beanFactory).registerSingleton(beanName, beanInstance);
    }

    private void deploy(){
        deploymentBuilder.addClasspathResource("bpmn/Simple01.bpmn").deploy();
    }
}
