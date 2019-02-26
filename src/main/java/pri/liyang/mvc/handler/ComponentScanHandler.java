package pri.liyang.mvc.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件扫描处理类
 */
public class ComponentScanHandler {

    //装找到文件的list
    private List<String> fileList = new ArrayList<String>();

    /**
     * 将文件以树的方式遍历收集
     * @param file 根文件
     * @return
     * @throws IOException
     */
    private List<String> fileTree(File file) throws IOException {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()){
                    fileTree(files[i]);
                }

                if (files[i].isFile()){
                    fileList.add(files[i].getPath().substring(0, files[i].getPath().lastIndexOf(".")));
                }
            }
        }else {
            fileList.add(file.getPath().substring(0, file.getPath().lastIndexOf(".")));
        }

        return fileList;
    }

    /**
     * 将文件路径转化为包名+类名
     * @param fileName 文件名
     * @param basePackage 基础包前缀
     * @return
     */
    private String parseFileName(String fileName, String basePackage){
        //先把前缀包名去掉
        fileName = fileName.substring(basePackage.length(), fileName.length());

        //再将分隔符\换成.
        fileName = fileName.replaceAll("\\\\", ".");

        return fileName;
    }

    /**
     * 找到具有特定注解的类的Class对象list
     * @param annotationClass 需要找的注解
     * @param basePackage 基础包，就是包名前面的那一串，一般在配置文件里面找
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public List<String> getAnnotationByPackage(Class annotationClass, String basePackage) throws ClassNotFoundException, IOException {
        File file = new File(basePackage);

        //获得所有的扫描到的文件
        List<String> files = new ComponentScanHandler().fileTree(file);

        List<String> classList = new ArrayList<String>();

        for(String fileName : files){
            //扫描到的文件，获取包名+类名
            fileName = parseFileName(fileName, basePackage);

            try {
                //获取其Class对象
                Class clazz = Class.forName(fileName);

                //如果该类有该注解，则将包名+类名加入到将要返回的classList
                if (clazz.isAnnotationPresent(annotationClass)){
                    classList.add(clazz.getName());
                }
            } catch (Exception e){
                //如果是遇到了其他非java文件，则跳过
                e.printStackTrace();
            }
        }

        return classList;
    }

}
