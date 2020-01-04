package urbanPlanning;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GUI extends JPanel {
	public Dimension size;
	public Graphics2D g2d;
	public List<MapObject> mapObjects = new ArrayList<>();
	
    public GUI(Dimension size) {
    	this.size = size;
    	setPreferredSize(size);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g2d = (Graphics2D) g;
    	        
        for (MapObject mapOject : mapObjects) {
        	g2d = mapOject.render(g2d);
        }
    } 
}
