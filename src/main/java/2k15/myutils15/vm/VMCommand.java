package myutils15.vm;

import java.util.List;
import java.util.Map;

public interface VMCommand {

    public void run(VM vm, List<String> params);

    static Map<String, VMCommand> comMapping() {
	return Map.of("hlf", new Hlf(),
		"tpl", new Tpl(),
		"inc", new Inc(),
		"jie", new Jie(),
		"jio", new Jio(),
		"jmp", new Jmp());
    }

}
