package aoc16;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

import myutils16.Instruction;
import myutils16.InstructionFactory;
import myutils16.MathUtils;
import myutils16.StaticUtils;

public class Day23 {

    private List<String> instructions;

    public Day23(File input) {
	instructions = StaticUtils.inputToList(input);
    }

    public int run1() {
	int[] registers = IntStream.of(0, 0, 0, 0).toArray();

	registers[0] = 7;

	int index = 0;
	while (index >= 0 && index < instructions.size()) {
	    Instruction instruction = InstructionFactory.getInstruction(instructions.get(index), registers, index,
		    instructions);
	    instruction.execute();
	    index = instruction.index();
	}

	return registers[0];
    }

    // Solved by reverse engineering the assembunny code, this obviously won't work
    // for you if our inputs don't match.
    // the one line solution is simply 12! + (84 * 76). This is much easier to solve
    // using a pen and paper approach.
    public int run2() {
	// register a starts at 12
	int a = 12;
	// starting instructions
	// cpy a b
	// dec b
	// cpy a d
	// cpy 0 a
	// cpy b c

	// this causes a: 0, b: 11, c:11, d:12
	// inc a
	// dec c
	// jnz c -2
	// dec d
	// jnz d -5
	// this translates to: a = c * d so we get a = 132

	// dec b
	// cpy b c
	// cpy c d
	// dec d
	/// inc c
	// jnz d -2
	// this causes: a: 132, b:10, c: c:20, d:0

	// tgl c
	// cpy -16 c
	// jnz 1 c
	
	// tgl c == tgl 20, which is out of bounds and does nothing for now
	// this causes c:-16 and jumps to instruction index - 16

	// cpy a d
	// cpy 0 a
	// cpy b c
	// inc a
	// dec c
	// jnz c -2
	// dec d
	// jnz d -5
	// this is the same set of instructions we went through above
	// this causes a:1320 which is c * d which is 132 * 10
	// at this point its easy to spot that this does nothing more than calculate a!

	// this is the last portion of the program:
	// 1. tgl c
	// 2. cpy -16 c
	// 3. jnz 1 c
	// 4. cpy 76 c
	// 5. jnz 84 d
	// 6. inc a
	// 7. inc d
	// 8. jnz d -2
	// 9. inc c
	// 10. jnz c -5
	// after a = 12!, the tgl instruction will have gradually overwritten row 9, row
	// 7, row 5 and row 3 which stops the instruction pointer from jumping back to
	// the
	// start. row 3 turns into a useless instruction and is skipped, row 5 turns
	// into a cpy instruction,
	// row 7 into dec, row 9 into dec which leads to the final part of the program:
	// 4. cpy 76 c
	// 5. cpy 84 d
	// 6. inc a
	// 7. dec d
	// 8. jnz d -2
	// 9. dec c
	// 10. jnz c -5

	// this copies 76 into c, and 84 into d. afterwards, it calculates a += c * d
	// which is a += 76 * 84
	// the result is 12! + (84 * 76)

	return (int) MathUtils.factorial(a) + 76 * 84;
    }

    public static void main(String[] args) {
	Day23 test = new Day23(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 23\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
