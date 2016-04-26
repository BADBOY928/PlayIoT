package site.liyan.playiot.hardware.component.detailentity;

/**
 * Created by l on 2016/4/25.
 */
public class MyGpioSensorComponentDetail {
    private Integer pinNum;
    private Boolean lowIsOpen;
    private Boolean open;

    public MyGpioSensorComponentDetail() {
    }

    public MyGpioSensorComponentDetail(Integer pinNum, Boolean lowIsOpen, Boolean open) {
        this.pinNum = pinNum;
        this.lowIsOpen = lowIsOpen;
        this.open = open;
    }

    public Integer getPinNum() {
        return pinNum;
    }

    public void setPinNum(Integer pinNum) {
        this.pinNum = pinNum;
    }

    public Boolean getLowIsOpen() {
        return lowIsOpen;
    }

    public void setLowIsOpen(Boolean lowIsOpen) {
        this.lowIsOpen = lowIsOpen;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "MyGpioSensorComponentDetail{" +
                "pinNum=" + pinNum +
                ", lowIsOpen=" + lowIsOpen +
                ", open=" + open +
                '}';
    }
}
