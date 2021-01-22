package myutils17;

import java.util.Objects;

/**
 *
 * @author Timucin
 * utility class for Day 7
 */
public class Program {

    private final String NAME;
    private final int WEIGHT;

    public Program(String name, int weight) {
	this.NAME = name;
	this.WEIGHT = weight;
    }

    public String name() {
	return NAME;
    }

    public int weight() {
	return WEIGHT;
    }

    @Override
    public String toString() {
	return NAME + " " + WEIGHT;
    }

    @Override
    public int hashCode() {
	return Objects.hash(NAME, WEIGHT);
    }

    @Override
    public boolean equals(Object o) {
	if(this == o)
	    return true;
	if(o == null)
	    return false;
	if(!(o instanceof Program))
	    return false;

	Program tmp = (Program) o;

	return this.NAME.equals(tmp.NAME) && this.WEIGHT == tmp.WEIGHT;
    }

}
