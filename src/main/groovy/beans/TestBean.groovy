package beans

import org.springframework.stereotype.Component

@Component
class TestBean {
    public String test(String var){
        var="Tested"
        println("test() var="+var)
        return var
    }

    public String init(){
        String var="Initialized"
        println("init() var="+var)
        return var
    }
}
