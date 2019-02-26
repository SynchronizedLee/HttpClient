package pri.liyang.mvc.annotations;

import java.lang.annotation.*;

/**
 * 控制器注解，被注解了Controller的类，就是一个控制器类
 * 控制器类，拥有映射请求，并返回对应响应的控制功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

}
