package site.liyan.playiot.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.liyan.playiot.dao.ComponentDao;
import site.liyan.playiot.entity.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by l on 2016/4/20.
 */
@Repository
public class ComponentDaoImpl implements ComponentDao {
    @Autowired
    private JdbcTemplate jt;

    public int queryCount() {
        return jt.query("SELECT COUNT(*) AS 'count' FROM component", resultSet -> {
            return resultSet.getInt("count");
        });
    }


    public List<Component> queryAll() {
        return jt.query("SELECT * FROM component", (resultSet, i) -> {
            return resultSet2Component(resultSet);
        });
    }

/*    public List<Component> queryAll() {
        return jt.query("SELECT * FROM component", (resultSet, i) -> {
            resultSet.next();
            return resultSet2Component(resultSet);
        });
    }*/

/*    public Component query(int id) {
        return jt.query("SELECT * FROM component WHERE id = ?", new Object[]{id}, new ResultSetExtractor<Component>() {
            @Override
            public Component extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                return resultSet2Component(resultSet);
            }
        });
    }*/

    public Component query(int id) {
        return jt.query("SELECT * FROM component WHERE id = ?", new Object[]{id}, resultSet -> {
            return resultSet2Component(resultSet);
        });
    }


/*    public int insert(Component component) {
        return jt.update("INSERT INTO component(type,name,about,detail) VALUES (?,?,?,?)", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, component.getType());
                preparedStatement.setString(2, component.getName());
                preparedStatement.setString(3, component.getAbout());
                preparedStatement.setString(4, component.getDetail());
            }
        });
    }*/

    public int insert(Component component) {
        return jt.update("INSERT INTO component(type,name,about,detail) VALUES (?,?,?,?)", preparedStatement -> {
            preparedStatement.setString(1, component.getType());
            preparedStatement.setString(2, component.getName());
            preparedStatement.setString(3, component.getAbout());
            preparedStatement.setString(4, component.getDetail());
        });
    }

/*    @Override
    public int update(Component component) {
        return jt.update("UPDATE component SET type=?, name=?, about=?, detail=? WHERE id=?", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, component.getType());
                preparedStatement.setString(2, component.getName());
                preparedStatement.setString(3, component.getAbout());
                preparedStatement.setString(4, component.getDetail());
                preparedStatement.setint(5, component.getId());
            }
        });
    }*/

    @Override
    public int update(Component component) {
        return jt.update("UPDATE component SET type=?, name=?, about=?, detail=? WHERE id=?", preparedStatement -> {
            preparedStatement.setString(1, component.getType());
            preparedStatement.setString(2, component.getName());
            preparedStatement.setString(3, component.getAbout());
            preparedStatement.setString(4, component.getDetail());
            preparedStatement.setInt(5, component.getId());
        });
    }

/*    @Override
    public int delete(int id) {
        return jt.update("DELETE FROM component WHERE id=?", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setint(1, id);
            }
        });
    }*/

    @Override
    public int delete(int id) {
        return jt.update("DELETE FROM component WHERE id=?", preparedStatement -> preparedStatement.setInt(1, id));
    }

    @Override
    public int deleteAll() {
        return jt.update("DELETE FROM component");
    }

    private Component resultSet2Component(ResultSet resultSet) throws SQLException, DataAccessException {
        Component component = new Component();

        component.setId(resultSet.getInt("id"));
        component.setType(resultSet.getString("type"));
        component.setName(resultSet.getString("name"));
        component.setAbout(resultSet.getString("about"));
        component.setDetail(resultSet.getString("detail"));

        return component;
    }

    @Override
    public boolean initDB() {
        String createComponentTable = "CREATE TABLE component ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, type TEXT NOT NULL, name TEXT NOT NULL, about TEXT NOT NULL, detail TEXT NOT NULL)";
        jt.execute(createComponentTable);

        String insertData1 = "INSERT INTO \"main\".\"component\" VALUES (1, 'MyGpioLEDComponent', 'LED灯', '测试', '{\"pinNum\":\"11\",\"lowIsOn\":\"false\",\"on\":\"false\"}')";
        //String insertData2 = "INSERT INTO \"main\".\"component\" VALUES (2, 'GpioPinDigitalMultipurpose', '光线传感器', '测试', '{\"pinNum\":\"15\",\"output\":\"false\",\"lowIsOn\":\"true\",\"on\":\"true\"}')";
        String insertData3 = "INSERT INTO \"main\".\"component\" VALUES (3, 'MyGpioLEDComponent', '继电器', '测试', '{\"pinNum\":\"12\",\"lowIsOn\":\"true\",\"on\":\"false\"}')";
        //String insertData4 = "INSERT INTO \"main\".\"component\" VALUES (4, 'GpioPinDigitalMultipurpose', '人体红外传感器', '测试', '{\"pinNum\":\"31\",\"output\":\"false\",\"lowIsOn\":\"false\",\"on\":\"false\"}')";

        int[] countArray = jt.batchUpdate(insertData1, insertData3);
        if (countArray[0] > 0 && countArray[1] > 0) {
            return true;
        } else {
            return false;
        }
    }
}
