<?php
ini_set('memory_limit', '2506M');

	global $bad_prefixes;
	$bad_prefixes = [];
	
	var_dump(
		getTotal('129#') + 
		getTotal('974#') +
		getTotal('805#') +
		getTotal('671#') +
		getTotal('386#')
	);

	function getTotal($input) {
	
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
		
        $memory = [];
        $count = countButtonPresses(str_split($input), 26, $pad0, $pad1, $memory);
        return $count * (int)$input;
        
    }
    
    function countButtonPresses($input_arr, $level, $pad0, $pad1, &$memory) {
        
        $cache_key = (implode('', $input_arr) . $level);

        if (array_key_exists($cache_key, $memory))  {
            return $memory[$cache_key];
        }
        
        $pad = $level == 26 ?$pad0 :$pad1;
        $result = 0;
        for ($i = 0; $i < count($input_arr); $i++) {
            $key = ($input_arr[$i -1] ?? '#') . $input_arr[$i];
            $possible_combinationsPressDirection = $pad[$key] ?? ['#'];
            
            if ($level == 1) {
                $result += min(array_map("strlen", $possible_combinationsPressDirection));
            } else {
                $min_press_direction = PHP_INT_MAX;
                foreach ($possible_combinationsPressDirection as $p) $min_press_direction = min($min_press_direction, countButtonPresses(str_split($p), $level - 1, $pad0, $pad1, $memory));
                $result += ($min_press_direction);
            }
        }
        
        $memory[$cache_key] = $result;
        
        return $result;
    }
	
	function findShortestPaths($grid) {
		$paths = [];
		
		$rows = count($grid);
		$cols = count($grid[0]);
		
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
				$neighbors[] = [$nx, $ny, $dir[2]];
			}
		}
		
		return $neighbors;
	}
	
	function bfs($grid, $start, $end, $rows, $cols) {
		$queue = [[$start, []]];
		$visited = [];
		$shortestPaths = [];
		$shortestLength = PHP_INT_MAX;
		
		while (!empty($queue)) {
			list($current, $bearings) = array_shift($queue);
			$x = $current[0];
			$y = $current[1];
			
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
			
			$visited[] = "$x,$y";
		}
		
		return $shortestPaths;
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
