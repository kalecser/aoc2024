import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import java.awt.*;
import java.nio.file.*;
import javax.swing.text.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.awt.Point;

class Day13 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 12, actual: " + walk(parse(sample), 100, new Point(11, 7)));
		System.out.println("Actual phase one result, expected: 12, actual: " + walk(parse(input), 100, new Point(101, 103)));
		System.out.println("Actual phase two result, expected: 7055, actual:" + walk(parse(input), 10000, new Point(101, 103)));
		
	}
	
	public static int countConnectedTiles(int[][] grid) {
		boolean[][] visited = new boolean[grid.length][grid[0].length];
		int largestClusterSize = 0;
	
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {
				if (grid[y][x] > 0 && !visited[y][x]) {
					// Count tiles in the current connected component
					int clusterSize = floodFill(grid, visited, y, x);
					largestClusterSize = Math.max(largestClusterSize, clusterSize);
				}
			}
		}
	
		return largestClusterSize;
	}

	private static int floodFill(int[][] grid, boolean[][] visited, int y, int x) {
		if (y < 0 || y >= grid.length || x < 0 || x >= grid[0].length || visited[y][x] || grid[y][x] == 0) {
			return 0;
		}
		
		visited[y][x] = true;
		
	
		int count = 1;
		
		count += floodFill(grid, visited, y - 1, x); // Up
		count += floodFill(grid, visited, y + 1, x); // Down
		count += floodFill(grid, visited, y, x - 1); // Left
		count += floodFill(grid, visited, y, x + 1); // Right
		
		return count;
	}
	
	public static long walk(List<Robot> robots, long numberOfSteps, Point fieldSize) {
		for (long i = 0; i < numberOfSteps; i++){
			for (Robot r : robots) {
			
				r.walk(fieldSize);
			}
			int[][] grid = new int[fieldSize.y][fieldSize.x];
			
			for (Robot r : robots) {
				grid[r.position.y][r.position.x]++;
			}
			
			var count = countConnectedTiles(grid);
			if (count > 20) {
				return i + 1;
			}
			
		}
		
		long quadrant1 = 0;
		long quadrant2 = 0;
		long quadrant3 = 0;
		long quadrant4 = 0;
		for (Robot r : robots) {
			
			if (r.position.x < fieldSize.x / 2) {
				if (r.position.y < fieldSize.y / 2) {
					quadrant1 +=1;
				} else if (r.position.y > fieldSize.y / 2) {
					quadrant2 +=1;
				}
			} else if (r.position.x > fieldSize.x / 2) {
				if (r.position.y < fieldSize.y / 2) {
					quadrant3 +=1;
				} else if (r.position.y > fieldSize.y / 2) {
					quadrant4 +=1;
				}
			}
		}
		
		
		return quadrant1 * quadrant2 * quadrant3 * quadrant4;
	}
	
	public static void printGrid(List<Robot> robots, Point fieldSize) {
		var filePath = "/tmp/grid.txt";
		int[][] grid = new int[fieldSize.y][fieldSize.x];
		
		// Count robots at each tile
		for (Robot r : robots) {
			grid[r.position.y][r.position.x]++;
		}
		
		// Append the grid to a file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			for (int y = 0; y < fieldSize.y; y++) {
				for (int x = 0; x < fieldSize.x; x++) {
					if (grid[y][x] == 0) {
						writer.write('.');
					} else {
						writer.write(Integer.toString(grid[y][x]));
					}
				}
				writer.newLine(); // New line for the next row
			}
			writer.newLine(); // Separate grids with an empty line
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	public static List<Robot> parse(String input) {
		List<Robot> robots = input.lines()
				.map(line -> line.split(" "))
				.map(parts -> new Robot(
					new Point(
						Integer.parseInt(parts[0].substring(2).split(",")[0]),
						Integer.parseInt(parts[0].substring(2).split(",")[1])
					),
					new Point(
						Integer.parseInt(parts[1].substring(2).split(",")[0]),
						Integer.parseInt(parts[1].substring(2).split(",")[1])
					)
				))
				.collect(Collectors.toList());
		return robots;
	}
	
	public static class Robot {
		public final Point position;
		public final Point velocity;
		
		public Robot(Point position, Point velocity) {
			if (position == null || velocity == null) {
				throw new IllegalArgumentException("Position and velocity cannot be null");
			}
			this.position = position;
			this.velocity = velocity;
		}
		
		public void walk(Point fieldSize) {
			this.position.x = this.position.x + this.velocity.x;
			if (this.position.x > (fieldSize.x - 1)) this.position.x = this.position.x % fieldSize.x;
			if (this.position.x < 0) this.position.x = (fieldSize.x - (Math.abs(this.position.x % fieldSize.x)));
			this.position.y = this.position.y + this.velocity.y;
			if (this.position.y > (fieldSize.y - 1)) this.position.y = this.position.y % fieldSize.y;
			if (this.position.y < 0) this.position.y = (fieldSize.y - (Math.abs(this.position.y % fieldSize.y)));
			
		}
	}
	
	
	
	static String sample =	"""
							p=0,4 v=3,-3
							p=6,3 v=-1,-3
							p=10,3 v=-1,2
							p=2,0 v=2,-1
							p=0,0 v=1,3
							p=3,0 v=-2,-2
							p=7,6 v=-1,-3
							p=3,0 v=-1,-2
							p=9,3 v=2,3
							p=7,3 v=-1,2
							p=2,4 v=2,-3
							p=9,5 v=-3,-3
							""";

	static String input =	"""
							p=69,95 v=70,-27
							p=95,51 v=-76,-2
							p=54,32 v=-80,-4
							p=41,28 v=27,6
							p=63,6 v=-33,55
							p=80,2 v=77,-78
							p=69,53 v=54,-1
							p=78,40 v=-11,99
							p=36,55 v=-18,9
							p=71,24 v=83,67
							p=3,77 v=-38,71
							p=13,54 v=5,-73
							p=89,20 v=-9,25
							p=39,70 v=-40,-61
							p=38,55 v=-27,30
							p=40,4 v=-87,19
							p=62,23 v=35,-48
							p=65,46 v=-61,13
							p=43,58 v=-93,2
							p=1,19 v=-61,17
							p=81,23 v=-51,53
							p=11,53 v=78,20
							p=70,67 v=-70,-67
							p=20,45 v=-88,40
							p=90,37 v=-37,-8
							p=90,0 v=-11,51
							p=50,89 v=-20,80
							p=84,58 v=29,48
							p=1,24 v=-76,-50
							p=39,70 v=-16,-67
							p=4,58 v=91,-97
							p=35,3 v=54,-86
							p=55,38 v=56,-14
							p=81,6 v=-4,-84
							p=90,42 v=17,-30
							p=54,0 v=8,13
							p=30,30 v=88,-42
							p=3,84 v=-29,-17
							p=75,6 v=-28,-88
							p=63,6 v=-94,-98
							p=58,19 v=-93,75
							p=97,63 v=-3,51
							p=6,99 v=-30,50
							p=48,83 v=-32,88
							p=59,40 v=-59,-73
							p=54,102 v=-86,13
							p=93,48 v=-91,-18
							p=80,4 v=72,-32
							p=89,11 v=98,-56
							p=64,93 v=-72,-5
							p=0,36 v=31,6
							p=46,56 v=-88,-91
							p=58,94 v=-67,1
							p=80,73 v=-18,96
							p=3,101 v=-2,-21
							p=67,30 v=-45,87
							p=97,77 v=-3,74
							p=24,54 v=66,-89
							p=96,17 v=99,-71
							p=84,7 v=50,-20
							p=89,62 v=65,-67
							p=13,12 v=-48,-90
							p=73,2 v=-45,39
							p=47,100 v=-59,90
							p=36,9 v=46,-76
							p=78,52 v=12,-70
							p=1,42 v=-91,18
							p=20,23 v=-89,61
							p=96,47 v=24,-79
							p=98,100 v=31,27
							p=35,73 v=-80,-29
							p=40,39 v=81,10
							p=8,7 v=25,21
							p=23,10 v=-1,19
							p=95,77 v=-27,49
							p=43,88 v=20,-80
							p=58,83 v=21,-35
							p=0,26 v=38,-20
							p=47,92 v=61,94
							p=23,74 v=72,-22
							p=52,54 v=1,-93
							p=8,61 v=65,-59
							p=8,52 v=72,-93
							p=63,14 v=17,60
							p=16,51 v=82,-9
							p=77,34 v=56,-20
							p=74,5 v=-58,-72
							p=35,77 v=6,58
							p=37,79 v=-81,-9
							p=25,78 v=26,94
							p=40,48 v=40,17
							p=62,73 v=-45,23
							p=47,72 v=42,84
							p=83,12 v=37,-90
							p=65,2 v=-65,65
							p=29,52 v=67,-77
							p=98,39 v=24,4
							p=76,66 v=83,40
							p=6,26 v=18,-16
							p=68,89 v=-52,-3
							p=64,19 v=30,-63
							p=36,39 v=-68,-10
							p=46,30 v=-53,-22
							p=77,44 v=-45,-4
							p=82,60 v=78,8
							p=87,77 v=-37,84
							p=93,58 v=-57,-89
							p=55,44 v=55,8
							p=1,60 v=-16,-81
							p=98,62 v=-30,-93
							p=64,74 v=-25,56
							p=64,22 v=3,29
							p=94,32 v=-57,-34
							p=35,88 v=-94,-19
							p=53,87 v=89,-78
							p=18,33 v=-33,-96
							p=62,30 v=-39,1
							p=3,79 v=-47,-24
							p=32,102 v=41,98
							p=63,85 v=-76,86
							p=55,28 v=-64,-26
							p=29,93 v=72,-87
							p=96,41 v=-50,-20
							p=38,21 v=-7,95
							p=67,20 v=-97,-54
							p=12,26 v=-26,32
							p=21,35 v=-68,77
							p=38,49 v=80,64
							p=94,55 v=57,-72
							p=60,65 v=90,60
							p=73,85 v=-75,-73
							p=83,42 v=83,42
							p=6,19 v=-89,-25
							p=72,68 v=63,-17
							p=15,31 v=86,83
							p=39,31 v=74,81
							p=42,12 v=-98,-25
							p=64,28 v=81,-31
							p=38,98 v=-35,-99
							p=1,67 v=85,-59
							p=44,7 v=-65,-58
							p=75,41 v=-64,70
							p=67,90 v=-37,-15
							p=56,20 v=-93,-44
							p=70,101 v=-58,-15
							p=69,8 v=90,-34
							p=56,86 v=42,74
							p=74,1 v=-85,-66
							p=55,16 v=48,53
							p=98,85 v=10,-86
							p=83,15 v=-84,-64
							p=34,33 v=98,-37
							p=30,59 v=88,52
							p=55,49 v=55,34
							p=97,39 v=-30,-36
							p=31,102 v=13,-80
							p=42,98 v=-33,-3
							p=27,71 v=-70,-10
							p=30,79 v=-82,44
							p=34,88 v=-8,-29
							p=0,55 v=51,-79
							p=26,12 v=19,-72
							p=3,71 v=-36,80
							p=50,59 v=22,-93
							p=74,44 v=-79,95
							p=16,5 v=-8,29
							p=99,11 v=30,47
							p=73,43 v=89,-28
							p=26,4 v=53,27
							p=11,21 v=-22,-28
							p=14,3 v=60,15
							p=26,1 v=-37,48
							p=6,62 v=-86,75
							p=24,87 v=90,17
							p=47,90 v=81,-11
							p=90,65 v=98,-71
							p=91,59 v=-4,-35
							p=9,29 v=97,82
							p=89,15 v=30,51
							p=50,100 v=20,-78
							p=62,36 v=-96,78
							p=9,102 v=99,29
							p=80,97 v=27,55
							p=36,13 v=74,-64
							p=88,20 v=36,35
							p=25,3 v=87,-90
							p=11,31 v=-83,-36
							p=67,51 v=-34,32
							p=38,66 v=-34,-67
							p=30,11 v=-39,50
							p=3,86 v=95,-29
							p=76,4 v=63,-64
							p=28,99 v=-89,56
							p=30,97 v=-48,3
							p=98,33 v=84,-48
							p=82,26 v=-64,69
							p=98,73 v=-3,-33
							p=16,81 v=-67,2
							p=21,96 v=92,-37
							p=44,36 v=-41,-87
							p=48,44 v=-32,77
							p=79,61 v=-90,19
							p=26,46 v=66,81
							p=11,69 v=25,-59
							p=13,75 v=12,-47
							p=79,83 v=63,-31
							p=40,82 v=-1,-3
							p=89,85 v=91,78
							p=27,28 v=-83,-17
							p=35,10 v=-82,73
							p=23,84 v=33,-11
							p=9,2 v=99,21
							p=39,86 v=5,-67
							p=45,39 v=87,-28
							p=80,44 v=-70,32
							p=14,59 v=-73,71
							p=51,99 v=-10,61
							p=99,51 v=-16,22
							p=2,17 v=-63,77
							p=45,31 v=-73,69
							p=77,7 v=63,-72
							p=31,74 v=-29,43
							p=100,77 v=-2,93
							p=23,38 v=53,65
							p=92,89 v=-94,-23
							p=71,12 v=-20,55
							p=92,1 v=-44,-48
							p=61,62 v=17,74
							p=44,2 v=-1,-51
							p=27,82 v=6,78
							p=89,19 v=-44,-42
							p=96,14 v=31,45
							p=32,31 v=53,67
							p=26,89 v=79,-21
							p=96,24 v=-51,59
							p=2,30 v=-66,72
							p=70,91 v=55,15
							p=87,1 v=37,29
							p=76,80 v=-11,46
							p=73,1 v=-72,-72
							p=100,43 v=65,10
							p=62,32 v=-12,79
							p=33,29 v=74,-22
							p=76,24 v=-6,-31
							p=3,71 v=-96,-50
							p=18,100 v=46,86
							p=36,101 v=73,-15
							p=81,76 v=-50,68
							p=35,25 v=81,-44
							p=74,79 v=16,74
							p=7,53 v=65,-81
							p=89,96 v=30,-9
							p=20,2 v=-82,-7
							p=47,97 v=-13,5
							p=50,16 v=96,-94
							p=19,45 v=-89,-4
							p=45,2 v=89,-64
							p=60,35 v=90,73
							p=47,45 v=42,95
							p=55,24 v=-32,31
							p=15,74 v=-96,34
							p=69,86 v=36,83
							p=69,94 v=76,98
							p=33,92 v=-4,-63
							p=52,5 v=28,-56
							p=14,58 v=91,-30
							p=9,30 v=26,-20
							p=9,14 v=19,-8
							p=17,69 v=12,46
							p=89,33 v=-68,-34
							p=7,88 v=59,-66
							p=79,93 v=-5,72
							p=43,95 v=27,-9
							p=43,62 v=41,10
							p=60,0 v=-52,-84
							p=7,25 v=71,-38
							p=76,13 v=-51,-24
							p=11,5 v=-89,-76
							p=69,58 v=-92,-55
							p=36,87 v=34,80
							p=57,76 v=-32,-29
							p=78,25 v=-34,27
							p=58,60 v=-39,-65
							p=13,9 v=33,-36
							p=38,5 v=-23,45
							p=99,49 v=93,-57
							p=18,11 v=-89,57
							p=74,36 v=-37,-73
							p=70,1 v=57,-60
							p=80,65 v=-11,46
							p=41,16 v=28,-18
							p=35,23 v=-80,-18
							p=82,11 v=76,67
							p=74,47 v=25,-81
							p=12,98 v=66,5
							p=69,23 v=-92,-52
							p=2,85 v=58,92
							p=17,68 v=-48,-63
							p=22,60 v=-12,89
							p=75,34 v=-11,73
							p=16,27 v=87,-99
							p=95,34 v=-3,-20
							p=16,20 v=38,-10
							p=10,22 v=-22,-58
							p=53,7 v=75,43
							p=74,27 v=63,63
							p=2,84 v=-27,-71
							p=99,33 v=61,90
							p=89,7 v=17,53
							p=65,2 v=49,41
							p=83,48 v=-78,-99
							p=34,27 v=52,-72
							p=46,24 v=-33,63
							p=48,64 v=-60,28
							p=46,16 v=21,-60
							p=88,23 v=38,-40
							p=77,72 v=63,46
							p=33,57 v=-21,28
							p=36,82 v=11,64
							p=83,38 v=17,91
							p=49,67 v=94,52
							p=95,38 v=23,-36
							p=87,29 v=50,75
							p=88,70 v=-64,50
							p=56,18 v=22,-14
							p=85,7 v=24,3
							p=69,39 v=56,6
							p=82,97 v=77,41
							p=28,3 v=-88,29
							p=59,15 v=-86,-48
							p=43,78 v=64,84
							p=78,86 v=-55,-3
							p=62,38 v=-79,-2
							p=81,68 v=94,46
							p=48,80 v=-85,-67
							p=13,77 v=-90,32
							p=51,43 v=49,30
							p=94,55 v=-57,30
							p=41,15 v=-13,-38
							p=40,97 v=-53,80
							p=39,6 v=34,-38
							p=97,96 v=77,27
							p=73,87 v=76,-21
							p=27,22 v=76,-17
							p=80,94 v=94,-7
							p=78,18 v=-31,-64
							p=34,47 v=81,-95
							p=87,96 v=51,94
							p=35,21 v=-14,-48
							p=95,16 v=-85,-53
							p=77,14 v=-90,59
							p=49,48 v=41,14
							p=75,52 v=-92,32
							p=16,91 v=96,-11
							p=50,24 v=-46,92
							p=88,38 v=37,-93
							p=97,13 v=85,43
							p=81,44 v=50,99
							p=97,45 v=-97,-75
							p=35,91 v=94,15
							p=3,74 v=18,-47
							p=64,32 v=62,91
							p=74,16 v=31,-64
							p=65,37 v=44,89
							p=90,25 v=-98,-64
							p=13,98 v=-36,-5
							p=83,102 v=84,-90
							p=42,15 v=-67,45
							p=8,10 v=86,73
							p=72,84 v=-11,-9
							p=58,99 v=36,-84
							p=59,19 v=-5,51
							p=22,38 v=-76,-78
							p=24,8 v=-68,45
							p=60,70 v=-32,44
							p=41,5 v=-19,-40
							p=28,4 v=-48,35
							p=53,47 v=-38,72
							p=82,80 v=23,84
							p=21,95 v=19,-17
							p=85,5 v=-71,-86
							p=90,2 v=-44,-46
							p=92,63 v=-84,-53
							p=82,67 v=83,-83
							p=9,76 v=-29,-65
							p=87,55 v=-90,-75
							p=2,59 v=-19,33
							p=17,78 v=38,-17
							p=99,8 v=51,37
							p=58,35 v=42,79
							p=9,97 v=-77,37
							p=63,19 v=-32,-36
							p=76,31 v=50,81
							p=14,68 v=-89,42
							p=49,45 v=97,7
							p=81,52 v=-38,-93
							p=75,97 v=3,80
							p=48,101 v=67,-78
							p=89,95 v=91,-7
							p=6,96 v=52,-1
							p=67,37 v=75,79
							p=73,47 v=37,2
							p=7,98 v=32,3
							p=7,87 v=-43,-23
							p=27,61 v=62,-79
							p=30,85 v=6,1
							p=42,29 v=34,-46
							p=47,30 v=95,73
							p=25,88 v=6,78
							p=72,92 v=36,84
							p=90,92 v=64,31
							p=79,70 v=-72,90
							p=39,77 v=81,-38
							p=50,2 v=-88,-35
							p=90,91 v=38,-35
							p=75,17 v=89,61
							p=98,60 v=-16,-49
							p=56,1 v=56,84
							p=1,82 v=55,-45
							p=22,59 v=86,-81
							p=100,84 v=-23,-88
							p=90,51 v=-17,-91
							p=82,53 v=3,-77
							p=77,96 v=36,-9
							p=76,52 v=-38,-69
							p=8,18 v=-72,-72
							p=48,74 v=-20,8
							p=22,81 v=90,-2
							p=55,102 v=-73,52
							p=83,79 v=-48,14
							p=83,13 v=97,-60
							p=35,96 v=-94,-9
							p=49,56 v=-45,89
							p=55,16 v=-10,71
							p=58,51 v=89,16
							p=1,15 v=-37,83
							p=27,66 v=-41,-67
							p=68,92 v=56,7
							p=48,41 v=35,47
							p=69,38 v=56,53
							p=57,87 v=-58,-42
							p=69,50 v=69,50
							p=11,101 v=72,7
							p=61,5 v=-20,-8
							p=41,22 v=-53,-54
							p=2,40 v=45,-8
							p=15,44 v=-21,67
							p=25,44 v=80,-79
							p=23,91 v=-25,-79
							p=82,55 v=-90,-85
							p=37,13 v=-26,-26
							p=10,2 v=-90,96
							p=10,45 v=-95,90
							p=61,12 v=55,-60
							p=97,37 v=98,89
							p=7,95 v=-2,-90
							p=23,93 v=39,5
							p=70,74 v=12,44
							p=28,54 v=-41,-98
							p=34,91 v=25,-84
							p=16,48 v=-21,-57
							p=87,83 v=17,-21
							p=82,64 v=23,36
							p=60,75 v=42,-29
							p=30,16 v=23,64
							p=4,80 v=92,92
							p=74,26 v=62,65
							p=24,11 v=25,-58
							p=26,97 v=57,20
							p=2,99 v=-62,21
							p=44,62 v=61,48
							p=8,43 v=79,-89
							p=32,93 v=-54,37
							p=31,33 v=6,-12
							p=80,35 v=-51,-16
							p=72,14 v=-18,22
							p=3,68 v=-15,86
							p=87,15 v=11,37
							p=4,57 v=-9,-61
							p=23,37 v=-82,-22
							p=9,77 v=-76,-37
							p=32,7 v=-21,60
							p=43,69 v=-50,95
							p=90,55 v=4,-43
							p=58,34 v=59,-27
							p=44,2 v=45,-24
							p=32,91 v=67,29
							p=52,35 v=-26,-60
							p=80,102 v=90,-20
							p=5,59 v=-43,-75
							p=51,57 v=45,70
							p=90,65 v=51,16
							p=54,40 v=-74,79
							p=47,12 v=-33,-54
							p=71,80 v=-85,-37
							p=24,12 v=-28,-50
							p=50,8 v=-79,61
							p=27,86 v=65,-30
							p=25,46 v=16,48
							p=5,3 v=-76,-90
							""";
}