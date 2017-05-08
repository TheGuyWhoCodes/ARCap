import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

/**
 * Created by chris on 5/1/2017.
 */
public class MainThread {
	public static Webcam webcam = Webcam.getDefault();
	public static WebcamPanel panel;
    public static void main(String[] args) {
    	resetWebcam();
		if(webcam.isOpen()){
			loadWindow();
		}

    }

    public static void resetWebcam(){
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		panel  = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
    }
    public static void loadWindow(){
		JFrame window = new JFrame("ARCap");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
    }
}
