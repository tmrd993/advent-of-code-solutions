package myutils15.vm;

import java.util.List;

public class Jie implements VMCommand {

    @Override
    public void run(VM vm, List<String> params) {
	char reg = params.get(0).charAt(0);
	int offset = Integer.parseInt(params.get(1));
	
	if(vm.getRegister(reg) % 2 == 0) {
	    vm.setInstructionPointer(vm.getInstructionPointer() + offset);
	} else {
	    vm.setInstructionPointer(vm.getInstructionPointer() + 1);
	}
    }

}
