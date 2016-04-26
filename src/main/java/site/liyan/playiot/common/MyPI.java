package site.liyan.playiot.common;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by l on 2016/4/20.
 */
public class MyPI {

    //GPIO控制器
    public static GpioController gpioController;

    //所有pi4j组件实例(key:id,value:object)
    public static Map<Integer, Object> pi4jComponentMap;

    //物理针脚号与软件针脚号的对应关系。 key:物理针脚号 value：软件针脚号
    public static Map<Integer, Pin> pinMap = new HashMap<Integer, Pin>() {
        {
            put(11, RaspiPin.GPIO_00);
            put(12, RaspiPin.GPIO_01);
            put(13, RaspiPin.GPIO_02);
            put(15, RaspiPin.GPIO_03);
            put(16, RaspiPin.GPIO_04);
            put(18, RaspiPin.GPIO_05);
            put(22, RaspiPin.GPIO_06);
            put(7, RaspiPin.GPIO_07);
            put(3, RaspiPin.GPIO_08);
            put(5, RaspiPin.GPIO_09);
            put(24, RaspiPin.GPIO_10);
            put(26, RaspiPin.GPIO_11);
            put(19, RaspiPin.GPIO_12);
            put(21, RaspiPin.GPIO_13);
            put(23, RaspiPin.GPIO_14);
            put(8, RaspiPin.GPIO_15);
            put(10, RaspiPin.GPIO_16);
            // pinMap.put(, RaspiPin.GPIO_17);
            // pinMap.put(, RaspiPin.GPIO_18);
            // pinMap.put(, RaspiPin.GPIO_19);
            // pinMap.put(, RaspiPin.GPIO_20);
            put(29, RaspiPin.GPIO_21);
            put(31, RaspiPin.GPIO_22);
            put(33, RaspiPin.GPIO_23);
            put(35, RaspiPin.GPIO_24);
            put(37, RaspiPin.GPIO_25);
            put(32, RaspiPin.GPIO_26);
            put(36, RaspiPin.GPIO_27);
            put(38, RaspiPin.GPIO_28);
            put(40, RaspiPin.GPIO_29);
        }
    };
}
