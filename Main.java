import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main extends JFrame{
	public static void main(String[] args){
		Main frame = new Main();
		Maps_5 window;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		if (h > 800) h = 800;
		if (w > 1200) w = 1200;

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200,800);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setTitle("Map Quiz");
		window = new Maps_5((int)(0.9*w), (int)(0.9*h));
		frame.add(window);
		frame.addKeyListener(window);
		frame.revalidate();
	}
	

}
