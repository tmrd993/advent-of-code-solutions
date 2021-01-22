package intcodeutils;

public interface OpCodeCommand {
    public void execute();
    public long moveInstructionPointer();
    public int opCodeId();
}
