package pri.liyang.mvc.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图和模型实体类
 */
public class ModelAndView {

    //视图名
    private String viewName;

    //视图参数
    private Map paramMap = new HashMap();


    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map paramMap) {
        this.paramMap = paramMap;
    }


    //设置单个属性
    public void setAttribute(String key, Object value){
        this.paramMap.put(key, value);
    }

    //设置多个属性
    public void setAttributes(Map paramMap){
        this.paramMap.putAll(paramMap);
    }

}
