package site.liyan.playiot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liyan.playiot.common.Const;
import site.liyan.playiot.common.MyPI;
import site.liyan.playiot.common.PiUtil;
import site.liyan.playiot.dao.ComponentDao;
import site.liyan.playiot.entity.Component;
import site.liyan.playiot.hardware.component.MyBaseComponent;
import site.liyan.playiot.service.ComponentService;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/4/17.
 */
@Service
public class ComponentServiceImpl implements ComponentService {
    private static final Log logger = LogFactory.getLog(ComponentServiceImpl.class);

    //所有类型组件控制类所在的包
    private static final String MY_COMPONENT_PACKAGE_WHIT_POINT = "site.liyan.playiot.hardware.component.";

    @Autowired
    private ComponentDao componentDao;

    /**
     * 初始化所有组件
     *
     * @return 初始化失败的组件
     * @throws Exception
     */
    @Override
    public List<Component> initAllComponent() throws Exception {
        logger.debug("initAllComponent start");

        //查询db中所有组件
        List<Component> componentList = componentDao.queryAll();

        ObjectMapper mapper = new ObjectMapper();
        logger.debug(mapper.writeValueAsString(componentList));

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return null;
        }

        //硬件初始化所有组件
        List<Component> failedComponentList = new ArrayList<>();
        componentList.forEach(component -> {
            boolean isSuccess = false;
            try {
                String className = MY_COMPONENT_PACKAGE_WHIT_POINT + component.getType();
                logger.debug(className);

                //反射调用相应类的静态init方法
                Class<?> clazz = Class.forName(className);
                Method method = clazz.getMethod("init", Component.class);
                isSuccess = (boolean) method.invoke(null, component);

                //失败组件汇总
                if (!isSuccess) {
                    failedComponentList.add(component);
                }
            } catch (Exception e) {
                logger.error(e.toString());
                //失败组件汇总
                failedComponentList.add(component);
            }
        });

        return failedComponentList;
    }

    @Override
    public List<Component> getAllComponent() throws Exception {
        logger.debug("getAllComponent start");

        //查询db中所有组件
        List<Component> componentList = componentDao.queryAll();

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return componentList;
        }

        //获取实时数据
        List<Component> realTimeComponentList = new ArrayList<>();
        componentList.forEach(component -> {
            try {
                MyBaseComponent myBaseComponent = (MyBaseComponent) MyPI.pi4jComponentMap.get(component.getId());
                component = myBaseComponent.get(component);

                //组件数据汇总
                realTimeComponentList.add(component);
            } catch (Exception e) {
                logger.error(e.toString());
                //TODO 个别组件获取失败如何处理
            }
        });

        return realTimeComponentList;
    }

    @Override
    public Component getComponentById(int id) throws Exception {
        logger.debug("getComponentById start");

        //获取已有数据
        Component component = componentDao.query(id);

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return component;
        }

        //获取实时数据
        MyBaseComponent myBaseComponent = (MyBaseComponent) MyPI.pi4jComponentMap.get(component.getId());
        component = myBaseComponent.get(component);

        return component;
    }

    @Override
    public boolean addComponent(Component component) throws Exception {
        logger.debug("addComponent start");

        //向db新增组件
        int id = componentDao.insert(component);
        //更新组件id
        component.setId(id);

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return true;
        }

        String className = MY_COMPONENT_PACKAGE_WHIT_POINT + component.getType();
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod("init", Component.class);

        return (boolean) method.invoke(null, component);
    }

    @Override
    public boolean setComponent(Component component) throws Exception {
        logger.debug("setComponent start");

        //更新db组件数据
        int count = componentDao.update(component);
        if (count <= 0) {
            return false;
        }

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return true;
        }

        //设置组件
        MyBaseComponent myBaseComponent = (MyBaseComponent) MyPI.pi4jComponentMap.get(component.getId());
        return myBaseComponent.set(component);
    }

    @Override
    public boolean deleteComponent(int id) throws Exception {
        logger.debug("deleteComponent start");

        //获取db中数据
        Component component = componentDao.query(id);

        //删除db中数据
        int count = componentDao.delete(id);
        if (count <= 0) {
            return false;
        }

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return true;
        }

        //删除组件
        MyBaseComponent myBaseComponent = (MyBaseComponent) MyPI.pi4jComponentMap.get(component.getId());
        return myBaseComponent.delete(component);
    }

    @Override
    public List<Component> deleteAllComponent() throws Exception {
        logger.debug("deleteAllComponent start");

        //删除结果
        List<Component> failedComponentList = new ArrayList<>();

        //查询db中所有组件
        List<Component> componentList = componentDao.queryAll();

        //删除db中所有组件数据
        int count = componentDao.deleteAll();

        //找出删除失败的组件记录
        if (count != componentList.size()) {
            //失败结果汇总
            failedComponentList.addAll(componentDao.queryAll());
            //未删除的记录不参与下一步删除
            componentList.removeAll(failedComponentList);
        }

        //非arm，不执行硬件操作
        if (!PiUtil.isArm()) {
            logger.debug("not arm, return.");
            return failedComponentList;
        }

        //删除组件
        componentList.forEach(component -> {
            try {
                MyBaseComponent myBaseComponent = (MyBaseComponent) MyPI.pi4jComponentMap.get(component.getId());
                boolean isSuccess = myBaseComponent.delete(component);

                //失败结果汇总
                if (!isSuccess) {
                    failedComponentList.add(component);
                }
            } catch (Exception e) {
                logger.error(e.toString());
                //失败结果汇总
                failedComponentList.add(component);
            }
        });

        return failedComponentList;
    }


    @Override
    public File getWebcamPhoto(int id) throws Exception {
        logger.debug("getWebcamPhoto start");

        //获取组件
        Component component = componentDao.query(id);
        //组件类型错误，返回null
        if (!Const.WEBCAM.equals(component.getType())) {
            return null;
        }


        //采集
        return PiUtil.getPhoto(component);
    }
}
