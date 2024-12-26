<?php
    
    ini_set('memory_limit', '12096M');
    
    echo  "Sample phase 1 result expected: 2024 actual: " . determineDecimalValueForPrefixRegisters(parseInput(sample())) . "\n";
    echo  "Actual phase 1 result expected: 59619940979346 actual: " . determineDecimalValueForPrefixRegisters(parseInput(input())) . "\n";
    echo  "Sample phase 2 result expected: bpt,fkp,krj,mfm,ngr,z06,z11,z31 actual: " . getBadWires(input()) . "\n";
    
    function getBadWires(string $input) {
        
        $bad_wires = getWireSwapsToFixOp($input);
        $combos = getAllPairCombinations($bad_wires);
        
    
        $x = determineDecimalValueForPrefixRegisters(parseInput($input), 'x');
        $y = determineDecimalValueForPrefixRegisters(parseInput($input), 'y');
        $expected = $x + $y;
        
        $found = null;
        foreach($combos as $combo) {
            $new_input = $input;
            foreach ($combo as $pair) {
                $new_input = str_replace(' -> ' . $pair[0] , ' -> ' . '*' . $pair[1], $new_input);
                $new_input = str_replace(' -> ' .$pair[1], ' -> ' .$pair[0], $new_input);
                $new_input = str_replace(' -> ' . '*' . $pair[1] ,' -> ' . $pair[1], $new_input);    
            }
            
            
            
            if ($expected == determineDecimalValueForPrefixRegisters(parseInput($new_input))) {
                $found = $combo;
                break;
            }
            
        }
        
        $wires = [];        
        foreach ($found as $pair) {
            $wires[] = $pair[0];
            $wires[] = $pair[1];
        }
        
        sort($wires);
        
        return implode(',', $wires);
        
    }
    
    function getAllPairCombinations(array $items): array
    {

        if (count($items) === 0) {
            return [[]];
        }
        

        $allPartitions = [];
        $first = array_shift($items);
        

        foreach ($items as $index => $other) {
            $pair = [$first, $other];
            
            $remaining = $items;
            unset($remaining[$index]);
            
            $remaining = array_values($remaining);
            
            $subPartitions = getAllPairCombinations($remaining);
            
            foreach ($subPartitions as $sub) {
                $allPartitions[] = array_merge([$pair], $sub);
            }
        }
        
        return $allPartitions;
    }
    
    function getWireSwapsToFixOp(string $input) {

        $circuit = parseInput($input);
        $xor_opps_xy = [];
        $xor_opps_not_xy = [];
        $and_opps_xy = [];
        $or_opps_xy = [];
        $xor_not_z = [];
        $result = [];
        
        foreach ($circuit['gates'] as $g) {
            if($g->type == 2) {
                if (str_starts_with($g->input1->name, 'x')) {
                    $key = (int)substr($g->input1->name, 1);
                    $xor_opps_xy[$key] = $g;
                } else if (str_starts_with($g->input1->name, 'y')) {
                    $key = (int)substr($g->input1->name, 1);
                    $xor_opps_xy[$key] = $g;
                } else {
                    $xor_opps_not_xy[$g->output->name] = true;
                }
            }
            if($g->type == 0) {
                if (str_starts_with($g->input1->name, 'x')) {
                    $key = (int)substr($g->input1->name, 1);
                    $and_opps_xy[$key] = $g;
                } else if (str_starts_with($g->input1->name, 'y')) {
                    $key = (int)substr($g->input1->name, 1);
                    $and_opps_xy[$key] = $g;
                }
            }
            if($g->type == 1) {
                $or_opps_xy[$g->output->name] = true;
            }
        }
        
        $half_add_xy = [];
        foreach ($xor_opps_xy as $key => $xor_xy) {
            $half_add_xy[$key] = $xor_xy->output;
        }
        
        $carry_xy = [];
        foreach ($and_opps_xy as $key => $xor_xy) {
            $carry_xy[$key] = $xor_xy->output->name;
        }
        
        
        

        //carry with op different than OR
        foreach ($carry_xy as $carry) {
            foreach ($circuit['gates'] as $g) {
                if (($g->input2->name == $carry || $g->input1->name == $carry) && ($g->type != 1)) {
                    $result[$carry] = true;
                }
            }
        }
        
        //half-add with OR operation
        foreach ($half_add_xy as $half_add) {
            $count = 0;
            foreach ($circuit['gates'] as $g) {
                if (($g->input2->name == $half_add->name || $g->input1->name == $half_add->name)) {
                    $count++;
                    if (($g->type != 2) && ($g->type != 0)) {
                        $result[$half_add->name] = true;    
                    }
                }
            }
        }
        
        //Z registers not from XOR
        foreach ($circuit['gates'] as $g) {
            if (str_starts_with($g->output->name, 'z') && $g->type != 2) {
                $result[$g->output->name] = true;
            }
            
        }
        
        
        //XOR not leading to z registers
        foreach (array_keys($xor_opps_not_xy) as $xor_opp) {
            if (!str_starts_with($xor_opp, 'z'))
                $result[$xor_opp] = true; 
        }
        
        //remove first and last elements special cases
        unset ($result[$circuit['gates_by_output']['z01']->input1->name]);
        unset ($result['z45']);

        
        return array_unique(array_keys($result));
    }

    function permutations($items) {
        if (count($items) === 1) {
            return [$items];
        }
    
        $result = [];
        foreach ($items as $key => $item) {
            $remainingItems = $items;
            unset($remainingItems[$key]);
    
            foreach (permutations($remainingItems) as $permutation) {
                $result[] = array_merge([$item], $permutation);
            }
        }
    
        return $result;
    }
    
    function determineDecimalValueForPrefixRegisters(array $circuit, $prefix = 'z') {
        $register_keys = array_keys($circuit['registers']);
        $z_register_keys = array_filter($register_keys, fn ($e) => str_starts_with($e, $prefix));
        rsort($z_register_keys);
        
        $z_output = "";
        foreach ($z_register_keys as $key) {
            $z_output .= $circuit['registers'][$key]->value;
        }
        
        
        if (str_contains($z_output, '-')) return 0;
        $decimalValue = bindec($z_output); 
        return $decimalValue;
    }
    
        
    function parseInput(string $input): array {
        $lines = explode("\n", $input);
        $registers = [];
        $gates = [];
        $gateByOutput = [];
        $gateByOp = [];
        
        foreach ($lines as $line) {
            $line = trim($line);
            if (empty($line)) continue;
            
            if (strpos($line, ':') !== false) {
                list($name, $value) = array_map('trim', explode(':', $line));
                $registers[$name] ??= new Register($name);
                $registers[$name]->setValue((int)$value);
            } elseif (preg_match('/^(.+?)\s+([A-Z]+)\s+(.+?)\s*->\s*(.+)$/', $line, $matches)) {
                list(, $input1, $operation, $input2, $output) = $matches;
                
                if (!isset($registers[$input1])) $registers[$input1] = new Register($input1);
                if (!isset($registers[$input2])) $registers[$input2] = new Register($input2);
                if (!isset($registers[$output])) $registers[$output] = new Register($output);
                
                $operationMap = ["AND" => 0, "OR" => 1, "XOR" => 2];
                $type = $operationMap[$operation] ?? null;
                
                if ($type === null) {
                    throw new Exception("Invalid operation: $operation");
                }
                
                $g = new Gate($type, $registers[$input1], $registers[$input2], $registers[$output]);
                $gateByOp[$input1 . ' ' . $operation . ' ' . $input2] = $g;
                $gates[] = $g;
                $gateByOutput[$output] = $g;
            } else {
                throw new Exception("Invalid line format: $line");
            }
        }
        
        return ['registers' => $registers, 'gates' => $gates, 'gates_by_output' => $gateByOutput, 'gate_by_op' => $gateByOp];
    }
    
    class Register {
        public string $name;
        public int $value = -1;
        private array $listeners = [];
        private bool $updating_value = false;
        
        public function __construct(string $name) {
            $this->name = $name;
        }
        
        public function setValue(int $value) :void {
            
            if ($this->updating_value) { 
                throw new Exception('short-circuit');
            }
            try {
                $this->updating_value = true;
                $this->value = $value;
                
                foreach ($this->listeners as $listener) {
                    $listener($value);
                }    
            } finally {
                $this->updating_value = false;
            }
            
        }
        
        public function onValue($func) {
            if ($this->value != -1) { 
                $func($this->value);
            }
            $this->listeners[] = $func;
        }
        
    }
    
    class Gate {
        public int $type; // 0 AND, 1 OR, 2 XOR
        public Register $input1;
        public Register $input2;
        public Register $output;
        
        public function __construct(int $type, Register $input1, Register $input2, Register $output) {
            if ($type < 0 || $type > 2) {
                throw new \InvalidArgumentException("Invalid gate type. Must be 0 (AND), 1 (OR), or 2 (XOR).");
            }
            
            $this->type = $type;
            $this->input1 = $input1;
            $this->input2 = $input2;
            $this->output = $output;
            
            $this->input1->onValue(function ($value1) {
                $this->computeOutput();
            });
            
            $this->input2->onValue(function ($value2) {
                $this->computeOutput();
            });
        }
        
        public function computeOutput(): void {
            // Ensure both inputs are set before computing output
            if ($this->input1->value == -1 || $this->input2->value == -1) {
                return;
            }
            
            $value1 = $this->input1->value;
            $value2 = $this->input2->value;
            $result = 0;
            
            switch ($this->type) {
                case 0: // AND
                    $result = $value1 & $value2;
                    break;
                case 1: // OR
                    $result = $value1 | $value2;
                    break;
                case 2: // XOR
                    $result = $value1 ^ $value2;
                    break;
            }
            
            $this->output->setValue($result);
            
        }
    }    
    
	
function sample() {
	return <<<EOD
    
    EOD;	
}

function sample2() {
    return <<<EOD
    x00: 0
    x01: 1
    x02: 0
    x03: 1
    x04: 0
    x05: 1
    y00: 0
    y01: 0
    y02: 1
    y03: 1
    y04: 0
    y05: 1
    
    x00 AND y00 -> z05
    x01 AND y01 -> z02
    x02 AND y02 -> z01
    x03 AND y03 -> z03
    x04 AND y04 -> z04
    x05 AND y05 -> z00
    EOD;	
}

function input() {
	return <<<EOD
    x00: 1
    x01: 0
    x02: 0
    x03: 1
    x04: 1
    x05: 0
    x06: 0
    x07: 0
    x08: 0
    x09: 0
    x10: 0
    x11: 1
    x12: 0
    x13: 1
    x14: 1
    x15: 1
    x16: 1
    x17: 0
    x18: 0
    x19: 1
    x20: 0
    x21: 0
    x22: 1
    x23: 1
    x24: 1
    x25: 0
    x26: 1
    x27: 1
    x28: 0
    x29: 1
    x30: 0
    x31: 1
    x32: 0
    x33: 1
    x34: 0
    x35: 1
    x36: 1
    x37: 0
    x38: 1
    x39: 1
    x40: 1
    x41: 1
    x42: 0
    x43: 1
    x44: 1
    y00: 1
    y01: 0
    y02: 0
    y03: 1
    y04: 1
    y05: 1
    y06: 0
    y07: 0
    y08: 0
    y09: 1
    y10: 0
    y11: 0
    y12: 0
    y13: 1
    y14: 1
    y15: 0
    y16: 1
    y17: 0
    y18: 1
    y19: 1
    y20: 1
    y21: 0
    y22: 0
    y23: 1
    y24: 1
    y25: 1
    y26: 0
    y27: 1
    y28: 0
    y29: 1
    y30: 0
    y31: 0
    y32: 0
    y33: 1
    y34: 1
    y35: 1
    y36: 1
    y37: 0
    y38: 0
    y39: 0
    y40: 0
    y41: 1
    y42: 0
    y43: 1
    y44: 1
    
    x36 AND y36 -> rpc
    swn OR jrk -> kfm
    x36 XOR y36 -> mvv
    y28 XOR x28 -> rnh
    bfp OR wqc -> rgb
    tkc OR mfm -> brs
    kmb XOR gfj -> z16
    x25 AND y25 -> mdt
    mpp AND hfd -> gjp
    dhd AND mvb -> vrf
    y14 XOR x14 -> qvt
    shc OR bkk -> wvr
    x29 AND y29 -> gdn
    x11 XOR y11 -> jpp
    rws OR fts -> mpp
    wmq OR ngr -> knj
    x24 XOR y24 -> gfg
    tpf AND mgq -> tkc
    wvr XOR jgw -> fkp
    brs AND nmr -> qpb
    x18 AND y18 -> qsw
    pnb OR vjh -> sfh
    x44 XOR y44 -> gcd
    x22 AND y22 -> mhd
    x37 XOR y37 -> dgg
    vfj XOR dmh -> z15
    x30 XOR y30 -> qgd
    rpw OR gdn -> wrv
    ptj OR vqm -> vjn
    gfg AND pqf -> pnb
    x17 XOR y17 -> rtv
    y19 AND x19 -> wpt
    sfp AND sbq -> tcv
    hvv OR vmr -> kpq
    pgc XOR fvj -> z02
    knj AND ctw -> vqm
    y42 XOR x42 -> vfb
    y13 XOR x13 -> vdk
    x43 AND y43 -> nhd
    krg XOR dkw -> z43
    y32 AND x32 -> vdm
    hfd XOR mpp -> z08
    nfq OR qgm -> cqq
    x02 AND y02 -> nss
    rvw XOR dtb -> z10
    qvt AND qqv -> rgj
    mvv XOR sgr -> z36
    y11 AND x11 -> wmq
    cnd XOR jqv -> z29
    vdk XOR vjn -> z13
    x34 AND y34 -> kjj
    qvt XOR qqv -> z14
    y18 XOR x18 -> mkt
    bwk OR krn -> pqf
    nhs XOR cqq -> z41
    y31 AND x31 -> z31
    y23 AND x23 -> bwk
    sfh AND pmf -> ctp
    rvw AND dtb -> sgv
    tns AND chq -> dwv
    rqt XOR snv -> z22
    jqv AND cnd -> rpw
    x33 AND y33 -> vtd
    ctw XOR knj -> z12
    bpp OR ghf -> z06
    ffn AND rdj -> shc
    cfw OR tnc -> sgr
    wdm AND psv -> fwc
    vwn OR wpt -> bvc
    jkn OR gvk -> z45
    x00 XOR y00 -> z00
    qpf XOR mkt -> z18
    y12 AND x12 -> ptj
    dvq XOR rkm -> z04
    x15 XOR y15 -> dmh
    qrm OR nss -> wdm
    mhv OR mnv -> rcs
    qtq OR cqn -> nwk
    x20 XOR y20 -> tpw
    x04 AND y04 -> ptd
    nhd OR pnk -> mdn
    hjc OR hth -> pgc
    x20 AND y20 -> qtq
    gcd XOR mdn -> z44
    mgq XOR tpf -> mfm
    x30 AND y30 -> bbk
    dmh AND vfj -> cpc
    x44 AND y44 -> gvk
    dwv OR jvf -> jjj
    pkh AND fkp -> rws
    x39 AND y39 -> wqc
    fwc OR nbc -> rkm
    bdc AND bbd -> dnc
    x26 XOR y26 -> tns
    csh AND mst -> vwn
    x43 XOR y43 -> dkw
    bvc XOR tpw -> z20
    nwk XOR ngm -> z21
    rtv XOR kfm -> z17
    x06 AND y06 -> bpp
    x10 XOR y10 -> dtb
    y29 XOR x29 -> cnd
    y08 XOR x08 -> hfd
    y03 XOR x03 -> psv
    rgj OR bph -> vfj
    psv XOR wdm -> z03
    dnc OR vtd -> mvb
    gcn AND tqf -> krn
    y38 XOR x38 -> krj
    x24 AND y24 -> vjh
    y41 AND x41 -> vmr
    jgw AND wvr -> ghf
    x09 XOR y09 -> sfp
    y28 AND x28 -> wpw
    x40 XOR y40 -> mkc
    hsn AND dgg -> snc
    jpp XOR stv -> ngr
    mjb OR cpc -> gfj
    rcs XOR rnh -> z28
    sfp XOR sbq -> z09
    rtv AND kfm -> jfr
    tjh OR wpw -> jqv
    x16 XOR y16 -> kmb
    bgn OR wnm -> snv
    nmr XOR brs -> z32
    rpc OR dvm -> hsn
    gfg XOR pqf -> z24
    dkw AND krg -> pnk
    kmb AND gfj -> jrk
    skt XOR kjn -> z01
    gcn XOR tqf -> z23
    jjj XOR rhc -> z27
    y07 AND x07 -> fts
    y21 AND x21 -> wnm
    kvd OR snc -> ntr
    nht XOR hsp -> z39
    wrv XOR qgd -> z30
    y07 XOR x07 -> pkh
    tdv OR krj -> hsp
    stv AND jpp -> z11
    x27 AND y27 -> mhv
    bdc XOR bbd -> z33
    x12 XOR y12 -> ctw
    mvv AND sgr -> dvm
    x27 XOR y27 -> rhc
    x21 XOR y21 -> ngm
    mhn XOR mdg -> z35
    x19 XOR y19 -> csh
    y35 XOR x35 -> mhn
    snv AND rqt -> tfs
    rkm AND dvq -> cjp
    pgc AND fvj -> qrm
    kpq XOR vfb -> z42
    qgd AND wrv -> gqt
    y26 AND x26 -> jvf
    x39 XOR y39 -> nht
    vdk AND vjn -> jqm
    bvc AND tpw -> cqn
    y32 XOR x32 -> nmr
    x25 XOR y25 -> pmf
    y09 AND x09 -> prs
    y14 AND x14 -> bph
    qpb OR vdm -> bdc
    gqt OR bbk -> tpf
    x40 AND y40 -> qgm
    sfh XOR pmf -> z25
    x22 XOR y22 -> rqt
    rhc AND jjj -> mnv
    csh XOR mst -> z19
    x42 AND y42 -> krw
    x34 XOR y34 -> dhd
    x35 AND y35 -> tnc
    ngm AND nwk -> bgn
    tdh OR jqm -> qqv
    y00 AND x00 -> skt
    y41 XOR x41 -> nhs
    ntr XOR bpt -> z38
    vrf OR kjj -> mdg
    kvf OR krw -> krg
    x03 AND y03 -> nbc
    dhd XOR mvb -> z34
    qpf AND mkt -> rsd
    y01 XOR x01 -> kjn
    x17 AND y17 -> bvg
    jfr OR bvg -> qpf
    y13 AND x13 -> tdh
    bpb OR gjp -> sbq
    x16 AND y16 -> swn
    x02 XOR y02 -> fvj
    y15 AND x15 -> mjb
    x23 XOR y23 -> tqf
    rnh AND rcs -> tjh
    x05 AND y05 -> bkk
    hsn XOR dgg -> z37
    qsw OR rsd -> mst
    sgv OR qnf -> stv
    y01 AND x01 -> hth
    y38 AND x38 -> bpt
    rgb AND mkc -> nfq
    y33 XOR x33 -> bbd
    tns XOR chq -> z26
    ctp OR mdt -> chq
    nhs AND cqq -> hvv
    pkh XOR fkp -> z07
    rdj XOR ffn -> z05
    mhd OR tfs -> gcn
    y10 AND x10 -> qnf
    hsp AND nht -> bfp
    gcd AND mdn -> jkn
    ntr AND bpt -> tdv
    prs OR tcv -> rvw
    x05 XOR y05 -> ffn
    y04 XOR x04 -> dvq
    x31 XOR y31 -> mgq
    y08 AND x08 -> bpb
    mhn AND mdg -> cfw
    y37 AND x37 -> kvd
    rgb XOR mkc -> z40
    cjp OR ptd -> rdj
    x06 XOR y06 -> jgw
    skt AND kjn -> hjc
    vfb AND kpq -> kvf
    EOD;	
}

?>
