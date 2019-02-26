package pri.liyang.mvc.entity;

/**
 * 封装映射信息的类
 */
public class Mapping {

    //请求url
    private String url;

    //请求方式
    private String method;

    //控制器class全类名
    private String controllerClass;

    //控制器对于方法名
    private String methodName;

    //返回媒体类型
    private String contentType;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(String controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    @Override
    public String toString() {
        return "Mapping{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", controllerClass='" + controllerClass + '\'' +
                ", methodName='" + methodName + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    /**
     * 返回重复映射的信息
     * @return 重复映射的信息
     */
    public String toDuplicateString(){
        return "{url='" + url + "', method='" + method + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Mapping)){
            return false;
        }

        Mapping compare = (Mapping) obj;

        if (this.getUrl().equals(compare.getUrl()) && this.getMethod().equals(compare.getMethod())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.getUrl().hashCode() * 31 + this.getMethod().hashCode();
    }
}
