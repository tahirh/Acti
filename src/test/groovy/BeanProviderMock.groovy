import beans.BeanProvider
import beans.TestBean
import org.activiti.engine.RuntimeService
import org.activiti.engine.repository.DeploymentBuilder
import org.activiti.engine.runtime.ProcessInstance
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import spock.lang.Shared
import spock.lang.Specification

class BeanProviderMock extends Specification {
    @Shared
    RuntimeService runtimeService

    @Shared
    DeploymentBuilder deploymentBuilder

    TestBean testBean
    ApplicationContext applicationContext

    def 'Run Provider'() {
        given: 'a deployed testflow'
        deploy()

        when: 'it starts'
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey('beanProvider')

        then: 'init() was called once'
        1 * testBean.init()
    }

    def setupSpec() {
        setUpContext()
        createTestBeanMock()
        createBeanProvider()
    }

    def deploy() {
        deploymentBuilder.addClasspathResource("bpmn/BeanProvider.bpmn").deploy();
    }

    def setUpContext() {
        applicationContext = new ClassPathXmlApplicationContext('activiti-context.xml')
        deploymentBuilder = applicationContext.getBean(DeploymentBuilder)
        runtimeService = applicationContext.getBean(RuntimeService)
    }

    def createTestBeanMock() {
        testBean = Mock(TestBean) {
            init() >> 'Mock init'
            test(_) >> 'Mock test'
        }
        registerBean('testBean', testBean)
    }

    def createBeanProvider() {
        BeanProvider beanProvider = new BeanProvider()
        beanProvider.testBean = testBean
        registerBean('beanProvider', beanProvider)
    }

    def registerBean(String beanName, Object beanInstance) {
        ConfigurableBeanFactory configurableBeanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
        configurableBeanFactory.registerSingleton(beanName, beanInstance)
    }
}
