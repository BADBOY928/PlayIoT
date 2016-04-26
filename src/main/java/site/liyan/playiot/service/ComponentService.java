package site.liyan.playiot.service;

import site.liyan.playiot.entity.Component;

import java.io.File;
import java.util.List;

/**
 * Created by l on 2016/4/17.
 */
public interface ComponentService {

    List<Component> initAllComponent() throws Exception;

    List<Component> getAllComponent() throws Exception;

    Component getComponentById(int id) throws Exception;

    boolean addComponent(Component component) throws Exception;

    boolean setComponent(Component component) throws Exception;

    boolean deleteComponent(int id) throws Exception;

    List<Component> deleteAllComponent() throws Exception;

    File getWebcamPhoto(int id) throws Exception;
}
