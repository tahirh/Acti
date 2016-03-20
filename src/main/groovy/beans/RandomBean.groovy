package beans

import org.apache.commons.lang3.RandomUtils
import org.springframework.stereotype.Component

@Component
class RandomBean {
    Boolean falseOrTrue(){
        return 1==RandomUtils.nextInt(0, 2)
    }
}
