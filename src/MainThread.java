import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

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
	  public static boolean isLoaded = false;
	  public static void main(String[] args) {
    	resetWebcam();
		if(webcam.isOpen() && !isLoaded){
			loadWindow();
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

		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		isLoaded = true;
    }
}
