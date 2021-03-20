package aoc15;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import myutils15.StaticUtils;
import myutils15.vm.VM;
import myutils15.vm.VMCommand;

public class Day23 {

    private List<String> instructions;

    public int run1() {
	VM vm = new VM();
	runVM(vm);
	return vm.getRegister('b');
    }

    public int run2() {
	VM vm = new VM();
	vm.setRegister('a', 1);
	runVM(vm);
	return vm.getRegister('b');
    }

    public void runVM(VM vm) {
	int ip = 0;

	Map<String, VMCommand> comMapping = VMCommand.comMapping();

	while (ip >= 0 && ip < instructions.size()) {
	    String instr = instructions.get(ip);
	    String comStr = instr.substring(0, 3);
	    VMCommand com = comMapping.get(comStr);

	    List<String> params = Arrays.stream(instr.substring(4).split(" "))
		    .map(str -> str.replace(",", "").trim())
		    .collect(Collectors.toList());

	    com.run(vm, params);
	    ip = vm.getInstructionPointer();
	}
    }

    public Day23(File input) {
	instructions = StaticUtils.fileToStringList(input);
    }

    public static void main(String[] args) {
	Day23 test = new Day23(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 23\\InputFile1.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
