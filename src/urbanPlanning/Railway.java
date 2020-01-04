package urbanPlanning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Railway extends Road {	
	public Railway(int id) {
		super(id);
		width = 5;
	}
	
	public Graphics2D render(Graphics2D g2d) {
		assert startPoint != null;
		assert endPoint != null;
		
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(width));
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
		
		// Draw black road
		g2d.setColor(Color.yellow);
		g2d.setStroke(new BasicStroke(width - 2));
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw white dashed lines
	    float[] dash = { 2f, 0f, 2f };
	    Stroke dashedStroke = new BasicStroke(width - 2, 
	            BasicStroke.CAP_BUTT, 
	            BasicStroke.JOIN_ROUND, 
	            1.0f, 
	            dash,
	            2f);
        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.black);
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        
        return g2d;
	}
}
