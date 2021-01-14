package myutils16;

public interface Instruction {
    public void execute();
    public int index();
    
    default int indexOf(char register) {
	return register - 97;
    }
}
