import beans.TestBean
import org.activiti.engine.RuntimeService
import org.activiti.engine.repository.DeploymentBuilder
import org.activiti.engine.runtime.ProcessInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

@ContextConfiguration(locations = "classpath:activiti-context.xml")
class TestBeanMockito extends Specification {
    @Autowired
    ApplicationContext applicationContext

    @Autowired
    RuntimeService runtimeService

    @Autowired
    DeploymentBuilder deploymentBuilder

    @Shared
    TestBean testBean

    def 'Run Simple'() {
        given: 'a mocked testbean'
        createTestBeanMock()

        when: 'the flow runs'
        deploy()
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey('testBean')

        then: 'init() was called once'
        processInstance
        verify(testBean, times(1)).init();
    }

    def deploy() {
        deploymentBuilder.addClasspathResource("bpmn/Simple01.bpmn").deploy();
    }

    def createTestBeanMock() {
        testBean = Mockito.mock(TestBean.class)
        Mockito.when(testBean.init()).thenReturn("Init Mock")
        Mockito.when(testBean.test(anyString())).thenReturn("Test Mock")
        registerBean('testBean', testBean)
    }

    def registerBean(String beanName, Object beanInstance) {
        ConfigurableBeanFactory configurableBeanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
        configurableBeanFactory.registerSingleton(beanName, beanInstance)
    }
}
