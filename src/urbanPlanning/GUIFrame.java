package urbanPlanning;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

public class GUIFrame extends JFrame {
	public GUI gui;
	
	public GUIFrame(String name, GUI gui) {
		super(name);
		this.gui = gui;
		add(gui);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackgroundColor(Color.white);
	}
	
	public void addMapObject(MapObject object) {
		gui.mapObjects.add(object);
	}
	
	public void addMapObjects(List<MapObject> objects) {
		gui.mapObjects.addAll(objects);
	}
	
	public void setBackgroundColor(Color color) {
		gui.setBackground(color);
	}
}
