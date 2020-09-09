package intcodeutils;

public interface OpCodeCommand {
    public void execute();
    public int skipCount();
}
