package site.liyan.playiot.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by l on 2016/4/17.
 */
public class Component implements Serializable {

    private Integer id;

    private String type;

    private String name;

    private String about;

    private String detail;

    public Component() {
    }

    public Component(Integer id, String type, String name, String about, String detail) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.about = about;
        this.detail = detail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("type", type);
        map.put("name", name);
        map.put("about", about);
        map.put("detail", detail);
        return map;
    }
}
