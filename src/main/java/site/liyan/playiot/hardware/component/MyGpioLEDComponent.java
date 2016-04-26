package site.liyan.playiot.hardware.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.component.light.impl.GpioLEDComponent;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import site.liyan.playiot.common.MyPI;
import site.liyan.playiot.entity.Component;
import site.liyan.playiot.hardware.component.detailentity.MyGpioLEDComponentDetail;

import java.util.Collection;

/**
 * Created by l on 2016/4/25.
 */
public class MyGpioLEDComponent extends GpioLEDComponent implements MyBaseComponent {
    private static final Log logger = LogFactory.getLog(MyGpioLEDComponent.class);

    private GpioPinDigitalOutput pin;
    private PinState onState;
    private PinState offState;

    private ObjectMapper mapper;
    private MyGpioLEDComponentDetail detail;

    public MyGpioLEDComponent(GpioPinDigitalOutput pin, PinState onState, PinState offState) {
        super(pin, onState, offState);
        this.pin = pin;
        this.onState = onState;
        this.offState = offState;
        mapper = new ObjectMapper();
    }

    public static boolean init(Component component) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        logger.debug(mapper.writeValueAsString(component));

        PinState onState;
        PinState offState;
        GpioPinDigitalOutput pin;

        MyGpioLEDComponentDetail detail = mapper.readValue(component.getDetail(), MyGpioLEDComponentDetail.class);

        if (detail.getLowIsOn()) {
            onState = PinState.LOW;
            offState = PinState.HIGH;
        } else {
            onState = PinState.HIGH;
            offState = PinState.LOW;
        }

        pin = MyPI.gpioController.provisionDigitalOutputPin(MyPI.pinMap.get(detail.getPinNum()), detail.getOn() ? onState : offState);

        MyGpioLEDComponent myGpioLEDComponent = new MyGpioLEDComponent(pin, onState, offState);

        MyPI.pi4jComponentMap.put(component.getId(), myGpioLEDComponent);
        logger.debug("已初始化的组件：" + mapper.writeValueAsString(MyPI.pi4jComponentMap));

        return true;
    }

    @Override
    public Component get(Component component) throws Exception {
        logger.debug(mapper.writeValueAsString(component));
        detail = mapper.readValue(component.getDetail(), MyGpioLEDComponentDetail.class);

        if (isOn()) {
            detail.setOn(true);
        } else {
            detail.setOn(false);
        }

        component.setDetail(mapper.writeValueAsString(detail));
        return component;
    }

    @Override
    public boolean set(Component component) throws Exception {
        logger.debug(mapper.writeValueAsString(component));
        detail = mapper.readValue(component.getDetail(), MyGpioLEDComponentDetail.class);

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
        if (detail.getLowIsOn()) {
            onState = PinState.LOW;
            offState = PinState.HIGH;
        } else {
            onState = PinState.HIGH;
            offState = PinState.LOW;
        }

        //开关
        if (detail.getOn()) {
            on();
        } else {
            off();
        }

        return true;
    }

    @Override
    public boolean delete(Component component) throws Exception {
        logger.debug(mapper.writeValueAsString(component));
        detail = mapper.readValue(component.getDetail(), MyGpioLEDComponentDetail.class);

        //关闭
        off();
        //解除
        MyPI.gpioController.unprovisionPin(pin);
        //移除
        MyPI.pi4jComponentMap.remove(component.getId());

        return true;
    }
}
