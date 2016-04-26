package site.liyan.playiot.common;

import site.liyan.playiot.entity.Component;

import java.io.File;
import java.util.Properties;

/**
 * Created by l on 2016/4/21.
 */
public class PiUtil {

    /**
     * 判断系统架构是否为arm
     *
     * @return
     */
    public static boolean isArm() {
        Properties props = System.getProperties();
        String arch = props.getProperty("os.arch");
        if (arch.contains("arm")) {
            return true;
        } else {
            return false;
        }
    }

/*    public static boolean initComponent(Component component) throws Exception {
        boolean isSuccess = false;

        switch (component.getType()) {
            case Const.MY_GPIO_LED_COMPONENT:
                isSuccess = initMyGpioLEDComponent(component);
                break;
        }

        return isSuccess;
    }

    private static boolean initMyGpioLEDComponent(Component component) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyGpioLEDComponentDetail detail = mapper.readValue(component.getDetail(), MyGpioLEDComponentDetail.class);



        return false;
    }*/

    /**
     * 采集图像
     *
     * @param component
     * @return
     */
    public static File getPhoto(Component component) throws Exception {
/*
        VideoInputFrameGrabber webcam = new VideoInputFrameGrabber(0); // 1 for next camera
        FrameGrabber grabber = webcam;
        grabber.start();
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

        Frame frame = grabber.grab();
        opencv_core.IplImage img = converter.convert(frame);

        BufferedImage bi = IplImageToBufferedImage(img);

        File file = new File("C:/Test/Y.jpg");
        ImageIO.write(bi, "jpg", file);


        return file;*/
        return null;
    }

/*    public static BufferedImage IplImageToBufferedImage(opencv_core.IplImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        Frame frame = grabberConverter.convert(src);
        return paintConverter.getBufferedImage(frame, 1);
    }*/

}
