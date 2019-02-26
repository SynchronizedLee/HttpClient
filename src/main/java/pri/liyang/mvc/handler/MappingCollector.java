package pri.liyang.mvc.handler;

import pri.liyang.mvc.annotations.Controller;
import pri.liyang.mvc.annotations.RequestMapping;
import pri.liyang.mvc.entity.Mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 控制器映射的收集类
 */
public class MappingCollector {

    /**
     * 根据控制器类的全路径名，收集Mapping信息
     * @param controllerClassName 控制器类全路径名
     * @return List<Mapping>
     * @throws ClassNotFoundException
     */
    public static List<Mapping> mappingCollect(String controllerClassName) throws ClassNotFoundException {
        List<Mapping> mappingList = new ArrayList<Mapping>();

        //实例化Controller
        Class controllerClass = Class.forName(controllerClassName);

        //判断是否有Controller注解
        boolean isController = controllerClass.isAnnotationPresent(Controller.class);

        //如果这个类不是Controller类，则返回一个空集合
        if(!isController){
            return mappingList;
        }

        //开始收集这个类上的信息
        Method[] methods = controllerClass.getMethods();

        //循环收集Mapping
        for (int i = 0; i < methods.length; i++) {
            //获取方法
            Method method = methods[i];

            //判断方法上是否有RequestMapping注解
            boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);

            //没有RequestMapping注解，则跳过
            if(!isRequestMapping){
                continue;
            }

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            //获取请求的url和method，以及contentType
            String requestUrl = requestMapping.url();
            String requestMethod = requestMapping.method();
            String contentType = requestMapping.contentType();

            //获取方法名
            String methodName = method.getName();

            //封装Mapping对象
            Mapping mapping = new Mapping();
            mapping.setUrl(requestUrl);
            mapping.setMethod(requestMethod);
            mapping.setControllerClass(controllerClassName);
            mapping.setMethodName(methodName);
            mapping.setContentType(contentType);

            //添加到集合中
            mappingList.add(mapping);
        }

        return mappingList;
    }

    /**
     * 检查是否有重复的映射
     * @param mappings 映射集合
     */
    protected static void checkDuplicateMapping(List<Mapping> mappings){
        Set<Mapping> mappingSet = new HashSet<Mapping>(mappings.size());

        for(Mapping mapping : mappings){
            int preSize = mappingSet.size();
            mappingSet.add(mapping);
            int postSize = mappingSet.size();

            //如果前后size没变，代表add的那个是已有的，重复了
            if (preSize == postSize){
                throw new RuntimeException("Duplicate Mappings: " + mapping.toDuplicateString());
            }
        }

    }

}
