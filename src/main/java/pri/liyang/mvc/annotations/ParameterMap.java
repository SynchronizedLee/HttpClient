package pri.liyang.mvc.annotations;

import java.lang.annotation.*;

/**
 * 参数Map注解，说明这是公用的参数
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParameterMap {

}
