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
		System.out.println("Sample phase one result, expected: 1,6,7,4,3,0,5,0,6, actual: " + computeAndReturnOutput(parseComputer(sample)));
	}
	
	public static String computeAndReturnOutput(Computer c) {
		while (c.step()) {};
		return c.output;
	}
	
	public static class Computer {
		public long a,b,c; //registers
		public int pc; //program counter
		public String output = "";
		
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
					output += (combo % 8) + ",";
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
							Register A: 63687530
							Register B: 0
							Register C: 0
							
							Program: 2,4,1,3,7,5,0,3,1,5,4,1,5,5,3,0
							""";

	static String input =	"""
							""";
}