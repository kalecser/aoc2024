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

class Day17 {
	
	public static void main(String[] args) {
		System.out.println("Sample phase one result, expected: 4,6,3,5,6,3,5,2,1,0 actual: " + computeAndReturnOutput(parseComputer(sample)));
		System.out.println("Actual phase one result, expected: 1,6,7,4,3,0,5,0,6 actual: " + computeAndReturnOutput(parseComputer(input)));
	
		System.out.println("Actual phase two result, expected: 21614833863330253 actual: " + findQuineChangingRegisterA(parseComputer(input)));
	}
	
	public static long findQuineChangingRegisterA(Computer c) {
		
		/*
		2,4, 1,3, 7,5, 0,3, 1,5, 4,1, 5,5, 3,0
		
		pseudo-code: program
		01: b = a % 8
		02: b = b % 3
		03: c = a / (2^b)
		04: a = a / (2^3)
		05: b ^ 5
		06: b ˆ c
		07: print b
		08: NEZ repeat
		
		notice: a only changes in decrements of 3 bits -> line 04: a = a / (2^3)
		*/
		
		byte[] digitsNumber3Bits = new byte[]{0,1,2,3,4,5,6,7};
		byte[] candidate = new byte[16]; //16 is the size of the program, see above
		byte[] outcome = new byte[]{2,4, 1,3, 7,5, 0,3, 1,5, 4,1, 5,5, 3,0}; //the program
		
		for(byte i = 0; i < candidate.length; i++){
			candidate[i] = 1; //initialize with 1
		}
		
		return findNumber(digitsNumber3Bits, candidate, outcome, 0, c);
	}
	
	public static Long findNumber(byte[] digitsNumber3Bits, byte[] candidate, byte[] expected, int word, Computer c) {
		if (word == expected.length) {
			String num = "";
			for(var d : candidate) {
				num = d + num;
			}
			var decimal = Long.parseLong(num, 8);
			return decimal;
		} 
	
		for (var digit : digitsNumber3Bits) {
			candidate[expected.length - word - 1] = digit;
			
			String num = "";
			for(var d : candidate) {
				num = d + num;
			}
			var decimal = Long.parseLong(num, 8);
			
			byte[] result = cloneComputerAndReturnOutputGivenRegisterA(decimal, c);
			if (result.length == expected.length && result[expected.length - word - 1] == expected[expected.length - word - 1]) {
				var downstream = findNumber(digitsNumber3Bits, candidate.clone(), expected, word+1, c);
				if (downstream != null) {
					return downstream;
				}
			}
		}
	
		return null;
	}
	
	public static byte[] cloneComputerAndReturnOutputGivenRegisterA(long registerA, Computer c) {
		Computer cloned = (Computer) c.clone(); // Perform shallow copy
		
		cloned.a = registerA;
		while(cloned.step()) {}
		
		return cloned.output;
	}
	
	public static String computeAndReturnOutput(Computer c) {
		while (c.step()) {};
		return Arrays.toString(c.output).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
	}
	
	public static class Computer implements Cloneable {
		public long a,b,c; //registers
		public int pc; //program counter
		public byte[] output = new byte[0];
		
		public byte step = 2;
		public byte[] program;
		
		
		public boolean step() {
			OpCode opCode = OpCode.fromByte(program[pc]);
			byte literal = program[pc + 1];
			pc = pc + step;
			
			long combo = 0l;
			if (literal < 4) {
				combo = literal;
			} else if (literal == 4) { //register A
				combo = a;
			} else if (literal == 5) { //register B
				combo = b;
			} else if (literal == 6) { //register C
				combo = c;
			} else {
				throw new IllegalArgumentException("Combo operand 7 is reserved and will not appear in valid programs");
			}
			
			switch (opCode) {
				case OpCode.ADV:
					a = a / (long)(Math.pow(2, combo));
					break;
				case OpCode.BXL:
					b = b ^ literal;
					break;
				case OpCode.BST:
					b = combo % 8;
					break;
				case OpCode.JNZ:
					if (a != 0) pc = (int)literal; //ATT
					break;
				case OpCode.BXC:
					b = b ^ c;
					break;
				case OpCode.OUT:
					var old_output = output;
					output = new byte[output.length + 1];
					System.arraycopy(old_output, 0, output, 0, old_output.length);
					output[old_output.length] = (byte)(combo % 8);
					break;
				case OpCode.BDV:
					b = a / (long)(Math.pow(2, combo));
					break;
				case OpCode.CDV:
					c = a / (long)(Math.pow(2, combo));
					break;
				default:
					throw new IllegalArgumentException("panic");
			}
			
			return pc < program.length;
		}
		
		public enum OpCode {
			ADV((byte)0),
			BXL((byte)1),
			BST((byte)2),
			JNZ((byte)3),
			BXC((byte)4),
			OUT((byte)5),
			BDV((byte)6),
			CDV((byte)7),
			;
			
			private final byte value;
			
			OpCode(byte value) {
				this.value = value;
			}
			
			public byte getValue() {
				return value;
			}
			
			public static OpCode fromByte(byte code) {
				for (OpCode e : OpCode.values()) {
					if (e.value == code) {
						return e;
					}
				}
				throw new IllegalArgumentException("Invalid code: " + code);
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Register A: ").append(a).append("\n");
			sb.append("Register B: ").append(b).append("\n");
			sb.append("Register C: ").append(c).append("\n");
			sb.append("Program: ");
			if (program != null) {
				for (byte b : program) {
					sb.append(b).append(",");
				}
				// Remove the trailing comma
				if (program.length > 0) {
					sb.setLength(sb.length() - 1);
				}
			}
			sb.append("\n");
			sb.append("PC: ").append(pc).append("\n");
			sb.append("Output: ").append(output).append("\n");
			sb.append("\n");
			return sb.toString();
		}
		
		@Override
		public Computer clone() {
			try {
				Computer cloned = (Computer) super.clone(); // Perform shallow copy
				cloned.program = this.program.clone();
				cloned.output = this.output.clone();
				
				return cloned;
			} catch (CloneNotSupportedException e) {
				throw new AssertionError("Cloning failed", e); // Should not happen
			}
		}
	}
	
	public static Computer parseComputer(String input) {
		Computer computer = new Computer();
		
		String[] lines = input.split("\n");
		
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("Register A:")) {
				computer.a = Long.parseLong(line.split(":")[1].trim());
			} else if (line.startsWith("Register B:")) {
				computer.b = Long.parseLong(line.split(":")[1].trim());
			} else if (line.startsWith("Register C:")) {
				computer.c = Long.parseLong(line.split(":")[1].trim());
			} else if (line.startsWith("Program:")) {
				String programData = line.split(":")[1].trim();
				String[] programParts = programData.split(",");
				
				computer.program = new byte[programParts.length];
				for (int i = 0; i < programParts.length; i++) {
					computer.program[i] = Byte.parseByte(programParts[i].trim());
				}
			}
		}
		
		return computer;
	}
	
	static String sample =	"""
							Register A: 729
							Register B: 0
							Register C: 0
							
							Program: 0,1,5,4,3,0
							""";
	
	static String sample2 =	"""
						Register A: 2024
						Register B: 0
						Register C: 0
						
						Program: 0,3,5,4,3,0
						""";

	static String input =	"""
							Register A: 63687530
							Register B: 0
							Register C: 0
							
							Program: 2,4, 1,3, 7,5, 0,3, 1,5, 4,1, 5,5, 3,0
							""";
}


