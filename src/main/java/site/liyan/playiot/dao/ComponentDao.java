package site.liyan.playiot.dao;

import site.liyan.playiot.entity.Component;

import java.util.List;

/**
 * Created by l on 2016/4/20.
 */
public interface ComponentDao {
    int queryCount();

    List<Component> queryAll();

    Component query(int id);

    int insert(Component component);

    int update(Component component);

    int delete(int id);

    int deleteAll();

    boolean initDB();
}
