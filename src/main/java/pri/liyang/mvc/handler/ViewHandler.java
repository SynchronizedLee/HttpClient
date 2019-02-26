package pri.liyang.mvc.handler;

import java.util.Map;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * 视图处理器
 */
public class ViewHandler {

    /**
     * 视图解析方法，可以根据指定视图和参数生成html的字符串
     * @param prefix 视图前缀
     * @param suffix 视图后缀
     * @param viewName 视图名称
     * @param param 视图动态数据
     * @return 视图静态的html
     */
    public static String parseView(String prefix, String suffix, String viewName, Map<String, Object> param){
        //构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

        //模板所在目录，相对于当前classloader的classpath
        resolver.setPrefix(prefix);

        //模板文件后缀
        resolver.setSuffix(suffix);

        //构造模板引擎
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //构造上下文(Model)
        Context context = new Context();
        context.setVariables(param);

        //根据模板和上下文参数，获取html的字符串
        String html = templateEngine.process(viewName, context);

        return html;
    }

}
