package urbanPlanning;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {	
	public static void main(String[] args) {
		int width = 500;
		int height = 500;
		List<Building> buildings = new ArrayList<>();
		List<SubwayStation> stations = new ArrayList<>();
		
		boolean invalid = true;
	    Scanner sc = new Scanner(System.in);
	    
	    pl("---Urban Generator for JohnnyLand---\n");
	    
	    // Get width
		while (invalid) {
			p("Enter map width (e.g: 500): ");
	
		    try {
			    width = sc.nextInt();
			    pl("map width = " + width);
			    break;
		    } catch (Exception ex) {
		    	pl("Invalid width input, integer expected");
		    	sc.next();
		    }    
		}
		
	    // Get height
		while (invalid) {
			p("\nEnter map height (e.g: 500): ");
	
		    try {
			    height = sc.nextInt();
			    pl("map height = " + height);
			    break;
		    } catch (Exception ex) {
		    	pl("Invalid width input, integer expected");
		    	sc.next();
		    }    
		}
		
		// Get buildings
		int i = 0;
		Random rand = new Random();
		do {
			
		    try {
				p("\nEnter name for building " + i + " or enter Z to finish: ");
				
				String desc = sc.next();
				if (desc.equals("Z")) {
					pl("finish");
					break;
				}
				
				pl("enter WIDTH HEIGHT VARIANCE NUMBER for " + desc +" e.g., 50 30 10 3: ");
				
				int bWidth = sc.nextInt();
				int bHeight = sc.nextInt();
				int variance = sc.nextInt();
				int number = sc.nextInt();

				float r = rand.nextFloat();
				float g = rand.nextFloat();
				float b = rand.nextFloat();
				Color randomColor = new Color(r, g, b);
				
				for (int n = 0; n < number; n++) {
					Dimension size = new Dimension(bWidth, bHeight);
					Dimension maxSize = new Dimension(bWidth + variance, bHeight + variance);
					

					Building building = new Building(i, size, maxSize, randomColor, desc);
					buildings.add(building);
				}
				
				pl("Building " + i + " DESC:" + desc + ", width:" + bWidth + ", height:" + bHeight + ", variance:" + variance + ", number:" + number);
				i++;

		    } catch (Exception ex) {
		    	pl("Invalid width input\n");
		    	sc.next();
		    }    
		} while (true);
		
		UrbanPlanner planner = new UrbanPlanner(width, height, buildings);
		planner.run();
	}
	
	public static void p(String t) {
		System.out.print(t);
	}
	
	public static void pl(String t) {
		System.out.println(t);
	}
	
}
