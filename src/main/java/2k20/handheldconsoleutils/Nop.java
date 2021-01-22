package handheldconsoleutils;

public class Nop implements Instruction{

    @Override
    public int run() {
	return 1;
    }

}
