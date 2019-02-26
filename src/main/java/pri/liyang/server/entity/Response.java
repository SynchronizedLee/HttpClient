package pri.liyang.server.entity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Response {

    //状态码
    private int statusCode;

    //状态内容
    private String statusContent;

    //媒体类型
    private String contentType;

    //响应体的内容
    private String response;


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusContent() {
        return statusContent;
    }

    public void setStatusContent(String statusContent) {
        this.statusContent = statusContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    @Override
    public String toString() {
        //制作响应报文
        StringBuffer response = new StringBuffer();

        //响应状态
        response.append("HTTP/1.1 ").append(this.statusCode).append(" ").append(this.statusContent).append("\r\n");

        //响应头
        response.append("Content-type:").append(contentType).append("\r\n\r\n");

        //要返回的内容
        response.append(this.response);

        //返回制作好的response报文
        return response.toString();
    }

    public void sendResponse(Socket client) throws IOException {
        //获取客户端的输出流
        OutputStream out=client.getOutputStream();

        //将以上内容写入
        out.write(this.toString().getBytes());
    }

}
