<?php
ini_set('memory_limit', '2560M');

	var_dump(
		getTotal('129#') + 
		getTotal('974#') +
		getTotal('805#') +
		getTotal('671#') +
		getTotal('386#')
	);

	function getTotal($input) {
		var_dump($input);
		global $pad0;
		$pad0 = [
			['7','8','9'],
			['4','5','6'],
			['1','2','3'],
			['','0','#']
		];
		$pad0 = findShortestPaths($pad0);
		
		
		global $pad1;
		$pad1 = [
			['','^','#'],
			['<','v','>'],
		];
		$pad1 = findShortestPaths($pad1);
		
		
		$result = ['min' => PHP_INT_MAX];
		encodePad(str_split($input), 0, $result, '', $pad0);
		
		
		for ($i = 0; $i < 2; $i++) {
			$result1 = ['min' => PHP_INT_MAX];
			foreach (array_keys($result) as $e) {
				encodePad(str_split($e), 0, $result1, '', $pad1);
			}
			$result = $result1;
			var_dump('count ' . count($result1));
		}
		
		$min = PHP_INT_MAX;
		foreach (array_keys($result1) as $e) {
			if (strlen($e) < $min)
				$min = strlen($e);
		}
		
		return $min * (int)$input;
	}
	
	function encodePad($input, $index, &$result, $path, &$pad) {
		
		if ($input == str_split('min')) return;
		
		if (strlen($path) > $result['min']) return;
		
		if (count($input) == $index) {
			$result[$path] = true;
			
			if ($result['min'] > strlen($path)) {
				array_splice($result, 0);
			}
			
			$result['min'] = strlen($path);
			return;
		}
		$previous = $input[$index - 1]??'#';
		$current = $input[$index];
		
		$possibilities = $previous == $current ?['#'] :$pad[$previous.$current];
		foreach ($possibilities as $e) {
			encodepad($input, $index + 1, $result, $path . $e, $pad);
		}
		
	}
	
	function findShortestPaths($grid) {
		$paths = [];
		
		// Get grid dimensions
		$rows = count($grid);
		$cols = count($grid[0]);
		
		// Build paths for every pair of characters
		$positions = [];
		foreach ($grid as $r => $row) {
			foreach ($row as $c => $char) {
				if ($char !== '') {
					$positions[$char] = [$r, $c];
				}
			}
		}
		
		foreach ($positions as $startChar => $startPos) {
			foreach ($positions as $endChar => $endPos) {
				if ($startChar !== $endChar) {
					$paths["$startChar$endChar"] = bfs($grid, $startPos, $endPos, $rows, $cols);
				}
			}
		}
		
		return $paths;
	}
	
	// Helper function to find neighbors and their bearings
	function getNeighborsWithBearing($grid, $x, $y, $rows, $cols) {
		$neighbors = [];
		$directions = [
			[-1, 0, '^'], // Up
			[1, 0, 'v'],  // Down
			[0, -1, '<'], // Left
			[0, 1, '>']   // Right
		];
		
		foreach ($directions as $dir) {
			$nx = $x + $dir[0];
			$ny = $y + $dir[1];
			if ($nx >= 0 && $nx < $rows && $ny >= 0 && $ny < $cols && $grid[$nx][$ny] !== '') {
				$neighbors[] = [$nx, $ny, $dir[2]]; // Add coordinates and bearing
			}
		}
		
		return $neighbors;
	}
	
	// BFS function to find all shortest paths with bearings
	function bfs($grid, $start, $end, $rows, $cols) {
		$queue = [[$start, []]]; // Start with empty bearings list
		$visited = [];
		$shortestPaths = [];
		$shortestLength = PHP_INT_MAX;
		
		while (!empty($queue)) {
			list($current, $bearings) = array_shift($queue);
			$x = $current[0];
			$y = $current[1];
			
			// Stop processing this path if it's longer than the shortest found
			if (count($bearings) > $shortestLength) {
				continue;
			}
			
			if ($current === $end) {
				if (count($bearings) < $shortestLength) {
					$shortestPaths = [implode('', $bearings) . '#'];
					$shortestLength = count($bearings);
				} elseif (count($bearings) === $shortestLength) {
					$shortestPaths[] = implode('', $bearings) . '#';
				}
				continue;
			}
			
			foreach (getNeighborsWithBearing($grid, $x, $y, $rows, $cols) as $neighbor) {
				[$nx, $ny, $bearing] = $neighbor;
				if (!in_array("$nx,$ny", $visited)) {
					$queue[] = [[$nx, $ny], array_merge($bearings, [$bearing])];
				}
			}
			
			$visited[] = "$x,$y"; // Mark current position as visited
		}
		
		return $shortestPaths;
	}
	
	/*
	var_dump($resultPad0);
	
	$resultPad1 = '';
	$current = '#';
	foreach (str_split($resultPad0) as $c) {
	if ($current == $c) {
	$resultPad1 .= '#';
	} elseif ($current > $c) {
	$resultPad1 .= inversePad0($pad1[$c . $current]);
	} else {
	$resultPad1 .= $pad1[$current . $c];
	}
	$current = $c;
	}
	var_dump($resultPad1);
	
	$resultPad2 = '';
	$current = '#';
	foreach (str_split($resultPad1) as $c) {
	if ($current == $c) {
	$resultPad2 .= '#';
	} elseif ($current > $c) {
	$resultPad2 .= inversePad0($pad1[$c . $current]);
	} else {
	$resultPad2 .= $pad1[$current . $c];
	}
	$current = $c;
	}
	var_dump($resultPad2);
	var_dump(strlen($resultPad2) . '*' . (int)$input);

	*/
	
function inversePad($input) {
	$result = '';
	foreach(str_split($input) as $c) {
		if ($c == '^')
			$result .= 'v';
		elseif ($c =='v')
			$result .= '^';
		elseif ($c =='<')
			$result .= '>';
		elseif ($c =='>')
			$result .= '<';
		elseif ($c =='#')
			$result .= '#';
	}
	
	return $result;
}	
function sample() {
	return <<<EOD
	980#
	EOD;	
}

function input() {
	return <<<EOD
	
	EOD;	
}

?>

//<v#<##>>^#v##<^#>#<v<#>>^#v#^#<v<#>>^#<v#>#^#<#v<#>>^#v#^#<v#<#>>^###<#>v#^#
//<v#<##>>^#v##<^#>#<v<#>>^#v#^#<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A