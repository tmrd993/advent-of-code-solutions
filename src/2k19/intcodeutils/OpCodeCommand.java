package intcodeutils;

public interface OpCodeCommand {
    public void execute();
    public int moveInstructionPointer();
    public int opCodeId();
}
