package gamedev.level;

import gamedev.entity.Tile;
import gamedev.entity.Tile.TileType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Map {
	private static Map instance;
	public static Map getInstance() {
		if (instance == null)
			instance = new Map();
		return instance;
	}
/* -1: return TileType.Used;
case  0: return TileType.steve;
case  1: return TileType.floor_yellow;
case  2: return TileType.floor_white;
case  3: return TileType.floor_black;
case  4: return TileType.water;
case  5: return TileType.grass;
case  6: return TileType.fire;
case  7: return TileType.glass_light;
case  8: return TileType.glass_dark;
case  9: return TileType.glass_special;*/
	private static final String map_1[] = {
			  "11177777111111111",
			  "11174447111111111",
			  "33333333333333111",
			  "11111111111113111",
			  "11777711113333711",
			  "11744777713744711",
			  "11777444713777711",
			  "22222777773333111",
			  "21112117447113111",
			  "21103333333333111",
			  "21112111174444711",
			  "22222111177777711",};
	
	public Point waypoints_1[] = {	// x, y
			new Point(0, 80),
			new Point(520, 80),
			new Point(520, 160),
			new Point(400, 160),
			new Point(400, 280),
			new Point(520, 280),
			new Point(520, 360),
			new Point(160, 360),
			};
	
	private static final String map_2[] = {
			  "00000000002222200",
			  "11100000002030200",
			  "00100000002212200",
			  "00100000000010000",
			  "00100111111110000",
			  "00100100000000000",
			  "00100100000000000",
			  "00100111111111000",
			  "00100000000001000",
			  "00100000000001000",
			  "00111111111111000",
			  "00000000000000000",};
	
	public Point waypoints_2[] = {	// x, y
		new Point(0, 40),
		new Point(80, 40),
		new Point(80, 400),
		new Point(520, 400),
		new Point(520, 280),
		new Point(200, 280),
		new Point(200, 160),
		new Point(480, 160),
		new Point(480, 80),
		};
	
	static String map_3[] = {
			  "99999999999999999",
			  "99999999999999999",
			  "99999999999999999",
			  "99999999999999999",
			  "99999999999999999",
			  "99999988888999999",
			  "99999989998999999",
			  "99998889998888999",
			  "99998999997778777",
			  "99888999997440447",
			  "99899999997444447",
			  "88899999997777777",};

	Point waypoints_3[] = {	// x, y
		new Point(0, 440),
		new Point(80,440),
		
		new Point(80,360),
		new Point(120, 360),
		
		new Point(160, 280),
		new Point(200, 280),
		
		new Point(240, 280),
		new Point(240, 200),
		
		new Point(400, 200),
		new Point(400, 280),
		new Point(520,280),
		new Point(520,320)
	};
	
	public static HashMap<Integer, TileType[][]> maps = new HashMap<Integer, TileType[][]>();
	
	public static TileType[][] generateMap(int i){
		TileType[][] map = maps.get(i);
		if(maps.get(i) == null){
			switch(i){
				case 1:
					map = convertStringToGrid(map_1);
					break;
				case 2:
					map = convertStringToGrid(map_2);
					break;
				case 3:
					map = convertStringToGrid(map_3);
					break;
				default:
					return null;
						
			}
			maps.put(i, map);
		}
		
		return map;
	}
	
	private static TileType[][] convertStringToGrid(String[] grid){
		int x = grid[0].length(), y = grid.length;
		
		TileType[][] map = new TileType[x][y];
		for (y = 0; y < grid.length; y++) {
			for (x = 0; x < grid[y].length(); x++) {
				map[x][y] = Tile.interpretType(Character.getNumericValue(grid[y].charAt(x)));
			}
		}
		
		return map;
	}
	
	public List<Point> getWaypoints(int type){
		switch(type){
			case 1:
				return new ArrayList<Point>(Arrays.asList(waypoints_1));
			case 2:
				return new ArrayList<Point>(Arrays.asList(waypoints_2));
			case 3:
				return new ArrayList<Point>(Arrays.asList(waypoints_3));
		}
		
		return null;
		
	}
}
