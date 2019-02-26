package pri.liyang.mvc.controller;

import pri.liyang.mvc.annotations.Controller;
import pri.liyang.mvc.annotations.ParameterMap;
import pri.liyang.mvc.annotations.RequestMapping;
import pri.liyang.mvc.entity.ModelAndView;
import pri.liyang.server.constant.HttpMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 */
@Controller
public class MyController {

    /**
     * 公用的参数Map
     */
    @ParameterMap
    public static Map paramMap = new HashMap();

    /**
     * 获取当前时间的方法
     * @return
     */
    @RequestMapping(url = "/current/time", method = HttpMethod.GET)
    public String getCurrentTime(){
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return currentTime;
    }

    /**
     * 打招呼的方法
     * @return
     */
    @RequestMapping(url = "/greet/hello", method = HttpMethod.GET)
    public String sayHello(){
        return "Hello " + paramMap.get("name");
    }

    /**
     * 模仿登录的方法
     * @return
     */
    @RequestMapping(url = "/login", method = HttpMethod.GET)
    public String loginAuthentication(){
        String username = (String) paramMap.get("username");
        String password = (String) paramMap.get("password");

        if ("admin".equals(username) && "123456".equals(password)){
            return "login success";
        }else {
            return "login failed";
        }
    }

    /**
     * 模仿登录的方法2
     * @return
     */
    @RequestMapping(url = "/login", method = HttpMethod.POST)
    public String loginAuthentication2(){
        String username = (String) paramMap.get("username");
        String password = (String) paramMap.get("password");

        if ("admin".equals(username) && "123456".equals(password)){
            return "login success";
        }else {
            return "login failed";
        }
    }

    /**
     * 服务器内部报错
     */
    @RequestMapping(url = "/server/error", method = HttpMethod.GET)
    public String throwException(){
        throw new RuntimeException("Server died");
    }

    /**
     * Post提交
     */
    @RequestMapping(url = "/test/post", method = HttpMethod.POST)
    public String testPost(){
        return "post commit accepted";
    }

    /**
     * Delete提交
     */
    @RequestMapping(url = "/test/post", method = HttpMethod.DELETE)
    public String testPost2(){
        return "delete commit accepted";
    }

    /**
     * 测试获取视图
     * @return
     */
    @RequestMapping(url = "/student/infos", method = HttpMethod.GET)
    public ModelAndView getStudentInfo(){
        //模拟数据库获取数据
        String title = "学生名单";
        String[] students = new String[]{"小明", "小红", "小强", "小刚", "小芳"};

        //设置ModelAndView
        ModelAndView mav = new ModelAndView();
        mav.setAttribute("title", title);
        mav.setAttribute("students", students);
        mav.setViewName("student");

        return mav;
    }

}
