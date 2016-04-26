package site.liyan.playiot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.gpio.GpioFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import site.liyan.playiot.common.MyPI;
import site.liyan.playiot.common.PiUtil;
import site.liyan.playiot.dao.ComponentDao;
import site.liyan.playiot.entity.Component;
import site.liyan.playiot.service.ComponentService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class PlayIoTApplication {
    private static Log logger = LogFactory.getLog(PlayIoTApplication.class);

    @Autowired
    private ComponentService componentService;
    @Autowired
    private ComponentDao componentDao;

    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {

            @Override
            public void contextInitialized(ServletContextEvent sce) {
                logger.debug("ServletContext initialized");

                //初始化数据库
                File dbFile = new File("playiot.db");
                if (!dbFile.exists()) {
                    logger.debug("db文件不存在，将创建数据库");
                    boolean isSuccess = componentDao.initDB();
                    if (!isSuccess) {
                        logger.error("数据库初始化失败");
                    } else {
                        logger.debug("数据库文件初始化成功，存储路径：" + dbFile.getAbsolutePath());
                    }
                }

                //获取gpio控制器实例
                if (PiUtil.isArm()) {
                    MyPI.pi4jComponentMap = new HashMap<>();
                    MyPI.gpioController = GpioFactory.getInstance();

                    if (null == MyPI.gpioController) {
                        logger.error("获取gpio控制器实例失败");
                    } else {
                        logger.debug("获取gpio控制器实例成功");
                    }
                }

                //初始化所有组件
                try {
                    List<Component> failedComponentList = componentService.initAllComponent();
                    if (null != failedComponentList && failedComponentList.size() > 0) {
                        ObjectMapper mapper = new ObjectMapper();
                        logger.error("部分组件初始化失败：" + mapper.writeValueAsString(failedComponentList));
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                }

            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                logger.debug("ServletContext destroyed");
            }

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PlayIoTApplication.class, args);
    }
}
