package cn.egova.jt808.servlet;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.egova.openapi.constant.InitConst;
import cn.com.egova.openapi.runnable.ConsumVehiclePosRunnable;
import cn.com.egova.openapi.threadpool.SendThreadPool;


public class InitServlet extends HttpServlet {

    private static final long serialVersionUID = 1969966439776479881L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void init() {

        logger.info("init config..");
        InitConst.initConfig();

        for (int i = 0; i < 5; i++) {
            SendThreadPool.execute(new ConsumVehiclePosRunnable());
        }

    }
}
