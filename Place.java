import javax.swing.ImageIcon;

public class Place {
	private String name;
	private ImageIcon pic;
	
	Place(String n, ImageIcon p){
		name = n;
		pic = p;
	}
	
	
	public String getName(){
		return name;
	}
	
	public ImageIcon getPic(){
		return pic;
	}
	
	
}
