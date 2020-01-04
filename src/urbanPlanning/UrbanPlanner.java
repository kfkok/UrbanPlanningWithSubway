package urbanPlanning;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class UrbanPlanner {
	String townName = "JohnnyLand";
	
	// A map to maintain the location of map objects
	private int [][] map; 
	private int lastId = 0;
	
	// Width and height of the map
	public static final int minMapWidth = 500;
	public static final int minMapHeight = 500;
	private int width = 500;
	private int height = 500;
	
	// Graphics used to draw the UI
	private GUIFrame overview;
	private GUIFrame subway;
	
	// padding around map objects
	private int padding = 10;

	// Main road properties
	private Road mainRoad;
	private int mainRoadWidth = 50;
	private int mainRoadStartX = 30;
	private int buildingStartPointX = mainRoadStartX + (int)(mainRoadWidth / 2) + 20;

	private Road secondaryMainRoad;
	private int secondaryMainRoadWidth = 10;
	private int secondaryMainRoadStartY = (int) height / 2;	
	
	// List of small roads 
	private List<Road> roads = new ArrayList<>();
	
	// List of railways
	private List<SubwayStation> stations = new ArrayList<>();
	private List<Railway> railways = new ArrayList<>();
	
	// List of buildings and its properties
	public List<Building> buildings = new ArrayList<>();
	int maxHeight;
	int minHeight;
	int maxWidth;
	int minWidth;
	Dimension maxSize;
	
	public UrbanPlanner(int width, int height, List<Building> buildings) {
		this.width = width;
		this.height = height;
		this.buildings = buildings;
		
		System.out.println("Urban planner v1.0");
	}
	
	public void run() {
		initGUIFrames();
		generateBluePrint();
		showGUIFrames();
	}
		
	private void initGUIFrames() {
		overview = new GUIFrame(townName, new GUI(new Dimension(width, height)));	
		subway = new GUIFrame(townName + " subway", new GUI(new Dimension(width, height)));
		subway.setLocation(overview.getX() + overview.getWidth(), overview.getY());
	}
	
	private void generateBluePrint() {
		generateSubwayStations();
		buildings.addAll(new ArrayList<SubwayStation>(stations));
		
		analyzeBuildingsWidthAndHeight();
		System.out.println("Generating blueprint for " + townName);	
		generateTownBluePrint();
		generateSubwayBluePrint();
		System.out.println("Blueprint for " + townName + " generated successfully..");
	}
	
	private void analyzeBuildingsWidthAndHeight() {
		// Determine the min and max height of buildings
		minWidth = 9999;
		maxWidth = 0;
		minHeight = 9999;
		maxHeight = 0;
		
		for (Building b : buildings) {
			if (b.size.width > maxWidth) {
				maxWidth = b.size.width;
			}
			
			if (b.size.width < minWidth) {
				minWidth = b.size.width;
			}
			
			if (b.size.height > maxHeight) {
				maxHeight = b.size.height;
			}
			
			if (b.size.height < minHeight) {
				minHeight = b.size.height;
			}			
			//System.out.print("width:" + b.size.width + "_heigth:" + b.size.height+"....");
		}
		
		System.out.println("\nminWidth:" + minWidth + ", maxWidth:" + maxWidth + ", minHeight:" + minHeight + ", maxHeight:" + maxHeight);
	}
	
	private void generateTownBluePrint() {
		generateMainRoads();
		Collections.sort(buildings, new HeightComparator());

		// This list maintain the remaining buildings left to place in the map
		List<Building> remainingBuildings = new ArrayList<Building>(buildings);
		
		// lastCreatedRoad stores the last created road as reference to generate the buildings on its left side
		Road lastCreatedRoad = null;
		
		// upRoad determines which direction the road is going from the secondary main road, UP or DOWN
		boolean upRoad = true;
		
		// x and y are the coordinates that a building will be placed, it will be updated after every building generated
		int x = buildingStartPointX;
		int y = padding;
		
		// roadXOffset sets the baseline that the new road should offset from along x 
		int roadXOffset = buildingStartPointX;
		
		// shouldCreateRoad indicates whether a new road should be created or not
		boolean shouldCreateRoad = true;
		
		// RoadX and roadY are the coordinates of the new road
		int roadX = 0;
		int roadY = 0;
		int i = 0;
		int firstBuildingWidth = 0;
		
		while (remainingBuildings.size() > 0) {
			// Sort the buildings according to width after the first building is placed
			if (i == 1) {
				//Collections.sort(remainingBuildings, new WidthComparator());
				Collections.shuffle(remainingBuildings);
				//System.out.println("Shuffled the building list");
			}
			
			Building b = remainingBuildings.get(remainingBuildings.size() - 1);
			
			// Create the road if building overflow over the secondary high way
			if (shouldCreateRoad) {
				Road road = new Road(getId());
				
				roadX = roadXOffset + b.size.width + padding * 2 + (int) (road.width / 2) * 2;				
				
				// Record the first building width, this width is the maximum building width for this corridor
				firstBuildingWidth = b.size.width;
				//System.out.println("firstBuildingWidth:" + firstBuildingWidth);
				
				// If the x coordinate flows out the map, switch over to down road and skip for now
				if (roadX > width) {
					if (upRoad) {			
						// System.out.println("overflow ! roadX: " + roadX + " > width:" + width + ", switching to down road..");
						upRoad = false;
						
						// Reset the offset to original value, e.g, starting from most left
						roadXOffset = buildingStartPointX;
						y = secondaryMainRoadStartY;
						continue;

					} else {	
						System.err.println("unable to allocate all buildings, try reduce the number or size of buildings");
						break;
					}
				} 
				
				// x coordinate still within map, no need create road
				else {
					if (upRoad) {
						y = padding;
						roadY = 0;
					} else {
						y = secondaryMainRoadStartY + padding + road.width / 2;
						roadY = height;
					}
				}
										
				// Update the roadXOffset baseline
				roadXOffset = roadX;
				
				road.startPoint = new Point(roadX, secondaryMainRoadStartY);
				road.endPoint = new Point(roadX, roadY);
				
				lastCreatedRoad = road;
				roads.add(road);
			} 
			
			// No need to create new road
			else {
				if (b.size.width > firstBuildingWidth) {
					//System.out.println(b.description + " width " + b.size.width + " > " + firstBuildingWidth);

					// Loop through the remaining buildings and find the one less than firstBuildingWidth
					for (int k = 1; k < remainingBuildings.size(); k++) {
						Building b2 = remainingBuildings.get(k);
						if (b2.size.width <= firstBuildingWidth) {
							//System.out.println("Setting b from " + b.description + "(width " + b.size.width +") to " + b2.description + " (width " + b2.size.width + ")");
							b = b2;
							break;
						}	
					continue;
					}
				}
			}
			
			x = lastCreatedRoad.startPoint.x - (int) lastCreatedRoad.width / 2 - padding - b.size.width;
			
			// nextY calculates the next y position after placing the building
			int nextY = y + b.size.height + padding;
			
			// If the next y overflows the 2nd main road if upRoad or overflow down the map, then should create new road
			if (b.size.width > firstBuildingWidth || upRoad && nextY > (secondaryMainRoadStartY) || !upRoad && nextY > height) {
				shouldCreateRoad = true;				
			} else {
				shouldCreateRoad = false;
				// Place the building
				// System.out.println("Placing building:" + b.description);
				b.startPoint.setLocation(x, y);
				
				// Remove the building in the list
				remainingBuildings.remove(b);				
				y = nextY;
			}
			
			i++;
		}
	}		
	
	public class SizeComparator implements Comparator<MapObject> {		
		@Override
	    public int compare(MapObject o1, MapObject o2) {
	        return o1.getArea() - o2.getArea();
	    }
	}
	
	public class WidthComparator implements Comparator<MapObject> {		
		@Override
	    public int compare(MapObject o1, MapObject o2) {
	        return o1.size.width - o2.size.width;
	    }
	}
	
	public class HeightComparator implements Comparator<MapObject> {		
		@Override
	    public int compare(MapObject o1, MapObject o2) {
	        return o1.size.height - o2.size.height;
	    }
	}
	
	public class YComparator implements Comparator<MapObject> {		
		@Override
	    public int compare(MapObject o1, MapObject o2) {
	        return o2.startPoint.y - o1.startPoint.y;
	    }
	}
	
	
	private void generateSubwayBluePrint() {
		// Sort the station from highest to lowest y position
		Collections.sort(stations, new YComparator());

		SubwayStation s0 = stations.get(0);
		SubwayStation s1;
				
		for (int i = 1; i < stations.size(); i++) {
			s1 =stations.get(i);
			
			Point start = new Point(s0.startPoint.x + s0.size.width / 2, s0.startPoint.y + s0.size.height / 2);
			Point end = new Point(s1.startPoint.x + s1.size.width / 2, s1.startPoint.y + s1.size.height / 2);
			
			Railway r = new Railway(getId());
			r.startPoint = start;
			r.endPoint = end;
			railways.add(r);
			
			s0 = s1;
		}		
	}
	
	private void showGUIFrames() {
		// Overview
		overview.addMapObject(secondaryMainRoad);
		overview.addMapObject(mainRoad);
		overview.addMapObjects(new ArrayList<>(roads));
		overview.addMapObjects(new ArrayList<>(buildings));
		overview.setVisible(true);		

		// Subway
		subway.addMapObjects(new ArrayList<> (railways));
		subway.addMapObjects(new ArrayList<> (stations));
		subway.setVisible(true);
	}
			
	private void generateMainRoads() {
		// Primary thick main road that leads out of town
		mainRoad = new Road(-100);
		mainRoad.startPoint = new Point(mainRoadStartX, 0);
		mainRoad.endPoint = new Point(mainRoadStartX, height);
		mainRoad.width = mainRoadWidth;
		
		// Secondary main road (horizontal) that connects all to roads in the town
		secondaryMainRoad = new Road(-99);
		secondaryMainRoadStartY = maxHeight + padding * 2 + (int)(secondaryMainRoad.width / 2);

		secondaryMainRoad.startPoint = new Point(30, secondaryMainRoadStartY);
		secondaryMainRoad.endPoint = new Point(width, secondaryMainRoadStartY);;
		secondaryMainRoad.width = secondaryMainRoadWidth;
	}
	
	public void generateBuildings() {
		buildings.add(new Building(getId(), new Dimension(20, 60), new Dimension(70, 280), Color.lightGray, "Building A"));	
		buildings.add(new Building(getId(), new Dimension(10, 10), new Dimension(120, 180), Color.orange, "Building B"));
		buildings.add(new Building(getId(), new Dimension(50, 60), new Dimension(90, 100), Color.pink, "Building C"));	
		buildings.add(new Building(getId(), new Dimension(20, 60), new Dimension(190, 170), Color.YELLOW, "Building D"));	
		buildings.add(new Building(getId(), new Dimension(70, 30), new Dimension(190, 170), Color.BLUE, "Building E"));
		buildings.add(new Building(getId(), new Dimension(40, 130), new Dimension(80, 100), Color.magenta, "Building F"));
		buildings.add(new Building(getId(), new Dimension(10, 20), new Dimension(30, 40), Color.pink, "Building G"));
	}
	
	public void generateSubwayStations() {				
		Dimension subwayStationSize = new Dimension((int)(width * 0.05), (int)(height * 0.05));
		Dimension subwayStationMaxSize = new Dimension((int)(width * 0.1), (int)(height * 0.1));

		for (int i = 0; i < buildings.size() / 3; i++) {
			stations.add(new SubwayStation(getId(), subwayStationSize, subwayStationMaxSize));
		}
	}
	
	
	private int getId() {
		lastId++;
		return lastId;
	}	
}


