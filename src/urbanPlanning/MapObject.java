package urbanPlanning;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class MapObject {
	public int id;
	public Dimension size;
	public Point startPoint = new Point(0, 0);
	public int padding = 10;
	
	public MapObject(int id) {

	}

	public int getArea() {
		return (int) (size.getWidth() * size.getHeight());
	}
	
	public abstract Graphics2D render(Graphics2D g2d);
}
