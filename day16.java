import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import java.awt.*;
import java.nio.file.*;

class Day16 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 7036, actual: " + walkAndReturnPoints(parse(sample), false));
		System.out.println("Actual phase one result, expected: 160624, actual: " + walkAndReturnPoints(parse(input), false));
		System.out.println("Sample phase two result, expected: 7036, actual: " + walkAndReturnPoints(parse(sample), true));
		System.out.println("Sample phase two result, expected: 692, actual: " + walkAndReturnPoints(parse(input), true));
		
	}
	
	public static long walkAndReturnPoints(Map<Point, Tile> map, boolean phaseTwo) {
		Tile start = findTile(map, 'S');
		
		Map<Long, Set<Point>> pathByScore = new TreeMap<Long,Set<Point>>();
		Map<Long, Long> visited = new TreeMap<Long, Long>();
		walkAndAccumulateScore(start, 'E', pathByScore, 0l, visited, new Stack<Point>());
	
		Set<Point> bestSpots = new HashSet<>();
		if (phaseTwo) {
			bestSpots.addAll(pathByScore.values().iterator().next());
			return bestSpots.size();
		}
		
		
		
		return pathByScore.keySet().iterator().next();
	}
	
	public static long iter = 0;
	
	public static void walkAndAccumulateScore(Tile t, char facing, Map<Long, Set<Point>> pathByScore, long currentScore, Map<Long, Long> visited, Stack<Point> stack) {
		
		
		stack.push(t.p);
		
		try {
			switch (facing) {
				case 'E':
					if (t.distanceEast <= currentScore) return;
					t.distanceEast = currentScore;
					break;
				case 'W':
					if (t.distanceWest <= currentScore) return;
					t.distanceWest = currentScore;
					break;
				case 'N':
					if (t.distanceNorth <= currentScore) return;
					t.distanceNorth = currentScore;
					break;
				case 'S':
					if (t.distanceSouth <= currentScore) return;
					t.distanceSouth = currentScore;
					break;
				default:
					break;
			}
			
			if (t.type == '#') { //wall
				return;
			}
			
			if (t.type == 'E') {
				Set<Point> s = pathByScore.getOrDefault(currentScore, new HashSet<Point>());
				s.addAll(stack);
				pathByScore.put(currentScore, s);
				return;
			}
			
			if (facing == 'E') {
				walkAndAccumulateScore(t.east, 'E', pathByScore, currentScore + 1, visited, stack);
				walkAndAccumulateScore(t.north, 'N', pathByScore, currentScore + 1001, visited, stack);
				walkAndAccumulateScore(t.south, 'S', pathByScore, currentScore + 1001, visited, stack);
			} else if (facing == 'N') {
				walkAndAccumulateScore(t.north, 'N', pathByScore, currentScore + 1, visited, stack);
				walkAndAccumulateScore(t.east, 'E', pathByScore, currentScore + 1001, visited, stack);
				walkAndAccumulateScore(t.west, 'W', pathByScore, currentScore + 1001, visited, stack);
			} else if (facing == 'W') {
				walkAndAccumulateScore(t.west, 'W', pathByScore, currentScore + 1, visited, stack);
				walkAndAccumulateScore(t.north, 'N', pathByScore, currentScore + 1001, visited, stack);
				walkAndAccumulateScore(t.south, 'S', pathByScore, currentScore + 1001, visited, stack);
			} else if (facing == 'S') {
				walkAndAccumulateScore(t.south, 'S', pathByScore, currentScore + 1, visited, stack);
				walkAndAccumulateScore(t.east, 'E', pathByScore, currentScore + 1001, visited, stack);
				walkAndAccumulateScore(t.west, 'W', pathByScore, currentScore + 1001, visited, stack);
			}
		} finally {
			stack.pop();
		}
	}
	
	public static Tile findTile(Map<Point, Tile> map, char type) {
		for(Tile t : map.values()) {
			if (t.type == type) return t;
		}
		
		throw new IllegalStateException("Tile " + type + " not found");
	}
	
	public static Map<Point, Tile> parse(String sample) {
		String[] rows = sample.split("\n");
		int height = rows.length;
		int width = rows[0].length();
		
		char[][] map = new char[height][width];
		
		
		Map<Point, Tile> tileMap = new HashMap<>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				var t = new Tile();
				t.p = new Point(j, i);
				t.type = rows[i].charAt(j);
				tileMap.put(new Point(j, i), t);
				tileMap.getOrDefault(new Point(j, i - 1), new Tile()).south = t;
				tileMap.getOrDefault(new Point(j, i + 1), new Tile()).north = t;
				tileMap.getOrDefault(new Point(j - 1, i), new Tile()).east = t;
				tileMap.getOrDefault(new Point(j + 1, i), new Tile()).west = t;
				t.north = tileMap.getOrDefault(new Point(j, i - 1), new Tile());
				t.south = tileMap.getOrDefault(new Point(j, i + 1), new Tile());
				t.east = tileMap.getOrDefault(new Point(j + 1, i), new Tile());
				t.west = tileMap.getOrDefault(new Point(j - 1, i), new Tile());
				t.map = map;
				t.id = (i * height) + j;
				map[i][j] = rows[i].charAt(j);
			}
		}
		
		return tileMap;
	}
	
	public static class Tile {
		public Tile north;
		public Tile south;
		public Tile east;
		public Tile west;
		public char type;
		public Point p;
		public char[][] map;
		public int id;
		
		public long distanceNorth = Long.MAX_VALUE;
		public long distanceSouth = Long.MAX_VALUE;
		public long distanceEast = Long.MAX_VALUE;
		public long distanceWest = Long.MAX_VALUE;
	}
	
	static String sample =	"""
							###############
							#.......#....E#
							#.#.###.#.###.#
							#.....#.#...#.#
							#.###.#####.#.#
							#.#.#.......#.#
							#.#.#####.###.#
							#...........#.#
							###.#.#####.#.#
							#...#.....#.#.#
							#.#.#.###.#.#.#
							#.....#...#.#.#
							#.###.#.#.#.#.#
							#S..#.....#...#
							###############
							""";

	static String input =	"""
							#############################################################################################################################################
							#.#...................#.........#...............#...#...........#...........#.....#.............#...............................#.#........E#
							#.#.#.###############.###.#####.###.###.#######.#.#.#####.#####.#######.#.#.#.###.#############.#.#####.#.#####.#####.#.#######.#.#.#.#######
							#...#.........#...#...#...#...#...#.#...#...#...#.#.....#.#...#...#.......#.#...#...#.........#.....#.#...#.....#...#.#...#.#...#...#...#...#
							#.###########.###.#.###.###.#####.###.###.#.#.###.###.#.#.#.#####.#.###.###.###.###.#.#######.#####.#.#####.#######.#.###.#.#.#########.#.#.#
							#...#.#.....#...#...#...#.#...........#...#.#.#...#.......#.....#.#.#.......#...#.....#.#...#...#.#...#...#.......#.#...#.#...#...#...#.#.#.#
							###.#.#.#.#####.#.#####.#.#.#########.#.###.#.###.#.#.#####.#.###.#.#######.#.#########.#.#.###.#.###.#.#.#######.#.###.#.#.###.#.#.#.#.#.#.#
							#.#.#.#.#.......................#.....#...#.#.#...#.#...#.#.#.....#.#.....#...#...#.......#.....#.....#.#...............#.#...#.#.#.#...#.#.#
							#.#.#.#.#.#.#.#.###.#.###.###.#.###.#.###.#.#.#.###.#.#.#.#.#######.#.###.#####.#.#####.###########.#####.#.#####.###.#.#.###.#.#.#.#####.#.#
							#.#.#...#...#.#...#...#...#...#...#.#.....#.#.#...#.#...............#.#.#.......#.....#.#.#.......#.......#.#.....#...#.#...#...#.#.......#.#
							#.#.#########.###.#####.###.#####.###.#####.#.###.#.#.#.#####.#.###.#.#.#############.#.#.#.###.#.###########.#####.#.#.#.#.#####.#######.#.#
							#.#.......#.....#...#...#...#...#.....#.#...#...#.#.#.#.....#.#...#.#...#...#.......#.#.#...#...#.............#.....#.#.#.#.#...#...#...#.#.#
							#.#######.#.###.###.###.#.#####.#####.#.#.#####.#.###.#####.#.###.#####.#.#.#.#####.#.#.#####.#.#####.#########.#######.###.#.#.###.#.#.###.#
							#.....#.#...#.#...#...#.#.#.......#.....#.#.#...#...............................#...............#...#.#...#.....#.......#...#.#.....#.#.....#
							#.#.#.#.#####.###.###.###.###.###.#.#.###.#.#.#######.###.#.###.#.#.#.###.#.#####.###.#####.#.###.#.###.#.#.#.###.###.###.#######.###.#####.#
							#.#.#...........#...#.....#...#.....#.....#...#.............#.#.#...#.#.....#...#.#...#.......#...#.....#.#.#.#...#...#...#.....#.....#...#.#
							###.#############.#.#######.#.#####.#.#######.#.#############.#.#.###.#.#####.#.#.#.###########.#########.#.###.###.#.###.#.###.#######.###.#
							#...#.......#.....#.#.......#.#...#.#.......#.#.#.......#.....#...#...#.......#.#.#.#.....#.....#.........#.#...#...#...#.....#...#.....#...#
							#.###.#####.#.#.###.#.###.#####.#.#########.#.#.###.#.###.#.#.###.#############.#.#.#.#.#.#.#####.#########.#.#####.###.#.#######.#.#####.###
							#...#...#.#...#.......#...#.....#.#...#.....#.#...#.#.#...#.......#...........#.#.#...#...#.....#.#.........#...#...#.#.#.#.....#.#.........#
							#.#.###.#.###############.#.#####.#.#.#.#####.###.###.#.###########.###.#####.#.#.#####.#.#####.#.#######.#.###.#.###.#.#.#.###.#.#.###.#.#.#
							#.#...#...........#.....#.#.#...#.#.#...#...#...#...#.#.#.....#...#.#.#.#.....#.....#...#.#.#...#...#...#.....#...#.#...#.#.#.#...#.#...#.#.#
							#.#.#.#.#######.###.###.###.#.#.#.#.#####.#####.#.#.#.#.#.###.###.#.#.#.#####.#####.#.###.#.#.#####.#.#.#####.#####.#.#####.#.#####.#.###.#.#
							#.#.#.........#.#...#.#.....#.....#...#.#.......#.#.#.#.#...#...#.#...#.....#.#.....#.#...#.#.#...#.....#.......#...#.#.....#...#...#.....#.#
							#.#.#.#########.#.###.#######.#######.#.#.#.#######.#.#.#.#.###.#.#.#.#####.###.#####.#.###.#.#.#.#####.#.#####.###.#.#.#######.#.#########.#
							#.#...#.........#.#...#.....#.........#.#...#.......#.#...#...#.#...#.#...#.....#...#.#.#...#.#.#...#...#.#.........#...#.......#...#.....#.#
							#.###.#.###.#####.#.#.#.#.#####.#.#####.###.#.#######.#####.#.#.#.###.#.#########.#.#.#.###.#.#.#####.###.#########.#######.#######.###.#.#.#
							#...#...#...#...#.#.#.#.#...#...#.#.#.....#.#.#...........#.#...#.#...#.....#...#.#.#.#.....#.#...#.....#...#.....#.#.......#.....#...#.#...#
							###.#.###.#.#.#.#.###.#.###.#.###.#.#.#.###.#.###.#.#####.#.#####.#.###.#.#.#.#.#.###.#####.#.###.#.###.#.#.#.###.#.###.###.#.###.###.#.###.#
							#...#.....#...#.#...#.......#.....#...#.#.....#...#.#...#.#.#...#.#...#.#.#...#.#...#.....#.#.#.....#.....#...#...#...#.#...#.............#.#
							#.###.#####.###.###.#.#.###.#.#######.#.#.#####.#.###.#.#.#.#.#.#.###.#.#.#####.###.#####.#.#.#.###.#.#.#######.#####.#.#.#####.#.#.###.#.#.#
							#...#.....#...#.#...#...#...#.#.......#...#...#.#.#...#.#.#...#.....#.#.#.#.#...#...#...#.#.#.#...#.#.........#.#...#...#.#.#...#...#...#...#
							###.#.###.#.#.###.#######.###.#.#####.#####.#.#.###.###.###.#.#####.#.###.#.#.#.#.#.#.#.#.#.#.#.#.#.#########.#.#.#.#####.#.#.#######.#.###.#
							#.#.......#.#.....#.....#.#...#.....#.......#.#...#.#.#.......#.....#.....#.#...#.#...#.#.#.#.#.#.#...#.#...#.#...#.........#.....#...#.....#
							#.###.#.###.#####.#.###.#.#.###.###.#########.#.#.#.#.#########.###########.###.#.#####.#.#.#.#.#.#.#.#.#.#.#.#######.#####.#####.###.#.#.#.#
							#.......#...#...#.#.#.#.#.#.#.#...#.....#.....#.#.#.#.......#.......#...........#.....#.#.#.#.#.#.#...#.#.#...#...#...#...#.....#...#.......#
							#.#.#.#.#.#.#.#.###.#.#.#.#.#.###.#.###.#.#######.#.#.#.###.#########.#########.#####.###.#.#.###.#.###.#.#######.#.###.#.#####.###.###.###.#
							#...#.#.#.....#.#...#.#.#.....#...#.#...#.#.#.........#.#.#.......#...#.......#.....#.#...#.#.....#...#...#.......#...#.#.........#.........#
							#.###.#.###.#.#.#.###.#.#.###.#.#####.#.#.#.#.#.#####.#.#.#.#.###.#.###.#####.#####.#.#.#######.#####.#.#.###.###.###.#.#####.###.#####.###.#
							#.#...#.#.....#...#...#.#.....#.#.....#.#.#...#.#.....#...#...#.#...#.#...#.#...#.....#.#.....#.......#.......#.....#.......#.#.......#.....#
							#.#.###.###.#.#####.#.#.#####.#.#.#####.#.###.#.#.#######.#####.#####.###.#.###.#####.#.#.###.#.#.###########.#####.#######.#.#########.###.#
							#.#...#.....#.....#.#.#.........#.#...#.#...#.#.#.......#.#.....#...........#.#.....#.#...#.#...#.......#...#.#.......#.....#...............#
							#####.###.#.#.###.###.#####.#.###.#.#.#.###.###.###.#####.#.###.#####.#.###.#.#####.#.#####.#.#######.#.#.#.###.#####.#################.#####
							#.......#...#.....#.....#.#.#...#.#.#.#...#...#.....#.....#...........#...#.#.....#.#...#...#.......#.#...#.#.......#.......#.......#.......#
							#.#######.#.#.#####.###.#.#.###.#.#.#####.###.#.#####.#####.#############.#.#.###.#.###.###.#.#####.#######.#.#########.#.#.#.#####.#.#.###.#
							#.........#...#.....#...#.#...#...#.....#.#.#.#...#.#.#.......#.....#.....#.#...#...........#...#...#.......#...#.......#.#.#...#.#.......#.#
							#.#######.###.#.#####.###.###.#######.###.#.#.###.#.#.###.###.#.###.###.#.#.###.#.#.#.#######.###.###.###.#####.#.#.#####.#.###.#.#########.#
							#.......#.#.....#...#.....#.#.........#...#.#...#...#...#...#.#...#...#...#.#...#.#.#.#.....#.#...#...#...#.....#.#.#.....#...#.#.....#...#.#
							#.###.#.#.#.#.#.#.#.#.###.#.#####.#.###.###.###.#######.#.#.#.#.#####.#####.###.#.#.###.###.#.#.#####.###.#.#####.###.#####.###.#.###.#.#.#.#
							#.....#.#.#.#.#.#.#.....#.#...#...#.#...#.....#...........#...#.#...#.#...#...#.#.......#.#...#.#...#...#...#...#.....#...#.......#...#.#...#
							#######.#.#.#.#.#.#####.#.#.#.#.#####.###.#.#############.#####.#.#.#.#.#.###.#.#.#######.#####.#.#.###.###.#.#.#########.#############.###.#
							#.#.....#.....#.#.#...#.#...#.#.......#...#.#.......#...........#.#...#.#.....................#...#...#...#...#.......#.....#...#...#...#.#.#
							#.#.#####.#####.#.###.#.#####.#########.#####.#####.#.###########.###.#.#########.#############.#####.###.###########.#.#.#.#.###.#.#.###.#.#
							#...#...........#.#...#.#.....#.#.....#.......#...#...#...#.....#.#.#.#...#...#...#...........#...#...#.....#.......#.#.#...#.#...#...#.....#
							#.###.###########.#.#.#.#.#####.#.#.###.#.#.###.#.#####.#.#.###.#.#.#.###.#.#.#.###.#########.#####.###.#####.#.#####.#.#.###.#.#############
							#.....#.......#.....#.#.#...#...#.#...#.#.......#.#.....#.#.....#.#...#...#.#.#...#.#.#.....#.....#.#.........#...#.....#...#.#.....#.......#
							#######.#######.#####.#.###.#.#.#.###.#.#######.#.#.#####.#.#.#.#.#####.###.#.###.#.#.#.###.#####.#.#.#.#########.#.#.#.###.#.#####.#.#####.#
							#.......#...#...#...#.#...#.#.#.#...#.....#.....#.#...#.#.#.#.#...#.....#.#.#...#.#.#...#...#.....#.#.........#...#.#...#.........#.....#...#
							###.#.#.#.#.#.###.#.###.#.#.###.#.#########.#####.###.#.#.###.#.###.#####.#.###.#.#.#.###.#.#.#####.#########.#.###.#.###.###.#####.#####.###
							#...#.#...#.....#.#.....#.#...#...#...#.....#...#.#...#.......#.#...#.....#.#.#.#.#.#.#...#.#.#.......#...#...#...#...#.#.#...#...#...#...#.#
							#.###.#########.#.#######.###.#####.#.#.#####.#.#.#.#############.###.#.#.#.#.#.###.###.###.#.#.#####.#.###.#####.#####.#.###.#.#.###.#.###.#
							#.....#.#...#...#.....#.....#.......#...#.....#.....#.............#.....#.#.#...#...#...#...#.#...#.#.#.....#...#.#...#...#...#.#...#.#...#.#
							#.#####.#.#.#####.###.#.###.#############.#.###.###.#.#############.#.#.###.#.###.###.#####.#.###.#.#.#.#####.###.#.#.#.###.#.#.###.#.#.#.#.#
							#.......#.#.....#...#.#.....#.....#.....#.#.#...#...#...#...............#...#.....#.........#.#.....#.#.......#...#.#...#...#.#.#.#.#...#.#.#
							#########.#####.###.#.###.#####.#.###.#.#.#.#.###.#.###.###.###.#######.#.#########.#######.#.###.###.#######.#.###.#####.###.#.#.#.#.###.#.#
							#.....#...#.....#...#...#...#...#.....#.#.#.#...#.....#.....#.#.....#...#...#.#.......#.....#.......#.....#...........#...#...#.#.#.#.#...#.#
							#.###.#.#.#####.#.#.###.###.#.#.#####.###.#.#.#.#####.#######.#####.#.#####.#.#.#####.#.###########.#####.###########.###.#.###.#.#.###.###.#
							#.#.....#.#...#...#...#.......#.#.....#...#.#.#.....#.#.......#.....#.#...#.#.........#...........#.....#...........#.#...#...#.#.......#...#
							#.#######.#.#.#######.#.#####.###.#.###.###.###.#.###.###.###.#.#####.#.#.#.###.#####.#####.###########.#######.###.#.#.#.###.#.#########.#.#
							#...#.#...#.#.#.....#.#...#...#...#...#...#.....#.........#...#.#.....#.#...#...#.....#.....#.........#.....#...#...#.#.#.#...#.....#...#.#.#
							###.#.#.###.#.#.###.#.###.###.#.#####.#.#.#.#######.#####.#####.#.#.###.###.#.#.#######.#####.#####.#######.#.#.#.###.#.#.#.#######.###.#.#.#
							#...#...#...#.#...#.#...#...#.#.....#.#...#...#...#.#...#.#.....#.#...#.#.#...#.......#...#...#...#.......#...#...#.#...#...#...#...#...#.#.#
							#.###.###.###.###.#.###.###.#.#####.#.#.###.###.#.###.#.###.#########.###.#.#.#######.###.#.###.#####.#.#.#########.#.#######.#.#.###.###.#.#
							#...#.#...#.#...#.#...#.#...#.#.#...#.#.#...#...#...........#.........#.....#...#.........#.#.......#...#.........#.#.#.......#.............#
							###.#.#.###.###.###.###.#.###.#.#.###.#.###.#.###############.###.#####.#######.#.#####.#.#.#####.#.#.#.#.#######.#.#.#.###########.###.###.#
							#...#.#...........#.....#.#.....#...#...#...#.....#.....#...........#...#...#...#.....#.#.#.....#.#.#.#.#.#...#...#...#...........#...#...#.#
							#.#####.#.###.###.#.#####.#####.###.#####.#.#.#.#.###.#.#####.#####.#.###.#.#.#######.#.#######.#.#.#.#.###.#.###.###.###.#######.#.#.###.#.#
							#...#...#...#.#...#.#...........#.#.#.....#.#...#.#...#.....#.#...#...#...#.#.#.....#.#.......#.#.#.#.#.....#...#...#.....................#.#
							###.#.###.###.#.###.#####.###.#.#.#.#####.#####.#.#.#######.###.#.#.###.###.#.#####.#.#######.#.#.#.#.#########.###.#######.#.#.###.#######.#
							#...#.#.#.#.......#.....#.........#.....#.......#...#.....#.....#.#.#...#.#...#.....#.#.....#...#.#.........#...#...#.....#.#.#...#.....#...#
							#.###.#.#.#.#####.#####.#.#######.#####.#.#.###.#####.#.#.#######.###.###.#####.###.#.#.#.#.###.#.#####.#####.###.###.###.#.#.###.#.#.###.###
							#.#...#...#.#...#.#.....#.#.#.........#.#.#.#...#.....#.#...#...#.....#.......#...#.#...#.#...#...#.....#...#.#.......#.#.#...#...#.#...#...#
							#.#.#######.#.#.#.#####.#.#.#.#########.#.#.#####.#####.#####.#.#######.#####.#.#.#######.###.###.#######.#.#.#.#######.#.#####.#####.#.#.#.#
							#...#.......#.#.#.#...#...#.....#.......#.#.........#.#.....#.#.......#.#...#...#.#.........#.#...#.....#.#...#.........#.......#.....#.....#
							#.###.#.#####.#.#.#.#.###.#####.#.#.#.###.#########.#.#.#.#.#.#.#####.#.#.#.###.#.#.#######.#.#####.###.#.#####.#.###############.###.#.#.###
							#.#...#.#...#.#.#...#.#...#...#...#.......#.......#...#...#.#.#.#...#...#.#.....#.......#.#.#...#...#.#.#.#.#...#.............#.......#.#...#
							#.#.#.#.#.#.#.#.#.###.#.###.#.#.#########.#.#####.#######.#.#.#.#.#.#######.#.#########.#.#.###.#.###.#.#.#.#.#########.#######.#####.#.#.#.#
							#.#.#.#.#.#.#.#...#...#.#.#.#...#.......#.#.#...#.#.....#.#.#.#.#.#.........#.......#.....#...#...#...#...#...#.......#...........#...#.#.#.#
							#.###.#.#.#.#.###.#.###.#.#.#####.#.###.#.#.#.###.#.###.###.#.#.#.#####.#.###.#####.#.#####.#.#####.#.#########.#####.###.#########.#.###.#.#
							#.....#.#.#...#.....#.#.#.#.#...#.#.#...#.#.#.....#.#.....#...#.#.....#.#.#.........#.#...#.#.........#.....#...#...#...#.#.#...#...#.....#.#
							#######.#.###########.#.#.#.#.###.#.#.#####.#.#####.#####.#####.#######.#.#.###########.#.#####.#####.###.#.#.#####.###.#.#.#.#.#.###.#.#.#.#
							#...#...#.......#.....#...#.#.....#.#.#.....#.....#...#.....#...#.......#.#.#...#.......#.......#...#.#...#...#.....#.#.#.#.#.#...#.....#...#
							#.#.#.#########.###.#.###.#.#####.#.#.###.#######.#.#.###.#.#.###.#########.#.###.#.#.#############.#.#.#.#######.#.#.#.#.#.#.#####.#.#.#.#.#
							#.#.#.....#...#...#.#...#.#.....#.#.#.....#.......#.#.....#...#.#.......#...#.....#.#.....#.#.......#...#...#.....#.#.#.#...#.....#.#.#...#.#
							#.#.#####.###.###.#.#.###.#####.###.#########.###########.#####.#.#####.#.#####.###.#.###.#.#.###.#########.#.#.###.#.#.###.#####.#.#.#.#.#.#
							#.#.....#.........#.#...#.....#...#.........#.#...........#.....#.#.#...#.#...#.#.#.#.#.....#...#...........#.#.#.#...#.#.#.#...#.....#...#.#
							#.#####.#####.#.#######.#####.###.#.#######.#.#.###.###########.#.#.#.###.#.#.#.#.#.#.#####.###.#####.#######.#.#.#.###.#.#.#.#.###.#.#.#.#.#
							#.#...#.....#.#.#.......#.....#...#.....#.#.#.#.#...#.#.......#.....#...#...#.....#.#...#.#.#.#.#...#...#.....#.#...#...#...#.#.#...#.....#.#
							#.#.#.#####.#.#.###.#####.#####.#######.#.#.#.###.###.#.###.#.#####.###.#####.#####.#.#.#.#.#.#.#.#.###.#.#####.#####.###.###.#.#.#.#.#.###.#
							#.#.#.....#.#.#...#.......#...#...#.....#.#.#.....#...#...#.#.......#.......#...#...#.#.#.....#...#...#.#...#...#...#.#.........#.#.........#
							#.#####.#.#.#.###.#########.#.###.#.#.###.#.#######.#.###.#.#######.#.###.#.###.#.###.#.#########.#####.#.###.###.#.#.###########.#.#.#.#.###
							#...#...#.#.#.#.#...#.....#.#...#...#.....#.#.......#.#...#...#.......#...#...#.#...#.#.#...#.....#.....#.#...#...#.#...........#.#...#.....#
							###.#.#####.#.#.###.#.###.#.###.###.#####.#.###.###.###.#####.#########.#####.#.###.#.#.#.#.#.#####.###.#.#.###.###.###########.#.#########.#
							#...#.......#.....#...#.#...#.#...#.#.....#...#...#.....#...#.#.......#.#.....#.......#.....#.#...#.#...#.#...#.#.#.....#.....#.#...........#
							#.#####.#########.#####.#####.###.#.#########.###.#.#####.###.#.#####.#.#.#.#.#########.#.#.#.#.###.###.#.###.#.#.#####.#.#.###.#.#.###.#.###
							#.....#.....#...#...#.........#...#...........#.#.....#.....#...#.....#.#.#.............#.#...#...#...#.#.#.#.#.......#.#.#.....#...#...#.#.#
							#.###.#.#.###.#.###.#.#.###.#.#.###.#.#########.#####.#.###.#####.#####.#.#.#############.#######.###.#.#.#.#.###.#####.#.#########.#.###.#.#
							#.#...#...#...#.....#.#...#...#.#...#...#.........#...#...#.....#.......#.#.#.....#.....#...#.....#.#.....#.#...#.#.....#.........#...#.#.#.#
							#.#.###.###.#############.#.#.#.#.#####.#.#.#####.#.###########.###.#####.###.###.#.###.###.###.#.#.#.###.#.###.#.#.###.#########.###.#.#.#.#
							#.#.......#...#...........#...#.#.......#.#...#...#.#.........#.........#...#...#.#...#.....#...#...#.#...#...#...#.#.......#.....#...#.....#
							#.#####.#.###.#.#######.###.#.#.#######.#.###.###.#.#.#######.#.#######.#.#.#.#.#.###.#######.#####.#.#.###.#.#####.#######.#.#####.###.###.#
							#.#.....#...#.......#...#...#.#.....#...#.........#.#.#.....#.#.......#.#...#...#.#...#...#.......#.#.#.#.......#.........#.#...#...#.....#.#
							#.#.###.###.#####.#.#.###.#.#.#####.#.#.###.###.###.#.#.###.#.#.#####.#.#.#####.#.#.#####.#.#####.#.#.#.###.###.#.#######.#.###.#.###.#.#.###
							#.#.#.#...#.......#.#.#...#.#.......#.#...#.#.#.....#.#...#...#.....#.#.#...#...#.#.#...#.....#.#.#.#.#.....#.#.#.......#.#.#.#.#.#.....#...#
							#.#.#.#.#.###.###.###.#.###.###.#.#.#.###.#.#.###.###.###.###.#####.#.#####.#.###.#.#.#.#####.#.#.#.#.#######.#.#####.#.#.#.#.#.#.#.###.#.#.#
							#.#...#.....#.#...#...#.#.#.#.#.#.#.#.#.#...#.....#...#.#.....#.....#.....#...#.#...#.#.....#...#.#...........#.......#.#.#...#.#.#.#.#.....#
							#.#####.#.###.#####.#.#.#.#.#.#.#.#.#.#.#####.###.#.###.#####.#####.#####.#####.#####.#####.#####.#.###########.#######.#.###.#.#.#.#.#.#.#.#
							#.#.....#.#...#...#...#.#.#.#...#.#.#...#.....#...#.#.....#.#.....#.....#...#.........#...#...#...#.......#.........#...#.#...#.#.....#.#.#.#
							#.#.#####.#.###.#.#.#.#.#.#.###.#.#####.###.###.###.###.#.#.###.#.#.#####.#.#.#.#####.#.#.###.#.#.#######.#.#.#.#####.###.###.#.#######.#.#.#
							#...#.....#.....#...#...#.....#.#.#...#.....#.#.....#...#.#.#.....#.#...#.#...#.........#...#.#.#...#...#...#.#.#...#...#...#.#.#.......#...#
							#######.###########.#.#######.###.#.#.#######.#######.###.#.#.#######.#.#.#.###.#.###########.#.###.#.#.#####.###.#.###.###.#.#.#.#####.#.###
							#.....#.......#...#.#...#...#.....#.#...........#.....#.....#.........#...#.....#.........#...#...#.#.#.....#.....#.........#.#.#.#...#.....#
							#.###.#####.#.#.#.#.###.#.#.#####.#.#.#######.#.#.###.#.###.#############.###############.#.###.#.#.#####.#.#############.#.###.#.#.#.#.#.#.#
							#...#.#.....#.#.#.#...#...#.#.....#.#...............#.#.............#.....................#...#.#.....#...#.........#...#.#.#...#...#...#.#.#
							#.#.#.#.#.#.#.#.#.#######.#.#######.###############.#.#.###########.#######.#.###########.###.#.#.###.#.#.#####.###.###.#.###.###.#####.#.#.#
							#.#.#.#.#.....#.#...........#.......#.#.....#.....#.#.#.......#...........#.#.#.....#.......#.#.#.#...#.#...#...#.......#.#...#...........#.#
							###.#.#.###.###.###.#.#######.#######.#.###.#.###.###.#.#.#.#.###.#######.#.#.#.###.#######.#.#.#.#.###.###.#.#.#.#######.#.###.#####.#####.#
							#...#.#.#.......#...#.........#.......#.#...#...#...#...#.#.#...#.#...#...#.#.#...#.......#...................#.#.#.......#...#.#...#.#...#.#
							#.###.###.#.###.#####.###########.#.###.#.###.#.###.###.#.#.###.###.#.#.###.#####.#######.###.###.#.#####.#####.#.#.#####.#.#.###.#.###.#.#.#
							#...#...#.....#.#.....#.........#.#.#...#.#...#...#.......#...#.....#.#...#.....#.....#.#...#...#.#...#...........#.....#.#.#...#.#.....#.#.#
							#.#.###.#.#.#.###.###.#.#######.#.###.###.#.#####.#####.#####.#######.###.###.#.#####.#.###.###.#.#.#.#############.###.#.#.###.#.#######.#.#
							#.#...#.#...#...#.#.#.........#.#.#...#...#.#.....#...#.#.....#...#...#...#...#...#...#...#.#...#...#.#...........#.#...#.....#.#.#.........#
							#.###.#.###.###.#.#.#.#####.#.#.#.#.###.#####.#####.#.###.#####.#.#.###.###.###.#.#.###.#.#.#.#####.#.#.#########.###.###.#.###.#.###########
							#...#.#.#.......#.#.#...#...#.#.#.#...#.......#...#.#...#.......#.#.#.......#...#.....................#...#.....#.#...#...#.....#...........#
							###.#.#.#.#.#####.#.#.#.#.###.#.#.###.###.#####.#.#.###.#####.#####.#.#######.#.#.###.#.#.#.#.###.#.#.###.#####.#.#.#######.###########.###.#
							#.#.#.#...#.......#.....#.#...#.#...#...#.#.....#.#.#...#.....#.....#...#.......#.#...#.#.#.#...#.#...#.#.#...#.....#.....#...#.......#.#...#
							#.#.#.#####.#########.###.#.#.#.#.#####.###.#####.#.#.###.#####.#########.#####.#.#.###.#.#.###.#.#.###.#.#.#.#######.###.#.#.#.#####.###.###
							#...#.#.....#...#...#.#.....................#...#.#.#.#...#.......................................#.#...#...#.#...#...#.#.....#.#...#...#...#
							#.###.#.#####.#.#.#.#.#.###.#######.#.###.#.#.#.#.#.#.#.###.#####.#####.#.#.#.#.###.#.###.#######.#.#.#.#####.#.#.#.###.#####.#.#.#####.###.#
							#S..#.........#...#.....#...........#...#.....#.#...#.....#...........#.....#.#.......#...........#...#.....#...#...............#...........#
							#############################################################################################################################################
							""";
}