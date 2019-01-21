package cn.com.egova.openapi.base.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class BeanUtils {

    public static ApplicationContext ctx = null;

    private static void initContext(String beanName) {
        // 可能web 环境没有启动即调用
        if (ctx == null) {
            ctx = ContextLoaderListener.getCurrentWebApplicationContext();
            if (ctx == null) {
                if (!beanName.equals("")) {
                    System.err.println(" if not tester, error init, please wait the web container init success! beanName :" + beanName);
                }
                ctx = new ClassPathXmlApplicationContext(new String[]{"classpath*:applicationContext-*.xml",
                        "classpath*:applicationContext.xml", "classpath*:application-*.xml"});
            }
        }
    }

    public static Object getBean(String beanName) {
        initContext(beanName);
        return ctx.getBean(beanName);
    }

}
