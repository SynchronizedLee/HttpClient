package pri.liyang.mvc.annotations;

import pri.liyang.server.constant.HttpContentType;

import java.lang.annotation.*;

/**
 * 请求映射注解，用来映射请求，并给予适当响应的类
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    //映射请求方法
    String method();

    //映射请求url
    String url();

    //返回媒体类型
    String contentType() default HttpContentType.DEFAULT_HTTP_CONTENT_TYPE;

}
