import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

class Day4 {
	
	public static void main(String[] args) {
		
		System.out.println("Sample phase one result, expected: 18, actual: " + countXmas(parse(sample)));
		
		System.out.println("Actual phase one result, expected: 2642, actual: " + countXmas(parse(input)));
		
		System.out.println("Sample phase two result, expected: 9, actual: " + countXmas2(parse(sample2)));
		
		System.out.println("Actual phase two result, expected: 1974, actual: " + countXmas2(parse(input)));
		
	}
	
	public static int countXmas2(char[][] input) {
		var result = 0;
		
		for(int line =0; line < input.length - 2; line++) {
			for (int column = 0; column < input[line].length - 2; column++) {
				var xShapedSlice = new char[]{
					input[line][column], input[line][column + 2],
					input[line + 1][column + 1],
					input[line + 2][column], input[line + 2][column + 2]};
				
				/*
					All possible combinations for the "two MAS in the shape of an X"
					M.S S.M S.S M.M
					.A. .A. .A. .A.
					M.S S.M M.M S.S
				*/
				boolean isXmas = 
					Arrays.equals(xShapedSlice,"MSAMS".toCharArray()) ||
					Arrays.equals(xShapedSlice,"SMASM".toCharArray()) ||
					Arrays.equals(xShapedSlice,"SSAMM".toCharArray()) ||
					Arrays.equals(xShapedSlice,"MMASS".toCharArray());
				
				if (isXmas)	result += 1;
			}
		}
		
		return result;
	}
	
	
	public static int countXmas(char[][] input) {
		var result = 0;
		for(int line =0; line < input.length; line++) {
			for (int column = 0; column < input[line].length; column++) {
				
				boolean fitHorizontal = (input[line].length - column) > 3;
				boolean fitVertical = input[line].length - line > 3;
				var fitHorizontalBackward = column >=3;
				
				if (fitHorizontal) {
					var horizontal = new char[] {
						input[line][column + 0], 
						input[line][column + 1], 
						input[line][column + 2], 
						input[line][column + 3]};
					if (isXmas(horizontal)) {
						result +=1;
					}
				
				}
				
				if (fitVertical) {
					var vertical = new char[] {
						input[line + 0][column], 
						input[line + 1][column], 
						input[line + 2][column], 
						input[line + 3][column]};
					
					if (isXmas(vertical)) {
						result +=1;
					}
					
				}
				
				if (fitHorizontal && fitVertical) {
					var diagonalRight = new char[] {
						input[line + 0][column + 0], 
						input[line + 1][column + 1], 
						input[line + 2][column + 2], 
						input[line + 3][column + 3]};
					if (isXmas(diagonalRight)) {
						result +=1;
					}
				}
				

				if (fitHorizontalBackward && fitVertical) {
					var diagonalLeft = new char[] {
						input[line + 0][column - 0], 
						input[line + 1][column - 1], 
						input[line + 2][column - 2], 
						input[line + 3][column - 3]};
					if (isXmas(diagonalLeft))  {
						result +=1;	
					}
				}
				
			}
		}
		return result;
	}
	
	public static boolean isXmas(char[] candidate) {
		
		var result = 	Arrays.equals(candidate, "XMAS".toCharArray()) || 
						Arrays.equals(candidate, "SAMX".toCharArray());
		return result;
	}
	
	public static char[][] parse(String input) {
		return Stream.of(input.split("\n"))
					.map(String::toCharArray)
					.toArray(char[][]::new);
	}
	
	static String sample =	"""
							MMMSXXMASM
							MSAMXMSMSA
							AMXSXMAAMM
							MSAMASMSMX
							XMASAMXAMM
							XXAMMXXAMA
							SMSMSASXSS
							SAXAMASAAA
							MAMMMXMMMM
							MXMXAXMASX
							""";
	
	static String sample2 =	"""
							.M.S......
							..A..MSMS.
							.M.S.MAA..
							..A.ASMSM.
							.M.S.M....
							..........
							S.S.S.S.S.
							.A.A.A.A..
							M.M.M.M.M.
							..........
							""";					
	
	static String input =	"""
							SAMMSSSSXSAMXMSMASMMSMSSMMSSXMASMAMMAMSSMSMSAXSSXMXMSASXMSAMXSMMXAMXMMSMSSMXSAMXMMASMSMSMMMAXAAXXASXMMXMSSXMASMMSMXAMSMMMMMAMXSASAMXMMXSAMXM
							SAAXAASAASXAAAAAMAAXMAAAMSAAAXAXMAXMSASAASAXAMAMXMSMMAXMAXAMXXAASMSMMAAXXAASASXSXSAMXSAMXAXMSMSMSMMASAMXMSXXAAMSAMMMSMMMAAMAMAXAMXSAMXMAMMAM
							SMMMMSMMMMMSMSXSSSSMMMMMMMMSMMASMXXAMXSXMMMMSAMXAMAAMMMAMSSMMSMMSAAXMSSSSMXMAAXSAMXSAMMMMSXXAAAAAAMMMSSMAMMSSMMSASXMAAMSSXSASMMXMASASMAMMMAM
							SAAXXMASAXAXMMMXAAXASAXXAMXXXXMMASMXMASAMXXAMSXSSSSSMAXAMXXAAAAAMXMMAAAAXXXMMSAMXMMMMMSAAMXSMSMSMSMMAXXMAXMAMAASXMASXSMAMXSASMSSMASXMXSXSSSS
							SSMSSXASXMXSASAMMMMMAAXSAMXXMASMAMMAMXXXMMMMSMXAAMAAXXSSSMSMMSAXSAXASXMMMMXXSXMSAXAMAMMMSMASAXAMAMAMXSXXSAMASMMSASMMAMMSSMMAMAAMMASXMMMAMAAS
							XAXXAMXSMXXSAXXSASMSMAMXAMXMMASMXMMSSSMAXMASMMMMMMSMMMAMAMAAXXXSXMMMMAMXMXMMMAASASXMMMXMAMAMAMAMASMMSMMAXMMASAMSMMXMSMAAAXMAMMMSMXXAMAMAMMMM
							MSMAMMMMAXMMMMSMASAAXAMSSMSXMASMSMAAAASMMSMSAXMASAAAAMASASMSMASMMXAASAMXAAAAMMMMAAMXXSMSXMASXXASAMAXAAMAMXMASAMXSMXSXMMSSMMMMSAMMSSMMXSAXMAS
							SAAXXAAXMXMAMMAMAMMMMXXAXAXXMASAAMMMSXMXAAMMMMMAMXSSXSASASAAMXMAMSMMMMMSSXXSXXXMSMSXMAMSAMXMASMMMSMSXXMASMMASAMASMMMXMAMAMSMMMAMAMMASAMMSAMS
							SMSMXSSSMXSASAMMMSAXASMSMSMSMMMXMXXXXMMMMMXAAMMAXMMMXMAMMMXMSASAMXSMSXAAXMAMMSXMASMXAMXMAMXSAMXAAMXSXASASXMAMAMAXAASXMASAMAAXSMMAXSAMASAMAMX
							MMAMAAAAAMSAMXXMASAMXSAMAMMAAMAAMMMXMASMSMMSSXXAXSMSSSSXMASXSMMASAMSAMMXSAAMASAXMASXSSMXASAMXSXMXXAMSXMASXMAMSMMSSMSAMAMAMSAMXAXMXMMSAMASMMS
							ASASMMSMMMMSMXMXASXXXMAMAMSXSMSMSAAAAXSAAXAXMXMSMSAAMAMXMAMMXMSMMASAMMSXSMXSASAMXAXXAAMSMMMSMMASMMMXAXMMMXMXXMAMAAASAMXSMMXMXSMMSSSXSMSAXMAS
							MMMSXAAXAMXAMASMASAXSSSMSXMAAAMAMXSMSMMMMMMXMXAXAMMMMAMXMAXAAMAXMXMMXAXXMAMMMMAAMMSMMMMAAAAAMMAMXAMASMSMMXAMXSAMMMMMASAAXAXSASAMAAXAMAMXMMAM
							XSSSMSSSSSSMSASMSMMMMAMAXAXSMMMSMAMAXAMAMASAAMSMSMSXSASASMSSMSSSXAAXMXSMMAMAASXMXMAXXAXXXMXSMMASMMMAXMAAMMSAMSAMXSXMAMXSMSSMASAMMXMAMAMXAMAS
							XSXMAMXAMAAAMMMXAAMSXMMSMMMMAMAXMAMMSSMASASMMMAAAXXASAXAXMAAAAMMXAMXMASAMASMXMXMAXXSSSSMXSAXASXSAXMSSMSXSAAXAXAXMSAMXSXMAXAMASXMXMSASASMMSAS
							SMMMSMMMMSMMMSMSXSMMAMXXAMASMMMSSXSAAXXXMAMXASXSMSMMMSMXMASMMMAAMSMSMAMAMXXXSXASMSMMMAAAAMMSAMAMXMAAAXAMMMMMSAMMXMAMAXMSMSAMXXAXXASASXMAXMAS
							MAAAAAASAXAAXAAXAXMSMMASAMASXMXMASMMMSAMMXMSASAMAXAAAMXSAXXXXMMXMXAXAMXMMSMAXSMXMAAAMXMMMSMSMMMMMSMXSMMSSSXAAAAXMSMMXMASAMMMMMSMMMMAMXMAMMAM
							SSMSXSMMSMSMSMSMXMAXXMXSAMAXMAMXXXXXAAMSSMAMAMMMSMSMMMAXMASXMSSMSMMMXSAXAAMXMXXASMSMSAMSMAASAMXAAXMXXAXAAMMXSSMMMAMASMAMAMSMSAAAXXMXMMSMAMAS
							XMAXXAXAXMAASXMMSMMMMSMSXMASMSXSXSSMMSXAXMAMAMAMAMXMXMXMMAMAAAMAAAXAASAMSSXSMXSXSXAASMSASMSMAXSMSSMAMMMMXMMXAXAXSAMASMXMASAAMSSMMSMXMAAXMSAS
							SMAXSXMASAMMMAMAMAMXAAASASXMAXAXAAXAXAMXXSASASASXSASASAAMASMMMSSXXMMMSXAMMMAMAMAMMMMSXSASXXXSMXAXXMAMAXXASAMMMMMSAMASMXSMSMSMAMASASAMXMSMMAS
							XMMMAXMASMXAXAMAXAMMSSSSXMAMAMMMSMSXMMSMMMXSXSASMSASAMSAMXSXAAXMXSASXSMAAASAMMSAMXSAMAMAMAMMAMXSMSXMXXMXMSAMXAAMXAMASAMXASAAMAXMSASXSAMAMMAM
							SAMMMXMASXSASXSSSMSAXMAXAXAMXSXAMXSMAAAMASXMAMMMXMAMXMMMSMXXMMSAASAMASASMMSAMASASXMASMMMMSMAMMAXAXASXXSAAMAMSXSMMXMXXASXSMSMSMSAMAMAMMSMSMMS
							SAXMXAMXSMMXAXXAAAAMXAAMMSSSXMMMSAMXMMSSMSAXAMXSXMMSMMMASAXXMMMMMSSMSMMXAASXMASXMASAMAAAAXXSXMMSSMMMAASMMSMMSAAASXMMSAMMXAXMAXMAMAMAMXAASAMX
							SXMMSASASXXMASMSMMMSMMSSXAXMAMMMMXSAMXAMXSMMXSXAXMASASMASXSMMAXMAMAMXMSSMMMAMASXXAMXSSMMXSSMAAAAAXXMMMMXMAMAMMMMMAAXMMMSSMMSSXSSSMSASXMMSAMX
							SAMXAAMASXMAXSAAXMAMXAAMMSMSAMAASASASMXSAMXMASXXMMXXAMMAXAXAXMSMMSAMAMMMAXSXMAXAMAMXXMXMASAMMMXSMMMXMMMXXAMAAAXXSMMMSXXXMAAXMAXAAAXMMXXXSAMX
							SMMAMMMMMAMSMMMSSMXSMMSSMXAMMSSMSASMMMAMASASAMXASXSMMMMSXSXSAMXAXSASMSASAMXAMSMXAAXMASAMXMASAMXMXAAXXAXMSMSMSMSASAMASMSMSMMSMSMSMMMAXASASAMX
							SMSMSAXSXXMAXAAMAMAAAXAXAMAMMXMAMXMMAMXXAXXXAMMXMAAMAXXXAMAMAXMXMMAMXXASXASXMAAMSXSAAMMSXSAMXSSSSSSMSXSAAXAXAMXMSAMASXSAAAXXAMXMASMSXAXAMAMM
							XAASMMXAXSSSSMXSAMXSSMMMMMSXAAMXMAMMASAXSSMSAMMAMSMSASXMAMASMMMSSMMMSMAXXMXMMMSMMASMXMSAMMXMXSAAAXXXAAMXMSXSMMAXSXMASAXAMSMXSSXSASAMMSMMMSMM
							MMMSMXSMXMAMXMAMXSAMMMXAXSAMMXMAXAMSAXMMMAMMMMMMMXAMXXXMMMMSAAAAAAXAXMXMSMSXAXAAMAMMMMMASXSSXMMMMMMMMSMMXSAAXSSMSXSMMMMSXXAMXAASAMAXMAAAAMAX
							ASXMMAMXAMAMAMAMAMXSASAXXAMXXAASXMMMMXSAMAMXSAXMAMXMSSSXSAAMMMMXSMMMSXAAAASXSMSXMAMAAASXMAAMMSXXXAAAXXASAMXMMAMXXAAMAMMMMMAXMMMMXMAMMASMXSAM
							MAAAMMXSASASXSMSSMMSASMXMAXXMMMMMXAMXASASMSMSASMMXAMMAAASMSSMSMXMXXAXMSSMXMAXAMXMMXSXMSAMMMMAMXSXSXSSXXMAMAXMSSMMMMMAXAAAXSMAXASXMXXXMAMXXXA
							SSSMXAXAAAMAAAAAAAXMXMASMSMSMAAASXSSMMSMMXAAMAMAMSSMMMMMMMAAAAMMMAMMMXMAXXMAMAMMXSAMASXXMAMMXSAMMMXMMMSMSMSSXAAXXXASXSSSSSXAXMAXASAMXXAXMAMS
							MXMAMXMMXMSSSMMSMMMMMMAXXAAAXSMMMAAMAXMASMMMMAMXMAMXSMXMASMMMMMAMASAAMSAMSMMSMMSAMASMXMASMMSMMMMAMMXAAXAAAXAMMSAMXMXAMAAMMMSMMSSMMAMXMMAASMM
							SSMMSSMXAXAAAMXMASAMXSSMSMSMXMXSMMMMSMMMAAXASMSSMAMMMAASXSXMXSMMXASMSXMASAMAMXAMASASAASMMSASASMSSSMMXMMSMSMSAXXAXAMMSMMMMAAAAXAAMMAMAASMMXAS
							AAAAAAAMMMMSMMAMXXASAXAAMAMMASAMXXAAAASMSSMASAAMSMSMMSMSMMMMASASMAXXXMASMXMASMXSAMAMMXSMAMXSAMAAMAASAMXXXXXASAXAMXXAMASMSMSSSMSSMSMSXMXMASAA
							MSMMSXMXMAMAMMASXMMMXSMXMAMMAMAMSXMSSMMMAMMXMMMMSMAXXAMXXAAMSMAMAMSMMSMMMXSAMMAMXMAMMXXMAMAMAMMMSSMMXMASMXMMMXSSMXMXMXMAXAXMAMXXAAAXMSSMMMSM
							XMXXXMMAMXXAXSASASAAXMAMSXSMASAMXAXAXXXMAXXSASXAMSMMXASXMSMSAMAMXXAAAAAAMMMMSMXMMMMSXMXSAMASAMAXMAMMMMAMMXMXMAXAAAMSAMMMMSMMMSAMMMXMAXAASAMX
							MSMMSASMSSMMMMASAMMSMAMXSMXMAXXSSXMASXSSSSMAAAMXXASAXXMAAAXSXSASXSSSMSSMSXAASXMASAMXAMASMSMSASXSSMMAXMSXSAXAXXSMMMAAAMXXMXAXXMASXMASXSSMMASX
							AAAAXMASAXAAAMMMMXXAAXMASMMMSSXMASMXMAAAASAMXMMMSASMSMAMMSXMASAMXAAAMXMASMMMSAMAXAXXAMASXSAMASAMXXSMSAMASXSMSAMXAXMSXMAXMMXMXXAMXMASAMAASXMX
							SMSMMXMAXSMMXXSAMMSMSSMAXAMAAXXXAXMAMXMMMMMSAXAXMMMAAAXMAMAMAMXMMMMMMXMAXAAAXAMMSSMSSMXSAMXMAMAXAXSXSAMAMAAAAAMAXSXMMXSAASMMSMASXMASASXMMMAS
							XXMMAAXSAMMSAMMASAXSAMMSSMMMSSMMMMSMMAMSXMXSXSMSAMMSSSMMXXMMSSMSXSMXMAMXXSMXMAMAAAXAXXAMXMAMXSSMMSXAXAMXMSMMMSMXSMAXAAMMAMAAASAMAMXSMMAMXMAM
							SAMXSXMMASAMAMSAMMSMMSAMAMXMXMAAAAAMXMMSAMMSAXAXXMMAMAAXXXSAMAAXAXAASXMMXMAMSSMMSSMMSMXMAXAMASXASXMMMMMXMXAMXAMSAXXMMSXMXSMXMMMXMXMXASAMMMSS
							MAMMXAXSAMXXSMMASMSAAMMSAMAAAMSSSSXSASXSAMAMMMMMSXMASAMMMMMASMMMMMSMSAAXAMAMAXAAAMAASAMSMSMSMSMSAMXAAXAXXSMXXAXSAMXSMXXSAMASXASAMXAMAMXSXAXX
							SAMASMMMASXSAMXSXASMMXASAMASMXAMMXMMMSASMMXSXAXAMASAMAMAXASMMMXAXMXXSMMMMSSMMSXMMMSMMXMAMAAAMMMXXSSSXMXXASAAMSMMAMXAAMXMASASAAXAMMSAAMAMMMMM
							SXMMSAAXSAMAMXMXMMMMSMASMMXAMMMMXMMSAMXMMXAAXASASAMASXSMSXSMAMSASMMMMAMXXAXXXXMXXAMMSMSXSMSMSAMXXMAMSXSSMMMMXXAMXSXMMAASAMXSMSSMMAMSMMAMAAAA
							MMSASMMMXMASXSAXAXAASMMMMMMAMSSSMAXAXXMASXMMMMSAMASAMAAAXAMMAMMAAXMXMAMMMMSMSAMXMASAAMMXMXAAXMSMXMAMMMMAAAMMASAMAXMXSSMSASXXAXAXSXMAXSMSSSSS
							XAMASXMASXAXXXSSXMMMSAMXAXSAMSAASMMSXXMSAMXAAAMXMXSXMSMXMMSXSAASXXSASASMAMXAXAMXXXMMMMSAMSSSSMAASMXMAAMXSMSMASAMXXAMXXMSXMMMSMXMXAMXMSAMXAMX
							XSMXMAMAMAMMXMASMAMASMMMMXMASMSMMMAMMAMMMAMSSXMXSAMAAXMSMXAAMXMMMXSASAXXAMMSMSSMSMAASAXAXAAXAXXSMMASXSSMAAAMXSXMAMSMMSXMSAAXXAMSSSMMSMMSXMAM
							SMSMSMMASAMAMXAMMAMXXAXSMMSMSAXMXMASMAMSSSMMMASMMASAMXAAMMMXMAAAXMMXMMSXMSXMXAAAAMXMMSSMMMXSMMSMMXMSXAAXMXXSAMAMMMMAAXAASXXMMSMAAAXMAAMXASMS
							SASMAXMAXAXSAMSSSMSMSMMSAMAAMAMXMMMSXMSXAAMASAMAXAMAMASASXXASMSSSXMXAAMAAAXMMSXSSXSXAAMAXAAMXMXAASXMMSMMXAMXASAXMASMMXMMMSSXAXMMSMSSSSMSAMAM
							MAMSXSMSMSAMXSXAAXAMAAMXXXMSMSMMXMMMAXAMSMMAMASXSSSMMAMAAASMSAXXAASMMASMMMXSAMXAAASMMMSAMMMSAMMAMXASAXSAMSAXAMMSSMSASAAAAXMMSXXMAMMAAXMAMMAM
							MAMSXMAAAXXMMXXMMSMXSXXXMASMMAAAAXSMSMMAMAMXXMMAAAAXMASMMMMMMXSMSMXAMAAXAXAMASMMMMMAXXXMASAAAXMXSMSMSMMMMXAMSAMXAAMAMXSMSMXMASXSSSMMMMASASXS
							XMMMMMAMMMMAASASAXMAMSMXAMXASXSMSMMAXSMASAMMSSMMMSMMMMXMAXXXAAXXAXSXMMXSXSMMXMXXSXSSMSMMMMMSMMMAAXXAXXMASMXMXAXSMMMSMMXAXXXXMAXXMAMXMXAMXAAM
							MXAXAASXSAXXXAMMXSMASAMASMSMMAAMXAMMMAMASAMAAXAMAMAMXAAMSMSMMSSXMMMMMSAMAMAMMMXAXAAXAMXAAXMAMAMMSAMAMMSASASAXXXMASAAAMMXMASMSSSXSXMAMMXMAMXM
							ASMSSMAMMMMSAMXSXSMXSAMMXAAXMSMMSMMXXXMASMMMXSAMXSMSSSXSAMAAXAXAMXMAAMASXSAMASASMMMMXMASXSSMSMSXMAMXMAMMSMMXMSMMAMMXAMXAAAXXAAXASXSMMSMASMMM
							XAAAAMAXAMAMXXAXXXXASMMXMSMMXAAXMXSAMXSAMASXMXMXXMAMXAMSASMSMSSMMASMSSXMXSASXMAMXXAAXSXMAXMASXMASXMSMMXAMXXAMAAMSSSSSSXSSXMMMSMMSASAASXXMASM
							SMAMXSMMMXXSAMMSMSMXMXXAXMASMSMMMAMASAAASAMMMAMSMMMMMMMSAMAXMAAASXSAAMAMXSMMMMSMMSASAMAMAMMAMAMXMAXXAAMSSMSMSXSXAAAAAAXMMMMSMAAXMAMMMSAXXXAM
							XXSXMAAMSMMAXSMAAAAXSMSMXSAMAAMAMAMXMMMXMAMAXAXAAASASAAMMMAMMSSMMMMMMXXSAMXXMAMAAAXMASAMASMMXMMSMSMSXMXAAXXXAXMXMMMMMMMMAAXAXMSMMXMXMMMMASMS
							SMMASMAMAAXMMAXMSMMXAAAAAMASXSSSSMSAAXSXMAMSMMSMSMSAMMSSSMSSXMMMAASAMXSMMMXMMSSMMSXMAXXXXSASXSMSAAASXMMSMMMMSSMAXAAXXAASMMSMXMAAXAXMXAXXXMXS
							SASAMXAMMXMXXXAMXASXMMMSMSXMAMAAAMXAMXAMMSXXAASAXMMXMXAAXAXMXAASXSSXXXAAAAAXMAMAMXAMSSXSMMAMAAAMSMMMASAMMXSAMAXMASXSXMXMXAAXASXSMMSMSMSMXAAS
							SXMASMMSSSSMSMMMMMMMAAAXAMXMXMMMSMMSMSMXXMAXMMMAMAAXMMMSMXMASMMXXAMAMMSMMSSMMASAMXAMAAASAMAMSMMMXSMXXMASAAAASXMXAMAMXSAMMXMSXSAXMXAAAAAMXMAS
							MMSAMAMAAXSASXAAASAMMMXSAMXMAXXAAAAAAAXXXMSMSMMMMMASAAMXXMMMMMSMMMMMAAXAXAMMSASASMSMMMMMASXXXMXMSAXSAMSMMMSAMXAAMMAMASASAMMMMMXMMSMSMSMSXMAS
							XAMXSAMMXMMXMASXSSXSXMASXXAXMXMXSSMMSMSXAAAAAXXMASAAMSSXAXASAAAAAMAXXSSXMASASXSAMAAAXAASAMXMAMAMSAMMAMXAXXXASMSMSMSMXMXMMXAAXMAMMAAAXAAXAMAS
							MXSASXMAXMASMXMMMMAAXAMXMSSSSXSAAAXXMAMXXMMSMXAXMMXSXMAMASXSMMSSMSMSSXMXMXMMSAMAMMMMMAXMASXMAMAMMMMSMMSMMSSMAAAAAXXAXMASXSMSMSASXMSMSXXSMMAS
							AAMASXMASMAXSAMXASMMMMSAMSAAAAMXSMMXMAMMMMXXMSMSXMAXAAAMXMMXMXAAAAMMAAXXSAMXMMMASASMXXMAXMASXSMSMMASAAAXMAMXMMMSMSMMMSMSASMAXMAMMAMXXMMMXAXS
							SMMMMAMASMMMMMSSMSAAXAXMMMMMMSAMXAXXMAAAAMXSAAAXAMSSMMMMAXMASMMMSSSSSSMAMXSAXXSMXAXXXXXSAMXAAXAAXMASMSMSMMXAXAMAMXAXAXMXAXMMSMAMSMASMMAASXMX
							MXSASAMMSAASAMXAASMMMMMSAAAAXMXASMMMSSSSSXMMSMXMAMAAMAXSSSMAMAXXAAAXAAMSMMSASMMMMSMMXSAXAXSMMMSMSMMSAAASXSMSMMXASMSMMSAMXMMAAMAMAMASASMMAMMX
							MASXSASAXMMSASXSMSXSMAASMSSSSMMXXXMXAAMXMMAMXSMMAMSSMXMAAXMASMMMMMMMXMAXAMMAMXAAAMAAMAAMSMMAXAAXAAXMXMXMAMMMAAXSXMASXAAAAXMASMAMMXAMXMMMAASM
							MXMMMAMMMMXXAMAMAXASAMXSAMXXAAMXMXMSMSMAMSMSAMXSAMAAMXSMSSSMSAXXAAMAMXASMMMMMSMMXXMXSMXMAXSAMSSMXAMXAMAMXMAMMMMMASAMMMSSMXXXMMASXMMSSXXMSMAA
							XAAAMAMMXSXMXMXMAMMSAMXMAXXSXMMMAAXMAMMAMAAMAXAMAMSMMAXXAAAXXMAXSMMAMSXSASXAASAASXSMSXSAMMMMXMMMAAXXASAMAMAMAAAMAMASAAXAXMMAMMXXAMXAMAXAMXXM
							XXMMMMXMASAMXXAXXXXSMMASMMMMMSAMMSSMXMSXSMXMSMMSSMAMMMSMMSMMMMXMAMSMMMASAMXMXSMMXAAAMAMXXSAMXSASMMSXASASMSSSSSSSMSMMMSXAAXAAMASMMMMASMASMMSS
							MSSSMSSMMSXMASMSMAXSXSXXAMXAASAXSAMXAAXXMXMAXAXAAMAMXMAXAAMMMAASAMASAMXMMMSSMMXSMAMMMSMMMXAMAMAMAAXMMMMMMAMXAAAMAAXMAXMMMASXSASAMXSXXXAAAAAA
							XSAAMAAAXMXMAMAAXXXSXMMSMMXMXSAMXMMSAMSXSXMASXMMSMSSSXSMASXAMSXSASAXMXXMAAXAAAAXMSAXXMAMXSXMXMMMMMMMXAXAMAMMAMASXMMSAXMASAMXMMSXMAAMMMSSMMSS
							XMAMMSSMMMAMAMSMSMAMAMXASMXMAMXMAAMXMASXSXMASXASAMMAMAXXAMMSXMASAMXSMSMSAMSSMMSSMMAXSMSAASXSXAAXAAXXAASMXSMMXXASAMAMMMXAAAMXSAMXMMXSAAAXMAMX
							MAXMAXAXXMAXAXAAXAASAMMMSXMXSXAMXSXAMASAMAMASXMMASMAMMMMMSAXAMAMMMMSAAAXMXMAMAAAAMXMXAMMSMAMSSMSMSMAAXXMAMAAXMASAMSSXMMSSSMAMASMSAMMMSSXMXAM
							ASAMXSMMSSSSSSMSMSAMXSAXMASAMXSMXMXXMASASAMXMASXXMMAMMAAMMASXMAXXAAMXMMMSXXAMMSSMMAAMXMSAMAMMMAMAMASXMSMAMAMXSXSXMASAAAAAAMXSAMAMASMXAXAMSAS
							MMASAXAXAAAXMAXXXAMXASXXSAMASAMMAMMXMXSASXSXSAMMSMSMSSSSSMAMXXAXMMSSSSSXMMSMMAMAASXSMXMXASMSAMXMAMMAXMAMASASAMAMMMASXMMXSAMXMAXMXXMAAMSMMAXM
							AXAMASMMMMMMSMMSAMXMXSAXMXSAMXSMAXSMMMSMXMAAMMXAAAAAMAMAMMMXMMMSAAAAXAMAXAAAMXSSMMXAXSMSMMXMMMXMXXMMMSSSMXMMAXXMAMASAXAMXAXSASMXMSAXAMAMMMSM
							MMMMXAXXMAMMXXAXAMSMMMXXXXMAMASXMXXAXAXMAXXXMAMSMSMSMMMASASAAAAMMMMSMMSSMSSSMXMASXSMMAMAXMASAMSMSMSXMAMXAMSSSXSMSSXSAMSSSMMSAMXAAXMMXSXMAXAX
							XAXAMMXMAAMMMMMSAMAXXAMMSXSAMXSAMSSSMSASMSMAMAXMAMAXXXSMSASMSMSSXMAXAXMAMAXAMXXAXMAXAASMMSAXMMAAAAMXMASAMXMAMAMAASAMXMAAAXAMAMMMXXSAXSASXSMS
							SXSMSMAMSXMAAAXMASMSMASAAXSMSMMMMAAXAMMXAAMAMMSMAMSXMAXAMAMAXAAMAXAMMSMSMMMXMMMAXSSMSASMAMMXSXMXMSMXSAMAXAMAMAMXMSXMAMMSMMXSAMXSAXMASMAMMAXX
							SAXXAMXXAASXSXSAASXSAMMMSMSAAMASMMMMMMAMXMSAXSAMAMXMSASXMSMMMMMSXMMSXMAMMXMSAMMSAMXAMAXMSMSASXSXXXAXMASXMASXXAXXMXMMAMXMXSXXASAMMSAXSMMMSSMM
							MAMXMAMXSXMXXXAMXXXMAMMMXAMSMSASAMXAAMMMAMMAMSAMXSXASASAAXXXAXXXXSAAXMAMAAASAAAXMASXMMMMASMAMAAAMMMMSMMMMASASMSMMAASMSMMAMXXSMXMAMXMXASAAAAX
							MSMSMSMAXMSXSAXMASMSAMXAMXMXXMASAMSSXMASXSMMMMMMXMMXMAMMXMSMMMMMMMMMXSAMSSXSMMMXAMXAAXMSMSMSMAMMAAAAXAAXMASAMXAAXSMMXAAMSSXXMASMSMAMMXMMXXMM
							MAAAAAMXMAAAXMMXSAASASMMSMMMXSASAXMXSMXXAAXMASASMXMSMAMXAMMMSAMAAMSSMSAXAMMXAAXSSMSSMXXAXSAMMAMXSXXXSSMSMMSXMSMMMXAXMMMMXAMXMAMAASXXMSSSMSXA
							MMSMSMSSMMMSMAAXAXXXAMXAAAAAAMASAMMAMMMMSMMSXSASASMAMAMSMXAASASXMSAAASXMASMXSMXXXAMAMXSMSMMMSASAXMSMAMMXMAMAXSAMSSXMAAASXMMSMSMSMMXAXXAAASXS
							SMXXAMAMXAMXXMASXMXSAMMXSSMMSMMMAXMAXAXXAAMXAMAMMASASAXAMSMXSXMASMXMMMSXXAAXMMSSMSMAMAAMAAAASASXMAAMAMSMSAXXMMAMAAXSSMXSAMXAAAAAMMSSMMMMMMAM
							AMMSMMSSSSSMSXMAXAAAXXXXMAXAAASXXASMSMXSSMMMMMAXXMSMSXXAMAMMSAMXMAAXASXSAMMMXAAAAASAMXSMXXMMMAMAMSMSAMXAXMMSASMMMSMMASASAMSMSMSMSAAAAMSMXMSM
							MAMXAXAAMMAAMMMAMMMMSXMSMAMMSMMXMAXMAAAMAMSSSSXSMXMASASMMASAMXSXXMXMXSAXSXMAMMMSSMXXXMMMMASXMAMXMXAMXSMSMAAMMMAAXAXSAMXSAMSAMAAXMMSSMMAMAMAM
							SAMXAMMMMSMAMASXMXMXSAAAMASAAAXSXMSSMSMMAMAAXMASAMMAMMAXSXMMXSAMMMSMMMAMXMASXMXAMXXMSMAASAMASMSMAMMMMXAAMMMSMMSMMMXMSSMSXMMAMSMSXXAMXXMXMSAS
							SASMSMAAMMSXSMSAMXSAMMSMSMSMSSMMMXAXXMASMMMSMMMMSASASXMMSMSAXMASXAAAAXMASXMAMXMMMMSAAMXMASXAXMAMXASAMXSMSXSAMXASAXXAAMXMMSSMMMAMMXXXMMSAMSAS
							MAMAXMXMMAMXMASAMAMASAMXMMMXXMASAMMSMSAMMXMAMXXAXXSASXXASASMXSAMMSSSMSMAMXSMSMAXAAMSSMSXAMMXSSXMSASMSAMXXMSAXSASMSMMMSAAMASMAMAMMSMSMAMAXMAM
							SSMSMMASMSMSSMXAMMSAMASXMASMSSMMSSXXASASAMXSSSMXSMMAMMMXMAMAAMAXAMXXXSMMSASXAMSMMXXMXAXMMMAMAAAMXAXAMASXXAXAMMAMXMASXXXSMAXMASMAMAAAMASXMMMM
							SAAXAMAXMXAAMXSAMXMASXMMXAMAAASAMXMAMMAXXMAMAAXMMAMXMASAMAMMMSSMSSSMMSAAMASMXMMXSXSMMMMXASMMMMSAMMMMMMMAMSMSMMAMSSSMASXMMSXSAMXAXMSMSXSASASM
							SMMMSMSSMMMMSMASXXSAMXMMSMSMXMAMSAMSAMXMMMXMSMMSSMMXSASASASXXAAAAXAMXMMMMSMMXAMXMAMXAMXMMXXAXXAMAAASASMXMAAAAMMSAXAXXSAAXAASASXMSAMXMASAMASX
							XSAAXAAAAXMAMMAMXMMMMAXAAAAXMAAXSASXAXMMXSXAAMXXAAAXMXMASAXAMSSMXSMXXXXXXMASMMMXMAMSSXXAMMSMMSMXSXSSXSXASMSMSSSMMSMASMMMMMMMAAAMAMSSSMMSMSMM
							SAMSSMSXMMMASMMSAXAAXSMSXXMSMMMXMXMMAMXMAMMSMSMSSMMSXSMMMXMXMMMAASMXSASMSMAMAASXSMMXAAMMMAAXAAXXXXAMAMMXMMXAAXAAXXXMAMAXXSXMMMAMXSAAAXAXXMAS
							SAMXAXMASASMSAASMMMXSXAMSSSXMAMMMXMXMAAMAMXXAXAAMAMXAMMXAAXAMXSMASAAMXMAMMXSSMSMXSAMXMSAMSXXXMSMMSMMASAAMSMMMSSMMMMSMSMMMMAXXMSMSMMSMMXMAMAM
							SXMMMMMASASAMMMSMSSSMMXMAXSASXMAXXSAMSXMASXMSXMSSSMMAMAMAMSMSAMAMMMMSMMSMMAXXXMAXAMAAXSAMAMMSXSAAXXAAXXXMAXXAXAAASAAXAXAAMMMSXAXXXAAMAAXAMXM
							XMXXXAMAMAMAMXXMAAMAAMSMXMMMMMSMSMSAMMMMSMMAMAAMAMXXMAAXXXAAMXMASXMAAMAAAMMSMMSSMSXMMXMXMASAAASMMMSMSSSXASMMXXXMSASMSSSSXSAAMSSSMMSSMSXMXSSS
							SAMMSSMSSXSXMMSMMMSSMMAAAMAXMXAAAASXMMAXAAMAMMXMAMSMSMMMMMMXMASASAMXSMMSXMMXAMAMXMXMMSMMSASMMXMMMAAAXAMMMMAMMMSMMMAMAAAASXMMXAXAXXMAMMMSMAAS
							AXAAXASAMXMAMAMMMMMMMSXXMXAMXMMXMXMMXXSSXSMSSSMMAMAAAASMXSXXXAMMSAMXXMMXASXSMMMSASASAXMAMASXXAAASXMSMAMAASAMAAAAMXSMMMMMMMASMMMMMMXMMXAAMMMM
							MXSSSSMMSASAMASAAAAXMASASXSSSMXMSMSAXXXSAXAMAAXMASMSMMMAAAMMMMSMXAXASXASXMASAAXMXSAMMSSSMXMASXMMSMXMAASXMSXMMMSMMAMAAXAXAXMSASASAMAASMSMSASX
							XXMAXMAXAASMSXXMSXSXMAXXMAXAXXAAAAMSSMAMSMMXSXMAASXXMXSMSMXAAMAMSSMASMMMAMAMAMXSAMXMMAMAMXAMXAXAXMASXMSXXMASMXMMMAXMSSXSSSXSAMXSASXSMAXXSASM
							SAMXMSMMMAMXMXMXMMXAMAXMMSMAMSSSMSMAXMAMXMMMXAAMXSXXXAXXAXMSMSAMAAMMMAXSXSASAMAMASXXMSSMSASXSSMMMSASASXXMMAXMASAMMSMMMMMAMXMMMXSXMMAMMMXMXMA
							SSSXMAMSMASASMMMAAXAMSSMAMMAMMAAXXMAXSXSAMAAXAMXXMASMSMSMSXAXSMMMSMSMSMAASASMMXSAMXSSMMXSAAASMAXAMXSMXMMMMMSMSMSAAAMAAAXASXMASMMMSSSMSSXMMMS
							SAXAMAMXMMSAXAASMMSSMAAMAMMSSMSMMMMXXAAMMSMSSSSXXMAAAAAXASMMMMMSSXXMAAXMAMMMXSMMXXAXAAMAMMMSMSSMMMAMXSMMAAMAMAAAMSMSSSMSASASASAXAAXMAMAXMASA
							MMMMSMSMMMMXMSAMAAAXMSSMMXAXXMMAMSASMMMMMAMXAAAMSMSMSSSMAMSAASASASAMSMMXXXMASAAASMSSSMMASAXMAAXSMMXSAMAMAMMASXSMXXAMAMMXMSXMASXMMMAMAMMASMSX
							ASAMAAAMMAAXXAASMMMXAMXMAMSSMXSAMMASXAXMSASMMMMMXAXXAAXMXMXXXSMMAMXMXAMXSAMXXXMAXAAXAXXASASMMMXAXAMXXSAMXSSXXMAAXMXMSMSAMMXSXMXAXMSXSSXAMMMM
							AXAMMSXSAMXMMSXMMAXASMSMMXAAAXSASMAMXXMASXMASAXAMXMMSMSXMASMXSXMMMMASASAMXXSMSSSMMMMXXSASAMMSSSSMMSMMSXSAMAMMSSMMXXAXASASAAMAMSSMXAAMMMXSAAM
							MMSMAMAXASXMAMXAMXSXXAXASASMMMSAMMSSSMSMSASMMSSSSMXAMXMMSAMXAMAMMMSAMSAMXMASXAAXMAXSAXSAMXMXAAAXXAAAAXAMMSAAXAAAXSMMMMMMAAMMAXMASMMXMAXAMMSS
							XAXMMMASMMXXSAMXMAAXMMMAMMXAMXMMMMMAMAAAMAMXAMMAAMMMMAAXMASMMXSMAAMAMMMSSSMSMMSMSAXMMMMAMSSSMMMMMMSSMMMMMSMSSMSMMXAAAXAXMASXMMSXMXSASMMMSSMM
							MMSAXMASAAXXMMAMMSSXAAMSSMSSMAMMMSAAMSMSMSSMMSMSMMAMSSSXSAMMMAXSMXMSMXXAASAXAXAMMMMMAAMAMAAAMXAAXMAXAAMAXMAAXMAMXSSMSMXSXASASXSXAAMAMAAXMMAX
							XASAMAASMMMMXSAMMMMMSXMXAAAMMXMXAAMXMAXAAAAASXXAXMAMAAXAAMMSMAMASMAMXXMMSMXSXMASAMASXSSSSMSMMXSMMMASXMSMSMMMMMAMXXAXMXMMMMSAMAXXMXXAXSMSXMXM
							MMSSMMMXASXSASXSAAMXXMXSMMMSMSMMMSMSMXSMSMSXMMSSSSSMSSMSMMAXMMSAMMAMASAMXMAMAMXMAXMSAAAAAXMASMAMXMAXMAAAXXMASXMSMSSMMASAAMMMMMMXSAMSAXXXAMMS
							MAMXXXMSXMAMASASXSMMMMMAMAXXMAAAAAAXXAMMXXMAAXAAMAAAXMAAAMXSAAMMMMASAAMXMMSSMSMSSMAXMMMMMMSAASAMSMSSMSMSMMMAMAXAASAXXAXXXMAXAAAAMAXXASASMMAX
							MSSSMAMXSMXMXMAMAXXAASMMMASMSSSMSMSMMASASASMMMMSMSMXMMSXSMMMMXSXSSXMMSSXXAMAAXMAXMXMMXXASAMXMMXMXAXXXXXXAMMSMMSMSXMMMSSMSSMSSSSXMSMMAMMAXMMS
							XAAAMMMAXXXSSMSMMMMSMMASXSXMAMAAAAAXXSMAMAXXAAXMAXXMXXMAMXAAMMXAMXMASAAXSASMMMMASMSMSASAMAMSXMMSMMMMMMASXMAXXXAMXMXXXMAXXAAXXMAMMAMMXMXMXXAM
							MMSMMSAMXMMXSAAXMAXXXMAMXMXMASMMMSMXSAMSMSMSSMSMAMXAMAXMASXSSMMMMASMMMSMSASXSXMASMAAXASMSMMXAMXAAAXMAMAXASASMXMXSAMXXSXMSSMXSXXMSMSSSMASAMMS
							XAAXXMAXAMMAMMMXSMSAMMSSSMASMSXXXAAXXMMAAMAAXAXASMMSAMSMMMMAAAAXXXSXAXAAMAMAMAMXSMMSMMMMAMSSMMSSSMSXSSMSXMASMXSAMSMMMMAAMXMAMMMXAXSAXSAMASAA
							MMSSMSMSMSXXSASXMAMXMAXAXMASMXMMSMMMSSSMSMMMMMMMMAXAXXAXAMMSSSMMMXMXMSMSMXMMSAMAXAXMXAASMSXASAAMMXSMXAASAMAXAAMMXAMAAMSSMAMASAAMMXMMMMXSAMXS
							MXAAXAAAAMMMMASXMSMSMXSAAMASXMAMAAMSAMXMXXXMMAAASXMSMSXSXXMAMAAXSXMAMAMAMMMXMASXSAMMSSXXXXXMMMSSMAMAMMMSAMAMMMSSSMSMXMAAMXSSXASXMASXSAMXASAM
							XMASMMSMSMAAMAMMAMAAAXXAAMASAASXSSMMXXAMAMXMMSSMSSMAAXASMSMASMMMMAMSSMSAMAMSSMMXAMAMXMASXXAXAXXXMMSAXMASAMXXXXAAAASAMXSSMMSAMXMXSASASMASXMAS
							MXMAMXAXAXXSSXSASMSMMMAMMMMSASXMAMAXSMMXAAMAAXXMMAMMMMMMAXSAMAMASXMMAMSMSSSMAAAMXSAMMXMAMXMSSSMMMASASMASXMAMMMMSMMMMMAMXAXMASMSXMASAMMMAXMAM
							AAXXSSMSMXSAMAXMXAAAASAMAXXSXMMMMSXMXASXSSXMMSAMSSMMXSAMSMAMSSMAAMMSAMXXAMAMSMMMASASMXMSXSXAXAAAXMMMMMMMMXXAXXAXXAAAMXXSMMSAAASAMXMXXXXMXMAS
							SASMXAAMMMAAMMMMMSMMMMASXSMXAMXXAXMXMMMAMXMMXSAMAMAAAMAMXAMXAXMMMXASASXMMSMMMXXMASAMXXXMASMMSMMMMAAAXASAMXXSXMMSSSXSAXAASAMSSMSXMASMMMMXASMS
							MASASMXMAXSSMXAXAMMXSSMMAMMSMMXMMMAMAXMXMAMSAMXMXSXMAXAMMSXMASXMXMASAMMXAAAAMSMMMMASMSMMXMAXXXAMXSMMMMSASMMMXXAAMMAAAXSAMXMAMXMAMXSAAMASMSAM
							MAMMXXAMMXXAMSSXMSMAXAXSMSMAAXMASASMSXMASAMMMMSAMXXMMSXSAXXSXSASAAMMAMSMSSSSSXMAMXAMMAMXMSSMSMMSAMAXSASAMAAXMMMSSMSMSMMAMXMSMSMSMMMMMSAMMSMS
							MMMMMSSSSMSAMAXAAXMSSMMMAAMMXXAMXAMAMASASMSXSASMMMXXAAXMMSAMAMAMXMXMASAAMAAMAMXASMSMSASAAAMSXAAMASAMMXMAMSMSAMMAXXXAAXSAMAXASAAAAAAXXMASASXX
							AMAXMAAAAMSAMASMMMMMAAAMSMSMMSSMMSMMMAMASAXAMMSAAAMMMMSXMMAMXMMMSMMXXMASMMMMSAMSAMXAXAXXMMSAMMMMXMMMSXSSMMMAMXMSSXMXMAXMMMSXXMSMMMSSXSAMXSMM
							ASASMMMSMMSAMXSAXXXXMMMXAAXAXAAAAXAXMMMXMMMXMASXMMSAASXMSSMSMSAAAXAXSAMXXAMAXAMXASMMMAMXMXXMSXSXAAAXXMAMXXMASMMMMAXAXMXSAMXXMXAXXMXMASXSMSXA
							XMASAXXAXXSAMXSXMAMSASXXAMSMMSSMMMAMXMMAMXMXMXMAMASXSXAAMAAAAXMMMSSMSAMXSXSAMAMSASAXXMAXMAMXAASXSMMXMASMMXMAMAAAXXSMSMAMAMAASAAAMSMSXSAMXMMX
							XMXSAMSSSMXXMAXAMAASAMMXXMAXAAXXSAMMAXSMSAMASAXAMXSMMMSSSMMMSMMSAAMAMXMASMMMSXMMASXMMSMSASXMMXMAMXXMSAMAXSMMSMSXSAMXAMMSMMMXAAAMSAASAMMMASMM
							XXAMAMAAAMXXMSSXMXMMSMMMMSASMXSASAASXMXMAASAMMXSAMXAAAMMXXSAMASAMXSSMSMAMXAAAMXMXMMMAAAMXXAMXMMAMMMMAAXXMMAASXMMSASXMXMAMSSSMXSXMMMMAMASMSAS
							SMXSMMXSAMMAAXMASMAXAXAAXXXXAAAAXXMXXXMAXMMXSXAMAXMSMSXSXMMAMXMMMMAAASMMMSXSMXMAMAAMMMSMASMMAMMAXAAXSSMSMXMXMASASAMMSAMMAMAAXAMAMXSSXMASXSAM
							MAXMMAMXAAMMSMSAMSMMMSSSSSSMSMMSMSMSSSSXSAMXAMSSMSAMAMMMASXSMSXXXAMMMMASAAMXAXSAMMMXAAAMXXASASMMSXXMAXAAAXMAMMMMMAMAMXSMAMXMMSXMMAAMMMASXMAM
							MSMMAMMMMMXAAXMMXAXAXXAAAAXAMXXAXAAAAXAASAMXMAMAAMAMAMASMMAXAMXMAMXAXXAMMSAMXMSASXXSMSMSXMMMASXAAASMMMSMSMSASXASAMMSSSXMASMSMMASMMSSSMMSMXAA
							AAAXXMASXSMSSSSMSXSMMMMMMMMSMMMMSXMMSMSMXMXXXSXMXMXMXSXMXXXMAMXMASMSSMSSXMXSXASAMMXAMXMXXAXMXMMMMXMAMXXAXAXAXMAXAASXMMASXMXXASAMMXAAXAMXASMS
							SSSMSMASAAAXMMMASMSASASMSMAMAMMXSMMXXAMXAMSMMMXMASXMAMMMMMMSXMXSAAAAAMAMXXAMMMMAMXMMMSASMSMSMMASXMSMMAMMMMMMMSMSSMMASMMMMMSMMMMMSMMSSSMSMMAA
							MAAAXMAMMMMMAAMMMASASASAAMASAMAAMASAMSSSXSAAAXASXSAMXSAAAAMMMAAMAMMMSMASMMXSAASAMAAXXMAMMAAAASASAAAAMASAAAAAAAXXAASAMXAAAAAAAXAAAXAAMAMXAMXM
							MSMMMMMSAMXSSMSXMMMXMXMMMSMSMSXXSSMXSAMXSSMSMSMSASMMSSMSSSXAXMXSXMASMXMSXXASXMSXMAMSAMXMSXSSXMSSMMMASMSXSSSMSSSSSMMMSXSSSSSSMSMSSSMXSSMSMMAX
							""";
}