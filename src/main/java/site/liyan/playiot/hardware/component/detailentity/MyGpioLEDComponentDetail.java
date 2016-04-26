package site.liyan.playiot.hardware.component.detailentity;

/**
 * Created by l on 2016/4/25.
 */
public class MyGpioLEDComponentDetail {
    private Integer pinNum;
    private Boolean lowIsOn;
    private Boolean on;

    public MyGpioLEDComponentDetail() {
    }

    public MyGpioLEDComponentDetail(Integer pinNum, Boolean lowIsOn, Boolean on) {
        this.pinNum = pinNum;
        this.lowIsOn = lowIsOn;
        this.on = on;
    }

    public Integer getPinNum() {
        return pinNum;
    }

    public void setPinNum(Integer pinNum) {
        this.pinNum = pinNum;
    }

    public Boolean getLowIsOn() {
        return lowIsOn;
    }

    public void setLowIsOn(Boolean lowIsOn) {
        this.lowIsOn = lowIsOn;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    @Override
    public String toString() {
        return "MyGpioLEDComponentDetail{" +
                "pinNum=" + pinNum +
                ", lowIsOn=" + lowIsOn +
                ", on=" + on +
                '}';
    }

}
