package site.liyan.playiot.hardware.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.component.sensor.impl.GpioSensorComponent;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import site.liyan.playiot.common.MyPI;
import site.liyan.playiot.entity.Component;
import site.liyan.playiot.hardware.component.detailentity.MyGpioSensorComponentDetail;

import java.util.Collection;

/**
 * Created by l on 2016/4/25.
 */
public class MyGpioSensorComponent extends GpioSensorComponent implements MyBaseComponent {
    private static final Log logger = LogFactory.getLog(MyGpioSensorComponent.class);

    private GpioPinDigitalInput pin;
    private PinState openState;
    private PinState closedState;

    private ObjectMapper mapper;
    private MyGpioSensorComponentDetail detail;

    public MyGpioSensorComponent(GpioPinDigitalInput pin, PinState openState, PinState closedState) {
        super(pin, openState, closedState);
        this.pin = pin;
        this.openState = openState;
        this.closedState = closedState;
        mapper = new ObjectMapper();
    }

    public static boolean init(Component component) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        logger.debug(mapper.writeValueAsString(component));

        PinState openState;
        PinState closedState;
        GpioPinDigitalInput pin;
        boolean isSuccess = false;

        MyGpioSensorComponentDetail detail = mapper.readValue(component.getDetail(), MyGpioSensorComponentDetail.class);

        if (detail.getOpen()) {
            openState = PinState.LOW;
            closedState = PinState.HIGH;
        } else {
            openState = PinState.HIGH;
            closedState = PinState.LOW;
        }

        pin = MyPI.gpioController.provisionDigitalInputPin(MyPI.pinMap.get(detail.getPinNum()), PinPullResistance.PULL_DOWN);

        MyGpioSensorComponent myGpioSensorComponent = new MyGpioSensorComponent(pin, openState, closedState);

        MyPI.pi4jComponentMap.put(component.getId(), myGpioSensorComponent);

        return isSuccess;
    }

    @Override
    public Component get(Component component) throws Exception {
        logger.debug(mapper.writeValueAsString(component));
        detail = mapper.readValue(component.getDetail(), MyGpioSensorComponentDetail.class);

        if (isOpen()) {
            detail.setOpen(true);
        } else {
            detail.setOpen(false);
        }

        component.setDetail(mapper.writeValueAsString(detail));
        return component;
    }

    @Override
    public boolean set(Component component) throws Exception {
        logger.debug(mapper.writeValueAsString(component));
        detail = mapper.readValue(component.getDetail(), MyGpioSensorComponentDetail.class);

        //针脚变动
        if (MyPI.pinMap.get(detail.getPinNum()).getAddress() != pin.getPin().getAddress()) {
            //检查新针脚是否已被占用
            boolean isPinUsed = false;
            String name = null;
            Collection<GpioPin> gpioPinCollection = MyPI.gpioController.getProvisionedPins();
            for (GpioPin gpioPin : gpioPinCollection) {
                if (gpioPin.getPin().equals(pin)) {
                    isPinUsed = true;
                    name = gpioPin.getName() == null ? "未知" : gpioPin.getName();
                    break;
                }
            }

            if (isPinUsed) {
                //已被占用
                throw new Exception("对应针脚已被" + name + "组件占用，请先将其删除");
            } else {
                if (init(component)) {
                    //删除原组件
                    delete(component);
                } else {
                    return false;
                }
            }
        }

        //电平与开关对应关系
        if (detail.getLowIsOpen()) {
            openState = PinState.LOW;
            closedState = PinState.HIGH;
        } else {
            openState = PinState.HIGH;
            closedState = PinState.LOW;
        }

        return true;
    }

    @Override
    public boolean delete(Component component) throws Exception {
        logger.debug(mapper.writeValueAsString(component));
        detail = mapper.readValue(component.getDetail(), MyGpioSensorComponentDetail.class);

        //解除
        MyPI.gpioController.unprovisionPin(pin);
        //移除
        MyPI.pi4jComponentMap.remove(component.getId());

        return true;
    }
}
