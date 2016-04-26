package site.liyan.playiot.entity;

import java.io.Serializable;

/**
 * Created by l on 2016/4/20.
 */
public class ResultMsg implements Serializable {
    boolean success;
    String message;

    public ResultMsg() {
    }

    public ResultMsg(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
