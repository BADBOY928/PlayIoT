package site.liyan.playiot.hardware.component;

import site.liyan.playiot.entity.Component;

/**
 * Created by l on 2016/4/25.
 */
public interface MyBaseComponent {
    Component get(Component component) throws Exception;

    boolean set(Component component) throws Exception;

    boolean delete(Component component) throws Exception;
}
