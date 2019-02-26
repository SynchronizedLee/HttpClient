package pri.liyang.server.test;

import pri.liyang.mvc.handler.ConfigurationHandler;
import pri.liyang.mvc.handler.MappingHandler;
import pri.liyang.mvc.handler.StartUpHandler;
import pri.liyang.server.entity.Request;
import pri.liyang.server.entity.Response;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 永远都在等请求，可以一直访问的服务器
 */
public class HttpServerForever {

    public static void main(String[] args) throws Exception {
        //启动预加载
        StartUpHandler.startUp();

        //获取端口号
        int port = Integer.parseInt(ConfigurationHandler.getSystemProperty("server.port"));

        //开启一个服务端Socket，监听12345端口
        ServerSocket server = new ServerSocket(port);

        while (true){
            //有客户端连进来
            Socket client = server.accept();

            //封装request
            Request request = Request.makeRequest(client);

            //根据Controller类定义的映射，处理request，并返回response
            Response response = MappingHandler.handleRequest(request);

            //发送响应回客户端
            response.sendResponse(client);

            //关闭客户端
            client.close();
        }
    }

}
