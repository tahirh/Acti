package beans

import org.springframework.stereotype.Component

@Component
class BeanProvider {
//    @Autowired
    TestBean testBean

    String testBeanInit(){
        return testBean.init()
    }

    String testBeanTest(String text){
        return testBean.test(text)
    }
}
