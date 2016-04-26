package site.liyan.playiot.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by l on 2016/4/17.
 */
@RestController
public class HomeController {

    private static Log logger = LogFactory.getLog(HomeController.class);

    @RequestMapping("/")
    public String home() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "系统运行中！主机当前时间：" + df.format(new Date());

        logger.info(str);

        return str;
    }
}
