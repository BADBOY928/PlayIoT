package site.liyan.playiot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liyan.playiot.entity.Component;
import site.liyan.playiot.entity.ResultMsg;
import site.liyan.playiot.service.ComponentService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/4/17.
 */
@RestController
public class ComponentController {
    private static final Log logger = LogFactory.getLog(HomeController.class);

    @Autowired
    private ComponentService componentService;

    /**
     * 获取所有组件实时数据
     *
     * @return
     */
    @RequestMapping(value = "/components", method = RequestMethod.GET)
    public List<Component> getAllComponent() {
        logger.debug("getAllComponent start");
        
        List<Component> componentList = new ArrayList<>();
        try {
            componentList = componentService.getAllComponent();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return componentList;
    }


    /**
     * 获取单个组件实时数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/components/{id}", method = RequestMethod.GET)
    public Component getComponent(@PathVariable int id) {
        logger.debug("getComponent start");

        Component component = new Component();
        try {
            component = componentService.getComponentById(id);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return component;
    }

    /**
     * 添加单个组件
     *
     * @param component
     * @return
     */
    @RequestMapping(value = "/components", method = RequestMethod.POST)
    public ResultMsg addComponent(Component component) {
        logger.debug("addComponent start");

        ResultMsg resultMsg = new ResultMsg();

        try {
            boolean isSuccess = componentService.addComponent(component);
            resultMsg.setSuccess(isSuccess);
        } catch (Exception e) {
            logger.error(e.toString());
            resultMsg.setSuccess(false);
            resultMsg.setMessage(e.getMessage());
        }

        return resultMsg;
    }

    /**
     * 设置单个组件
     *
     * @param component
     * @param id
     * @return
     */
    @RequestMapping(value = "/components/{id}", method = RequestMethod.PUT)
    public ResultMsg setComponent(@RequestBody Component component, @PathVariable int id) {
        logger.debug("setComponent start");

        ResultMsg resultMsg = new ResultMsg();

        try {
            boolean isSuccess = componentService.setComponent(component);
            resultMsg.setSuccess(isSuccess);
        } catch (Exception e) {
            logger.error(e.toString());
            resultMsg.setSuccess(false);
            resultMsg.setMessage(e.getMessage());
        }

        return resultMsg;
    }

    /**
     * 删除单个组件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/components/{id}", method = RequestMethod.DELETE)
    public ResultMsg deleteComponent(@PathVariable int id) {
        logger.debug("deleteComponent start");

        ResultMsg resultMsg = new ResultMsg();

        try {
            boolean isSuccess = componentService.deleteComponent(id);
            resultMsg.setSuccess(isSuccess);
        } catch (Exception e) {
            logger.error(e.toString());
            resultMsg.setSuccess(false);
            resultMsg.setMessage(e.getMessage());
        }

        return resultMsg;
    }


    /**
     * 删除所有组件
     *
     * @return
     */
    @RequestMapping(value = "/components", method = RequestMethod.DELETE)
    public ResultMsg deleteAllComponent() {
        logger.debug("deleteAllComponent start");

        ResultMsg resultMsg = new ResultMsg();

        try {
            List<Component> failedComponentList = componentService.deleteAllComponent();
            if (null == failedComponentList) {
                resultMsg.setSuccess(true);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                resultMsg.setSuccess(false);
                resultMsg.setMessage("部分组件删除失败：" + mapper.writeValueAsString(failedComponentList));
            }
        } catch (Exception e) {
            logger.error(e.toString());
            resultMsg.setSuccess(false);
            resultMsg.setMessage(e.getMessage());
        }

        return resultMsg;
    }

    /**
     * 获取指定摄像头组件采集最新图像
     */
    @RequestMapping(value = "/components/{id}/photo", method = RequestMethod.GET)
    public void getWebcamPhoto(@PathVariable int id, HttpServletResponse response) {
        logger.debug("getWebcamPhoto start");

        try {
            //采集图像
            File file = componentService.getWebcamPhoto(id);
            //读取，输出
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int length = inputStream.read(data);
            inputStream.close();

            response.setContentType("image/png");
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}
