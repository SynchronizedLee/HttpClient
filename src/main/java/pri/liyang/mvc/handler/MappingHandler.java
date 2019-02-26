package pri.liyang.mvc.handler;

import pri.liyang.mvc.annotations.Controller;
import pri.liyang.mvc.annotations.ParameterMap;
import pri.liyang.mvc.entity.Mapping;
import pri.liyang.mvc.entity.ModelAndView;
import pri.liyang.server.constant.HttpContentType;
import pri.liyang.server.entity.Request;
import pri.liyang.server.entity.Response;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller类的映射处理类
 */
public class MappingHandler {

    //Controller的mapping列表
    public static List<Mapping> mappingList = new ArrayList<Mapping>();

    //初始化建立mapping列表
    static{
        //获取系统配置：base.package
        String basePackage = ConfigurationHandler.getSystemProperty("base.package");

        try {
            //获取所有的拥有Controller注解的包名+类名字符串，用来Class.forName
            List<String> classNames = new ComponentScanHandler().getAnnotationByPackage(Controller.class, basePackage);

            for (String className : classNames){
                //每一个都收集Mapping
                List<Mapping> maps = MappingCollector.mappingCollect(className);

                //然后全部弄到mappingList里面去
                mappingList.addAll(maps);
            }

            //映射查重
            MappingCollector.checkDuplicateMapping(mappingList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Server过来的Request，找到Controller对应的方法，返回Response对象
     * @param request 请求对象
     * @return response对象
     */
    public static Response handleRequest(Request request){
        //获取请求对象url
        String url = request.getUrl();

        //获取请求方法
        String method = request.getMethod();

        //获得参数Map
        Map paramMap = request.getParameters();

        //从Mapping中找到映射
        Mapping currentMapping = null;
        for (Mapping mapping : mappingList){
            if(url.equals(mapping.getUrl()) && method.equals(mapping.getMethod())){
                currentMapping = mapping;
                break;
            }
        }

        //如果没找到，报404
        if(currentMapping == null){
            return errorResponse(404, null);
        }

        //执行mapping里面的方法，获得回应
        try {
            //实例化Controller
            Class controllerClass = Class.forName(currentMapping.getControllerClass());

            //如果有参数，则赋值参数
            Field[] fields = controllerClass.getFields();
            for (Field field : fields){
                if (field.isAnnotationPresent(ParameterMap.class)){
                    field.setAccessible(true);
                    try {
                        //将参数设置到Controller的成员属性里
                        field.set(controllerClass.newInstance(), paramMap);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }

            //获得Controller里面映射的方法
            Method controllerMethod = controllerClass.getDeclaredMethod(currentMapping.getMethodName());

            //执行该方法，并获得结果
            Object responseContext = "";
            try {
                responseContext = controllerMethod.invoke(controllerClass.newInstance());

                //如果是返回视图，则进行额外处理
                if (responseContext instanceof ModelAndView){
                    //获取ModelAndView对象
                    ModelAndView mav = (ModelAndView) responseContext;

                    //获取视图属性
                    String viewName = mav.getViewName();
                    Map paramsMap = mav.getParamMap();

                    //根据配置文件获取视图前后缀
                    String prefix = ConfigurationHandler.getSystemProperty("mvc.prefix");
                    String suffix = ConfigurationHandler.getSystemProperty("mvc.suffix");

                    //调用方法，获得试图解析后的字符串
                    String html = ViewHandler.parseView(prefix, suffix, viewName, paramsMap);

                    //重新赋值responseContext
                    responseContext = html;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return errorResponse(500, e.toString());
            } catch (InstantiationException e) {
                e.printStackTrace();
                return errorResponse(500, e.toString());
            } catch (InvocationTargetException e) {
                e.printStackTrace();

                //获取反射方法的内部错误堆栈信息，在response里面体现
                return errorResponse(500, getStackTrace(e.getTargetException()));
            }

            //封装成功的Response
            Response response_200 = new Response();
            response_200.setContentType(currentMapping.getContentType());
            response_200.setStatusCode(200);
            response_200.setResponse(responseContext.toString());
            response_200.setStatusContent("OK");

            return response_200;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return errorResponse(500, e.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return errorResponse(500, e.toString());
        }

    }

    private static Response errorResponse(int code, String errorMessage){

        switch (code){
            case 404:
                Response response_404 = new Response();
                response_404.setContentType(HttpContentType.DEFAULT_HTTP_CONTENT_TYPE);
                response_404.setStatusCode(404);
                response_404.setResponse("<h2>404 not found!!</h2>");
                response_404.setStatusContent("NOT FOUND");
                return response_404;

            case 500:
                Response response_500 = new Response();
                response_500.setContentType(HttpContentType.DEFAULT_HTTP_CONTENT_TYPE);
                response_500.setStatusCode(500);
                response_500.setResponse("<h2>Internal Server Error!!</h2>\r\n\r\nError Message: \r\n" + errorMessage);
                response_500.setStatusContent("Server Error");
                return response_500;

            default:
                return null;
        }

    }

    /**
     * 获取Throwable的StackTrace
     * @param e source Throwable
     * @return stack trace string
     */
    public static String getStackTrace(Throwable e) {

        //如果Throwable为null
        StringBuffer msg = new StringBuffer("null");

        if (e != null) {
            msg = new StringBuffer();

            //Throwable原始信息
            String message = e.toString();

            //堆栈信息的长度
            int length = e.getStackTrace().length;

            //编辑堆栈信息
            if (length > 0) {
                //先上错误信息
                msg.append(message + "<br/>");

                //拼凑堆栈信息
                for (int i = 0; i < length; i++) {
                    msg.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + e.getStackTrace()[i] + "<br/>");
                }
            } else {
                //没有堆栈信息，就只返回错误信息
                msg.append(message);
            }

        }

        return msg.toString();
    }

}
