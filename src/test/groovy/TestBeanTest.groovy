import org.activiti.engine.RuntimeService
import org.activiti.engine.repository.DeploymentBuilder
import org.activiti.engine.runtime.ProcessInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(locations = "classpath:application-context.xml")
class TestBeanTest extends Specification {
    @Autowired
    RuntimeService runtimeService

    @Autowired
    DeploymentBuilder deploymentBuilder

    def 'Run Simple'() {
        given: 'a deployed testflow'
        deploy()

        when: 'it starts'
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey('testBean')

        then: 'it should have been started'
        processInstance
    }

    def deploy() {
        deploymentBuilder.addClasspathResource("bpmn/Simple01.bpmn").deploy();
    }
}
