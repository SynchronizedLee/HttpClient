package pri.liyang.server.entity;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求的类，封装了各请求参数，以及实用方法
 */
public class Request {

    //请求方法
    private String method;

    //请求url
    private String url;

    //请求参数
    private Map<String, String> parameters;

    //请求体
    private String requestBody;

    //请求主机
    private String host;

    //端口号
    private int port;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", parameters=" + parameters +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    /**
     * 根据请求报文，封装返回Request对象
     * @param client 客户端Socket
     * @return Request
     * @throws IOException
     */
    public static Request makeRequest(Socket client) throws IOException {
        //获取到客户端输入流
        InputStream in = client.getInputStream();

        //准备一个缓冲数组
        byte data[]=new byte[4096];

        //这里有一个read（byte[] b）方法，将数据读取到字节数组中，同返回读取长度
        int len=in.read(data);

        //返回封装好的Request
        return parseRequest(new String(data));
    }

    /**
     * 根据请求报文，封装Request对象
     * @param requestMessage 浏览器发来的请求报文
     * @return Request
     */
    private static Request parseRequest(String requestMessage){
        Request request = new Request();

        //分解请求报文
        String[] requestMessages = requestMessage.split("\r\n");

        //获取请求方法
        String requestMethod = requestMessages[0].split(" ")[0];
        request.setMethod(requestMethod);

        //获取请求url
        String requestUrl = requestMessages[0].split(" ")[1];
        if(requestUrl.contains("?")){
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        request.setUrl(requestUrl);

        //获取请求的参数
        String requestParams = requestMessages[0].split(" ")[1];
        if(requestParams.contains("?")){
            requestParams = requestParams.split("\\?")[1];
        }
        String[] paramArray = requestParams.split("\\&");
        Map<String, String> paramMap = new HashMap<String, String>();

        //如果没有参数：
        if(paramArray.length == 1 && !paramArray[0].contains("=")){
            request.setParameters(paramMap);
        }else{
            for (int i = 0; i < paramArray.length; i++) {
                String key = paramArray[i].split("=")[0];
                String value = paramArray[i].split("=")[1];
                paramMap.put(key, value);
            }
            request.setParameters(paramMap);
        }

        //获取请求的主机
        if (requestMessages[1].contains(":") && requestMessages[1].split(":").length >= 2){
            String host = requestMessages[1].split(":")[1].trim();
            request.setHost(host);
        }else {
            request.setHost("Unknown Host");
        }


        //获取请求的端口
        if (requestMessages[1].contains(":") && requestMessages[1].split(":").length >= 3){
            String port = requestMessages[1].split(":")[2].trim();
            request.setPort(Integer.parseInt(port));
        } else {
            request.setPort(0);
        }

        //如果有，获取请求体
        if (requestMessage.contains("\r\n\r\n")){
            int pos = requestMessage.lastIndexOf("\r\n\r\n");
            String requestBody = requestMessage.substring(pos + "\r\n\r\n".length(), requestMessage.length());
            request.setRequestBody(requestBody);
        }

        //如果有requestBody存在，则将之弄成参数
        if (request.requestBody != null && request.requestBody.trim().length() > 0) {
            try {
                //将请求体转化为Map
                Map requestBodyMap = JSON.parseObject(request.requestBody);

                //追加进去
                paramMap.putAll(requestBodyMap);
            }catch (Exception e){
                System.err.println("RequestBody的Json解析错误！");
                e.printStackTrace();
            }
        }

        return request;
    }

    /**
     * 获取请求中的参数
     * @param key 参数key
     * @return 参数值
     */
    public String getParameter(String key){
        return this.parameters.get(key);
    }

}
