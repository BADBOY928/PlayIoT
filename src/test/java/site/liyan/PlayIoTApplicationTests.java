package site.liyan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import site.liyan.playiot.PlayIoTApplication;
import site.liyan.playiot.dao.ComponentDao;
import site.liyan.playiot.entity.Component;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PlayIoTApplication.class)
@WebAppConfiguration
public class PlayIoTApplicationTests {

    @Autowired
    private ComponentDao componentDao;

    @Test
    public void contextLoads() {
        Component component = new Component();
        //component.setId(9);
        component.setType("GPIO...");
        component.setName("组件名");
        component.setAbout("组件关于");
        component.setDetail("组件详情");


//        componentDao.queryAll();
//        componentDao.query(9);
//        componentDao.insert(component);
//        componentDao.update(component);
//        componentDao.delete(component.getId());

    }


}
