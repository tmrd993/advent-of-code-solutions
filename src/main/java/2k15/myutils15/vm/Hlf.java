package myutils15.vm;

import java.util.List;

public class Hlf implements VMCommand {

    @Override
    public void run(VM vm, List<String> params) {
	char register = params.get(0).charAt(0);
	vm.setRegister(register, vm.getRegister(register) / 2);
	vm.setInstructionPointer(vm.getInstructionPointer() + 1);
    }

}
