import beans.RandomBean
import org.activiti.engine.RuntimeService
import org.activiti.engine.repository.DeploymentBuilder
import org.activiti.engine.runtime.ProcessInstance
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import spock.lang.Specification

class RandomBeanMock extends Specification {
    ApplicationContext applicationContext
    RuntimeService runtimeService
    DeploymentBuilder deploymentBuilder
    RandomBean randomBean

    def setupSpec() {
//        setUpContext()
//        createTestBeanMock()
    }

    def 'Run random'(){
        given: 'a mocked bean'
        setUpContext()
//        createTestBeanMock()
        randomBean = Mock(RandomBean)
        1 * randomBean.falseOrTrue() >> { println 'Mocking true'; return true }
        registerBean('randomBean', randomBean)

        when: 'the flow is started'
        deploy()
        ProcessInstance processInstance=runtimeService.startProcessInstanceByKey('randomTest')

        then: 'it was started'
        processInstance
    }

    def deploy() {
        deploymentBuilder.addClasspathResource("bpmn/Random01.bpmn").deploy();
    }

    def setUpContext() {
        applicationContext = new ClassPathXmlApplicationContext('activiti-context.xml')
        deploymentBuilder = applicationContext.getBean(DeploymentBuilder)
        runtimeService = applicationContext.getBean(RuntimeService)
    }

    void createTestBeanMock() {
        RandomBean randomBean = Mock(RandomBean) {
            falseOrTrue() >> {
                println 'Mocked true'
                return true
            }
        }
        registerBean('randomBean', randomBean)
    }

    def registerBean(String beanName, Object beanInstance) {
        ConfigurableBeanFactory configurableBeanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
        configurableBeanFactory.registerSingleton(beanName, beanInstance)
    }

}
