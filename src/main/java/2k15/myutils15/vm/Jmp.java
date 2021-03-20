package myutils15.vm;

import java.util.List;

public class Jmp implements VMCommand {

    @Override
    public void run(VM vm, List<String> params) {
	vm.setInstructionPointer(vm.getInstructionPointer() + Integer.parseInt(params.get(0)));
    }

}
