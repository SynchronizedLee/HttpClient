package pri.liyang.mvc.handler;

/**
 * 启动处理类
 */
public class StartUpHandler {

    /**
     * 启动Server的预处理
     */
    public static void startUp(){
        try {
            //预加载系统配置文件
            Class.forName("pri.liyang.mvc.handler.ConfigurationHandler");

            //预加载Mappings
            Class.forName("pri.liyang.mvc.handler.MappingHandler");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
