package urbanPlanning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Road extends MapObject {
	public int width = 10;
	public Point endPoint = new Point(0, 0);
	
	public Road(int id) {
		super(id);	
		
		// Calculate size
		int dist = (int) Math.hypot(Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));	
		size = new Dimension(width, dist);
	}
	
	public Graphics2D render(Graphics2D g2d) {
		assert startPoint != null;
		assert endPoint != null;
	
		// Draw black road
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(width));
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw white dashed lines
	    float[] dash = { 2f, 0f, 2f };
	    Stroke dashedStroke = new BasicStroke(1, 
	            BasicStroke.CAP_BUTT, 
	            BasicStroke.JOIN_ROUND, 
	            1.0f, 
	            dash,
	            2f);
        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.white);
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        
        return g2d;
	}
}
