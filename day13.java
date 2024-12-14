import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import java.awt.*;
import java.nio.file.*;

class Day13 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 480, actual: " + getCountOfTokensToWinEveryPossibleGame(parseMultipleGames(sample, 0l)));
		System.out.println("Actual phase one result, expected: 39290, actual: " + 
			getCountOfTokensToWinEveryPossibleGame(parseMultipleGames(input, 0l)));
		System.out.println("Sample phase two result, expected: 875318608908, actual: " + getCountOfTokensToWinEveryPossibleGame(parseMultipleGames(sample, 10000000000000l)));
		System.out.println("Actual phase two result, expected: 73458657399094, actual: " + getCountOfTokensToWinEveryPossibleGame(parseMultipleGames(input, 10000000000000l)));
		
	}
	
	public static long getCountOfTokensToWinEveryPossibleGame(List<Game> gameList) {
		
		long result = 0;
		
		for(Game g : gameList) {
			long[] solution = solveEquations(g.buttonAx, g.buttonBx, g.prizeX, g.buttonAy, g.buttonBy, g.prizeY);
			if (isValidSolution(solution[0], solution[1], g)) {
				result += solution[0] * costA;
				result += solution[1] * costB;
			}
		}
		
		return result;
	}
	
	static boolean isValidSolution(long presses_a, long presses_b, Game g) {
		long totalX = (g.buttonAx * presses_a) + (g.buttonBx * presses_b);
		long totalY = (g.buttonAy * presses_a) + (g.buttonBy * presses_b);
		
		return g.prizeX == totalX && g.prizeY == totalY;
	}
	
	
	
	public static long[] solveEquations(long a1, long b1, long c1, long a2, long b2, long c2) {
		long multiplier1 = a2;
		long multiplier2 = a1;
		
		long newA1 = a1 * multiplier1;
		long newB1 = b1 * multiplier1;
		long newC1 = c1 * multiplier1;
		
		long newA2 = a2 * multiplier2;
		long newB2 = b2 * multiplier2;
		long newC2 = c2 * multiplier2;
		
		long bEliminated = newB1 - newB2;
		long cEliminated = newC1 - newC2;
		
		// Step 2: Solve for y
		if (bEliminated == 0) {
			System.out.println("No unique solution for y (division by zero). Check the input.");
			return new long[0];
		}
		
		long y = cEliminated / bEliminated;
		long x = (c1 - (b1 * y)) / a1;
		
		return new long[]{x,y};
	}
	
	public static int costA = 3;
	public static int costB = 1;
	public record Game(int buttonAx, int buttonAy, int buttonBx, int buttonBy, long prizeX, long prizeY) {}
	
	public static Game parseGameInfo(String input, long prizeIncrease) {
		String regex = "Button A: X\\+(\\d+), Y\\+(\\d+)\\s*" +
						"Button B: X\\+(\\d+), Y\\+(\\d+)\\s*" +
						"Prize: X=(\\d+), Y=(\\d+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find()) {
			int buttonAx = Integer.parseInt(matcher.group(1));
			int buttonAy = Integer.parseInt(matcher.group(2));
			int buttonBx = Integer.parseInt(matcher.group(3));
			int buttonBy = Integer.parseInt(matcher.group(4));
			long prizeX = prizeIncrease + Integer.parseInt(matcher.group(5));
			long prizeY = prizeIncrease + Integer.parseInt(matcher.group(6));
			
			return new Game(buttonAx, buttonAy, buttonBx, buttonBy, prizeX, prizeY);
		} else {
			throw new IllegalArgumentException("Input does not match the expected format");
		}
	}
	
	public static List<Game> parseMultipleGames(String input, long prizeIncrease) {
		String[] games = input.split("\\n\\n");
		List<Game> gameList = new ArrayList<>();
		
		for (String gameInput : games) {
			gameList.add(parseGameInfo(gameInput, prizeIncrease));
		}
		
		return gameList;
	}
	
	static String sample =	"""
							Button A: X+94, Y+34
							Button B: X+22, Y+67
							Prize: X=8400, Y=5400
							
							Button A: X+26, Y+66
							Button B: X+67, Y+21
							Prize: X=12748, Y=12176
							
							Button A: X+17, Y+86
							Button B: X+84, Y+37
							Prize: X=7870, Y=6450
							
							Button A: X+69, Y+23
							Button B: X+27, Y+71
							Prize: X=18641, Y=10279
							""";

	static String input =	"""
							Button A: X+55, Y+84
							Button B: X+64, Y+29
							Prize: X=6049, Y=5045
							
							Button A: X+52, Y+11
							Button B: X+67, Y+67
							Prize: X=2826, Y=1760
							
							Button A: X+78, Y+56
							Button B: X+22, Y+50
							Prize: X=1222, Y=1322
							
							Button A: X+13, Y+49
							Button B: X+55, Y+12
							Prize: X=7975, Y=7671
							
							Button A: X+26, Y+34
							Button B: X+47, Y+13
							Prize: X=4326, Y=1974
							
							Button A: X+13, Y+55
							Button B: X+83, Y+70
							Prize: X=3982, Y=4195
							
							Button A: X+81, Y+75
							Button B: X+13, Y+45
							Prize: X=7639, Y=8985
							
							Button A: X+78, Y+27
							Button B: X+57, Y+81
							Prize: X=5625, Y=2376
							
							Button A: X+24, Y+73
							Button B: X+71, Y+24
							Prize: X=18732, Y=979
							
							Button A: X+45, Y+22
							Button B: X+20, Y+45
							Prize: X=14935, Y=4567
							
							Button A: X+94, Y+28
							Button B: X+32, Y+61
							Prize: X=8878, Y=3468
							
							Button A: X+43, Y+21
							Button B: X+32, Y+60
							Prize: X=9015, Y=9749
							
							Button A: X+21, Y+80
							Button B: X+81, Y+66
							Prize: X=4188, Y=6494
							
							Button A: X+29, Y+95
							Button B: X+88, Y+33
							Prize: X=7523, Y=6775
							
							Button A: X+24, Y+54
							Button B: X+67, Y+42
							Prize: X=4164, Y=2844
							
							Button A: X+58, Y+20
							Button B: X+11, Y+46
							Prize: X=5388, Y=2536
							
							Button A: X+28, Y+95
							Button B: X+26, Y+13
							Prize: X=1888, Y=3698
							
							Button A: X+30, Y+62
							Button B: X+63, Y+18
							Prize: X=4893, Y=5512
							
							Button A: X+93, Y+28
							Button B: X+26, Y+61
							Prize: X=1290, Y=1505
							
							Button A: X+24, Y+35
							Button B: X+73, Y+30
							Prize: X=2938, Y=1685
							
							Button A: X+48, Y+11
							Button B: X+22, Y+46
							Prize: X=10294, Y=15523
							
							Button A: X+11, Y+29
							Button B: X+54, Y+20
							Prize: X=734, Y=9474
							
							Button A: X+39, Y+14
							Button B: X+31, Y+67
							Prize: X=5132, Y=10002
							
							Button A: X+14, Y+79
							Button B: X+56, Y+13
							Prize: X=17476, Y=2004
							
							Button A: X+30, Y+65
							Button B: X+41, Y+16
							Prize: X=18817, Y=1772
							
							Button A: X+34, Y+36
							Button B: X+73, Y+14
							Prize: X=6739, Y=1882
							
							Button A: X+39, Y+15
							Button B: X+12, Y+37
							Prize: X=6539, Y=568
							
							Button A: X+41, Y+18
							Button B: X+36, Y+63
							Prize: X=4150, Y=16235
							
							Button A: X+15, Y+62
							Button B: X+77, Y+27
							Prize: X=6398, Y=7804
							
							Button A: X+61, Y+23
							Button B: X+16, Y+29
							Prize: X=3576, Y=2979
							
							Button A: X+13, Y+47
							Button B: X+56, Y+22
							Prize: X=6005, Y=4747
							
							Button A: X+26, Y+68
							Button B: X+45, Y+12
							Prize: X=2605, Y=8356
							
							Button A: X+46, Y+14
							Button B: X+40, Y+72
							Prize: X=11684, Y=16452
							
							Button A: X+52, Y+25
							Button B: X+12, Y+51
							Prize: X=8884, Y=8281
							
							Button A: X+11, Y+87
							Button B: X+95, Y+86
							Prize: X=8416, Y=10007
							
							Button A: X+40, Y+58
							Button B: X+32, Y+15
							Prize: X=5056, Y=4725
							
							Button A: X+19, Y+48
							Button B: X+38, Y+22
							Prize: X=215, Y=5088
							
							Button A: X+63, Y+33
							Button B: X+11, Y+36
							Prize: X=1876, Y=2676
							
							Button A: X+38, Y+70
							Button B: X+42, Y+16
							Prize: X=5236, Y=3938
							
							Button A: X+32, Y+53
							Button B: X+41, Y+14
							Prize: X=11873, Y=15017
							
							Button A: X+13, Y+48
							Button B: X+62, Y+26
							Prize: X=14966, Y=1168
							
							Button A: X+72, Y+22
							Button B: X+28, Y+58
							Prize: X=6392, Y=5612
							
							Button A: X+36, Y+11
							Button B: X+20, Y+56
							Prize: X=3636, Y=2907
							
							Button A: X+14, Y+22
							Button B: X+93, Y+18
							Prize: X=2426, Y=1762
							
							Button A: X+71, Y+35
							Button B: X+20, Y+50
							Prize: X=15672, Y=13920
							
							Button A: X+98, Y+57
							Button B: X+37, Y+75
							Prize: X=10343, Y=9492
							
							Button A: X+19, Y+65
							Button B: X+39, Y+16
							Prize: X=2511, Y=10975
							
							Button A: X+28, Y+97
							Button B: X+56, Y+20
							Prize: X=1988, Y=1493
							
							Button A: X+26, Y+72
							Button B: X+56, Y+18
							Prize: X=5576, Y=15002
							
							Button A: X+79, Y+14
							Button B: X+15, Y+74
							Prize: X=12551, Y=14374
							
							Button A: X+38, Y+82
							Button B: X+60, Y+33
							Prize: X=6786, Y=9048
							
							Button A: X+19, Y+47
							Button B: X+23, Y+11
							Prize: X=14723, Y=11967
							
							Button A: X+50, Y+14
							Button B: X+13, Y+67
							Prize: X=3747, Y=5421
							
							Button A: X+65, Y+12
							Button B: X+49, Y+64
							Prize: X=6626, Y=3916
							
							Button A: X+28, Y+21
							Button B: X+11, Y+26
							Prize: X=7752, Y=18194
							
							Button A: X+14, Y+58
							Button B: X+87, Y+19
							Prize: X=6365, Y=6225
							
							Button A: X+28, Y+79
							Button B: X+74, Y+40
							Prize: X=4510, Y=3779
							
							Button A: X+12, Y+54
							Button B: X+52, Y+52
							Prize: X=2700, Y=4506
							
							Button A: X+24, Y+91
							Button B: X+72, Y+35
							Prize: X=3048, Y=5845
							
							Button A: X+15, Y+53
							Button B: X+67, Y+28
							Prize: X=4757, Y=5119
							
							Button A: X+52, Y+17
							Button B: X+22, Y+57
							Prize: X=4530, Y=3025
							
							Button A: X+68, Y+12
							Button B: X+34, Y+51
							Prize: X=8772, Y=4878
							
							Button A: X+59, Y+58
							Button B: X+19, Y+77
							Prize: X=4357, Y=6791
							
							Button A: X+15, Y+63
							Button B: X+70, Y+26
							Prize: X=7705, Y=6801
							
							Button A: X+29, Y+18
							Button B: X+16, Y+43
							Prize: X=15447, Y=6091
							
							Button A: X+20, Y+43
							Button B: X+38, Y+21
							Prize: X=4280, Y=9740
							
							Button A: X+82, Y+21
							Button B: X+34, Y+50
							Prize: X=7068, Y=2925
							
							Button A: X+21, Y+52
							Button B: X+84, Y+43
							Prize: X=9429, Y=8333
							
							Button A: X+29, Y+36
							Button B: X+92, Y+16
							Prize: X=7941, Y=4260
							
							Button A: X+22, Y+32
							Button B: X+65, Y+11
							Prize: X=413, Y=183
							
							Button A: X+41, Y+97
							Button B: X+97, Y+60
							Prize: X=3717, Y=7099
							
							Button A: X+60, Y+15
							Button B: X+37, Y+82
							Prize: X=5374, Y=8389
							
							Button A: X+79, Y+18
							Button B: X+11, Y+53
							Prize: X=4202, Y=14514
							
							Button A: X+26, Y+51
							Button B: X+22, Y+11
							Prize: X=2886, Y=11383
							
							Button A: X+67, Y+22
							Button B: X+13, Y+43
							Prize: X=14616, Y=10056
							
							Button A: X+29, Y+67
							Button B: X+36, Y+11
							Prize: X=15745, Y=7213
							
							Button A: X+11, Y+24
							Button B: X+39, Y+25
							Prize: X=12543, Y=5975
							
							Button A: X+44, Y+39
							Button B: X+17, Y+85
							Prize: X=4666, Y=9031
							
							Button A: X+31, Y+73
							Button B: X+22, Y+16
							Prize: X=3767, Y=6221
							
							Button A: X+75, Y+14
							Button B: X+18, Y+79
							Prize: X=16421, Y=6966
							
							Button A: X+76, Y+59
							Button B: X+18, Y+94
							Prize: X=2568, Y=9356
							
							Button A: X+27, Y+40
							Button B: X+69, Y+27
							Prize: X=5310, Y=4933
							
							Button A: X+20, Y+12
							Button B: X+14, Y+44
							Prize: X=2006, Y=3596
							
							Button A: X+12, Y+86
							Button B: X+99, Y+48
							Prize: X=960, Y=4234
							
							Button A: X+97, Y+16
							Button B: X+17, Y+14
							Prize: X=9963, Y=2136
							
							Button A: X+41, Y+24
							Button B: X+31, Y+84
							Prize: X=3471, Y=3744
							
							Button A: X+76, Y+49
							Button B: X+14, Y+68
							Prize: X=6062, Y=7034
							
							Button A: X+57, Y+12
							Button B: X+32, Y+79
							Prize: X=8324, Y=8186
							
							Button A: X+79, Y+81
							Button B: X+14, Y+92
							Prize: X=4066, Y=10070
							
							Button A: X+38, Y+24
							Button B: X+12, Y+93
							Prize: X=2998, Y=4029
							
							Button A: X+11, Y+90
							Button B: X+52, Y+17
							Prize: X=2971, Y=7970
							
							Button A: X+41, Y+87
							Button B: X+74, Y+13
							Prize: X=3361, Y=1947
							
							Button A: X+92, Y+15
							Button B: X+94, Y+99
							Prize: X=9090, Y=8427
							
							Button A: X+40, Y+15
							Button B: X+24, Y+40
							Prize: X=10928, Y=18240
							
							Button A: X+19, Y+57
							Button B: X+44, Y+23
							Prize: X=3197, Y=2942
							
							Button A: X+13, Y+64
							Button B: X+84, Y+32
							Prize: X=5962, Y=416
							
							Button A: X+28, Y+92
							Button B: X+95, Y+29
							Prize: X=1701, Y=3607
							
							Button A: X+39, Y+14
							Button B: X+18, Y+46
							Prize: X=16355, Y=11272
							
							Button A: X+27, Y+88
							Button B: X+94, Y+21
							Prize: X=9678, Y=7572
							
							Button A: X+89, Y+15
							Button B: X+55, Y+88
							Prize: X=9706, Y=8013
							
							Button A: X+16, Y+65
							Button B: X+82, Y+67
							Prize: X=8348, Y=8898
							
							Button A: X+48, Y+75
							Button B: X+95, Y+29
							Prize: X=6486, Y=5118
							
							Button A: X+93, Y+16
							Button B: X+26, Y+60
							Prize: X=5960, Y=5412
							
							Button A: X+20, Y+67
							Button B: X+89, Y+23
							Prize: X=5438, Y=6661
							
							Button A: X+95, Y+72
							Button B: X+23, Y+67
							Prize: X=2807, Y=6539
							
							Button A: X+84, Y+53
							Button B: X+24, Y+66
							Prize: X=4392, Y=5314
							
							Button A: X+35, Y+79
							Button B: X+50, Y+14
							Prize: X=15635, Y=17971
							
							Button A: X+37, Y+22
							Button B: X+11, Y+82
							Prize: X=2367, Y=4954
							
							Button A: X+17, Y+25
							Button B: X+84, Y+29
							Prize: X=8039, Y=3598
							
							Button A: X+29, Y+32
							Button B: X+74, Y+19
							Prize: X=1622, Y=662
							
							Button A: X+84, Y+52
							Button B: X+22, Y+80
							Prize: X=7282, Y=6964
							
							Button A: X+37, Y+13
							Button B: X+40, Y+74
							Prize: X=12931, Y=8309
							
							Button A: X+73, Y+25
							Button B: X+19, Y+66
							Prize: X=6840, Y=5020
							
							Button A: X+65, Y+44
							Button B: X+11, Y+31
							Prize: X=11920, Y=19148
							
							Button A: X+69, Y+20
							Button B: X+15, Y+32
							Prize: X=4662, Y=3840
							
							Button A: X+87, Y+92
							Button B: X+17, Y+79
							Prize: X=8004, Y=13773
							
							Button A: X+15, Y+49
							Button B: X+59, Y+26
							Prize: X=5897, Y=1473
							
							Button A: X+11, Y+31
							Button B: X+65, Y+33
							Prize: X=501, Y=6017
							
							Button A: X+12, Y+57
							Button B: X+60, Y+21
							Prize: X=11864, Y=11330
							
							Button A: X+53, Y+69
							Button B: X+99, Y+30
							Prize: X=2202, Y=1779
							
							Button A: X+51, Y+22
							Button B: X+19, Y+47
							Prize: X=758, Y=17214
							
							Button A: X+24, Y+11
							Button B: X+20, Y+63
							Prize: X=12228, Y=1005
							
							Button A: X+19, Y+67
							Button B: X+73, Y+25
							Prize: X=18401, Y=6065
							
							Button A: X+94, Y+69
							Button B: X+24, Y+82
							Prize: X=4560, Y=6502
							
							Button A: X+97, Y+60
							Button B: X+15, Y+85
							Prize: X=6119, Y=4315
							
							Button A: X+85, Y+52
							Button B: X+12, Y+73
							Prize: X=4054, Y=3268
							
							Button A: X+17, Y+86
							Button B: X+59, Y+19
							Prize: X=5843, Y=3009
							
							Button A: X+57, Y+12
							Button B: X+26, Y+59
							Prize: X=17154, Y=10263
							
							Button A: X+46, Y+12
							Button B: X+23, Y+31
							Prize: X=4301, Y=2797
							
							Button A: X+65, Y+72
							Button B: X+59, Y+17
							Prize: X=4460, Y=4215
							
							Button A: X+15, Y+37
							Button B: X+65, Y+23
							Prize: X=19045, Y=8367
							
							Button A: X+75, Y+12
							Button B: X+29, Y+38
							Prize: X=5675, Y=1742
							
							Button A: X+23, Y+86
							Button B: X+70, Y+25
							Prize: X=4312, Y=8074
							
							Button A: X+14, Y+81
							Button B: X+89, Y+21
							Prize: X=6426, Y=2604
							
							Button A: X+44, Y+14
							Button B: X+19, Y+63
							Prize: X=524, Y=5800
							
							Button A: X+15, Y+42
							Button B: X+27, Y+12
							Prize: X=3140, Y=12260
							
							Button A: X+15, Y+60
							Button B: X+72, Y+23
							Prize: X=16886, Y=5749
							
							Button A: X+12, Y+43
							Button B: X+51, Y+21
							Prize: X=7208, Y=16647
							
							Button A: X+75, Y+17
							Button B: X+19, Y+39
							Prize: X=6943, Y=2337
							
							Button A: X+50, Y+82
							Button B: X+47, Y+14
							Prize: X=9258, Y=4252
							
							Button A: X+93, Y+91
							Button B: X+17, Y+99
							Prize: X=780, Y=2740
							
							Button A: X+52, Y+62
							Button B: X+68, Y+19
							Prize: X=7932, Y=5919
							
							Button A: X+61, Y+12
							Button B: X+56, Y+96
							Prize: X=3494, Y=2472
							
							Button A: X+11, Y+64
							Button B: X+64, Y+38
							Prize: X=2487, Y=5442
							
							Button A: X+11, Y+32
							Button B: X+35, Y+17
							Prize: X=6381, Y=6696
							
							Button A: X+21, Y+35
							Button B: X+41, Y+12
							Prize: X=7120, Y=13403
							
							Button A: X+81, Y+24
							Button B: X+47, Y+78
							Prize: X=3923, Y=4302
							
							Button A: X+80, Y+13
							Button B: X+11, Y+57
							Prize: X=6737, Y=4794
							
							Button A: X+70, Y+25
							Button B: X+22, Y+63
							Prize: X=14728, Y=2402
							
							Button A: X+42, Y+19
							Button B: X+11, Y+16
							Prize: X=5090, Y=16796
							
							Button A: X+31, Y+99
							Button B: X+89, Y+33
							Prize: X=2416, Y=3696
							
							Button A: X+44, Y+76
							Button B: X+97, Y+51
							Prize: X=10786, Y=10006
							
							Button A: X+25, Y+79
							Button B: X+87, Y+63
							Prize: X=9493, Y=11137
							
							Button A: X+42, Y+98
							Button B: X+17, Y+12
							Prize: X=320, Y=636
							
							Button A: X+36, Y+59
							Button B: X+89, Y+14
							Prize: X=4822, Y=2892
							
							Button A: X+14, Y+48
							Button B: X+23, Y+22
							Prize: X=1570, Y=4928
							
							Button A: X+24, Y+42
							Button B: X+85, Y+28
							Prize: X=8621, Y=4340
							
							Button A: X+74, Y+22
							Button B: X+14, Y+72
							Prize: X=3208, Y=19574
							
							Button A: X+84, Y+43
							Button B: X+12, Y+51
							Prize: X=12068, Y=11215
							
							Button A: X+61, Y+24
							Button B: X+22, Y+61
							Prize: X=2628, Y=4384
							
							Button A: X+23, Y+62
							Button B: X+43, Y+12
							Prize: X=4204, Y=18086
							
							Button A: X+13, Y+22
							Button B: X+69, Y+18
							Prize: X=3627, Y=2286
							
							Button A: X+44, Y+15
							Button B: X+30, Y+68
							Prize: X=16210, Y=12603
							
							Button A: X+35, Y+70
							Button B: X+47, Y+28
							Prize: X=3789, Y=5796
							
							Button A: X+20, Y+81
							Button B: X+96, Y+91
							Prize: X=8716, Y=9689
							
							Button A: X+67, Y+13
							Button B: X+17, Y+66
							Prize: X=13352, Y=10339
							
							Button A: X+45, Y+51
							Button B: X+99, Y+27
							Prize: X=2880, Y=2838
							
							Button A: X+68, Y+62
							Button B: X+73, Y+11
							Prize: X=6236, Y=2130
							
							Button A: X+27, Y+51
							Button B: X+91, Y+12
							Prize: X=2656, Y=540
							
							Button A: X+50, Y+35
							Button B: X+26, Y+83
							Prize: X=2110, Y=5365
							
							Button A: X+20, Y+61
							Button B: X+55, Y+26
							Prize: X=795, Y=5611
							
							Button A: X+23, Y+74
							Button B: X+69, Y+36
							Prize: X=4600, Y=8290
							
							Button A: X+28, Y+59
							Button B: X+48, Y+18
							Prize: X=14328, Y=18592
							
							Button A: X+22, Y+83
							Button B: X+92, Y+65
							Prize: X=5438, Y=7822
							
							Button A: X+66, Y+21
							Button B: X+36, Y+41
							Prize: X=3276, Y=1131
							
							Button A: X+73, Y+31
							Button B: X+13, Y+24
							Prize: X=7263, Y=4692
							
							Button A: X+61, Y+83
							Button B: X+70, Y+18
							Prize: X=9753, Y=8095
							
							Button A: X+47, Y+21
							Button B: X+49, Y+76
							Prize: X=6735, Y=5676
							
							Button A: X+60, Y+28
							Button B: X+11, Y+45
							Prize: X=12663, Y=18625
							
							Button A: X+65, Y+16
							Button B: X+19, Y+73
							Prize: X=3354, Y=1559
							
							Button A: X+44, Y+11
							Button B: X+12, Y+76
							Prize: X=14508, Y=3963
							
							Button A: X+55, Y+23
							Button B: X+28, Y+51
							Prize: X=12467, Y=16721
							
							Button A: X+16, Y+60
							Button B: X+72, Y+13
							Prize: X=18240, Y=9440
							
							Button A: X+32, Y+84
							Button B: X+80, Y+44
							Prize: X=7056, Y=8728
							
							Button A: X+13, Y+36
							Button B: X+58, Y+18
							Prize: X=816, Y=1404
							
							Button A: X+94, Y+92
							Button B: X+12, Y+87
							Prize: X=8226, Y=8352
							
							Button A: X+16, Y+66
							Button B: X+55, Y+20
							Prize: X=1467, Y=4332
							
							Button A: X+36, Y+80
							Button B: X+66, Y+24
							Prize: X=7668, Y=8944
							
							Button A: X+15, Y+89
							Button B: X+54, Y+41
							Prize: X=2232, Y=6817
							
							Button A: X+11, Y+48
							Button B: X+29, Y+16
							Prize: X=17250, Y=5792
							
							Button A: X+57, Y+25
							Button B: X+11, Y+36
							Prize: X=1912, Y=7192
							
							Button A: X+18, Y+68
							Button B: X+63, Y+22
							Prize: X=2151, Y=5318
							
							Button A: X+29, Y+23
							Button B: X+21, Y+99
							Prize: X=3461, Y=10403
							
							Button A: X+68, Y+53
							Button B: X+20, Y+70
							Prize: X=7468, Y=10228
							
							Button A: X+39, Y+40
							Button B: X+50, Y+14
							Prize: X=4830, Y=3388
							
							Button A: X+30, Y+63
							Button B: X+73, Y+40
							Prize: X=4210, Y=4309
							
							Button A: X+34, Y+26
							Button B: X+11, Y+29
							Prize: X=12871, Y=16869
							
							Button A: X+97, Y+52
							Button B: X+26, Y+43
							Prize: X=8503, Y=7232
							
							Button A: X+89, Y+65
							Button B: X+18, Y+72
							Prize: X=7859, Y=8447
							
							Button A: X+14, Y+49
							Button B: X+74, Y+31
							Prize: X=10208, Y=6320
							
							Button A: X+83, Y+36
							Button B: X+26, Y+86
							Prize: X=9239, Y=10284
							
							Button A: X+76, Y+42
							Button B: X+53, Y+88
							Prize: X=8337, Y=9128
							
							Button A: X+15, Y+59
							Button B: X+31, Y+16
							Prize: X=1591, Y=2974
							
							Button A: X+29, Y+81
							Button B: X+93, Y+68
							Prize: X=6065, Y=7544
							
							Button A: X+16, Y+51
							Button B: X+75, Y+21
							Prize: X=5791, Y=10292
							
							Button A: X+89, Y+12
							Button B: X+33, Y+47
							Prize: X=4270, Y=2065
							
							Button A: X+65, Y+16
							Button B: X+15, Y+56
							Prize: X=12245, Y=14688
							
							Button A: X+20, Y+45
							Button B: X+49, Y+16
							Prize: X=7066, Y=6564
							
							Button A: X+36, Y+72
							Button B: X+50, Y+16
							Prize: X=2030, Y=18560
							
							Button A: X+87, Y+49
							Button B: X+41, Y+80
							Prize: X=7149, Y=8636
							
							Button A: X+11, Y+44
							Button B: X+69, Y+66
							Prize: X=5966, Y=8954
							
							Button A: X+47, Y+20
							Button B: X+33, Y+53
							Prize: X=6432, Y=19460
							
							Button A: X+12, Y+46
							Button B: X+75, Y+18
							Prize: X=4688, Y=8220
							
							Button A: X+96, Y+31
							Button B: X+31, Y+96
							Prize: X=5526, Y=5396
							
							Button A: X+83, Y+17
							Button B: X+46, Y+51
							Prize: X=3319, Y=1054
							
							Button A: X+11, Y+55
							Button B: X+94, Y+11
							Prize: X=8039, Y=1639
							
							Button A: X+55, Y+21
							Button B: X+16, Y+32
							Prize: X=16098, Y=1206
							
							Button A: X+33, Y+74
							Button B: X+59, Y+15
							Prize: X=10234, Y=677
							
							Button A: X+20, Y+58
							Button B: X+81, Y+30
							Prize: X=5441, Y=3280
							
							Button A: X+16, Y+21
							Button B: X+93, Y+18
							Prize: X=2051, Y=1131
							
							Button A: X+16, Y+12
							Button B: X+34, Y+99
							Prize: X=1672, Y=2136
							
							Button A: X+96, Y+25
							Button B: X+39, Y+90
							Prize: X=7710, Y=3445
							
							Button A: X+22, Y+60
							Button B: X+69, Y+35
							Prize: X=18665, Y=10145
							
							Button A: X+70, Y+75
							Button B: X+50, Y+14
							Prize: X=4650, Y=2687
							
							Button A: X+35, Y+95
							Button B: X+67, Y+48
							Prize: X=4480, Y=7475
							
							Button A: X+44, Y+44
							Button B: X+13, Y+71
							Prize: X=1005, Y=1759
							
							Button A: X+34, Y+24
							Button B: X+14, Y+85
							Prize: X=958, Y=1803
							
							Button A: X+66, Y+39
							Button B: X+14, Y+47
							Prize: X=1496, Y=17192
							
							Button A: X+54, Y+16
							Button B: X+24, Y+61
							Prize: X=3444, Y=4631
							
							Button A: X+88, Y+68
							Button B: X+17, Y+53
							Prize: X=4893, Y=4937
							
							Button A: X+42, Y+14
							Button B: X+33, Y+82
							Prize: X=2604, Y=2856
							
							Button A: X+67, Y+28
							Button B: X+41, Y+64
							Prize: X=2825, Y=4180
							
							Button A: X+34, Y+57
							Button B: X+84, Y+38
							Prize: X=7174, Y=6783
							
							Button A: X+25, Y+50
							Button B: X+51, Y+13
							Prize: X=942, Y=3646
							
							Button A: X+83, Y+23
							Button B: X+13, Y+62
							Prize: X=17604, Y=15013
							
							Button A: X+55, Y+19
							Button B: X+29, Y+65
							Prize: X=13715, Y=1295
							
							Button A: X+11, Y+29
							Button B: X+54, Y+35
							Prize: X=4218, Y=3390
							
							Button A: X+75, Y+43
							Button B: X+19, Y+47
							Prize: X=17673, Y=13249
							
							Button A: X+74, Y+22
							Button B: X+41, Y+68
							Prize: X=8228, Y=7804
							
							Button A: X+85, Y+94
							Button B: X+90, Y+25
							Prize: X=9855, Y=7172
							
							Button A: X+82, Y+40
							Button B: X+25, Y+72
							Prize: X=1027, Y=800
							
							Button A: X+99, Y+39
							Button B: X+43, Y+74
							Prize: X=9734, Y=7030
							
							Button A: X+31, Y+54
							Button B: X+43, Y+14
							Prize: X=8147, Y=9886
							
							Button A: X+85, Y+64
							Button B: X+27, Y+98
							Prize: X=9506, Y=11274
							
							Button A: X+63, Y+26
							Button B: X+14, Y+45
							Prize: X=12716, Y=14865
							
							Button A: X+36, Y+85
							Button B: X+40, Y+28
							Prize: X=2516, Y=3017
							
							Button A: X+32, Y+83
							Button B: X+91, Y+26
							Prize: X=7654, Y=2630
							
							Button A: X+94, Y+88
							Button B: X+87, Y+13
							Prize: X=6766, Y=2638
							
							Button A: X+29, Y+81
							Button B: X+91, Y+54
							Prize: X=5961, Y=4239
							
							Button A: X+67, Y+76
							Button B: X+61, Y+17
							Prize: X=5274, Y=2903
							
							Button A: X+72, Y+14
							Button B: X+15, Y+74
							Prize: X=17021, Y=2074
							
							Button A: X+28, Y+76
							Button B: X+47, Y+13
							Prize: X=2863, Y=5365
							
							Button A: X+82, Y+45
							Button B: X+47, Y+88
							Prize: X=4179, Y=6088
							
							Button A: X+17, Y+33
							Button B: X+43, Y+17
							Prize: X=6468, Y=13352
							
							Button A: X+50, Y+24
							Button B: X+15, Y+39
							Prize: X=13565, Y=6269
							
							Button A: X+52, Y+37
							Button B: X+13, Y+78
							Prize: X=3003, Y=8393
							
							Button A: X+21, Y+64
							Button B: X+63, Y+15
							Prize: X=18638, Y=14477
							
							Button A: X+13, Y+43
							Button B: X+92, Y+70
							Prize: X=5698, Y=5726
							
							Button A: X+88, Y+80
							Button B: X+12, Y+71
							Prize: X=2008, Y=2186
							
							Button A: X+74, Y+14
							Button B: X+75, Y+79
							Prize: X=7398, Y=6066
							
							Button A: X+22, Y+72
							Button B: X+74, Y+21
							Prize: X=9094, Y=10004
							
							Button A: X+15, Y+48
							Button B: X+76, Y+29
							Prize: X=5497, Y=363
							
							Button A: X+62, Y+12
							Button B: X+66, Y+89
							Prize: X=7490, Y=5947
							
							Button A: X+95, Y+42
							Button B: X+50, Y+75
							Prize: X=12610, Y=10071
							
							Button A: X+85, Y+58
							Button B: X+14, Y+90
							Prize: X=7393, Y=12848
							
							Button A: X+36, Y+84
							Button B: X+94, Y+68
							Prize: X=1948, Y=3032
							
							Button A: X+15, Y+44
							Button B: X+57, Y+24
							Prize: X=4517, Y=17684
							
							Button A: X+23, Y+70
							Button B: X+46, Y+13
							Prize: X=18546, Y=13578
							
							Button A: X+58, Y+99
							Button B: X+90, Y+24
							Prize: X=5186, Y=7815
							
							Button A: X+65, Y+14
							Button B: X+22, Y+77
							Prize: X=12752, Y=17595
							
							Button A: X+83, Y+61
							Button B: X+25, Y+69
							Prize: X=5025, Y=5465
							
							Button A: X+70, Y+36
							Button B: X+17, Y+56
							Prize: X=13187, Y=8320
							
							Button A: X+42, Y+15
							Button B: X+18, Y+52
							Prize: X=5672, Y=15378
							
							Button A: X+23, Y+49
							Button B: X+39, Y+19
							Prize: X=1960, Y=952
							
							Button A: X+11, Y+47
							Button B: X+37, Y+22
							Prize: X=1131, Y=6717
							
							Button A: X+59, Y+33
							Button B: X+27, Y+54
							Prize: X=7326, Y=12827
							
							Button A: X+37, Y+12
							Button B: X+31, Y+61
							Prize: X=13132, Y=17507
							
							Button A: X+12, Y+36
							Button B: X+87, Y+17
							Prize: X=4104, Y=2552
							
							Button A: X+14, Y+54
							Button B: X+61, Y+24
							Prize: X=9176, Y=5150
							
							Button A: X+14, Y+65
							Button B: X+33, Y+13
							Prize: X=758, Y=2678
							
							Button A: X+51, Y+22
							Button B: X+11, Y+28
							Prize: X=2888, Y=3362
							
							Button A: X+12, Y+38
							Button B: X+77, Y+38
							Prize: X=1968, Y=15852
							
							Button A: X+75, Y+39
							Button B: X+22, Y+56
							Prize: X=11849, Y=4169
							
							Button A: X+21, Y+56
							Button B: X+70, Y+30
							Prize: X=16272, Y=12592
							
							Button A: X+14, Y+27
							Button B: X+40, Y+11
							Prize: X=13504, Y=10466
							
							Button A: X+17, Y+98
							Button B: X+70, Y+30
							Prize: X=3163, Y=8522
							
							Button A: X+16, Y+34
							Button B: X+64, Y+44
							Prize: X=4448, Y=5036
							
							Button A: X+19, Y+43
							Button B: X+55, Y+30
							Prize: X=5638, Y=6241
							
							Button A: X+27, Y+96
							Button B: X+76, Y+14
							Prize: X=3362, Y=2986
							
							Button A: X+45, Y+33
							Button B: X+14, Y+36
							Prize: X=6040, Y=2366
							
							Button A: X+33, Y+63
							Button B: X+87, Y+44
							Prize: X=10917, Y=9365
							
							Button A: X+91, Y+20
							Button B: X+59, Y+89
							Prize: X=12062, Y=7289
							
							Button A: X+17, Y+55
							Button B: X+36, Y+18
							Prize: X=4757, Y=14123
							
							Button A: X+30, Y+78
							Button B: X+27, Y+19
							Prize: X=1182, Y=1742
							
							Button A: X+69, Y+11
							Button B: X+18, Y+72
							Prize: X=13307, Y=2563
							
							Button A: X+49, Y+17
							Button B: X+14, Y+35
							Prize: X=865, Y=18170
							
							Button A: X+43, Y+78
							Button B: X+77, Y+19
							Prize: X=6683, Y=1986
							
							Button A: X+38, Y+43
							Button B: X+80, Y+20
							Prize: X=6710, Y=3855
							
							Button A: X+22, Y+73
							Button B: X+75, Y+20
							Prize: X=19243, Y=2562
							
							Button A: X+63, Y+21
							Button B: X+13, Y+28
							Prize: X=7771, Y=18043
							
							Button A: X+21, Y+59
							Button B: X+53, Y+25
							Prize: X=16739, Y=7557
							
							Button A: X+38, Y+18
							Button B: X+39, Y+98
							Prize: X=5442, Y=6236
							
							Button A: X+15, Y+39
							Button B: X+36, Y+15
							Prize: X=13094, Y=16196
							
							Button A: X+53, Y+59
							Button B: X+84, Y+13
							Prize: X=3942, Y=2134
							
							Button A: X+46, Y+11
							Button B: X+63, Y+88
							Prize: X=6045, Y=7645
							
							Button A: X+20, Y+94
							Button B: X+83, Y+32
							Prize: X=8907, Y=9992
							
							Button A: X+23, Y+62
							Button B: X+69, Y+29
							Prize: X=19443, Y=17585
							
							Button A: X+42, Y+15
							Button B: X+13, Y+32
							Prize: X=17771, Y=3701
							
							Button A: X+85, Y+25
							Button B: X+14, Y+42
							Prize: X=6657, Y=5481
							
							Button A: X+11, Y+80
							Button B: X+67, Y+14
							Prize: X=2843, Y=8458
							
							Button A: X+37, Y+57
							Button B: X+32, Y+15
							Prize: X=18786, Y=9479
							
							Button A: X+40, Y+75
							Button B: X+47, Y+25
							Prize: X=6049, Y=5850
							
							Button A: X+58, Y+13
							Button B: X+28, Y+32
							Prize: X=5880, Y=2244
							
							Button A: X+51, Y+94
							Button B: X+91, Y+33
							Prize: X=7286, Y=7097
							
							Button A: X+11, Y+52
							Button B: X+81, Y+53
							Prize: X=3310, Y=5750
							
							Button A: X+72, Y+31
							Button B: X+16, Y+52
							Prize: X=12264, Y=8321
							
							Button A: X+11, Y+88
							Button B: X+85, Y+53
							Prize: X=7938, Y=12090
							
							Button A: X+19, Y+39
							Button B: X+40, Y+23
							Prize: X=15370, Y=3989
							
							Button A: X+61, Y+34
							Button B: X+32, Y+55
							Prize: X=1925, Y=2671
							
							Button A: X+71, Y+86
							Button B: X+34, Y+11
							Prize: X=3685, Y=2894
							""";
}