/*
 * Updates:
 * 10/29/15 -- Change location and display of location name
 * 			-- Align map image to top-right corner
 * 			-- Constant screen width and height are passed through the constructor
 * 
 * 11/03/15 -- read in quizzes separated by a space
 * 
 * 11/12/15 -- prevent pressing and holding to cause the quiz to fast forward
 * 			-- Movable display name/box (now controlled by a Rectangle)
 * 			-- clicking on the screen does not continue with the quiz
 * Future Updates:
 * 			-- add a time delay to the double pressing prevent.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Maps_5 extends JPanel implements ActionListener, KeyListener, MouseListener{
	//======= Instance Fields =============//
	private ArrayList<Place> quizPlaces;
	private int imgHeight;
	private int startY, startX;
	private int pos;
	private int[] seq;
	private Color bg;
	private Rectangle correct, incorrect, reveal, nameDisp;
	private int screenW, screenH;
	private boolean released, showName, movingNameDisp;
	private Timer clock;
	

	
	
	//======= Constructor =======//
	public Maps_5(int w, int h){
		this.addMouseListener(this);
		this.addKeyListener(this);
		// Constants
		imgHeight = 566;
		screenW = w;
		screenH = h;
		// Background Color
		int r = (int)(Math.random()*100)+100; // colors between 100 and 200;
		int g = (int)(Math.random()*100)+100;
		int b = (int)(Math.random()*100)+100;
		bg = new Color(r,g,b);
		clock = new Timer(100, this);
		this.setUp();
	}
	
	// ====== Functions ======//
	public void paintComponent(Graphics g){
	
		g.setColor(bg);
		g.fillRect(0, 0, 1200, 800);
		// Print the map image oriented with the top-right corner in the top-right corner
		quizPlaces.get(seq[pos]).getPic().paintIcon(this, g, screenW-quizPlaces.get(seq[pos]).getPic().getIconWidth(), 0);
		
		if(showName){
			g.setColor(Color.green);
			g.fillRect(correct.x, correct.y, correct.width, correct.height);
			
			g.setColor(Color.red);
			g.fillRect(incorrect.x, incorrect.y, incorrect.width, incorrect.height);
			
			// Display location name
			g.setColor(new Color((float)1.0,(float)1.0,(float)1.0,(float)0.7));
			g.fillRect(0, nameDisp.y, Toolkit.getDefaultToolkit().getScreenSize().width, nameDisp.height);
			g.setColor(Color.black);
			g.drawRect(0, nameDisp.y, Toolkit.getDefaultToolkit().getScreenSize().width, nameDisp.height);
			// Text
			g.setFont(new Font(Font.SERIF, Font.BOLD, 40));
			g.drawString(quizPlaces.get(seq[pos]).getName(), nameDisp.x, nameDisp.y+40);
			
			
			g.drawRect(correct.x, correct.y, correct.width, correct.height);
			g.drawRect(incorrect.x, incorrect.y, incorrect.width, incorrect.height);
		}
		else{
			g.setColor(Color.yellow);
			g.fillRect(reveal.x, reveal.y, reveal.width, reveal.height);
			g.setColor(Color.black);
			g.drawRect(reveal.x, reveal.y, reveal.width, reveal.height);
		}
		g.setColor(Color.black);
		g.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
		g.drawString(seq.length+" places remaining", 0, 600);
		
	}
	public ArrayList<Place> getPlaces(int quiz){
		ArrayList<Place> fullList = getListOfPlaces(quiz);
		return fullList;
	}
	
	
	public void reset(){
		// set up randomized sequence of countries
				seq = new int[quizPlaces.size()];
				pos = 0;
				for (int i = 0; i < quizPlaces.size(); i++){
					seq[i] = i;
				}
				for (int i = 0; i < quizPlaces.size(); i++)
					System.out.print(seq[i] + " ");
				System.out.println();
				for (int i = 0; i < (quizPlaces.size()+1)/2; i++){
					int temp = seq[i];
					int a = (int)(Math.random()*quizPlaces.size());
					seq[i]=seq[a];
					seq[a]=temp;
				}
				for (int i = 0; i < quizPlaces.size(); i++)
					System.out.print(seq[i] + " ");
				System.out.println();
				
				// set up variables for GUI section
				showName = false;
				released = true;
				startY = (int)nameDisp.getY();
				startX = 250;
				movingNameDisp = false;
				// change background color
				int r = (int)(Math.random()*20)-10 + bg.getRed();
				int g = (int)(Math.random()*20)-10 + bg.getGreen();
				int b = (int)(Math.random()*20)-10 + bg.getBlue();
				if (r > 200 || r < 100) 
					r = 150;
				if (g > 200 || g < 100) 
					g = 150;
				if (b > 200 || b < 100) 
					b = 150;
				bg = new Color(r,g,b);
				this.repaint();
	}
	public void setUp(){
		this.repaint();
		String quizNums = JOptionPane.showInputDialog("Enter the numbers of the quizzes separated by spaces (e.g. for quizzes 1,2,3 enter: 1 2 3)");
		quizPlaces = new ArrayList<Place>();
		
		String s = "";
		for (int i = 0; i <= quizNums.length(); i++){
			if (i < quizNums.length() && quizNums.charAt(i)!=' ') s+=quizNums.charAt(i);
			else{
				if (s.length() > 0){
					int b = Integer.parseInt(s);
					System.out.println("b = "+b);
					quizPlaces.addAll(getPlaces(b));
					s="";
				}
			}
			System.out.println("String segment: " + s);
		}

		
		correct = new Rectangle(0,0,100,imgHeight/2);
		incorrect = new Rectangle(0, imgHeight/2, 100, imgHeight/2);
		reveal = new Rectangle(0,0, 100, imgHeight);
		nameDisp = new Rectangle(250, screenH-50-40, screenW, 60);
		this.reset();
	}

	public int[] shuffle(int[] a){
		int temp;
		for (int i = 0; i < (a.length+1)/2; i++){
			int b=(int)(Math.random()*a.length);
			temp = a[i];
			a[i] = a[b];
			a[b] = temp;
		}
		return a;
	}

	public void keyPressed(KeyEvent e) {
		if (!released) return;
		released = false;
		int code = e.getKeyCode();
		if (!showName){
			if (code == KeyEvent.VK_SPACE || code==KeyEvent.VK_RIGHT || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN){
				showName = true;
			}
		}
		else{
			if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP){
				showName = false;
//				if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP){
					//remove the position from the sequence
					int[] temp = new int[seq.length];
					// copy integers over to temporary array
					for (int i = 0; i < temp.length; i++)
						temp[i] = seq[i];
					//re-copy without current position
					seq = new int[temp.length-1];
					for (int i = 0; i < temp.length; i++){
						if (i < pos)
							seq[i] = temp[i];
						if (i > pos)
							seq[i-1] = temp[i];
					}
					if (seq.length <= 0){
						reset();
						pos = 0;
//					}
				}
			}
			if (code == KeyEvent.VK_DOWN){
				int[] temp = new int[seq.length];
				// copy integers over to temporary array
				for (int i = 0; i < temp.length; i++)
					temp[i] = seq[i];
				seq = new int[temp.length+1];
				for (int i = 0; i <temp.length; i++)
					seq[i]=temp[i];
				// make the last one in the sequence the current country
				seq[seq.length-1] = seq[pos];
				// shuffle the places around to minimize liklihood of double countries one after the other.
				seq = shuffle(seq);
				showName = false;
				pos++;
				if (pos >= seq.length){
					if (seq.length > 0)
						pos=0;
					else
						reset();
				}
			}
			if (code == KeyEvent.VK_RIGHT){
				showName=false;
				pos++;
				if (pos >= seq.length){
					pos = 0;
				}
			}			
		}
		if (code == KeyEvent.VK_LEFT){
			showName = false;
			pos--;
			if (pos<0)
				pos=seq.length-1;
		}
		
		// Reset
		if (code == KeyEvent.VK_R){
			reset();
		}
		// Reset with choice of map and everything
		if (code == KeyEvent.VK_Q){
			this.setUp();
		}
		
		// Something to always check:
		if (pos < 0)
			pos = 0;
		if (pos >= seq.length)
			pos = seq.length-1;
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		released = true;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent a) {
//			Point e = MouseInfo.getPointerInfo().getLocation();
//			nameDisp.setLocation((int)nameDisp.getX(), (int)(nameDisp.getY()+(e.getY()-this.getLocation().y-startY)));
//
//			nameDisp.setLocation((int)(nameDisp.getX()) + (int)(e.getX()-this.getLocation().x - startX), (int)(nameDisp.getY()));
			 this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point loc = new Point(e.getX(), e.getY());
		
		if (nameDisp.contains(loc)){
			startY = e.getY();
			startX = e.getX();
			movingNameDisp = true;
//			clock.start();
		}
		System.out.println(movingNameDisp);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point loc = new Point(e.getX(), e.getY());
		System.out.println(loc + "--------" + e.getPoint());
		if (!showName){
			if (reveal.contains(loc)){
				showName=true;
			}
		}
		else{
			// If correct, remove it from sequence
			if (correct.contains(e.getPoint())){
				//remove the position from the sequence
				int[] temp = new int[seq.length];
				// copy integers over to temporary array
				for (int i = 0; i < temp.length; i++)
					temp[i] = seq[i];
				//re-copy without current position
				seq = new int[temp.length-1];
				for (int i = 0; i < temp.length; i++){
					if (i < pos)
						seq[i] = temp[i];
					if (i > pos)
						seq[i-1] = temp[i];
				}
				showName = false;
				pos++;
				if (pos >= seq.length){
					if (seq.length > 0)
						pos=0;
					else
						reset();
				}
			}
			// if incorrect, keep it in the sequence, but add it again in the end
			if (incorrect.contains(e.getPoint())){
				int[] temp = new int[seq.length];
				// copy integers over to temporary array
				for (int i = 0; i < temp.length; i++)
					temp[i] = seq[i];
				seq = new int[temp.length+1];
				for (int i = 0; i <temp.length; i++)
					seq[i]=temp[i];
				// make the last one in the sequence the current country
				seq[seq.length-1] = seq[pos];
				// shuffle the places around to minimize likelihood of double countries one after the other.
				seq = shuffle(seq);
				showName = false;
				pos++;
				if (pos >= seq.length){
					if (seq.length > 0)
						pos=0;
					else
						reset();
				}
			}
			
		}
		
		if (movingNameDisp){
			int threshold = 5;
			movingNameDisp=false;
//			clock.stop();
			if (Math.abs(e.getY()-startY) > threshold)
				nameDisp.setLocation((int)nameDisp.getX(), (int)(nameDisp.getY()+(e.getY()-startY)));
			if (Math.abs(e.getX()-startX) > threshold)
				nameDisp.setLocation((int)nameDisp.getX() + (e.getX() - startX), (int)(nameDisp.getY()));
		}

		System.out.println(movingNameDisp);
		//System.out.println("CLICK");
		this.repaint();
	}
	public ArrayList<Place> getListOfPlaces(int quiz){
		ArrayList<Place> places = new ArrayList<Place>();
		if (quiz == 2){
		//		places.add(new Place("Blank Europe Map", new ImageIcon("Europe/Blank_Europe_Map.png")));
				places.add(new Place("England", new ImageIcon("src/Europe/England.png")));
				places.add(new Place("Ireland", new ImageIcon("src/Europe/Ireland.png")));
				places.add(new Place("Portugal", new ImageIcon("src/Europe/Portugal.png")));
				places.add(new Place("Spain", new ImageIcon("src/Europe/Spain.png")));
				places.add(new Place("France", new ImageIcon("src/Europe/France.png")));
				places.add(new Place("Germany", new ImageIcon("src/Europe/Germany.png")));
				places.add(new Place("Italy", new ImageIcon("src/Europe/Italy.png")));
				places.add(new Place("Netherlands", new ImageIcon("src/Europe/Netherlands.png")));
				places.add(new Place("Belgium", new ImageIcon("src/Europe/Belgium.png")));
				places.add(new Place("Denmark", new ImageIcon("src/Europe/Denmark.png")));
				places.add(new Place("Sweden", new ImageIcon("src/Europe/Sweden.png")));
				places.add(new Place("Norway", new ImageIcon("src/Europe/Norway.png")));
				places.add(new Place("Finland", new ImageIcon("src/Europe/Finland.png")));
				places.add(new Place("Czech Republic", new ImageIcon("src/Europe/Czech_Republic.png")));
				places.add(new Place("Lithuania", new ImageIcon("src/Europe/Lithuania.png")));
				places.add(new Place("Bosnia and Herzegovina", new ImageIcon("src/Europe/Bosnia_and_Herzegovina.png")));
				places.add(new Place("Greece", new ImageIcon("src/Europe/Greece.png")));
				places.add(new Place("Turkey", new ImageIcon("src/Europe/Turkey.png")));
		}
		if (quiz == 3){
			places.add(new Place("Albuquerque", new ImageIcon("src/US/Albuquerque.png")));
			places.add(new Place("Atlanta", new ImageIcon("src/US/Atlanta.png")));
			places.add(new Place("Baltimore", new ImageIcon("src/US/Baltimore.png")));
			places.add(new Place("Boston", new ImageIcon("src/US/Boston.png")));
			places.add(new Place("Buffalo", new ImageIcon("src/US/Buffalo.png")));
			places.add(new Place("Charlotte", new ImageIcon("src/US/Charlotte.png")));
			places.add(new Place("Chicago", new ImageIcon("src/US/Chicago.png")));
			places.add(new Place("Dallas - Ft. Worth", new ImageIcon("src/US/Dallas.png")));
			places.add(new Place("Washington, DC", new ImageIcon("src/US/DC.png")));
			places.add(new Place("Denver", new ImageIcon("src/US/Denver.png")));
			places.add(new Place("Detroit", new ImageIcon("src/US/Detroit.png")));
			places.add(new Place("Hartford", new ImageIcon("src/US/Hartford.png")));
			places.add(new Place("Houston", new ImageIcon("src/US/Houston.png")));
			places.add(new Place("Jacksonville", new ImageIcon("src/US/Jacksonville.png")));
			places.add(new Place("Las Vegas", new ImageIcon("src/US/Las_Vegas.png")));
			places.add(new Place("Los Angeles", new ImageIcon("src/US/Los_Angeles.png")));
			places.add(new Place("Miami - Ft. Lauderdale", new ImageIcon("src/US/Miami.png")));
			places.add(new Place("Newark", new ImageIcon("src/US/Newark.png")));
			places.add(new Place("Philadelphia", new ImageIcon("src/US/Philadelphia.png")));
			places.add(new Place("Phoenix", new ImageIcon("src/US/Phoenix.png")));
			places.add(new Place("Pittsburgh", new ImageIcon("src/US/Pittsburgh.png")));
			places.add(new Place("Portland", new ImageIcon("src/US/Portland.png")));
			places.add(new Place("San Antonio", new ImageIcon("src/US/San_Antonio.png")));
			places.add(new Place("San Diego", new ImageIcon("src/US/San_Diego.png")));
			places.add(new Place("San Francisco", new ImageIcon("src/US/San_Francisco.png")));
			places.add(new Place("Seattle", new ImageIcon("src/US/Seattle.png")));
			places.add(new Place("St. Louis", new ImageIcon("src/US/St_Louis.png")));
		}
		if (quiz == 4){
			places.add(new Place("Alabama", new ImageIcon("src/US/Alabama.png")));
			places.add(new Place("Alaska", new ImageIcon("src/US/Alaska.png")));
			places.add(new Place("Arizona", new ImageIcon("src/US/Arizona.png")));
			places.add(new Place("Arkansas", new ImageIcon("src/US/Arkansas.png")));
			places.add(new Place("California", new ImageIcon("src/US/California.png")));
			places.add(new Place("Colorado", new ImageIcon("src/US/Colorado.png")));
			places.add(new Place("Connecticut", new ImageIcon("src/US/Connecticut.png")));
			places.add(new Place("Delaware", new ImageIcon("src/US/Delaware.png")));
			places.add(new Place("Florida", new ImageIcon("src/US/Florida.png")));
			places.add(new Place("Georgia", new ImageIcon("src/US/Georgia.png")));
			places.add(new Place("Hawaii", new ImageIcon("src/US/Hawaii.png")));
			places.add(new Place("Idaho", new ImageIcon("src/US/Idaho.png")));
			places.add(new Place("Illinois", new ImageIcon("src/US/Illinois.png")));
			places.add(new Place("Indiana", new ImageIcon("src/US/Indiana.png")));
			places.add(new Place("Iowa", new ImageIcon("src/US/Iowa.png")));
			places.add(new Place("Kansas", new ImageIcon("src/US/Kansas.png")));
			places.add(new Place("Kentucky", new ImageIcon("src/US/Kentucky.png")));
			places.add(new Place("Louisiana", new ImageIcon("src/US/Louisiana.png")));
			places.add(new Place("Maine", new ImageIcon("src/US/Maine.png")));
			places.add(new Place("Maryland", new ImageIcon("src/US/Maryland.png")));
			places.add(new Place("Massachusetts", new ImageIcon("src/US/Massachusetts.png")));
			places.add(new Place("Michigan", new ImageIcon("src/US/Michigan.png")));
			places.add(new Place("Minnesota", new ImageIcon("src/US/Minnesota.png")));
			places.add(new Place("Mississippi", new ImageIcon("src/US/Mississippi.png")));
			places.add(new Place("Misouri", new ImageIcon("src/US/Misouri.png")));
			places.add(new Place("Montana", new ImageIcon("src/US/Montana.png")));
			places.add(new Place("Nebraska", new ImageIcon("src/US/Nebraska.png")));
			places.add(new Place("Nevada", new ImageIcon("src/US/Nevada.png")));
			places.add(new Place("New Hampshire", new ImageIcon("src/US/New_Hampshire.png")));
			places.add(new Place("New Jersey", new ImageIcon("src/US/New_Jersey.png")));
			places.add(new Place("New Mexico", new ImageIcon("src/US/New_Mexico.png")));
			places.add(new Place("New York", new ImageIcon("src/US/New_York.png")));
			places.add(new Place("North Carolina", new ImageIcon("src/US/North_Carolina.png")));
			places.add(new Place("North Dakota", new ImageIcon("src/US/North_Dakota.png")));
			places.add(new Place("Ohio", new ImageIcon("src/US/Ohio.png")));
			places.add(new Place("Oklahoma", new ImageIcon("src/US/Oklahoma.png")));
			places.add(new Place("Oregon", new ImageIcon("src/US/Oregon.png")));
			places.add(new Place("Pennsylvania", new ImageIcon("src/US/Pennsylvania.png")));
			places.add(new Place("Rhode Island", new ImageIcon("src/US/Rhode_Island.png")));
			places.add(new Place("South Carolina", new ImageIcon("src/US/South_Carolina.png")));
			places.add(new Place("South Dakota", new ImageIcon("src/US/South_Dakota.png")));
			places.add(new Place("Tennessee", new ImageIcon("src/US/Tennessee.png")));
			places.add(new Place("Texas", new ImageIcon("src/US/Texas.png")));
			places.add(new Place("Utah", new ImageIcon("src/US/Utah.png")));
			places.add(new Place("Vermont", new ImageIcon("src/US/Vermont.png")));
			places.add(new Place("Virginia", new ImageIcon("src/US/Virginia.png")));
			places.add(new Place("Washington", new ImageIcon("src/US/Washington.png")));
			places.add(new Place("West Virginia", new ImageIcon("src/US/West_Virginia.png")));
			places.add(new Place("Wisconsin", new ImageIcon("src/US/Wisconsin.png")));
			places.add(new Place("Wyoming", new ImageIcon("src/US/Wyoming.png")));
		}
		

		if (quiz == 5){
			places.add(new Place("Argentina", new ImageIcon("src/South_America/Argentina.jpg")));
			places.add(new Place("Bahamas", new ImageIcon("src/South_America/Bahamas.jpg")));
			places.add(new Place("Belize", new ImageIcon("src/South_America/Belize.jpg")));
			places.add(new Place("Bolivia", new ImageIcon("src/South_America/Bolivia.jpg")));
			places.add(new Place("Brazil", new ImageIcon("src/South_America/Brazil.jpg")));
			places.add(new Place("Chile", new ImageIcon("src/South_America/Chile.jpg")));
			places.add(new Place("Columbia", new ImageIcon("src/South_America/Columbia.jpg")));
			places.add(new Place("Costa Rica", new ImageIcon("src/South_America/Costa_Rica.jpg")));
			places.add(new Place("Cuba", new ImageIcon("src/South_America/Cuba.jpg")));
			places.add(new Place("Dominican Republic", new ImageIcon("src/South_America/DR.jpg")));
			places.add(new Place("Ecuador", new ImageIcon("src/South_America/Ecuador.jpg")));
			places.add(new Place("El Salvador", new ImageIcon("src/South_America/El_Salvador.jpg")));
			places.add(new Place("Guatemala", new ImageIcon("src/South_America/Guatemala.jpg")));
			places.add(new Place("Guyana", new ImageIcon("src/South_America/Guyana.jpg")));
			places.add(new Place("Haiti", new ImageIcon("src/South_America/Haiti.jpg")));
			places.add(new Place("Honduras", new ImageIcon("src/South_America/Honduras.jpg")));
			places.add(new Place("Jamaica", new ImageIcon("src/South_America/Jamaica.jpg")));
			places.add(new Place("Mexico", new ImageIcon("src/South_America/Mexico.jpg")));
			places.add(new Place("Nicaragua", new ImageIcon("src/South_America/Nicaragua.jpg")));
			places.add(new Place("Panama", new ImageIcon("src/South_America/Panama.jpg")));
			places.add(new Place("Paraguay", new ImageIcon("src/South_America/Paraguay.jpg")));
			places.add(new Place("Puerto Rico", new ImageIcon("src/South_America/PR.jpg")));
			places.add(new Place("Surinam", new ImageIcon("src/South_America/Surinam.jpg")));
			places.add(new Place("Trinidad and Tobago", new ImageIcon("src/South_America/T&T.jpg")));
			places.add(new Place("Uruguay", new ImageIcon("src/South_America/Uruguay.jpg")));
			places.add(new Place("Venezuela", new ImageIcon("src/South_America/Venezuela.jpg")));
			
		}
		if (quiz == 6){
			// Middle East
			places.add(new Place("Afghanistan", new ImageIcon("src/ME/Afghanistan.jpg")));
			places.add(new Place("Iran", new ImageIcon("src/ME/Iran.jpg")));
			places.add(new Place("Iraq", new ImageIcon("src/ME/Iraq.jpg")));
			places.add(new Place("Israel", new ImageIcon("src/ME/Israel.jpg")));
			places.add(new Place("Jordan", new ImageIcon("src/ME/Jordan.jpg")));
			places.add(new Place("Kuwait", new ImageIcon("src/ME/Kuwait.jpg")));
			places.add(new Place("Lebanon", new ImageIcon("src/ME/Lebanon.jpg")));
			places.add(new Place("Saudi Arabia", new ImageIcon("src/ME/Saudi_Arabia.jpg")));
			places.add(new Place("Syria", new ImageIcon("src/ME/Syria.jpg")));
			places.add(new Place("United Arab Emirates", new ImageIcon("src/ME/United_Arab_Emirates.jpg")));
			places.add(new Place("Yemen", new ImageIcon("src/ME/Yemen.jpg")));
			// Africa
			places.add(new Place("Egypt", new ImageIcon("src/Africa/Egypt.gif")));
			places.add(new Place("Ethiopia", new ImageIcon("src/Africa/Ethiopia.gif")));
			places.add(new Place("Ghana", new ImageIcon("src/Africa/Ghana.gif")));
			places.add(new Place("Kenya", new ImageIcon("src/Africa/Kenya.gif")));
			places.add(new Place("Liberia", new ImageIcon("src/Africa/Liberia.gif")));
			places.add(new Place("Libya", new ImageIcon("src/Africa/Libya.gif")));
			places.add(new Place("Nigeria", new ImageIcon("src/Africa/Nigeria.gif")));
			places.add(new Place("Senegal", new ImageIcon("src/Africa/Senegal.gif")));
			places.add(new Place("Sierra Leone", new ImageIcon("src/Africa/Sierra_Leone.gif")));
			places.add(new Place("Somalia", new ImageIcon("src/Africa/Somalia.gif")));
			places.add(new Place("South Africa", new ImageIcon("src/Africa/South_Africa.gif")));
			places.add(new Place("Sudan", new ImageIcon("src/Africa/Sudan.gif")));
			places.add(new Place("Tanzania", new ImageIcon("src/Africa/Tanzania.gif")));
			places.add(new Place("Uganda", new ImageIcon("src/Africa/Uganda.gif")));
			
		}
		if (quiz == 7){
			places.add(new Place("Bangladesh", new ImageIcon("src/Asia/Bangladesh.jpg")));
			places.add(new Place("Cambodia", new ImageIcon("src/Asia/Cambodia.jpg")));
			places.add(new Place("China", new ImageIcon("src/Asia/China.jpg")));
			places.add(new Place("India", new ImageIcon("src/Asia/India.jpg")));
			places.add(new Place("Indonesia", new ImageIcon("src/Asia/Indonesia.jpg")));
			places.add(new Place("Japan", new ImageIcon("src/Asia/Japan.jpg")));
			places.add(new Place("Mongolia", new ImageIcon("src/Asia/Mongolia.jpg")));
			places.add(new Place("North Korea", new ImageIcon("src/Asia/North_Korea.jpg")));
			places.add(new Place("Pakistan", new ImageIcon("src/Asia/Pakistan.jpg")));
			places.add(new Place("Philippines", new ImageIcon("src/Asia/Philippines.jpg")));
			places.add(new Place("Russian Federation", new ImageIcon("src/Asia/Russian_Federation.jpg")));
			places.add(new Place("South Korea", new ImageIcon("src/Asia/South_Korea.jpg")));
			places.add(new Place("Taiwan", new ImageIcon("src/Asia/Taiwan.jpg")));
			places.add(new Place("Vietnam", new ImageIcon("src/Asia/Vietnam.jpg")));
		}
		return places;
	}
}
