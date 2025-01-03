import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import java.awt.*;
import java.nio.file.*;

class Day11 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 1930, actual: " + getCost(parse(sample), false));
		System.out.println("Actual phase one result, expected: 1400386, actual: " + getCost(parse(input), false));
		System.out.println("Sample phase two result, expected: 1206, actual: " + getCost(parse(sample), true));
		System.out.println("Sample phase two result, expected: 851994, actual: " + getCost(parse(input), true));
	}
	
	public record Edge(Point p, int type) {}
	
	public static long getCost(char [][] input, boolean calculateBySize) {
		long result = 0l;

		int height = input.length;
		int width = input[0].length;
		int totalPositions = height * width;
		
		Set<Point> taken = new HashSet<Point>();
		
		int position = 0;
		while (position < totalPositions) {
			int x = position % width;
			int y = position / height;
			
			var p = new Point(x,y);
			if (taken.contains(p)) {
				position++;
				continue;
			}
			

			Set<Point> plot = new HashSet<Point>();
			floodFindNeighbours(input[y][x], p, input, plot);
			taken.addAll(plot);
			
			
			Comparator<Edge> typeXY = Comparator
					.comparingInt(Edge::type)
					.thenComparing(edge -> edge.p().y)
					.thenComparing(edge -> edge.p().x);

			Set<Edge> edges = new TreeSet<Edge>(typeXY);
			var pperim = 0l;
			for(Point plot_p : plot) {
				pperim += calculatePerimeter(plot_p, plot, edges);
			}
			
			int linesCount = 0;
			Edge prev = null;
			for(Edge edge : edges) {
				if (edge.type != 3) continue;//up
				
				if (prev == null) {
					linesCount++;
					prev = edge;
					continue;
				}
				
				if (prev.p.y == edge.p.y) {
					if ((prev.p.x + 1) == edge.p.x) {
						prev = edge;
						continue;
					}	
				}
				
				prev = edge;
				linesCount++;
			}
			prev = null;
			for(Edge edge : edges) {
				if (edge.type != 1) continue;//down
				
				if (prev == null) {
					linesCount++;
					prev = edge;
					continue;
				}
				
				if (prev.p.y == edge.p.y) {
					if ((prev.p.x + 1) == edge.p.x) {
						prev = edge;
						continue;
					}	
				}
				
				prev = edge;
				linesCount++;
			}
			
			
			Comparator<Edge> typeYX = Comparator
					.comparingInt(Edge::type)
					.thenComparing(edge -> edge.p().x)
					.thenComparing(edge -> edge.p().y);
			
			var edgesByY = new TreeSet<Edge>(typeYX);
			edgesByY.addAll(edges);
			edges= edgesByY;
			int columnsCount = 0;
			
			prev = null;
			for(Edge edge : edges) {
				if (edge.type != 0) continue;//right
				
				if (prev == null) {
					columnsCount++;
					prev = edge;
					continue;
				}
				
				if (prev.p.x == edge.p.x) {
					if ((prev.p.y + 1) == edge.p.y) {
						prev = edge;
						continue;
					}	
				}
				
				prev = edge;
				columnsCount++;
			}
			prev = null;
			for(Edge edge : edges) {
				if (edge.type != 2) continue;//left
				
				if (prev == null) {
					columnsCount++;
					prev = edge;
					continue;
				}
				
				if (prev.p.x == edge.p.x) {
					if ((prev.p.y + 1) == edge.p.y) {
						prev = edge;
						continue;
					}	
				}
				
				prev = edge;
				columnsCount++;
			}
			
			if (calculateBySize) {
				
				result += (columnsCount + linesCount) * plot.size();	
			} else {
				result += pperim * plot.size();	
			}
			
			

			
			
			
		}
		
		
		return result;
	}
	
	public static long calculatePerimeter(Point p, Set<Point> plot, Set<Edge> edges) {
		var currentPoint = p;
		var result = 4l; //all four edges
		
		
		try {
			Point right = (Point)p.clone();
			right.x = right.x + 1;
			if (plot.contains(right))
				result -=1;
			else 
				edges.add(new Edge(right, 0));
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		try {
			Point down = (Point)p.clone();
			down.y = down.y + 1;
			if (plot.contains(down)) 
				result -=1;
			else 
				edges.add(new Edge(down, 1));
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		try {
			Point left = (Point)p.clone();
			left.x = left.x - 1;
			if (plot.contains(left)) 
				result -=1;
			else 
				edges.add(new Edge(left, 2));
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		try {
			Point up = (Point)p.clone();
			up.y = up.y - 1;
			if (plot.contains(up)) 
				result -=1;
			else
				edges.add(new Edge(up, 3));
			
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		
		
		return result;
		
	}
	
	public static void floodFindNeighbours(char needle, Point p, char[][] haystack, Set<Point> acc) {
		if (acc.contains(p)) {
			return;
		}
		
		if (haystack[p.y][p.x] == needle) {
			acc.add(p);
		} else { 
			return;
		}
		
		try {
			Point right = (Point)p.clone();
			right.x = right.x + 1;
			floodFindNeighbours(needle, right, haystack, acc);
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		try {
			Point down = (Point)p.clone();
			down.y = down.y + 1;
			floodFindNeighbours(needle, down, haystack, acc);
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		try {
			Point left = (Point)p.clone();
			left.x = left.x - 1;
			floodFindNeighbours(needle, left, haystack, acc);
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
		
		try {
			Point up = (Point)p.clone();
			up.y = up.y - 1;
			floodFindNeighbours(needle, up, haystack, acc);
		} catch (ArrayIndexOutOfBoundsException ignored_lazy) {}
	}
	
	public static char[][] parse(String input) {
		return input.lines()
			.map(String::toCharArray)
			.toArray(char[][]::new);
	}
	
	static String sample =	"""
							RRRRIICCFF
							RRRRIICCCF
							VVRRRCCFFF
							VVRCCCJFFF
							VVVVCJJCFE
							VVIVCCJJEE
							VVIIICJJEE
							MIIIIIJJEE
							MIIISIJEEE
							MMMISSJEEE
							""";
	
	static String input =	"""
							YYYYYYYYYEJJEEEEEEEEEEEEEEGGGGGGGGGGGGGGGGGGCCCCCCCCCCWWCCLLLKKKKJKKKKKKFFFFFFFBBBBBBBBBBBAEEEEEEEEEEEEPPPPPPPPPOOOOZYYYZZZZZZAAUUUUUUUUUUUU
							YYYYYYYYNEEEEEEEEEEEEEEEEEGGGGGGGGGGGGGGGGGCCCCCCCCCCCCCCCLLLLKKKKKKKKKKKFFFFFBBFBOBBBBBBBBBEEEEEEEEEEEPPPPPPOOOOOOOZZZZZZZZZZZAAUKUUUUUUUUU
							YYYYYYNNNNYEEEEEEEEEEEEEEEGGGGGGGGGGGGGGGGGGCCCCCCCCCCCCCCCCLKKKKKKKKKKKKFFFFFFFFBBBBBBBBBBBBEEEEEEEEPPPPPPPOOOOOOOOOZZZZZZZZZUUUUUUUUUUUUUU
							YYYNNNNNNNEEEEEEEEEEEEEEEEEGGGGGGGGGGGGGGGVVVVCCCCCCCCCRREEEEKKKKKKKKKKKKFFFFFFXFBBBBBBMMBBBBBEEEEEFEPPPPPPOOOOOOOOOOOZZZZZZZZZZUUUUUUUUUUUU
							YYYNNNNNNWWEEEEEEEEEEEEEEEEGGGGGGGGGGGGGGGWVVVVVCCCCCCRRREEMEEKKKKKKKKKKKFFFFFXXFFBBBMBMMEEEEEEEEEEEPPPPPPPOOOOOOOOOOOZZZZZZZZZUUUUUUUUUUUUU
							YYNNNNNNNNEEEEEEEEEEEEEEEEEEIIIIGGGGGGGGGWWWVVVVVCCCCCCEEEEMEKKKKKKKKKKKKKFFFFXXXBBBMMMMMEEEEEEEEEEEPPPPPPPPOOOOOOOOOOOOZZZZUUZUUUUUUUUUUUUU
							YNNNNNNNNNNEEEEEEEEEEEEEEEEEIIIIGGGGGGGWWWWVVVVWWWWCWCCWEEMMMKKKKKKKKKKKKKFFFFXXXXXXMMMMEEEEEEEEEEEPPPPHHHHPPOOOOOOOOOOOZZZUUUUUUUUUUUUUUUUN
							NNNNNNNNNNNNEEKEEKKKVEEEEEEIIIIIGIGGVGGGWCWWWWVVVWWWWCWWMMMMKKKKKKKKKKKKKKXXXXXXXXXXMMMMMEEEEEEEEEEPPHHHHHHPPOOOOOOOOOOOZZZUUUUUUUUUUUUUUUUN
							NNNNNNNNNNNNAKKKKKKKKEEEEEEIIIIIIIGVVGGGWCCWWWWWWWWWWWWWWMMMMMMBBBKKKKBKKKBBXXXXXXXXMMXMMMEMMEEEEEQCPCHHHHHHPPOOOVVVOOOOZOOOOUUUUUUUUUUUUUNN
							JJNNNNNNNNNNNKKKKKKKKEEEETTTTTTTTTGGGGGGCCCWWWWMWMMWWWWWWWMMMMMMMBBBKBBBKBBBBXXXXXXXXXXMMMMMMEEEQQQCCCHHHHHHHPVOOVVVOVOOOOOOOOOUUUUUUUUUUNNN
							JJNNJNNNNNNJKKKKKKKKKKEEETTTTTTTTTTTIGGGCCCCWWWMMMMMWWWWWWWWWWMMKVBBBBNBBBBBBXXXXXXXXXXMMMMMMEEQQYQCCCHHHHHHHVVOOVVVVVOOOOOOOOOOUUQQUUUUNNNN
							JJNNJNNNJJJJJKKKKKKKKKKKZTTTTTTTTTTTTTCGCCCCWWWMMMMMWWWWWWWWWWMVKVBBBNNNNBBBBXXXXXXBXNMMMMMMMMHQQQQCCCCHHHHHVVVHVVVVVVVVOOOFFFOOQQQQUNNNNNNN
							JJJJJNNNJJJJJJKKKKKKKKKZZTTTTTTTTTTTTTCCCCCCCWMMMMWWWWWWWVWWVVVVVVBBBNXNNNNBBBBXXBBBMMMMMMMMMQQQQQQCCCCHHHHHHVVHVVVVVVVVOFFFFFOOQQQQUQNNNNNN
							JJJJJNIJJJJJKKKKKKKKKKKKZZZZITTTTTTTTTCCCCCCCCMMMMWWWWWWVVVVVVVVVVVBVNNNNNNNBBBBXBBBBBMMMMMMMKQQQQQQCQCHHHHHHVVVVVVVVVVVVFFFFFOQQQQQQQQNNNNN
							JJJJJJJJJJJJJKKKKKKKKKKKZLZZZTTTTTTTTTCCCCCCCCMMMMMMMWVWVVVVVVVVVVVVVNNNNNNNBBBBXBBBBBMMMMMMMQQQQQQQQQHHHHHMHHZVVVVVVVVVVVFFQOOOQQQQQQNNNNNN
							JJJJJJJJJJCCCKVKKKKKKKZZZLLLLZZCTTTTCCCCCCCCCCMMMMMMMMVVVVVVVVVVVVVVNNNNNNRRRRRBBBBBMMRRRMMMMMQQQQQQQQMMMXMMMHVVVVVVVVVVVVQQQQQOOOQQQQNNNNNN
							JJJJJJJJJJCCCCKKKKKKKZZZLLLLCCCCTTTTTTTTTCCLLMMMMMMMMMTVVVVVVVVVVVVVNNNNNNRRRRRRRBBBMMMMRMMMMQQQQQQQQQMMMMMMMEMVVVVVVVVVVVQQQHQQQQQQQQNNNNNN
							JJJJJJJJJJCCCCIKKKKKKZLLLLLDLCCCTTTTTTTTTCLLLMMMMMMMMTTTTVVVVVVVVVVVNNNNNNNRRRRRRRBBMMMMMMMMQQQQQQQQQQMMMMMMMMMMVVVVVVVVVVVVQHHHQQQQNNNNNNNN
							JJJJJJJJJCCCCCIKKKKKZZLLLLLLLCCCTTTTTTTTTCCLLLLLMMMEMTTTIIIIVVVVVVVVNNNNNNNNRRRRRRRRMMMMMMMMMQQQQQQQQMMMMMMMMMMMVVVVVVVVVVVVQHHHHQQQQNNNNNNN
							JJJJJJDJCCCCCCCCKKKKKCLLLLLLLCCCTTTTTTTTTLLLLLLEEMMEITIIIIIIIIIVVVVVNNNNNNNRRRRRRRRJJJMMMMMMMBBQQQQQQMMMMMMMMMVVVVVVVVVVZIHHHHHHHQQQNNKKNNNN
							JJJJJJDJCCCCCCCCCCCCCCLLLLSLLZZZZZTTTTTTTLLLLLLLEEEEIIIIIIIIIIIEVVNNNNNNNNRRRRRRRRRRMMMMMMMMBBBQBPQQQMMMMMMMMMVVVVVZVVVVZIHHHHHHHHHNNNNNNNNN
							DJJJDDDCCCCCCCCCCCCCCLLLLLSSSZZZZZTTTTTTTLLLLLLLLEEIIIIIIIIIIIIIINNNNNNNNNRRRRRRRRRRMMMMMMMMMBBBBBBMMMMMMMMMMMVVVVVZZZVVZIIIIHHHHHHNNNNNNNNN
							DDDJDDCCCCCCCCCCCCCCCLLLLLLSSZZZZZTTTTTTTLLLLLLIEEIIIIIIIIIIIIIIIINNNNNNNCNRRRRRRQQRMMMMMMMMMBBBBBBMMMMMMMMMMMMVVVVZZZZZZIIIIHHHHHHNNNNNNNNN
							DDDDDDCCCCCCCCCCCCCCCLLLLSSSSZZZZZTTTTTTTLLLLLLIIIIIIIIIIIIIIIIIPIPNNCNNNCCCCRRRRRMMMMMMMMMMMBBBBBBMMMMMMMMMMMVVVVVZZZZZZIIIHHHHCNNNNNNNNNNN
							IDDDDDCCCCCCCCCCCCCCLLNNLSSWSSZZZZTTTTTTTLLLLLIIIIIIIIIIQIQFQQIPPPPPPCNCCCCGCRRRMMMMMMMMMMMMBBBBBBBBBBMMMMMMMMMMMZZZZZZZZIIIHHHHHCNNNNNNNNNN
							IIIDDIIYCCCCCCCCCCCLLLLNNSSWWWWZWWWEELLLLLLLLLIIIIIIIIIIQQQQQQPPPPPPCCCCCCCCCRRMMMMMMMMMMMMMBTBBBBBBBBMMMMMMMMMBBBZZZZZZZZIHHHNNNNNNNNNNNNNN
							IIIDIIIICCCCCCCCLLLLLLLNNSSWWWWWWWWEEEEELLLLLIIDDDIIIIQQQQQQQQQPPPPPCCCCCCCCCRMMMMMMMMMMMMMMBBBBBBBBZZTMMMMMMMBBBBBBZZZZZZZNNNNNNNNNNNNNNNNN
							IIIIIIIIIKCKCCCCXLLLLLNNNSSWWWWWWEEEEEEELLLLLIIDDDIIIQQQQQQQQQPPPPPPPPPCCCCCCRMMMMMMMMMMCCCMTBBBBBZZZZZPMMMMKMBBBBBBZZZCZZZNNNNNNNNNNNNNNNXX
							IIIIIIIIIKCCCCCCXLLLLLNNNNNWWWWWWWEEEEELLELLLAIDDDDDDQQQQQQPPPPPPPPPPCCCCCCCCCCCMMMMMMMMMMMMMWBBBZZZZZLLLMMKKSBBSBBBZZZXZZZNNNNNNNNNNNNNNNXX
							IIIIIIIIIXCCCCXXXXXLLLNNNYNVWWWWWEEEEEELEELLLLDDDDDDDQQQQQQPPPPPPPPPCCCCCCCCCCCCCMMMMMMMMMMMMWWZZZZZZZZLLULLKSSSSBSBXNNXXRXXNNNNNNNNNNNXXXXX
							IIIIIIIIIXXCCXXXXXXXNNNNNYNVBBBWWBEEEEEEELLDDDDDDDDDDQQQQQQQQQPPPPPPPCCCCCCCCCCCCMMMMFFMMMMNMZZZZZZZZZLLLULLKSSSSSSXXXNXXXXXXNNNNNNNNNNXXXXX
							IIIIIIIXXXXXXXXXXXNAANNNNYNNBBBBBBBEJJEAEDDDDDDDDDQQQQQQQQQQPQPPPPPPPCCCCCCCCCCCTFFFFFFFMMMMMMMUZZZZZZLLLLLLLSSSSSSSXXXXXXXNNNNNNNNNNNNXXXXX
							IIIIIIIXXXXXXXXXXXNAANNNNNNNBBBBBBBJJJDDEDDDDDDDBBIIIQQQQQQQPPPPPPPCCCCCCCCCCCCUFYFFFFFNFMMMMEUUZZZZZAALLLLLLSSSSSSXXXXXXXXNNNNNNNNNNGGGXXXX
							IIIIIIIIIXXXXXXXXNNNNNNNNNDNBBBBBBBBJJDDDDDDDDDBBIIIQQQQQQQQPQPPRPPCCCCCCCCCCCCFFFFFFFFFFFFMEEUUZZQZQLLLLQSSLSSSSSSSXXXXXXXNNNNNNNNGGVGGGXXX
							IIIIIIIXXXXXXXXXXXXNNNNNNDDDBBBBBBBJJJDDDDDDDBBBBBBBQQQQQQQQQQPCRRRCCCCCCCCCCYYYYFFFFFFFFFFUUUUUUZQQQLLLLQSSSSSSSSSSXXXXXXNNNNNNNNNGGGGGXXGG
							IIIIIIIIXXXXXXXXXXXNNNNNDDBBBBBBBBBBBDDDDDDDBBBBBBBBQQQQQQQQQQPCRRCCCCCCCCCCCYYYFFFFFFFFFFTTUUUUUQQQQQQLLQQQSSSSSSSXXXXXXXXNNNNNNNNGGGGGGGGG
							IIIIIIXXXXXXXXXXXXXXNNNNDDBBBBBBBBBJJDDDDDDDBBBBBBBBBBQQQQQQCCCCCRCCCCCCCCCYYYYYYYFFFFFFFZTTTUUUQQQQQQQQQQQQQSSSSSXXXXXXXXNNNNNNNNNNGGGGGGGG
							IIIIIIIXXXXXXXXXXXXNNNNNNNNVVBBBBBBJJJJDDDDDBBBBBBBBBBBQQQQQQQCCCCCCCCCYCCYYYYYYYYYFFFFFFZTTTZQQQQQQQSSSQQQQSSSSSSXXXXXXXXXNNNNNNGGGOGGGGGGG
							IIINNNNZCXCXXCCXXXXNNNNVVVVVVBBBJJBBJJJDDKDKBBBBBBBTTBBQQQUQQQCCCCCCCYYYYCYYYYYYYYYFFFZZZZZZZZZQQQQSSSSSQQQQQQXXXXXXXXXXXXXNNNNNNGGGGGGGGGGG
							NNNNNNNZCCCXCCXXXXXXNNNOVVVVVBBBJJBBJJJJDKKKBBFTTTTTTBBQQQQQQCCCCCCCCYYYYYYYYYYYYYYZQQZZZZZZZZZZSSSSSSSSQSQQQQXXXXXXXXXXKXXNNNNNNNGGGGGGGGGG
							CCNNNNNNCCCCCXXXXFFXXNOOOOOVOOBBJJJJJJJJDKKKKKFFTTUTTBTQHQQHQCCCCCCCHYYYYYYYYYYYYZYZZZZZZZZZZZZZSSSSSSSSSSQQXXXXXXXXKXXKKKXNNNNNNNGGGGGGGGGG
							CCCCNNNCCCCCXXXXFFFXXXOOOOOVOOJJJJJJJJJJKKKKKKFFFFFFFTTHHQHHQCCCCCCCHYYYYYYYYYYYYZZZZZZZZZZZZZZZSSSHSSSSSSSSFXXXXXXXKKKKKKKNNNNNNNNGGGGGGGGG
							CCCCCCNCCCRCXXFXFFFOOOOOOOOOOOJAAAAAAJKKKKKKTTFFFFFFFTTHHHHHCCCCCCCCCYYYYYYYYYYYYYZZZZZZZZZZZZZZSSSSSSSSSSSSFFXXXSXXKKKKKKKNNNNNNNGGGGGGGGGY
							CCCCCCCCCCRCCXFFFFFFFOOOOOOOOOOOAAAAAKKKKFFFFFFFFFFFFTTHHHHHHCCCHCCHHHQQYYYYYYYZZZZZZZEEEEEEEZZZZZZSSSSSSSSFFFSXSSXXKKKKKKKKKNNNNNNGGGGGGGGY
							CCCCPCCCCCCXXXFFFFFFFOOOOOOOOOOOOAAAKKKKKFFFFFFFFFFFFTTTHHHHHCCCHCHHHHHQQYYYYYYYYYZHZZEEEEEEEZZZZSSSSSSSSSSSSFSSCSXHKKKKKKKKNNNNCCNGGGGGGGGG
							CCCCCCCCCCCCXXFFFFTTFOOOOOOOOOOOAAAKKKKKKFFFFFFFTTTTTTTTHHHHHHHHHHHHHHHQQYYYYYYYYYZZZEEEEEEEEEZZZSSSSSSSSSSOSSSSSSSHHHKKKKKKKNCCCCJGGGLGGGGG
							SCMMMCCCCCCVVFFFFFFTOOOOOOOOOOAAAAAKKKKKKFFFFFFFTTTTTTTTHHHHHHHHHHHHHHQQRRYYYYYBYYZEEEEEEEEEEEZZZSSSSSSSSSSOSSSSSSSSSKKKKKKRKKKSCCCCCLLLGGLL
							SCMMMMMTCTTTTTTFFFTTOOOOOOOOOOAAAAAKKKKKKFFFFFFFTTTTTTTHHHHHHHHXHHHNNNQQRRRRUNNBYMMEEEEEEEEEEEZCCCCCSSSSSSSOSSSSSSSSKKKKKKKRKKKCCCCXCLLLLLLI
							SSITMTTTTHTTTTTEETTTOOOOOOIOAAAAAAKKKKKKKFFFFFFFTTTUTTTTHHHHHHHHHHHHNNNRRRREEEEEEEEEEEEEEEEEEECCCCSSSSSSTSSSSSSSSSSSSKQQKKKKKSSCXCCJLLLLLLLL
							IIITTTTTTTTTTTTTTTTOOOOOOOOOAQAQQAKKKKKKKKKKKKKTTKUUDTTTTHUHSHHHHHHNNNRRRRREEEEEEEEEEEEEEEEEESSCCEEESSSSTTSSSSSSSSSSSSSSCKEEJJJJJJJJULLLLLUU
							IIIIMTTTTTTTTTTTTTTCOOXXOOOOQQAQQKKKKKKKKKKKKKKKKKUUDTTTUUUEUMMHHHHHNPRRRRREEEEEEEEEEEEEEEEEEISCCEEEESMMSSSSSSSSSSSSSSSSJJJEJJJJJJJJULLLLUUU
							IIIIMMTTTTTTTTTTTTDOOOXXOOOPQQQQQQYYYNKKKKKKKNNKKKUUUUUUUUUUUMMHHHHPPPRRPRREEEEEEEEEEEEEESSSSSSCCEEEEEEESSSSSSSSSSSSSSSJJJJJJJJJJJJUUUUUUUUU
							IIIMMMTRTTTTTTTTTTTEOOOOOOOIQQQQQQYYYNNNNNNNKNNKKKUUUUUUUHUUUMMMMPPPPPPPPFROSXXXXXXEEEEEEXSSSSSXCEEEEEEEBBSSSSSSSSSSSSSFJJJJJJJJJJUUUUUUUUUU
							IIITMMMTTTTTTTTTTTTZZZZDDIIIIQIQQQYNNNZNNNNNNNNNKKKUUUUUUUUMMMMMPPPPPPPPFFRRSXXXXXXEEEEEESSSSSXXXXXEEEEEEQSSEEESESSSSSSSRJJJJJJJTNUUNUUUUUUU
							IIITTTTTTLTTTTTTTTTZZZZIDIIIIIIQQYYNNNNNNNNNNNNNKKKUUUUUUUUUMMMMPPPPPPPPPSSSSSXXXXXXXXXXXXXSSSXXXXXXEEEEQQQEEEEEESSSSSJSSJJJJJJJTNNNNUUUUUUU
							IIIIIIIILLLLTTTTTTTZZZZIIIIIIIIQIAYYNNNNNNNNNNNNNKNUUUUUUUUUMMMPPPPPPPPPPSSSSSSXXXXXXXXXXXXXSXXXXXXXXEEEQQQEEEEEESSSSSSSSJJJJJJJJNANNUUUUUUU
							IIIIIIILLLLLZTTTTZZZZZIIIIIIIIIIIAANNNNNNNNNNNNNNNNUUUUUUUUUUMMMPPPPPPPPVVSSSSXDDDDXXXXXXXXXXXXXXXXXEEEEQQQQQEEEYSSSSCSDDDJJJJJJJNNNVVUUUUUU
							IIIIIIILLLKLZZZZTZZZZZIIIIIIIIIIIAANNNNNNNNNNNNNNNNUUUUUUUUMMMMMPPPPPPPPPVSSSSXDDDDXXXXXXXXXXXXXXXXHHQCQQQQQQQQQYSSSSSDDDFFJJJJNNNNNVVVVVUUU
							IIIIIIILSLKLZZZZTZZZZZZZIIIIIIIIAAANNNNNNNNNNNNNNUUUUUUUUUUUUMMMPPPPPPPPPSSSSSXDDDDXXXXXXXXXXXXXXKKKHQQQQQQQQQQQSSVSDDDDDDJJJJJJNNNNVVVVVVUU
							IIIIIIILLLLLZZZZZZZZZZLZLIIIIIIZQAANNNNNNNNNNNNNUUUUUUUUUUUMMMMPPPPPPPPPPPSGGSGDDDDXXXXXXXXXXXXXXKHHHQQQQQQQQQQQQSVDDDDDDDDJRRRNNNNNFVVVVVVV
							IIIIIILLLLLLZZZZZLZLLZLLLLIIIIZZZTTTTTNNNNNNNNNUUJUUUUUUUUUUNNMPPPPPPPPPPPSGGGGDDDDXXXXXXXXXXXXXXXHHHHHHQQQQQQQQQVVDDDDDDDRJRRRRNNNLVVVVVVVV
							IIIIIILLLLLLLLZZLLLLLLLLLLIIIZZZZTTTTTZNNNNYNNJJUJUJUUUUUUUNNNPPPPPPPPPPPFCCCGGDDDDGXXXXXXXXXXHHHHHHHHHHHHQQQQQQQQQDDDDDDDRRRRNNNNNVVVVVVVVV
							RRIPLILLLLLLLLLLLLLLLLLLLLLLIZTTTTTTTTTTZYYYYNJJJJUJJUUUUNNNNNNZPPPPPPPPDDDDDDDDDDDGXXXXXXXXXXXXHHHHHHHHHQQQQQQQQQDDDDDDDDRRRRRNNNRVVVVVVVVV
							RRPPLLLLLLLLLZLLLLLLLLLLLLLLIZTTTTZTTTTTZYYYYJJJJJJJNJUUJNNNNNNNRRPPPPCCDDDDDDDDDDDGXCOXXXXXXXXDDHHHHHHHHHQQQQQQQQDDWDDDDRRRRRRRRRRVVVVVVVVV
							RPPPLLLLLLLLLZLLLLLLLLLLLLLIIATTTTZTTTTTZZZZYJJJJJJJJJJJJJNNNNNNNRPPPCCCDDDDDDDDDDDGOOOXXXXXXXXXDDHHHHHHHHHQQQQQQQFDWWDDDRRRRRRRRRVVVVVVVVVV
							PPPPELLLLLZZZZLLLLLLLLLLLLLLAATTTTZTTTTTTTTPJJJJJJJJJJJJJJJNNNNRRRRPCCCCDDDDDDDDDDDDVOOXXOIOOXXXDDHHHHHHHHQQQQQQQQWWWWVVDRRRRRRRRRRVVVVVVVVV
							PWPPPPLLLLLLLZLLLLLLLLLLLLLLAATTTTZZZZTTTTTXXXJJJJJJJJJJJJSNNMMMMRRRRCCCDDDDDDDDDDDDOOOOOOOOOAXXKYHYYYHHHQQQQQQQQQWWWWWWWRRRRRIRRRRVVVVVVVVV
							PPPPPYLLLLLLLLLOLLLLLLLLLLLAAATTTTZZZZTTTTTXXXXXJJJJJJJNNJNNNMMMMMRRRRRRDDDDDDDDDDDDOMOOOOOOOAXXKYYYYYYYYQQQQQQHWWWWWWWWWRIIIIIIRZVVVVVVVVVV
							PPPPPPPPSSLLLLBBGGLLLLLLLTTTTTTTTTTZZZTTTTTXXXXXJJJJJJJNNNNNNMMMKKMRRRRRDOOOOOOOOODDDDDDDOOOOXXXKKYYYYYYMMQQQQHHWWWWWWWWIIIIITIZZZVVVJVVVVVV
							PPPPPPPPPSSLBBBGGGGLLLLLATTTTTTTTTTZZZZZXXXXXXXXXJSJJJJJJJNNNMMMMMMRRRRRDOOOOOOOOODDDDDDDHOOOPPKKKYYYYYYYYQQQQHHHWWWWWWIIIIIIIIWZZZZZVVVVVVV
							PPPPPPPPSSBBBBBBBBBBLLLLATTTTTTTTTTZZZZUUXXXXXXXJJJJJWJJJJBNNNNMMMMMRRROOOOOOOOOOODDDDDDDHOPPPPKKKKYYYYYYLQHHHHOHHIIWWIIIIIIIIIIIZZZVVVVVVVV
							PPPPPPPPPBBBBBBBBBBAALAAATTTTTTTTTTZZZZZXXXFFFJJJJJJJJJJJJBNNBMMMMMMRRROOOOOOOOOOODDDDDDDOPPPPKKKKAYYYYYLLQHHOOOHHHIIIIIIIIIIIIIIZZZZVVVVVVV
							PPPPPPPPPPBBBBBBBBAAAAAANTTTTTTTTTTZUZUXXXYBBFJJBBJJJBBBMMBLLDRRRRRRRRROOOOOOOOOOODDDDDDDIIIIIIIKKAYYKYYYLQAOOOOOOIIIIIIIIIIIIIZZZZZZZVVVVVR
							PPPPBBBBPBBBBBBBBBBAAAAANTTTTTTTTTTZUUUXXXYBBBBBBBBJJBBBBBBLLDDRRROOOOOOOOOOOOOOOOOOOOHIIIIIIIIIAAAAYYYYYAAAPPOOOIIIIIIIIIIIIIIIZZZZZZZZVVVR
							PPPPPTBBBBBBBBBBBBBAAAANNTTTTTTTTTTZZUUXXUBBBBBBBBBBBBBBBBDDDDDRDROOOOOOOOOOOOOOOOOAOOHIIIIIIIAAAAAAAAAAYAAAAAWWOWIIIIIIIIIIIOOIZZZNZZZZVVRR
							PPPPPPBBBBBBBBBBBBAAAANNNNNNNNNNNNZZZUUUUUBBBMBBBBBBBBBBBBDDDDDDDROOOOOOOOOOOOPOOOOOOOHIIIIIIAACCCAAAAAAAAAAAAWWWWWBIIIIIIIIIOOUUZZZZZRZRRRR
							PPPPPBBBBBBBBBBBBAAAANNNNNNNNNNNNNNUUUUUUUUBBMMBBBBBBBBBBBDOOOOOOOOOOOOOOOOOOOPOPOOOOOOIIIICICACUUUUAAAAAAAAAAWWWEWBIIIIIIIIIIUUUZUUZRRRRRRR
							VPPPBBBBBBBBBBBBAAAAAARRRRRNNNNNNNNUUUUUUUUUMMMMBBBBBBBBDDDOOOOOOOOOOOOOOOOOOOPPPPOOOIIIIICCCCCCCUUUUAAAAAAAWWWWWEWBIIIIIIUIIIUUUUUUUURRRRRR
							VPVPUBBBBBBBBBBBBAARRRRRRRRRNNNNNNNUUUUUUUUMAAMMBBBBBBDDDDDOOOOOOOOOOOOOOOOOPPPPPPOOOOIIIIICCCCCUUUMUUUAAAAVWWWEWEEIIIIITUUUUUUUUUUUUURRRRRR
							VVVVBBBBBBBDDDYBAAARRRRRRRRRNUNNUUUUUUUUUUUMMMMMBBBBBBBDDDDOOOOOOOOORKKKKRRPPPPPPLPPOOIIIIICCCCCCCUUUUUAAWSSSSSEEEEEEEEETTTUUUUUUUUUUURRRRRR
							VVVVBBBBBBDDDDDBOAARRRRRRRRRUUUUUUUUUUUUUUMMMMMMBBBBBBBDDDDDDDKNDRRRRKKKKKRRRPPPPLPPIIIIIIICCCCCCCUUUUUUUUSSSSSSEEEEEEETTTTEUUUUUUUUUURRRRRR
							VVVVVVVNDDDDDDDDOOOORRRRRRRRRUUUUUUUUUUUUUUUMZXXBBBBBHBDDDDDDHKKKKKKKKKKKKRRRRPPPPPPIIIIIIICCCCCCUUUUUUUUUSSSSSSSSSEEETTTTTUUUUUUDDUUURRRRRR
							VVVVVVVDDDDDDDDDOOOORRRRRRRUUUUUUUUUUUUUUUUUZZZBBBBBBBDDDDDDDDKJJKKKKKKKKKKKKKPPPPPPIIIIIICCCCCCCUUUUUUUNNSSSSSSSMMEMETTTTTTUUQQDDDDQUURRRRR
							VVVVVVVVDDDDDDOOOOOORRRRRRUUUUUUUUUUUUUUUUUUUZZBBBUUUUDDDDDDDKKJJKKKKKQKKKKKKGPPPPPPPIIIIIIICCCCCUUUUUUUNSSSSSSSSMMEMEETTTTTUUQQQQDDQQQPPPPP
							VVVVVVVHHDDDDDDOOOOOORRRRRRRJUUUUUUUUUUUUUUZZZZZXBZZZDDDDDDDDJJJJJKKKKKKKKKKKGFPVPPPPIIIIICCCCCQCUUUUUUUUTUSSSSSMMMMMMETTTTTUQQQQQDDQQQPPPPP
							VVVVVVHHHDDDDDDOOOOOOORRRRRRJUSJUUJUUUUUHUZZZZZZXZZAAADDDDGGDJJJJKKKKKKKKKKKKKFPPFPPIIIIIIIIIIIIUUUUUUUUUUUUSSSSSSMMMUCCTCTTTTQAQQQQQQQPPPPZ
							VVVVVVHHHDSDDOOOOOOOOORRRRRRJUSJJJJZUZZUZZZZZZZZZZZAADDDDDGGDJJJJKKKKKKKIKKFFFFPFFIIIIIIIIIIIIIUUUUUUUUUUUUUSSSSSSMUUUCCCCCCCCAAAQQQQQQQPPPP
							VVVVVVHHNNNODOOOOOOOOORRRRRRJJJJJJAZZZZZZZZZZZZZZZDDDDDDDDGGGJJJKKKKKKKKKKKFFFFFFFIFIIIIIIIIIIIIUUUUUUUUUYUNSSSSSSMSUUCCCCCCCCAAAAAAAAQQQQQQ
							VVNNNVHHNNNOOOOOOOOOOOOJAARJJJJGJJJZZZZZZZZZZPPPZZDDDDDDDDDGGJJJCCKKKKKKKKFFFFFFFFFFFIIIIIIIIIIUUUUUUUUUUUNNSSSSSSSSSCCCCCCCAAAAAAAAAAAQQQQQ
							VVNNNNNNNNNOOOOOOOOOOOOJJJJJJJJJJJJZZZZZZZZZZPPPZZZDDDDDDDDDDDDCCCCKKKKKCFFFFFFFFFFFGGIIIIIIIIIUUUUNNUNNNNNSSSSSSSSSSSCCCCCCAAAAAAAAAAQQQQQQ
							TTTNNNNNNNNOOOOOOOOOOOOJJJJJJJJJJJJZZZZZZZZPPPPPPPZDDDDDDDDDDDDDCCKKKKKCCCFFFFFFFFTFRRRRRIIIIAAAXUUNNNNNNNSSSSSSSSSSSZCCCCCCAAAAAAAAAZEQQQQQ
							TNNNNNNNNNNNNOOOOOOOOOOJJJJJJJJJJJJZZZZZZZZPPPPPPPPDDDDDDDDDDDDCCCKCCCCCCCCCFFFFFFRRRRRRRRIIIAAAUUUUNNNNNISSSSSSSSSSZZCVCCCCAAAAAAAAAZZQQQQQ
							TNNNNNNNNNNNNOOOOOOOOOOJJJJJJJJJJJJJJZZZZPPPPPPPPPPDDDDDDDDDDDDDCCCCCCCCCCCCFFFXFFIIRRRRRRAIIAAAFFUNNNIINISSSSSSSSSSSSSSCCCCCAAAAAAAZZZZQQQQ
							TNNNNNNNNNNNOOOPOOOOOOOLJJJJJJJJJJJJJZZZZPPPPPPPPPPDHDDDDDDDDYDDDCCCCCCCCCCCXSFXXXXIRRRRRRAAAAAAAANNNIIIIIISSSSSSSSSSSSSSSSCOAAAAAAAZZAZQQQQ
							NNNNNNNNNNNUUOPPPPPPOOOJJJJJJJJJJJJJZZZTTTPPPPPPPPPPHDDDDDDDDYDDDDCCCCCCCCCXXXXXXXXXRRRRRRRRAAAAAANNNIIIIIISSISSSSSSSSSSSSSSOAAAAAAAAAAAQQQQ
							NNNNNNNNNNNUUPPPPPPPPOOJYYJJJJJJJJJJZZZTTTTTTTTTPPNNDDDDDDDDDDDDDCCCCCCCCCCXXXXXXXXXXXRRRRRAAAAAAANNNIIIIIIIIISSSSSSSSSSSSRRAAAAAAFFAAAAAQQQ
							NNNNNNUNNNPUUUUPPPPPPPPPJJJJJJJJJJJJZZZTTTTTTTTTPPPNNDDDDDDDDDDDDCCCCCCCCCCXXXXXXXXXXXRRRRRAAAAAANNNNIIIIIIIIISSSSSSFFRRSRRRAAAEFFFFAAAAAAQQ
							NNNNNNUUPPPUUUPPPPPPPPPBIBJJJJJJTTTTTTTTTTTTTTTTPPPNNNYDDDDDDDDDDCCCCCCCCCCCXXXXXXXXXXRRRRRAAARJNNNNNIIIIIIIISSSSSSSFFRRRRRRRREEFFFFTAFFVRQQ
							NCNNNNUUPPPPUUUPPPPPPPBBBBCJBJJJTTTTTTTTTTTTTTTTPPPPYNYYDVDDDDDDDDCCCCCCCCCCXXXXXXXXXXRRRRRRRRRJJJNJJIIIIIIIISSSFFFFFFFFRRRRRRREFFFFFFFFRRRR
							CCCNUNUUPPPPUUPPPPPPBPBBBBBBBJBBTTTTTTTTTTTTTTTTPPPYYYYYVVDDDDDDDDCCCCCCCCCCBXXXXXXXRRRRRRRRRRRJJJJJJJIIIISSSSSSFFFFFRRRRRRRRXFFFFFFFFFFRRRR
							CCCCUUUUPPPPUUBPPPPPBBBBBBBBBBBBTTTTTTTTTTTTTTTTPPPYYYYYDDDDDDDEDCCCCCCCCCKKKKKKKKKKRRRRRRRRJJJJJJJJJJIJJJJSSSSSFFFFFRRRRRRRRXFFFFFFFGFRRRRR
							CCCUUUUUUPPPPUUPPPPPTTTBBBBBBBBBTTTTTTTTTTTTTTTTPPPYYYYYDDDDDDDDDCCCRCCCCCKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJJJOSJSSSFFFFFFRRRRRRRRRFFFFFRGRRRRRR
							CCCUUUUUPPUUPUIPPPPPTTBBBBBBBBBYTTTTTTTTTTTTTTTTBBPPPYYYYYDDDDDDDCCCCCCCCCKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJJJJJJSSSFFFFRRRRRRRRRRRQQFQQRRRRRRRR
							CCUUUUUUUUUUUNGGTPPPTTBBBBBBBBBYTTTTTTTTTTTTTTTTGBGYYYYYYYYDDDDDDDCCCCCCBCKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJJJJJJSSFFFFFRRRRRRRRRRRQQFQQRRRRRRRR
							UUUUUUUUUUUUUGGGTPTTTTTBBBBBBBBBYYYYTTTTTTGGGGGGGGGGGYYYYYYDDDDDDNCCCCCCBBKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJJJJJJJFFJFFFRRRRRRRRRRQQQQQQQRRRRRRR
							UUUUUUUUUUUUUGGGTTTTSTBBBBBBBBBYYYYYTTTTTTGGGGGGGGGGGYYYYYYYDGDDDDFCCCCBBBKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJJYJJJJJJJFRRRRRRRRHWRRRQQQQQQRRRRRRR
							UUUUUUUUUGUUGGGGGGGSSTSSBBBBBBBBYYYYYYYYYWGGGGGGGGGGYYYYYYYGGGGGGGFCCCCBBBKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJYYJJJJJJJJJRRRRRFRHWWWWQQQQQQRRRRRRR
							UUUUUUUUUUAUGGGGGGSSSSSSSBBBBBBYYYYYYYYYWWGGGGGGGGGYYYYYYYYGGGFGFFFFFCBBBBKKKKKKKKKKKKKKKKKKKUJJJJJJJJJJYYJJJJJJJJJJRRRRRWWWWWWWQQQQQRLLRRRR
							GUUUUUUUUUGGGGGGGGSSSSSSSSBBBBBYYYYYYYWYWWWGGGGGGGPPGJYYYYYYFFFFFFFFFFFFBBBBBBBBBBBKKKKKKKKKKUJJJJJJJYYYYYYJJZJJJJJJJRJRBWWWWWWWWWTQQLLLLRRR
							GUUUUUUUUUUGGGGGGGGSSSSSSSSRYYYYYYYYYYWWWWWWGGGGGGGPGGYYYYYFFFFFFIFFFFFBBBBBBBBBBBCKKKKKKKKKKUUJJJJJYYYYYYYJJZJJJJJJJJJWWWWWWWWWWWQQQLLLLLLR
							GGGUUUUUUUGGBBBCCBBSSSSSSSSRYYYYYYYMMWWWWWWWGGGGGGGGGYYYYYYFFFFFFFFFFFFFFFBBBBMMBBCKKKKKKKKKKUUUJJJJJJJYYZZZDZZJJJJJJJWWJWMWWWWWWWLLLLLLLLLL
							GGGGUUUUGGGGBBCCCBBPPSSSSSSSSSOOMMYMWWWKKWWWGGGGGGGGEEEEYYYDFDDFFFFFFFFFFFBBBBBMBBCCCKKKKKKKKUUUJPPJJYYYYYZZZZJJJJJJJJJWJWWWWWWFWWWLLLLLLLLL
							GGGGGUUUGGGGBBBCBBBBBBRSSSSSSBMMMMMMMWWKKKKWGGGGGGGEEEEEYYYDEDMMMFFJJFFCMMBBBMMMCCCCHCUUUUUUUUUUUUPPPPPYYYYZZZZZJJJJJJJJJJWCWWWWWLLLLLLLLXLX
							PGGGGGGGGBBBBBBBBBBBBBBSSSSSSBBBMMMMMWKKKKKKGGGGGGGGEEEEEDRDDDMDDDDJJJJJMMMMMMMMMCMCHCUUUUUUUUUUUUPEPEYYYYYZZZZJJJJJJJJJJJWCCWWWWTTLLLLLXXXX
							PGGGGGGDGGBBBBBBBBBBBBBIISSSSSBBMMMMMKKKKKKKKKGGGGGBBBBEBDDDDDDDDDDJJJJMMMMMMMMMMMMHHCUUUUUUUUUUUUPEEEEYYYYZZZZJJJJJJJJJJJCCCWWWWTTLLXXLXXXX
							PPPGGDDDDBBBBBBBBBBBBNBISSSSSSBBBMMMMKKKKKKKKKKGGLBBBBBBBDDDDDDDDDDJJJJMMMMMMMMMMHHHHHHHHHUUUUUUUPPPEEEEEYZZZZZJJJJJJJCCCJCCCWWCCTTTLLXXXXXX
							PPPGGDDDDDDBBBBBBBBNNNBBSSSSSSSBBBMMMMKKKKKKKKGGABBBBBBBBDDDDDDDDJJJJJJJJMMMMMMMMMHHHHHHHHHUUUUUUUPEEEEECZZZZZZZZJJJJCCRCCCCCCCCTTTTLLLXXXXX
							PPPPGDDDDDBBBBBBBBBNNNNNNMSSSSSSBBMBBBBKKKKKKKKKKKBBBBBBBBBYDDDDDJJJJJJYYYYMMMMMMMHHHHHHHHHUEEUUUZEEEEEECZZZZZZZZZJJJCCCCCCCCTTTTTTTLXXXXXXX
							PPPBPRDDDDKBBBBBBBBBNNNNMMSSSSSBBBBBBBBKKKKKKKKKKKBBBBBBBBBDDDDDDJJEEEEYYYYMMMMMMMHHHHHHHHUUEEEEEEEEEEEZZPZZZZZZZZJJAECCCCCCCHHHTTTTWXWXXXXX
							PPPPPRRDDVBBBBBBBMMMMNNNMMMSSSZZBBBBBBBKKKKKKKKKKBBBBBBBBBBDDDDDDJEEEEEEEEYMAMMMMMMMMHPHHHUUUEEEEEEEEEEZZZZZZZZZZZZJAACCCCCHHHHTTTTTWWWWWWWX
							PPPPPPPPVVVBBBBBBNNMMMMMMSSSZZZZZBBBBBKKKKKKKKKKKBBBBBBBBBBBDDDDDJJEEEEEEEEMAMMMMMMMMMMMMHHUUEEEEEEEEEZZZZZZZZZZZZZAAAACCCCCHHHTTTTTMWWWWWWX
							PPPPPPPPPVVBBBBBBMMMMMMMMMMZZZZZZZZZBBKKKKKKKKKKBBBBBBBBBBBBDGDQEEEEEEEEEEEEEOMMMMMMMMUCUUUUUUEEEEEEEEEZZZZZZZZZZZZBAAAAACCCCAHHHHWMMWWWWWWX
							PPPPPPPPNDDDBBBBMUMMMMMMMMMMMZZZZZZBBZKKKKKKKKKKBBBBBBBBBBBLDBUEEEEEEEEEEDDDDDDDDDDDDMUUUUUUUEEEEEEEEEEZZZZZZZZZZZAAAAAAAACAZAHHHWWWWWWWWWWW
							PPPPPPPPDDDDGBBBMMMMMMMMMMMMMMZZZZZZZZKKKKKKKKKKBBBBBBBBBBBBBBBEEEEEEEEEEDDDDDDDDDDDDJUYUUUUUEEEEEEEZZZZZZZZZZZZWWAAAAAAAAAAAAAAWWWWWWWWWWWW
							PPPPDDDDDDDGGBBMMMMMMMMMMMMMMMZZZZZZZZZZKKDKKKKKBBIBIIBBBBBBBQEEEEEEEEEEEDDDDDDDDDDDDJJYUUUUUEEEEEEEZHZZZZZZZZZZZAAAAAAAAAAAAAAAWWWWWWWWWWWW
							PDDDDDDDDDDDDDDFFMMMMMMMMMMMMZZZZZZZZZZZKKKKKKKDDIIIIIIBBBBBBBEEEPPEEEEEEEEEEEEDDDDDDYYYUUUUUUEEEEEEZZZZZZZZZZZZZAAAAAAAAAAAAAAAGWWWWWWWWWWW
							PDDDDDDDDDDDDFFFFMMAAAMMMMPMZZZZZZZZZZSZZWKDDDDDDIIIIIIIIBBBBBHEEPPPEEEEEEEEEEEDDDDDDYYYUUUUUEEEEEEEZZZZZHHHHHHHHHHAAAAAAAAAAAAAWWWWWWWWWWWW
							DDDDDDDDDDDDDFFFFFFAAAMMMMMMZZZZZZZZZZZZZZZZDDDDDIIIIIIIIIBBLBPPVPPPEPEEEEEEEEEDDDDDDYYYYYUUUUUHEEEEZZZZZHHHHHHHHHHHAAAAAAAAAAAAWWWWWWWWWWWW
							DDDDDDDDDDDDXXFFFFFAAAMMMMMMZZZZZZZZZZZZZZDDDDDDDIIIIIIIIILLLPPPPPPPEPPPPEEEEEEDDDDDDYYYYYUUUHHHHHZZZZZZZHHHHHHHHHVVVAAAAAAAAAAAWWWWWWWWWWWW
							DDDDDDDDDDDSXXFFFFFAAAMMMAZZZZZZZZZZZZZZOZIIDZZZZZIIIIIIIILLLPPPPPPPPPPPPPPPEELDDDDDDYYYYYUUUHHHHHZZZZZZZHHHHHHGHVVAAAAAAAAAAAAAAWWWWWWWWWWW
							DDDDDDDDDDXXXXFFFFAAAAAMAAAZZZZZZZZZZZZZOOIIDDZZIIIIIIIIIILLLPPPPPPPPPPPPPPPGLLLGDDDDYYYYYUHHHHHHHZZZZZHHHHHHHHHHVVVAAAAAAAAAWWAFWWWWWWWWWWW
							DDDDDDDDDDDXXXFFFAAAAAAAAAAZZZZZZZZZZRRIIIIIIIZZZZIIIIIIIILLZPPPPPPPPPPPPPGGGGLLGDDDDYYYYYUUUUUHHHZZZZZHIHHHHHVVVVVAAAAAAAAAAAAFFWWWWWWWQWQW
							DDDDDDDDDDDXXFFFFAAAAAAAAAAAAAAZZZZZZIIIIIIIIIIIZZIZIIIIIILZZZPPPPPPPPPPJPGGJGLGGGGGYYYYYYUUUUUIIIPIIIHHIIHHHVVVVVUUUUAAAFAAAFFFFWWWWWWQQQQQ
							NDDDDDDDDDDXXFFFFAAAAAAAAAAAAAAZZZZIIIIIIIIIIIIIIZIZZIIIIIZZZXPPPPPPPPPJJPDGGGGGGGGGNGYYYYYUUUUUIIIIIIHHIIIIHVVVVUUUUUUUAFAAAFFFFFFWQWQQQQQQ
							DDDDDDFDDFFFXFFFAAAAAAAAAAAAAAAXZXZZIIIIIIIIIIIIZZZZZIIIIZZZZPPPPPPPPPPPPPGGGGGGGGGGGGYYYYYUUJJUIIIIIIIIIIIVVVVVUUUUUUUUAFFFFFFFFFQQQQQQQQQQ
							VVDDDDDFFFFFXFFFFAAAAAAAAAAAAXXXXXXZIIIIIIIIIIIIIZZZZZZZZZZZZPPPPPPPPPPDGGGGGGGGGGGGGGYYYYYUJJJJJJIIIIIIIIIIIIVVUUUUUUUUUUUFFFFFFFFQQQQQFFQQ
							VVDDDFFFFFFFFFFFFAAAAAAAAAAAXXXXXXIIIIIIIIIIIIIIZZZZZZZZZZZZZPPPPPPPPPPPDGGGGGGGGGGGGGGGYYYUUUJJJJIIIIIIIIIIIVVVUUUUUUUUUUUUFFFFFFFQQQFFFFFQ
							VVFFFFFFFFFFFFFFFAAAAAAAAAAAXXXXXXIPIIIIIIIIIIZZZZZZZZZZZZZZZDDDDDDPPPPDDDGGGGGGGGGGGGGGYYYYYJJJJIIIIIIIIIIIVVUUUUUUUUUUUUUUUFFFFFFQFFFFFFFF
							FVFFFFFFFFFFFFFFFAAAAAAAAAAXXXXXXXIIIIIIIIIIIIZZZZZZZZZZZZZZZDDDDDDDPDDDDGGGGGGGGGGGGGGGYYYYJJJJJJJIIIIIIIIIVUUUUUUUUUUUUUUUUFFFFFFFFFFFFFFF
							FFFFFFFFFFFFFFFFFFAAAAATTAXXXXXXIIIIIIIIIIIIIIIZZZZZZZZZZZZZZDDDDDDDDDDDDDGGGGGGGGGGGGGGJJJJJJJJJJJJIIIIIIIIVVVUUUUUUUUUUUUUFFFFFFFFFFFFFFFF
							""";
}