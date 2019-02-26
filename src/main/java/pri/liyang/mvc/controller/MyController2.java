package pri.liyang.mvc.controller;

import pri.liyang.mvc.annotations.Controller;
import pri.liyang.mvc.annotations.ParameterMap;
import pri.liyang.mvc.annotations.RequestMapping;
import pri.liyang.server.constant.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试多个Controller
 */
@Controller
public class MyController2 {

    /**
     * 公用的参数Map
     */
    @ParameterMap
    public static Map paramMap = new HashMap();

    /**
     * 测试多Controller
     * @return
     */
    @RequestMapping(url = "/test/multi", method = HttpMethod.GET)
    public String testMultiController(){
        String param = (String) paramMap.get("multiParam");

        return "test multi controller success: " + param;
    }

}
