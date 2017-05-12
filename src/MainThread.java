import java.awt.event.WindowAdapter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.glass.events.WindowEvent;

/**
 * Created by chris on 5/1/2017.
 */
public class MainThread {
	public static Webcam webcam = Webcam.getDefault();
	public static WebcamPanel panel;
	  private JFrame frame;
	  private static JMenuBar menuBar;
	  private static JMenu fileMenu;
	  private static JMenu editMenu;
	  private static JMenuItem openMenuItem;
	  private static JMenuItem cutMenuItem;
	  private static JMenuItem copyMenuItem;
	  private static JMenuItem pasteMenuItem;   
	  private static TargetDataLine microphone;
      private static SourceDataLine speakers;
	  public static boolean isLoaded = false;
	  public static void main(String[] args) {
    	resetWebcam();
		if(webcam.isOpen() && !isLoaded){
			loadWindow();
			audio();
		}

    }

    public static void resetWebcam(){
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		panel  = new WebcamPanel(webcam);
	   
//		panel.setFPSDisplayed(true);
//		panel.setDisplayDebugInfo(true);
//		panel.setImageSizeDisplayed(true);
    }
    public static void loadWindow(){
		JFrame window = new JFrame("ARCap");
		window.add(panel);
	    fileMenu = new JMenu("File");
	    menuBar = new JMenuBar();
	    openMenuItem = new JMenuItem("Open");
	    fileMenu.add(openMenuItem);

	    menuBar.add(fileMenu);
	    // put the menubar on the frame
	    window.setJMenuBar(menuBar);
	    try {
			window.setIconImage(ImageIO.read(new File("icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    window.addWindowListener(new WindowAdapter()
	    {
	        public void windowClosing(WindowEvent e)
	        {
	        	webcam.close();
	            speakers.drain();
	            speakers.close();
	            microphone.close();
	        }
	    });
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		isLoaded = true;
    }
    public static void audio(){
    	AudioFormat format = new AudioFormat(8000.0f, 16, 2, true, true);
    	boolean collecting = true;
        try {
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            while (collecting) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                out.write(data, 0, numBytesRead); 
                // write mic data to stream for immediate playback
                speakers.write(data, 0, numBytesRead);
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } 
    }
}
