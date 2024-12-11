import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import java.awt.*;

class Day11 {
	
	public static void main(String[] args) {
		System.out.println("Actual phase one result, expected: 198089, actual: " + getStoneCountAfter25Blinks(parse(sample)));
		
	}
	
	public static long getStoneCountAfter25Blinks(List<Long> stoneList) {
		
		Map<Long, Long> stoneCountByValue = new HashMap<Long, Long>();
		
		for(Long stone : stoneList) {
			stoneCountByValue.put(stone, 1l);
		}
		
		
		
		var map = change(stoneCountByValue, 75);
		
		long sum = 0l;
		for (Map.Entry<Long, Long> newEntry : map.entrySet()) {
			sum += newEntry.getValue();
		}
		return sum;
	}
	
	
	public static Map<Long, Long> change(Map<Long, Long> stoneCountByValue, int count) {

		
		Map<Long, Long> result = new HashMap();
		
		for (Map.Entry<Long, Long> entry : stoneCountByValue.entrySet()) {
			long e = entry.getKey();
			var subResult = computeSubResult(e, count);
			
			for (Map.Entry<Long, Long> newEntry : subResult.entrySet()) {
				result.put(newEntry.getKey(), result.getOrDefault(newEntry.getKey(), 0l) + newEntry.getValue());
			}
		}
	
		
		return result;
	}
	
	static Map<String, Map<Long, Long>> memory = new HashMap<>();
	
	public static Map<Long, Long> computeSubResult(Long e, int count) {
		
		if (memory.containsKey(e + ":" + count)) {
			return memory.get(e + ":" + count);
		}
		
		Map<Long, Long> result = new HashMap<Long, Long>();
		
		if (count == 1) {
			String es = "";
			
			if (e == 0l) {
				result.put(1l, 1l);
				return result;
			} 
			
			if ((es = "" + e).length() % 2 == 0) { //even number of digits
				
				var a = Long.parseLong(es.substring(es.length()/2));
				var b = Long.parseLong(es.substring(0, es.length()/2));
				
				if (a == b) {
					result.put(a, 2l);	
				} else {
					result.put(a, 1l);
					result.put(b, 1l);	
				}
				
				return result;
			}
			
			Long ne = e * 2024;
			result.put(ne, 1l);
			return result;
		}
		
		Map<Long, Long> subResult = computeSubResult(e, 1);
		
		for (Map.Entry<Long, Long> newEntry : subResult.entrySet()) {
			var sub_sub_result = computeSubResult(newEntry.getKey(), count - 1);
			for (Map.Entry<Long, Long> sub_sub_entry : sub_sub_result.entrySet()) {
				result.put(sub_sub_entry.getKey(), result.getOrDefault(sub_sub_entry.getKey(), 0l) + (sub_sub_entry.getValue() * newEntry.getValue()));
			}
		}			
		String str = "" + result;
		
		if (count > 7) {
			memory.put(e + ":" + count, result);		
		}
		
		
		return result;
	}
	
	public static List<Long> parse(String input) {
		return Arrays.stream(input.split(" "))
			.map(Long::valueOf)
			.collect(Collectors.toList());
	}
	
	static String sample =	"3028 78 973951 5146801 5 0 23533 857";
	
	static String input =	"""
							
							""";
}

	