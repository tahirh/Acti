import org.activiti.engine.RuntimeService
import org.activiti.engine.repository.DeploymentBuilder
import org.activiti.engine.runtime.ProcessInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(locations = "classpath:application-context.xml")
class RandomBeanTest extends Specification{
    @Autowired
    RuntimeService runtimeService

    @Autowired
    DeploymentBuilder deploymentBuilder

    def 'Run random'(){
        given: 'a deployed flow'
        deploy()

        when: 'the flow is startef'
        ProcessInstance processInstance=runtimeService.startProcessInstanceByKey('randomTest')

        then: 'it should have been started'
        processInstance
    }

    def deploy() {
        deploymentBuilder.addClasspathResource("bpmn/Random01.bpmn").deploy();
    }
}
