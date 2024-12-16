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

class Day15 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 10092, actual: " + simulate(parse(sample, false)));
		System.out.println("Actual phase one result, expected: 1515788, actual: " + simulate(parse(input, false)));
		System.out.println("Sample phase two result, expected: 9021, actual: " + simulate(parse(sample, true)));
		System.out.println("Actual phase two result, expected: 9021, actual: " + simulate(parse(input, true)));
	}
	
	public static long simulate(Maze m) {
		
		
		
		
		
		
		
		int ic =0;
		//perform moves
		for (char c : m.moves) {
			Point r = findRobot(m.map);
			tryMove(r, c, m.map);
			
		}
		
		//printMap(m.map);
		
		//calculate GPS
		long result = 0l;
		for (int i = 0; i < m.map.length; i++) {
			for (int j = 0; j < m.map[i].length; j++) {
				if (m.map[i][j] == 'O' || m.map[i][j] == '[') {
					result += ((100 * i) + j);
				}
			}
		}
		
		return result;
	}
	
	public static boolean tryMove(Point p, char direction, char[][] map) {
		Point np = (Point)p.clone();
		if (direction == '<') np.x = np.x - 1;
		if (direction == '>') np.x = np.x + 1;
		if (direction == '^') np.y = np.y - 1;
		if (direction == 'v') np.y = np.y + 1;
		
		char peek = map[np.y][np.x];
		
		if (peek == '#') { //wall
			return false;
		}
		
		if (peek == '.') {
			map[np.y][np.x] = map[p.y][p.x];
			map[p.y][p.x] = '.';
			p.x = np.x;
			p.y = np.y;
			return true;
		} 
		
		if (peek == 'O') {
			if (tryMove((Point)np.clone(), direction, map)) {
				map[np.y][np.x] = map[p.y][p.x];
				map[p.y][p.x] = '.';
				p.x = np.x;
				p.y = np.y;
				return true;	
			} else {
				return false;
			}
		}
		
		if (peek == '[' && direction == '^') {
			
			char[][] mapClone = new char[map.length][map[0].length];
			for (int i = 0; i < mapClone.length; i++) {
				for (int j = 0; j < mapClone[i].length; j++) {
					mapClone[i][j] = map[i][j];
				}
			}
			boolean success = true;
			if (tryMove((Point)np.clone(), direction, mapClone)) {
				mapClone[np.y][np.x] = mapClone[p.y][p.x];
				mapClone[p.y][p.x] = '.';
				np.x = np.x + 1;
				if (tryMove((Point)np.clone(), direction, mapClone)) {	
					mapClone[np.y][np.x] = mapClone[p.y][p.x];
					mapClone[p.y][p.x] = '.';
					for (int i = 0; i < mapClone.length; i++) {
						for (int j = 0; j < mapClone[i].length; j++) {
							map[i][j] = mapClone[i][j];
						}
					}
					
					p.x = np.x - 1;
					p.y = np.y;
					return true;	
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		if (peek == ']' && direction == '^') {
			
			char[][] mapClone = new char[map.length][map[0].length];
			for (int i = 0; i < mapClone.length; i++) {
				for (int j = 0; j < mapClone[i].length; j++) {
					mapClone[i][j] = map[i][j];
				}
			}
			boolean success = true;
			if (tryMove((Point)np.clone(), direction, mapClone)) {
				mapClone[np.y][np.x] = mapClone[p.y][p.x];
				mapClone[p.y][p.x] = '.';
				np.x = np.x - 1;
				if (tryMove((Point)np.clone(), direction, mapClone)) {	
					mapClone[np.y][np.x] = mapClone[p.y][p.x];
					mapClone[p.y][p.x] = '.';
					for (int i = 0; i < mapClone.length; i++) {
						for (int j = 0; j < mapClone[i].length; j++) {
							map[i][j] = mapClone[i][j];
						}
					}
					
					p.x = np.x + 1;
					p.y = np.y;
					return true;	
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		if (peek == '[' && direction == 'v') {
			char[][] mapClone = new char[map.length][map[0].length];
			for (int i = 0; i < mapClone.length; i++) {
				for (int j = 0; j < mapClone[i].length; j++) {
					mapClone[i][j] = map[i][j];
				}
			}
			boolean success = true;
			if (tryMove((Point)np.clone(), direction, mapClone)) {
				mapClone[np.y][np.x] = mapClone[p.y][p.x];
				mapClone[p.y][p.x] = '.';
				np.x = np.x + 1;
				if (tryMove((Point)np.clone(), direction, mapClone)) {	
					mapClone[np.y][np.x] = mapClone[p.y][p.x];
					mapClone[p.y][p.x] = '.';
					for (int i = 0; i < mapClone.length; i++) {
						for (int j = 0; j < mapClone[i].length; j++) {
							map[i][j] = mapClone[i][j];
						}
					}
					
					p.x = np.x - 1;
					p.y = np.y;
					return true;	
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		if (peek == ']' && direction == 'v') {
			
			char[][] mapClone = new char[map.length][map[0].length];
			for (int i = 0; i < mapClone.length; i++) {
				for (int j = 0; j < mapClone[i].length; j++) {
					mapClone[i][j] = map[i][j];
				}
			}
			boolean success = true;
			if (tryMove((Point)np.clone(), direction, mapClone)) {
				mapClone[np.y][np.x] = mapClone[p.y][p.x];
				mapClone[p.y][p.x] = '.';
				np.x = np.x - 1;
				if (tryMove((Point)np.clone(), direction, mapClone)) {	
					mapClone[np.y][np.x] = mapClone[p.y][p.x];
					mapClone[p.y][p.x] = '.';
					for (int i = 0; i < mapClone.length; i++) {
						for (int j = 0; j < mapClone[i].length; j++) {
							map[i][j] = mapClone[i][j];
						}
					}
					
					p.x = np.x + 1;
					p.y = np.y;
					return true;	
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		if (peek == '[' || peek == ']') {
			if (tryMove((Point)np.clone(), direction, map)) {
				map[np.y][np.x] = map[p.y][p.x];
				map[p.y][p.x] = '.';
				p.x = np.x;
				p.y = np.y;
				return true;	
			} else {
				return false;
			}
		}
	
		
		throw new IllegalStateException("panic");
		
	}
	
	public static void printMap(char[][] map) {
		for (char[] row : map) {
			System.out.println(new String(row));
		}
	}
	
	public static Point findRobot(char[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == '@') {
					return new Point(j, i);
				}
			}
		}
		throw new IllegalArgumentException("Robot not found in the map.");
	}
	
	public record Maze(char[][] map, char[] moves){};
	
	public static Maze parse(String input, boolean scale_up) {
		String[] lines = input.strip().split("\n");
		
		int moveStartIndex = lines.length - 1;
		
		int height = (moveStartIndex - 1);
		int width = (moveStartIndex - 1);
		
		if (scale_up) width = width * 2;
		
		char[][] map = new char[height][width];
		for (int i = 0; i < height; i++) {
			char[] line = lines[i].toCharArray();
			for (int j = 0; j < width; j++) {
				if (scale_up) {
					
					if (line[j/2] == '#') {
						map[i][j] = '#';
						map[i][j + 1] = '#';
					} else if (line[j/2] == '@') {
						map[i][j] = '@';
						map[i][j + 1] = '.';
					} else if (line[j/2] == 'O') {
						map[i][j] = '[';
						map[i][j + 1] = ']';
					} else if (line[j/2] == '.') {
						map[i][j] = '.';
						map[i][j + 1] = '.';
					}
					
					j++;
				} else {
					map[i][j] = line[j];
				}
					
			}
		}
		
		char[] moves = lines[moveStartIndex].toCharArray();
		
		return new Maze(map, moves);
	}
	
	static String sample =	"""
							##########
							#..O..O.O#
							#......O.#
							#.OO..O.O#
							#..O@..O.#
							#O#..O...#
							#O..O..O.#
							#.OO.O.OO#
							#....O...#
							##########
							
							<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
							""";

	static String input =	"""
							##################################################
							#.O#..O#.....##.O.O.OO#..OO..OO.......O....O.O.O.#
							#.....O.#.....O.#..OO#....O#O..#OO#.#..#OO......O#
							#.....OO...#..O.O.........OOO#..O...#O...O.O...OO#
							#..OO....O.#...O.O...O....OO............O.OO..O..#
							#OO.OO...O#.OO....#....O.OO........OO.....#..O.#.#
							#OOO...#....OOO...OOO...O.O.....O..O...O..O.O.#..#
							#......O.#O.....#OO............OOO...OO....O.O...#
							##....O...#.OO.##.O..O..#.#...#.O..#.....O..#....#
							#.O..#.#...O#..OO.OO.O..O..O.O.O...O#..OOOO......#
							#.............O.O...O..OO..#...O.O..O..O....O....#
							#.O#O.OO..O....O......OO.............O....O..O.#.#
							#....O....O.O..........O.##..O.OOO...##O.O.O.O.OO#
							##...O...#......#.#..O..O..OO#O...#...OO..#OO.O.O#
							#.OO..OO.#.##.OO....O.O.#.OO...O.O.......O.#..OOO#
							#.O#OO#.O.O..OO.....O....OO.#O..O.#.....O..#.O...#
							#.OO........O.......O.O...O...O.O#......O.O..O...#
							#....O...O.OO..#...#.O.#..#..O.OOOOO.....O.#.O.#O#
							#.O...#O..#.#.#OO.O...O.##O.O......O.###.O.#O#...#
							#.#O...........O.........O...OO.OO#..O......O.OOO#
							##.....#O..#....O.O...#O.#.O..OOO#......O.O......#
							#O.OO...O..O#....O#O.O....#....O.#.O....O..#.....#
							#........#.#....O.O.OOO......OO.#....#.....O...OO#
							#O...#.#O..O.OO..O...O.#..OOO.OO....#...O.....#..#
							#..O.....OO#O.O....#O..#@...#...#.O....OO....#.#.#
							#O.O.#......#O....OO.OO.OO.#..OO.#OO.........O...#
							#OOOO.##...#..#OO..O...O..O#O....#.OOO...##....O.#
							#.O............O...OO.#........OOO.....O.O..#..OO#
							#......O..OO.....OO.O.O...O....O.O.O.#........#..#
							#............O#.O.O....O.O.O.#OO.O.#....OOO.....O#
							#.O.#..........OOO...O..OOO...#.....O.........O..#
							##OOO.O....#OOO.O...O....O..O...O.O#O.OO.O......##
							#OOO##.......OO...#.........OO..O....O..#......O.#
							#OOO....O.....O.#.O..O.#.......O....O.O..O.O.....#
							#.....O....O.OO....OO.OO..O.OOOO..#..O.O..O.O.O.O#
							#O..OOO#....#.#..OO.O.#..O#OOO.O...O.............#
							#OO......OOO..OO..O.O...##...#..#O..O....O.......#
							#...#..O.#O#.O..O....OO.O.....O#.O.....O..O..O..##
							#O....O..O.OO#.....O#O.#......OOOOOO.O.#OO...#O..#
							#OOO...#O.....OO#O..OOO#...#.........O....O..O...#
							#O.#.O.OOOO....O#O...O.....##.OO....O...O..O#.O..#
							#.OO.OO.......O..O.O.......OO.#OO..#OO..O.O..O.O.#
							##O..............O..#O.....OO.......#O.#.#.O#....#
							##...OO..#.O.......O..#.O...#..OO.O..O...#...O.O.#
							#..OO#....O.....O..O..O#...#.....O#...O...O..O.#.#
							#..O#..OOO.O.O.....OO....O#O...O.O#....OO........#
							#..#..O.O.....#...O#.O........O..O.OO.#O#..OO.O..#
							#...........O..O....OO..O.......O..O#....O...OO.O#
							#.O.O.O...OO......OO....O..OO.....OO#.O.O...OO.OO#
							##################################################
							
							>>v>>^<><<><vv<<v><^v>v^<<>vv<<^<<>v<^<<>><<v>>^><>v<v>>>^^vvv^^<v<<^<^<^<^^>^^>^<^v^<>vv><<>v^<^<v^<v<^vv^^<v^<<v^vv<^>v<^<><v>^<v^v^<>>>><>^<v^<<><>>^v>v>>>vv>v^^^>vvv><^^>^v<v<^<v>vv^^v<^>^v<<v>^>><^<<vv<^>><v<>v>><<<^>>v>^^>>vv><<^>^v^^^<>vvv^<^^>^^>v^vvvv>^>vv>v<vv<v^<^^>>^v>^^<v<^><>>v>>>^>^v>^^^v>vv^v^><^^^>>v<^<>v<^^<<>>v>v>^<<>>v^><<vv<<<^^<>^vvv<^<^>>>^><v>>>>^>><<v>^v<>^>^<^<^><<<>v<<>vv^^v>v><>vv<><^^^vvv>^<>vvv>v^^vv^^<^>v>><<<^<^><^><<^>v<^>><>vv<<v<^^><><>^<<v<^^^v<^v>^>><^^^<<^^<^>^^><^vv>vvvvv<^v>>^vv>vv>^vvv^^v><<<>^vv><>^^<^v<v^^v<^<^>^>>>><v<>v><v<><>vv^<^^v^^vv<<>><>^<v^v<>v^^<^><>^>^^vvvv>>^^>^<<v^v<<>v<v<v><<v>v<>v<<<^<^<>>vv<<<^v<>vv>v><v<^vv^><^><^^^^<<>vv>v^^^>>^>>v>v^v^<>>^<>v<^^v>^^vv^>v^v^<>>>>vv>><<^>^<vv>^^^v<^v>v^v><^>vv<>>><v^>><>>vv<^^<<><^v<<^<v<^<>^>^>^>^v<<^v>v<<><><v<><>v<><<><>^v>^v<><<^v>>^^<^^v^^vv>v>^>v>>v<^vv<>>><^><^^>v>><><>^>^<<v<vv^vv<<vv^^vvv^><v^>vv<<v><><>vvvv>^vv>><>^^v<><>>^v<vv<v<>v^>v><v><<v>>>>>><<>>v<<>vvv^^v>v^>v<>>>><^v>v^><><<<>v^^^><^<><>^>><>v<>vvv>v>>>v<><vv>>vv>>^v>>v>^<^^v>>>^v^<><^<^<<>^<<v^<^^>>><v<<>vv<^v>v<><^v>^vv>vv>^>>>v><<<<>vv<^>v<^^^<>^^vv<><<>^<^v<^<<<^>v^v^<^>>>vv^v>><vvvv><^v^>v^<>><>vvv^<<<<>>v^v<vv><v^vv>><vvv><<>>><>v>vvvvvv>^>vv<v<>^vvv<^v><<vvvv<>v<<><^v^^<v<v<^<^v><^v^v^><>v>><^^<>v<>v>><<^>><^v^>>^^>vv>v><><^>>^v<vvvv^^<>>^>v<v^<v><^^<<^<vvvvv>>>^^^v<^^<<v^vv>>^^v<v<<^<<>v<v<>vv>^<>v>^>>v>v><><>>^<>^v^<><^v^v<<v>>v<>>^v>><><<>v<vvv<<^<>^><<>^>v<<v>^<v><><^>^><<v^<^<v>^>v^><v>>v<v^^^vvv^>>v^<^vv<>><v><<<vv<>v^<<^v><><^>v>^vv^><v>v>^v<>>^^<^<>^v>vvvv<^<v>v<v<>^><v^^^>>>vv>v<^^>v<<><^<<<>>v>>^<<^v>^vv>v<^^^vv>^<<^v<<^v^vv^<v<<>v^<v>vv><^<^>^^<vv^^<^^><^><<v>>>v^^v^>>v<^^v>^>^<>^<>^>><>>^^^v<<^^<>vv^^v^<^>vv<><^^<>>>v<<<^<v^vv<^<vvv><^<<v<<>^v>v^^v<v>>>^^><>>^v^^><<v<>vv^^vv<^<^<v>v^^^^>vvvvv^<<>^^^^^v<^<^^<<>>^^<<<<v<v<<v<>^v^>>><>^<>^<>^<><vv<^>v<^^<v^^^<^>vv<v>v><^^>vv<^v^^v>^<<^v^v<<<vv^v>>>^^v>^<v<<v^^><>v>v^vv>v^^v^^v>^v<><v<><<v<<^^>v>>>>^^<<<<v>><v<><^>v^^>^v^v>v<<>v^>v<>>v^<><>v^v<<>v^<v>>^<<<^><<v^^^^v^v^<<^v^<<>>>^>v^v<^>><<^<^^^<<^v^vv<^<v<<vv^v><<><>><^v^>v>^v<<<v^<^<^<vvvv^<<^<^<<>^<v^^<^>v<>><>>>vv^>><vv><v>v^>v>^>v^^<^^^<vv>><vv<^^v>^><^<<^><^^^^<<^v<v^>vv>>^v^v^>>^>v<v^<^>v^^vv^<><<v<<<>^>>^^v>>>v><vv<<^>><<^v<^v^><>>^<>vv>>^^^v<v^<v^<v<v^^<<v<vv^<v<^<^vvv>>^v^v<<<^^<<v^<^<^vvv>^>^vv<v^v^<>>>^><<<v^^>^<^>v>^>^v^><v><>v><>>v<><>>^><^><<><>^^>^<<<>><^v^^^>^>^^>^<^>^><<><v<vv^>^^^>^^^v^<v>><<^^v<>^v>>v>v^>^^>^>>v^><^^^^^<<v^v^><<^><v^v^>vv<v<v^v^<^^^>v^>v^^><<<^<<v^^<vv<v^<^<^^<^v^^^>^<>>>v^^^^<>^<vv<vv>v^^>>v^v<<<<<<v^>v^>^^^<<vv^>>vvvv>vv>>v<><>^v>^^vv><<<v^<<v^v>^vv^<<v<v<^><v<vv<^>vvv<<vv^>vv<>>^<<vv>>^^<<>>v^^^^<><v>><v^^>^<>>^<><^<<<vv^<<>^^<^<>^>v<>v<v<<v>^v^>^<^vv>><>^<<><^<<>>^^^><v^v<<^<vvv^<>vv>v^<^><v^v^<>v^v^>^^vv^^^>>^<v>^<<v>^<^<v>v<<v<>v^^<>v<>><^^v<>^v^<>^v<^vv<^vv^<>^v<<<v><vv<^v<v<^v>v>>>^v<<^v^><^>v^v^vv<>v<^>^<^<v^<^^<<>v^<<vv>v<^>v<^>vv^>^><<<<^vv^>>vv^><<>vv<v<vvv<<>v>^v^vv<v<^v^>><v^>^>v<>^<v>vv<<>v<^v>>v^^><>>^<<v^><^>^<<^>vv<<v<<><>vv^<<>>v^^<<>><^<>>^^>^>^>v<^^><v<^<v>^v<v>v^<<^>vvv^<v^><v>>vvv^<<^<^^<v>><^v^^>^vvv^v>^<^<<v<^<<><v<<^<v^>vv><^<>><v^^<^^>^>>vv<v^<<<^>vv^>>>v<>^^<v>^vvv<^v><v<<^v>^vvv<>^<^<<v>>v>>v>><v<v^v>v>^<^^><>vv>v^<><v<^>>>^^<<>v^>>^v>^^^vv>^v^>>^v<>>><v>^<^v>><^>v<v<v>^>^v<^v<v^vvvv^^^<v>>>v^^^^v<>>^v<v^^^<^^>vv<^^v^v<^^<<<vv>vv^<><vv><^<v<<>v<><>v><<v^vv<^>>^^v^<v<^<><^<vvvv<vv<^v<^^^vv<><^>vv^><>vv>^>^v^<vv<<v<v><>>>v^<^v>vv<^>^^>v>>^vv>><v<><^^<<>>^><>v<^^^>v<^<v>>^vv<>><v>^^^<^v><><<<<>>v^<>>><^<^^v^<<v<^<^^vvv^<vv>v>><vv^>vv<v<vv^<<v<<<vv<v<>><vv>^<>^<v<<>><^v<<^<>vv><>^^>><><<^>><<<<^^^^v>^^^>v^v<<><<>>v<^^>^^^><^^v>>^^>v>v<^^>^^^<^<^<^^v<vv<^>v>^^<<>>^v<<<<<^<><<<v^>>>^v^^<^<>><vv<<<v^<<vv^^<vvv^v^v^^^^vv^^v><v>v>^><>v<<<<^<v<><^v<>^<>v^^v><<^<<^><v>^vv^<<^^^^<<^><>^v>^<<v^<^v>v<^v^<v^>v<vv^vvv^>^>>^>><v<v<^<^^^<<<>vv><><^^v^><>v<v<<^v>v<^<vvv^>v<^>^v^>v<^<^^^^>^>><<v^^^^>^^><>>v^><<^<><>^v>>^vvv^^v<>^<^<^<<^><vv>^v><<><<<<<>v>v>^><v^v><<v<>>>><^>vv^v>v<>^v>^<v<<^<v<^<v<>>^><<v<><^^^<^^>v><>^<<^v^^v>v><><<^^^^^<<^<vv<<^v>>v^>^<v^<<<^>^>^vv>^^><^>v>v><<<vv<<^^>^><^^<v^^^>v>^<<>>^^^v^><vv^>v^>v<<^>^>v<<>^>>>^<v<>>>^<<<>v<<<<<>><^<<^>^^v<^v<><<v>v><v<>^<<^v^^v^<<<^>><v>>v^v>^v>^<>vvvv><^<<v^vv^^<>>v>^>vv<^v<v^^<^^<^>>>vv>^^<vv^v>>vv<^<^>>^^^><v<><<^<^<<>^^^<^vv^^v>^>v<<v>>v^<v^<vvv^v<v^v>>>v>vv>vvv^>^<^vv>^><<^v^><^vv><<<v>^^v^>^><v<v>^v<^v^>>><v^v^^<<>v^v^^^^v><^<<<^^>><<v^><^>v><>vvv^<^>^><>^vv>v^v^v<<^vvvv>v>v<<^vv<v<vv>v<>v^^<^v^v>^v<vv>^v>v<>>vvv<<vv>><vvv>^<>v>><vv<<v^<<v>v<<<v<^>^^>vv>>^>^v><^<<>v>><<^<v><vv<<vvv^^vv<^>>v>^^>v<<<>v<><^v>v<^<<v^^<<^v^<>^v<>>>^vvv>^<>v>v>>^<v<<v<^>^^v<>^<vv^^<>vvv>>vv<<^v>><vv><^^>v<^v<>v><<>v^>^^<<<v<<<<^vv^<>^^>v^v>>v<><^<><^^><^^v^>>v<v<>>><>v<v<v^>v<<v^^^<^v<v<v^>v>>>^>>v<v>v><v<v>^^v>><>>v><>^>>>^<<vvv>v<<v^^<>v><<<>>v<>v^vv><^<>^v<><>v>vvv<><>>>><<<^<^v^v>^^v^<vv<vv^v^>>><<<>^v^><>><<<>^vv>^v>v>>>>^v^>>^^<v>>>>^^>vv^v^<^v<>v>vv<>v><<><vv>^^^^^vvv>>>^v^vv<<<v>>><v>^^vvv^<<^>^^vv^>>v<^>^>><vvv>><v<^v<<<v<>vvv>v<^^vv<>^^><<<<<>^^^<>><>^<^<><v<^^^<v<><<<v^^>^^<^><<vv>v<vv>v<v<><>^<><^^^<v^>v^v^^v^>>>^^><^<<^v^v>^v^^<>^<^^v<vv>^>><<>^v<^>vv<><><>><^<>v<v<>^><^<v^v><<v^^><<<^^^v^^^>v^<>vv^^^^^<vvvv<v>>^>>v>vv<>v<^<v>v>vv^>^vv<^<^^<^v>^>>v<<>v<<>v>^<<v>v>>^<vv<>^vv^<^vvvv<v<<v<^^>^v^^<>^^>^v<vvv<v>^v^^<^>^^><<v<<><<<^<><><^^<>^>>>><v<>>><<vv^^v><v>v<^^>><vv^>vv^^<^^v<<<<<><^^<v^v>v^v<<>>v^v^><><>^<^^<^<v<v<vv^^>v^v<<v^><>><<^^^>>^<v^^<<v<<vv^><<^v<vvv>><<vv<<^vv>>^>v>^>v>><v^vv^v<^>^>>^v<<<^^<v><v>>^>vv^<^<><><vv<v^v>><>>>>>>><>^><>v<v>>^><vv><<v^>^^>>v^^>v><>>^vv<>v^<>>v^<><<<<^^vv<>^^^v><<^>>>>>><^>>^<v^^vvvv>v^^<^v>v>v^>v<>^>^>^<^<><v^>>^^v>^>^^<<v<vv>v^^><v>^vv<^>^v<<>>>^^>>>^<^^v^v>^><v>><>^>vv<^>^vv>^vv<^^>^^>vv^vv^^v^^<v<^^<^^^v>v^^^v<^><^>>>v^<^><<^v>v><^<<<><^>^vvv<<<^^>>v^^>><>v>v^v<v>>^v<>v^>^^v^<^>><>v><<v><<<^<>><vv<v<<^<><<>>^<><v^>>vvv>^v><><^><v<v>^^v<^vvvv<>>v<v><>^>v>^<>v<>^><^^v<v>v>>^>^^<<>><^>v^><^^>>>v>>^<^vvv<>^<vv>>vv^>vv<^^><vvvv<^>>^vv^>v^<^<^v>v<>^^<v>v><<^^v^>><>>^^^v^>><<vv<<vv^>^><><>v^<^>^<><v^v<v<>vvvv<<^^>>>vv^>^vvvv<^<^v<><^<vvv<v^^<>vvvv>^>v<^^<v>v^v<^<^^>v>v^<^><><<v<<^>^<^><v^vvv>^v<^v>^<^^^<<^<v^>><>>^v<^^<>><^>^<>^^>><v<v<v><^<v><v<vv^<<v<>^v<^<<>><>^<<<v>^v^v<^vv><^v^>v>><<<^v<^^^v<>><<>>v>>^^^^<^^<^^>v^<v>^v<<<v^><^v><^><>>v<>><^<>v^v^^^<v^>^><>^v^vvv<^^<>v<<>^vv>v<^^><<<<>><^><<>^^v^v>^>v<^v>v^v>>v>>^^>v^>><>>^^^^^<^><v^v^>>^>^<<>^v^^v^v<^^v^v<^>>><>^v<^^>^vv>v<>^^v<v>><<^^v^^^<>v<^><vv>^^<v^>><>v<v<><><v>^^<^<<<>v<>v^^^v<^>^<>^^v<>^v^^>^><<v<>^><>^>^^>vv<>v<<^^>>vv>vv^<>>>>><><vvvv<^>v^^>>>>><^>>^v><v>v^>^^^<<^vv^>^vv><v<^<<^v^vv><v<<>^<<<^<v^<<>v<>>^vv<^v>^^^v^^^<v<>vvv>^<^>v>v<<v>v>>><v^<v<^^>><<>^^<>v<^v><^^v^^>^vvv^><^v<^>v><^v^v^v<vv^>^v>>v<v><^v^<^<v<<><v><>><>^^^^v<v>><vvv<<>v<^^v<^>v^v^v^^>v>^<v<^^^v>v>^v^>vvv<>^>>>><v<v>^v><vv<vvv>^<>v^<^<v<v>v>>^>^>>^v<vv<v^<<>v^<vv<vv^^<v>><v>^^<v<vvv<^vv^^v^<^<>v^><v<v^v^^v^><>v^v>>^^>><<>vv>^^^vvv>v>vv<>>^v^<^v<<^>^>vv^^>v<v<v^v><^<^>^^^v^^<^<<^^<v<v><>>v^^vv^v^v<<<<>>v^<v<<v><>><>v<>^>v<>^vv^>v<<v<^^>v^><>^v<<<><vv<<<>v>vv^v>>v^>v^^<^^<<v^<>vvv<>^v><^<<<>><<<<v^v<<<>>^vv^<<<>><<v<^<v<<<v<v>>^<<><v><>><v>^^<><v<<<v>v^><v^v>^v^<><^^>v^^<>^<^<^^>vv><^^^v<<<<>^vv^v<v>>vv<<^<^>>^>><><^>><<^<<v^><v^vv<<>>v<>^v^>>v^>vv^<^v>>><>^<^>^^<<<><v<v^v>^^>^v>^vv^<v^<^vv><<>v^>^^v^>^<>v<<<<v^^v>>v^v<<^^v><^>>^<v<>><v<^v^v<>^^<^^v>^v<<^^<v<>>v<v<^<<<^<v^><^>v^^^<^>>^>><v<>^v>vv>^v^^v<<<^>>^><v>>vv^<^<v<v^v<v>^<>^<>v^vv<v<>^v<>^><^v<>v<^<^v<^vv><<<<v<<<^^v<<v<>v>>>v><^v^^vv><vv>vv>>v^<>^>^^><>>^<^>>v><v^>v^><v>>><^^v<v<<^v<>^^^<<<^^v^>>^<v^<>vv^<v>^v<<^v^>vv^vv^><^^vv>><^>^^^<><^>v<>^v<>v^<<>^v>>><>v^v^>^><><<<>vv<^<v>>^^>^^^^^^v^<>>>>v><>v<v^v^v><><vvv<^^<>v<^><<<v>^>^^>^>><>v>v>^^<><v>^^>v^v>v><vvv^<<v>^<<<v>>v>><<<v^v>v>v>v^>^>>><>v^^^vv^v<vvv^>v>v^^vv><><v>>^v^^<>>v<v<v>v>>><>>v^<>><>>^^^^><<^>>^v^v<>>^^v<vv>v^<><^^><v>^v<<vvv^^^<^<^<vv>>^><>v<v^<>^^^^>v<^^vv<v>><<<>v<v<<>v<v>v^^<^<<>>^v^<v^^v><^<<>v><<<<>^v>vv<v<^>v>v>>v<vv>v>v><^>>^^^>^><^><><<><>^>v<v^^>^>>>>><v^^><v^>><^^vv<<<v>v<>^>^<<v<>^vv<>v<>^<v>v>^^<^v<<v^^><>>^<<v<>v^<<^v>>^>^<>v<>^v>v>>>^><<^>><v<^^>^>^v^><^vv^v<<v^^^<<^><^<<<>^^><<<>v>^<<v>^<<<>>>><vvv>>>vv^<<^><v><^<v^v<v^v>^>^v<<^vv<^vv^^^vv^<^v^v<>^v><^^^<v<>^^>><<^v<>^<vv>^v>v^>>^^>v>><^v<^<>>><v<v^^<vv>v<<<^<>vv<^><><<>>><^v^<^<vv^>>>>^>v<v<<v>^>^><^^^<^vv<>><<vv<^v>v^v>vvv<^<><v>^v<^^^vv^^^<^v<<<>>>>vv^^^vv><<v^>^>v^^<^<<<^>v<>^<<<<^>><>^><<v<<<v^<<v<vvv<<^^><>^v^<<^^<^<^^v^>><<^>>>vv^vv>>v>>v<<v>vv^<^>v><v>v^>^>>^^v>>v<><>>v><>^vv<^^^>><>v>vv<vv<<vv<vv<<v>v^><v^>^<v<^vv^<vvvv>>>><<^^<^^>v<<<v^vv^<>>>>>^v><><<<><^^v<vv^v^v<>>v^<><<^vv^><vv>v<v>>vvv<^><v>>>^><v>>^^<vv^<^^^<>>>>>v^>vv^<v<>^^^^>^^<<^v>v<<<vvvv<><^<<^<>^><^>><>>><>>>^<^^^vv^>>^<><^vv<<<<v<^>^v>^^v^>^^><v<^v^<^>>v>>>^><<v^v<v^^^^v>>v><^<>>><v^v^v<^^v<<v<<><<^vv<^^<^^^vv><>v^^v>^<v^><<>>vvv^>>^v>><<<>^<>^^<<>v<^><^<vv<<>^>^<<<v><^<<^^vv<vv>vv^^>^^^<>^v<><^v<^>vv>>v>v^^<>v^v^>^^><>^vv>^<v^^<^^<v^^vv>vvv<<>v^^<>vv^v<^v^><<v^<<<>^>>>vv<v>v^><^<^^><^v>^<>v>vv^>vvv<^>v^v>>v^v>^^^<>v^<vv^v^<<>vvv^>^><><>^<v^v>><vv>^v<^vv^>^<<>^^><>v>^v>^>^^<>v<>^<><v<^<><><^v>>v<v>>vv><^<>>^^^>>>>v<vv>^vv<v>^<v<>v^><v><>v<^vvv><^v><<^vv^^>>>v>vv^>v<^v>vv^^v<^<v<<^<^>v<vv<>>v^^vv^v^^^^><v<>v<^>>><v<^>><>^^^v^<^v^^^>v>v>>^v^^<^><^^<vv<^^<<^v<^<<<^vvv<><<v><<v^<>^>v<^v><>><><<>>v<^^<<v<<^v><vvvv><^<v<>vv<><vv^>>^^^>><>>^<<<vv^^<>>v^><v<<>>^<>>>v^^^v>><>vv^><^<^vv<<v<<>>v>^v^v^v<<>v<^v><v><<v^<>^^^v>v^<<><^v<vv^vv>>^^vvv^>>^>v^^vv>^vv^>>>><^>v<<^v><<>^v>v>v>>vvvvv<^<<v>>>vvv>>>^<^<^^^<^<><vv>^>>>vv^<^>>><><^v<>v>><<<<>^<><vv^>v^>>^v^<<<^v^v<^v>>^<>^><<>^v<<v^<v<^<<vv><<>vvv^v><^vvvv<><>>^^>>>v<^>^^v><<vvv^>v<v>><^^^<v<>>v<>^v^><<^^<^<v>^>>>^v<^^^>v>^>v^^<v>v>vv^v><>v^><<<^^v^v>v>>><<vv^v^vv^<^>^<^v<^v<^^^v<<v>>v>^^<<^<<^>>><>>>>>v>v>^^^^>^^>>^><>^<v><vv<<<v><<^<><><<>>^^<v><v><<<^^<vv^v^>>^v^^v<^v^>>>^><^><>v<>v<<<^><><vv<<>v^<<v<>^^^v^vvv>v^<v<^^>>><>>vvv<<<><vvvv^<<<^<^^>vv^>><v>vv<^><^^v<<>v>>v<>v<><^<v^v<<^><><<<v<>>^v>>^^v<^><>><<v<^^vv<>>v^<>>v<<^^<<>^><>vv<^<>^v^<<v<v>v><^<^<>>^<>><vvv><<^^v<<<>^<><v<<^vv<>^^>>>^vv^^^v<>^<^^<^<>v^>>>v<v>>>v^^>^^^<><v<vv<^^<><^v^<<v>>v>>^vv^^>>>^^<v<v>^<<^<>>><>^>>^^v^v>>v>>><>^v<>^^^>^^><>v^^vvvvv>>^v<^<<^<><v<v^v^<>^^vvv^^v>v<>^><^^v<v><<<><v<vv^^v^^>^>^^^vv^<^^^vv<>^v<>v>^<><^^<v^<>^<v>>v>vv<v<^^<>v^^^<v>>^^>v<^>^<^><vv>v><><v>v<^vv>^v<vv^^vvv<vv>^<>>>>v<<vv^<^^<<^^><vv^^>v>^<<^>^>><<<v^><<<><<<v<v>v>^>v><v>>^^<<v<<<^^v^><><^<v>v><^v>v<>v<<^><^><vv^vv^<>^v^<^^^v><>v<<<>^^^><<><^^>^>>^>vv<^><^>^^<^^v>vvv^v>^^>>^v^v<vvv^v^<^^vvv<>^>^^^>^^>vv^<<<<v^><v<^><>>^<^><^<v<<vv>>v<^>v><^<>^<>v^v>^<v><^<>v>>vv><>v<>v^^^vv<vv^<v><<^<<<<v^^^v^>>>vv>^>>vv^^v>v<^^><^><>^>^<^^><v<>>^>v<<v><v>v<<><>>^v^vv<^v>v><>vv>^><<>^><vv>>v<>v^><><^^<>>><^<<^>v>^v<v<^^>><v^^<vvv<^^^><v>v>^^v<<v^^^>>>v>>^><>>v>>^<<<^^^<^^>v^^<^>^><>vv>><>^v<<vv^>v><>^v><v<<><<^<v^v^<<v<v<^<^v^<^>^vv<^>^^>^<v>^>^v>>><>^vv<v<v>><<>><v<^^<v>v><>>v><<vv^v<<<>><v^>vv<<<><><vv^^^>^vv^^v<<><^>v^>v<<v^v<vvv^>^^>vv<^vv<^<<^<<v^v><v>>>v>^<<<^>^v>^vv^<v^v<v>vvv^^<v<^><<^^v^>^<^^<>^>^^<v^^^v>>><^^>v^^>v<<v<>vvvv<>^>^<<vv^<<>><^^<>v^>^v<^v^>>>>>>^<><<v<<<v^v^>><^^^<><v^>vv^vv>^<<>^^>^v^^v^><>v<<^<>>>v<<^>>>>^<>>><<v>>>><<v<>v<>>v><vv<<v^>^^v><^<><<v^>v^>v<vvv^>v>v<vv>vv>><v>^vv<v>><<<<vv^><<^>^v>^<^>v^v^>^^>^vv^v>vv^^^<<<v<<<<v<vv<<v>v<<v>v><v<>^v<>^v^>^<<>>v<v<vv><>>v>^><<vv<>vvv^v>><v><>^^<<>v^^><<^<>^>>^<>>><v<vv^>^<^>>v^>^^^<v<>v>v^<>><>>><^<vv>vv^v^><^<<^>v^^>^<>>^vv><>^v^^<><>v>v>^v<^<^<vv<v>^v>><>>><v><<<><vv^^>v>>^vv^v<>^<<^^v>v>v<v^v<^>>v>>><vvv<v>>v><>^v<><v^<<^>><v^>v<<<<><<^<>vv^vv<>>vvv<vvvvv^^><v>^>^^vv^v<vvvvv<>>v^<^<>>^<^v><<^<<vvv>>>v><>^v<^<vv^<<<><v<vvv>vv<><^<vv>^<v<^v<^vv<v<<>>>v^>v<<>>v>^<^>vv^>>^vv<>v<^v<<^v<v><<>^^<v^>^^vv^^^v><v><>^v>^><<>>v><v><><>v><>>v^^>>>>>^v>^>^v<>v><^>vvv>>v^>^^>><v><^^v>^vv<^><<>^v><>>><><>^><>^vv<<v<<>>^v^<v><<v<<^^>^^<<><>v><>v^<^^>><<><v^^v>>vv<vv<^>v<v>>^v^>^<^v<^v<<>>^<>vv^>v^<^>><vv>^>^vv<><v<>>>v<v^^<><<^^^<<v>>^<><><<v<<v^vvvv^<<<v<<><v>><>^^v^>>v<^^<<^vv>^<v<^v>^><^^<>^<^^>><<<><v><^><^<<>^v>^<><<^^^>^^<>v^<v><^^>^v<^^>v>><>>v<<<^<<vv<^^<<<v<>^v^vv<vv^<<>vvv^<^<>>v^>^>^^v><>v^><<vv^>^^v^v<>><<>v^>v<^<vvv^>vv^<>>><v>v<^^v<^v^v<<^<vv>v><>^>v>>v<^^>>v<^><><<v^>^<<vvvvv^<>^<<<v^<v>^^vv^<^^<<v^^<v<<v^vv^<vv^<v^^^v^v<v^<<>>^<v>>><v^><v^>>^^v<<>^>v^vv^>><>^<><<^<^<v><>v>v^>v^v<^<<<^<<><^v>^<>^>>>v^v>^<<>^>>v>v<v^v<v>>^<^v<>vvv^<<vv>>>>^^vv>^^>^v><<<<^<>^>^>>>>>^^>^<<^v><vv<<^>^<<<^v>><><^>v>>^<<v^v<>^<<vv^>^>>>><^vv>>vv^v^^>>^^^<vv^^>><^<^^^>><<<<>><^<^v^v^vv<<>v><>^^>>><^^v<vvvv>^><^<v<v^v<v<<>v^v^>>^^vv^^vv><>vv>^v><>v^>vv<v^>^>vv>^v<>>^><v><<v>>>^>vvv^<>>^v<^><vv><v<<>^v<vv>v^^v><<>^>v>^^<v><><^>><v^v>^><<v><vv^>v>^^vv<v>^><v>v<<<<<^^>^><><v<v^<>vv><^>>>^>^v><<<>v^<^^<<>><^^<^<^<v^v>>^v>^><><^>^^<v<^<<>^>v<<v<>v<<vv>^^v^v<v^v<v<vv>v<<<<^v>vv>v^<<^>><>>><^>>><^^<>v^^<>>><>vv^>>vvv><><<><^<v<vv^v>^>v>^>v>>^><<^v<^<<><^<^<^v^v<^^<<^<>^<<<^vv><><v^<<<^<<>>^^<^^<v^<^^>^^^vv>>vv^>v><>^v>vv^^>>>v>v^v^^^><>^>v>vv<v>v^>^vv>^v<^^^^>^v<^v<<^><^^^v^v^<v>v^<^><<^<v<>>><>>>>>>v<v<>^>^>vv><^^>^<v<^^>vv>v^^v^<<vv<><^<^<<^v<v^>^><<>vv^vv^v<<^^v>^>>^>^>vvvv>>v<^<<><<>><<<<^^^v>v>vvv<^<v><><><^>^v^><^^^^^v><v>>v^^vv^<v<^><v<>^><^<^<<<>>><>^>>^<<v<<v>^^^>^^v^^v^>>^>vv<<><v<^<<<^vv<v<<>^^v>>v<v>^v>>v<>v<^v<<^v<vv^><^>v>v>vv^>^^<<>^^^v<<^>>><<<v>^v<<^^<<<^>v>>v>^^v^^v^^v>^^<<^><<<v>v><<>>vv<^^<^^v^^v^^<<<<<>^^<v<^><v^<v><>>v><>^v<v^v<v<^>v<>v^v^<<<^^>v>vv^v^<<^^^^v><^^^<<>^^<><^^v^^v<^v>v<<<><^<^><v>vv<<<^>vv><^vv<<>^<vv<^^vv><^<<>^vv>v>>^><^v^vv<^v<<<v^^<>^><<v<vv<^<><^^<^<v>^<>>>^^^v<v<^v>v^<v<vvvv><><^^v<^>v^v^><<>^v>^>v^^v^^>vv<vv^<^>vv>v>^v>>^<<v<>><v<^<>^^<>><vv^v<>vv^^<<v<v>v^<^vv^^<^>>^^<v^^<^vv>>vv>^<>v<>vv>v<^^vv^<<v^<<>v>>v<>^><>^v>v<<<<<<^<v^^^>v^^<^><v<>^^<^<v<^<<<v><>v<^^v>^vv^>>>>^v>^<<>vvv>v<^^<><<>vv^><^v^v>^<v<v<<<v^v<^v<^v<vv>^^>v>v<v>v>><><>^v>v<<>^vv<><^^<>v<v^^><^>><vv<>^v>v^v><v>^v^^<>v^^v>^^v^vv<>v<<>>^v<vv<<<v^^>v^>^^v<^<v<^v>^>v<>^^^v<^^<^><><>^>v>v>^<vv>^<^<^vvvv^^v<^^><<<^>^v<<<vv^>v^>^v<v>>><v><v^>v^><^<>><^vv<^>>^>^^<v^<>^^v^^v^vvv>v><v>>^<<>^<<v^<><v^>v><>^>^<^<>>v<>v^^><v><><<^^>>v<v^vvv>^v>^^^>^vv<<v><^^^><<>v<<v^v<<^^<<v<><^>>>v>vv><>>^v^v<v<^vv^v>>v<<^<^v<v^^>vv>>v<<^v<><v<>>>vv^<v>v>><>><<v^>>^v>v>^>>^^^^<v>><<><^vvv^>>^>><vvv<vvv><<v>^^<<^<<<>^vvv><v>><<v<v<><v<><<>^><^^>^<>>^>>>>^^v<>>^^^v>>vv<>>>>^<v^>><^v^<^^<v><v^>^vvvvv^v^^><<^v>vvv^^>>>^<<>^<>^^v^^>>^>>v>^>>^^^v<^<>^^vv>>v^v>^^^>v<<^v>^<>v>^>^vvv>v^<^<^<v><>><<v>v^><<<^<^^<v^v>^v^<<^>vv^<<v><^<^>>>^v>>vv<><^>>>>^v<<><v<<<>vv><^v<^v<v^^><>>v>v><^v<^v<><><^v>^><>^^^<^^vv<<<>^>>><v^^>v^<>vvv<>^<v<<v<^<><v^<<^<^>vv>><^<^>^<v>v<^<>v>><^^>>v><<^^>>>>^>^<v<vvv<<<<>^^^<^v^^<<vv>vvv<>v>>^v>>>^>v^v^^>><<><vvv>>^^^vv^^>v>v^v^>>v<<<vv>^v<vv>>v>v>^<<>^vv>^vv<<vv>^vv^^>>><><>>v^^>vv^^<<<><^v>v<^^^<v^>^>>v<^><vv^<v>v<<v>>^>^^vv^v^vv>^^>vv><v^v>>>^^^^>>v>>v>^>^^<^^^<>v<><^v^^v^vv^>v<v^^>^v^><>v>v^v<v><^^v^>v><<^<>v^v>>^^v<>^v>vv^v^>v<><^^>>^<^><^^>^>^>>v^^<<>^><>^<^>><<>^<<>>v^^v<v^v>v>>^^<<<v<<>>^>>>>>^<<><^>v<><>v^><<<><>v<<vv^v^>>v>><>><^<<vv^v>>^^^><^^v^^>^^v>>^^><vvv<^^vv<v<^v>>v<v<>^><^>>^^vv^^<^><v^^v<><^>>v>^^<<><^^<>><^<>^v^v<^v<v>>v><>>>^^vv><^vv>v^>^<^^<^<<^vv^v^<<^^<>>^<>^^^>><v><<v><^<>vv^>>>><vv<>v>v^>>>^v^vv><^<v<>^v^v^<^v>>^<><>>^v>v<<^v^vvvvv<v^v<<><vv>v<><<<^>^<<><v<^>^<<<v<>>>v<<<v>><<v^<^<^>v^>v<<<<v^^><vv^<>v<v^^<>v>><^>v<^>v<v>v<v>v<>^<v<^<<v><vv^^>^^>>^vv<v<<^v^^>^vv^v<<<<v<v^v><v>>v^<v>^^^^v>v<^^>^<>>^<vv<>v<v<vv>^v^>v<<<>v^>>v>>^v<v^^vv<>v^^<>>v<<<^v<>^>^<^>v>>^^^^><<v<<><<>v<><<^<v<><>v>vvvv<vvv^^<v>v^><v<<v>>v>>v>v<v>>vv<^v<<^>vv>vv<<v><>v>>>><^><>vv>v^<^>vvv><>>v><<>^>^<v^<<>>>>>>^vv^<^<><>>><vv<>^v^<v^v^>>>v^<<^^^<>v<^>^>>^^<>^^>vv><^^^^><<>v><>vv<^<<v<>^<><^><vv>v^v^>v<>>v>^>v^v^<<^>v<<<>vv^<^<v<><>><^v^^>vv<>>^<>^^^^>>v^<<<><^^v>^^<<>^<>>v>^<vv^^^v^<>^<>v<<<^^^>vv>^<^>v<><<v^^^^<v<<<^vv^^<<v>^>>^<<v>v>v>>>>v>v^><vv>><vv>v<><v>>><^^>^v<><<vv>v><<v>v<>vv^>^<vv>>v<<^^^<<>^><v<>^^v>v<>>v^>><>^<>^>^>v>v<v<v^><><vvv<>>>^><vv>v><<<v<>^^>vvvv>v>^v<><v<v^vv^>><<><^<<<vv><v>><<>^v>><^v^v<v^v^vv^^>vv>^v<v^^>vv^v^v<v^^^v<<<^<<vvvv^vv^^^v>>^vvv<vv>^><^v^v><^>v<^<<^vvv^^><v>^^<v<^vv><^^v<><v<^><<v<v>v^>^^<>^^<v^v<^^>>^v><<><<<vvvv<^v^>><^<><^<^<^><^^v<><>^v^^>v<>vv^v<^>^vv^v><^<>v^<<vv>>><v>><v>^>><<<><^^<v<^vv^<^v<v><><v><>^^><<v><^^v>><vv>v<v^>v^>^v><^^<>^v^>>^><^<v<<>v^<vvv^^<^<<><<><<^^v>v<<v>>v<^<>^v^><<<<><^><<vv><<<vv<>v<>^v<<^>^>v>v^><^>>vv>vv<<v<>^^v<<v>>^>><>>>vv^^v^>vv^v>^<<<<^^<vv<>><><^>^<vvv>v>^<v><>^vv>vv^><v<v<^<^^v<^^vv<>^^<v<>v>^^<v^>>^<<^^^^>v^><^<v^>^^>>^<>>>><^<^<v^v><<v>>>v^vv<^<><^^v^<^v<>v<^^^>v^vv^^v>><<vv^v>^<^>v<^>^^^^>vv<vv<>^v<^>^<vv^>vv>^v>>><>><<vv<^<<^>>^<v^><v>v^^vv^>><<>^v^>vv<<>><vv<><<>vv<v<>v<<<<^><<v<>><^v><<v^^^>vv><vv<<v>v<^^><vvvvvv>v^>v^v^v^<>^v<vv^^^<^vvv^>><<>vv^v><v><>vv>v>>v^>vvv>v<vv<^<>v>^^<^v^^v^^<>>>v<><>v>v<vv<>>v^v^><<^>^^^^^><><^>^v^^vv^>^^<<^>vv^^<^<<>^v^v<<^<>^v>^<<<><v<v<v^vvv<<^>v>>^^<>>>^>^^v^v^^<><>><v^>^<<>v>>^^>v>^>^v<^^<>^<v>^<<<^<<>>^^><v>><^^v^vv>>^<v>vvv><^v<<v^>>>^^v>vvv>v>>>v^>^<v<^^<v<^>><<v>vv>vv<<^v<<>v>>>^v^<<v>v>>^vv>^v<vvv<^^v^>>^><^vv<^vv^<^><v^<>^<>^^^v^v<v^<>vv><vv<>vv^^v>>^>vv>v^^<><^v^^^><>>vv><><>><<>^^>^<<v>^v^>^>^><^<^vv>>v<^^>v<<<<<<v>^^^<^v>><>^>>>vv^^>v^v^^v>>><<<<>^>>^<<>v<<vv<vv^v^^^>v^v>v^<<<^>><v><^>^<v^^>v><^<<v<>^^<>^<^>><><<^^<^vv<v^^>>^^>><v<vv>>v^<^>>v<^^<>^<>>vvvv><<v<>v>^>^<^<^<<><^^v^>>v><^<^>^v><^>>^<v^v^^>v>^<^>^<vv^<<^^<>>^<<<v^vv^>v>^^<<<>><<^^v>><^><<^v<^><<<vv<<><<>v<>v^^<^^v<v<v>v^><v<^^>^v>^<v>v>>>>^<^<>^<>>>v<^<<^^v><v>><><<^<>^^v>>^>^^>vv<<^>>v<^>><<v^<<<v^<<<<>vv>^^<vv^<<^v^>>v<^^>>^><^v<^v^v<^^v^v^<><><^<<v<>v<><^^<<<>v>^v^<<vv^>v>^v<<^>^^<v<^<v<<>v<v<<<>v>^>>>v<><<>><>>^>v^><<<<vvv>v^>^>vv^<<<v>^>>^<>vv^>^>><<>^^^^<>^^<^>>^^^v^vv<>><>>^^>^vv<v<^<vv><>>><v<^v><v><><>>>^>><v<>vv^v^<v<<<v<vvv<><^>>>v^^v<<v>^vv^<^<^<>^^v>>><vv<>^<><>>>^<>vv^^>^v>>>v>>v>>>v^>>>^<<v<v^<^v^^<<^<<>^^<>>>^>^>>>>>^>>^vv^^^<>>v<>vv>v^^>^v>><<>v^<v>^<v^vv><vvv^<v<>^<><^v^>><v>v<v>^<><>>>v<v^>>>vvvvvv<v<<^^>v^v>>>^^^><v^^v>>^^>>^^>v<vvvv>><<^>v<><<<vv><^^>>v>^vv^^^v<<^^>^>^>^^^^v>><vv<v<><^><<^<v>v<>>>>v<>v<^v<<<>>vv^<v<>>><<><^<^^v<v^>^^>v>^v><<v<v>>v>^<<<^><>^^>>^v>vv^v^<v><><vvvvv<v^>>^>^v>^>v^<>>^<^v><^<>v^>>>^^^><<>v>^<^^<<<vv<v^><>>^<v^<<>^^^>^>^<<v><^^<v<><<^>^<<<^v>>>^^v<>vvv>v>^v><>>>vv^v<>^>^vv^>^^vvvv^v^>>v^<<^v^^><v><<v>^><>>v^v>^^<v>^^^v<>v<<^>vv>^>><<<^^><>>v^vv><><<^>vv^<v^^^<<>v<<^>^>^<<v>^v><vv><>^^<v><<^<^^v<>>^^vv>vv<><^>v>v><vv<<^>v^>v>^>^<v><<><^v^^v<<vv<^><v<<^><v<>v^v<>v^<<^<^<^^v>>><>^v^>^^<>v<<v>^^v><^<v<<v<^<<>vvv><v^v<^v<^vv<^>>v>^v>^^v<>v^v><^<>v<>>v>>v^v<v^>^v<v><<><^<v>>>>>^v^^^vv^v>^<v<^vv<>^<^^>^v>v<v^^vvv^<^>^>v<<>^vvvv<^<^v^v>^^>^v<<>^>^>^^>^>vvv^v>>vv<^<><vv><>>v^^>^v<v<<><<>>v>>><v>vv>><v<><<>v<<^<v<>><v<<<^>>>>v<v><<<v^v^>vv<^vv>^>v<>>^><>v^<^<^^v^^>v^^>><<<>>>^^v>^^^^^^<>v^<>>^<^<v^^<v>v<<v>^<v^v<v<<>>>^v>v^^<><>><><>^<<>^><^^><^^vvv<v<<^<>^^^><^<<<^^>v<<<vvv<>^v<^<^vv^v^><vvv><>>>>^v^vvv<vvvv<v<v<v>v<>v<>^^^v>^^<><<v^<>v>>vv<>v<<^^>vvvv^<<>v^^^^^vv^^vv<>><>v<v^^v^><^v^>>^v>^vv<>v><>vv>v<<v>v>><vvv><^vv^vv>>>^^^<^v>^v<v^^<^v>v^vv^>v<<<v>v<^<v<v^>v>^vv<<vv^<<<>v<v^<v^v<v^vv^vv<>^v><v^^^<^v<^^vv^>^v^<><^^^>vv<^^vv>^>^^><><>><><<^vv><<><>>^<>>^^vvv<vvv<^^<<^^>^><v>>>^^vvv^>><<>>^><^<<>^<<^>v<v^<v^>^v>v>><^^>vv>v^><<v^<<>^>>vv>^vv<>v^>>^<>>^<vv<>^>>^><<<^>>v<>^>^>>vv^<<^^v^<v>v^v^<>^^>^><<>>><<^v^v^>><>^v^>v<>>>^v>vv^>><<v^>v>v<>^^^v>><vv<^^>^^>^>><<^<^v<^<<v>v><>>vv><v>^v^^^^>v<>^vv^v>^^v<><<<v>>^<^<<<v<<^^<<<>v><v<><^^><>v^^>v<><<vv^>v>^>v>v>vvv<vv^^v^><><<<^>v<<<<<>>vv>vv^v<<<<<>^>^>^><^v>>^^<<<<<^v^vv><^v^vv<vvv^v<^>>^^vv<<^v>v^^>v^v><^<<>^^^^<>v<v^<^<><vv>^><^>><^^>>^^^>v<>^<v>v>^v>>^v<^>>>^>><<<v>^<v^v<v><^v>>v<vv<^v^<^>^v<>>>^>>^>>^>>>>>^>^><vv>v><<^<<>><<<v^<<>v<>>^^<><<vvv^>v^><^>v<<^<>vv^><<^v<<<>vv><>v>>v<^>^<^^<<^<^vvv^<^<><^>v<^<<>>v<<^>>>>^<<v><vvv<^>>^v^<<^<^v>^><v<><>>>^v>vv^^<v<^^>>><<v^^v<<>><^><<<v^v^v><^>^<>><>vv^><^<>>v<<^>vv>^v><<^^vv<<v<^^^<^<<><>>>v<vv<v<^^v>>v<^>v<<^><<v<^^^><^<v<<>>v><^>v>v>vv<v^^<v<<><^>v>^>v<><v<^<vv^<<v>>><^>vv^>^<v>>><><v<^v<<v^^>^^v>>^>^>vv^<v<v^><<>^^vvv><>><v^<<<^>^>^<>>><v<><^>^^vv<^v>>><^v<>>^>^^^^>^v<>v>>v>v>>vv<>vvv>>v<>v<<vvv<<^v><^
							""";
}