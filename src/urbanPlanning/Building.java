package urbanPlanning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Building extends MapObject {
	public Dimension minSize;
	public Dimension maxSize;
	public String description;
	public Color color;
	
	public Building(int id, Dimension minSize, Dimension maxSize, Color color, String description) {
		super(id);
		
		// Set size
		double rand = Math.random();
		this.minSize = minSize;
		this.maxSize = maxSize;
		int width = (int) (rand * (maxSize.width - minSize.width)) + minSize.width;
		int height = (int) (rand * (maxSize.height - minSize.height)) + minSize.height;
		size = new Dimension(width, height);
		this.color = color;
		this.description = description;
	}
	
	public Graphics2D render(Graphics2D g2d) {
		assert startPoint != null;
		assert size != null;
		
		// Fill
        g2d.setColor(color);
        g2d.fillRect(startPoint.x, startPoint.y, size.width , size.height);
        
        // Stroke
		Stroke stroke = new BasicStroke(1);
		g2d.setStroke(stroke);
		g2d.setColor(Color.black);
		g2d.drawRect(startPoint.x, startPoint.y, size.width, size.height);
	
		// Text
		g2d.drawString(description, startPoint.x + 5, startPoint.y + (int)(size.height/ 2));
		
		return g2d;
	}
}
