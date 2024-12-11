import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import java.awt.*;

class Day11 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 55312, actual: " + getStoneCountAfterNBlinks(parse(sample), 25));
		System.out.println("Actual phase one result, expected: 198089, actual: " + getStoneCountAfterNBlinks(parse(input), 25));
		System.out.println("Actual phase two result, expected: 236302670835517, actual: " + getStoneCountAfterNBlinks(parse(input), 75));
		
	}
	
	public static long getStoneCountAfterNBlinks(List<Long> stoneList, int blinks) {
		
		long result = 0l;
		
		for(Long stone : stoneList) {
			result+= blinkAndReturnCount(stone, blinks);
		}
		
		return result;
	}
	
	public static long blinkAndReturnCount(Long stone, int times) {
		
		if (times == 0) {
			return 1;// last stone
		}
		
		String es;
		Long a,b = null;
		if (stone == 0l) {
			return memoizeBlinkAndReturnCount(1l, times -1);
		} else if ((es = "" + stone).length() % 2 == 0) { //even number of digits
			a = Long.parseLong(es.substring(es.length()/2));
			b = Long.parseLong(es.substring(0, es.length()/2));
			return memoizeBlinkAndReturnCount(a, times -1) + blinkAndReturnCount(b, times -1);
		} else {
			return memoizeBlinkAndReturnCount(stone * 2024, times -1);
		}
	}
	
	private static HashMap<String, Long> memory = new HashMap<String, Long>();
	public static long memoizeBlinkAndReturnCount(Long stone, int times) {
		var key = stone + ":" + times;
		if (memory.containsKey(key)) {
			return memory.get(key);
		}
		
		var result = blinkAndReturnCount(stone, times);
		memory.put(key, result);
		return result;
	}
	
	public static List<Long> parse(String input) {
		return Arrays.stream(input.split(" "))
			.map(Long::valueOf)
			.collect(Collectors.toList());
	}
	
	static String sample =	"125 17";
	
	static String input =	"3028 78 973951 5146801 5 0 23533 857";
}