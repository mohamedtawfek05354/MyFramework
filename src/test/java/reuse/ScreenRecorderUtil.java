package reuse;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
//org.bytedeco.ffmpeg.global.avcodec: Imports constants related to codecs used in video encoding/decoding.
//org.bytedeco.javacv.FrameRecorder: Imports the FrameRecorder class, which is responsible for recording video frames.
//org.bytedeco.javacv.Java2DFrameConverter: Imports a converter to transform BufferedImage objects into Frame objects for recording.
//org.bytedeco.javacv.Frame: Imports the Frame class, which represents a single video frame.
//javax.swing.*: Imports classes from the Swing library for GUI components, like dialog boxes.
//java.awt.*: Imports classes from the AWT library for graphical components, including screen dimensions and images.
//java.awt.image.BufferedImage: Imports the BufferedImage class, which represents an image with an accessible buffer of image data.

public class ScreenRecorderUtil {
//handle the video recording : a library-provided class (likely from a multimedia library like "JavaCV") that manages video recording and output.
    private FrameRecorder recorder;
// Robot : used for screen capturing which is an AWT class in Java used to capture screen images programmatically.
    private Robot robot;
// Dimension : is used to store the screen's "width" and "height".
    private Dimension screenSize;
//Rectangle : Declares a variable to define the area of the screen to capture and
// defines a rectangular region. Here, it specifies the area of the screen to capture.
    private Rectangle captureArea;

    // constructor to run screen recorder when takes the instance form this class.
    public ScreenRecorderUtil(String fileName) throws Exception {
        
        // Initialize screen size and capture area
        //  fetches the resolution of the primary display (e.g., 1920x1080).
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Defines the area of the screen to capture as a Rectangle that covers the entire screen.
        captureArea = new Rectangle(screenSize);

        // Initialize FrameRecorder with the given "file name" and the dimensions of the screen
        recorder = FrameRecorder.createDefault(fileName, screenSize.width, screenSize.height);
        // Sets the video codec to H.264, a commonly used and efficient video compression format.
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // Set the output format to MP4
        recorder.setFormat("mp4");
        // Set the frame rate
        recorder.setFrameRate(30);
        // Set video bit rate for higher quality
        // Sets the video bitrate to 4 Mbps which determines the quality of the output video.
        recorder.setVideoBitrate(4000000);

        // Set the pixel format for better quality (YUV for H.264)
        //Specifies the pixel format as YUV420P
        // a standard format for "H.264" video encoding that provides good compression and quality.
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        // Start record the Test Class
        recorder.start();
        // Create a Robot instance for screen capturing
        robot = new Robot();
    }

    public void startRecording() throws Exception {
        while (true) {
            // Captures the specified area "captureArea" of the screen and stores it as a BufferedImage.
            BufferedImage screen = robot.createScreenCapture(captureArea);
            // Convert BufferedImage to Frame
            Frame frame = new Java2DFrameConverter().convert(screen);
            // Record the frame
            recorder.record(frame);
        }
    }

    public void stopRecording() throws Exception {
        recorder.stop();
        //Releases any resources held by the recorder.
        recorder.release();
    }

}