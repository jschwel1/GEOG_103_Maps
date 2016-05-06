import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Namer {
	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		String folder = JOptionPane.showInputDialog("Enter the src/ folder");
		System.out.println("Enter the list of places:\n");
		String line = s.nextLine();
		String place = "";	
		while(line.length() > 0){
			int i = line.length()-1;
			while (line.charAt(i) != ' '){
				i--;
			}
			i++;
			line = line.substring(i); 
			//System.out.println(line.substring(i));
			for (int j = 0; j < line.length() - 4; j++){
				if (line.charAt(j) == '_') place+= ' ';
				else place += line.charAt(j); 
			}
			System.out.println("places.add(new Place(\""+ place +"\", new ImageIcon(\"src/"+folder+"/"+line+"\")));");
			line = s.nextLine();
			place="";
		}
		
	}
}

// places.add(new Place("Place", new ImageIcon("src/US/Wyoming.png")));