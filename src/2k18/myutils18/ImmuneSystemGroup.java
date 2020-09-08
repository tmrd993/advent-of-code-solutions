package myutils18;

import java.util.Objects;
import java.util.Set;

public class ImmuneSystemGroup {

    public enum Type {
	IMMUNE_SYSTEM, INFECTION
    };

    private int units;
    private int hitpointsPerUnit;
    private Set<String> weakTo;
    private Set<String> immuneTo;
    private String attackType;
    private int attackPower;
    private int initiative;
    private Type type;

    public ImmuneSystemGroup(Type type, int units, int hitpoints, Set<String> weakTo, Set<String> immuneTo,
	    String attackType, int attackPower, int initiative) {
	this.type = type;
	this.units = units;
	this.hitpointsPerUnit = hitpoints;
	this.weakTo = weakTo;
	this.immuneTo = immuneTo;
	this.attackType = attackType;
	this.attackPower = attackPower;
	this.initiative = initiative;
    }

    @Override
    public String toString() {
	return type + ", " + units + " units each with " + hitpointsPerUnit + " hitpoints (weak to " + weakTo
		+ "; immune to " + immuneTo + ") with an attack that does " + attackPower + " " + attackType
		+ " damage and an initiative of " + initiative;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (!(o instanceof ImmuneSystemGroup))
	    return false;

	ImmuneSystemGroup tmp = (ImmuneSystemGroup) o;

	if (this.type == tmp.type && this.hitpointsPerUnit == tmp.hitpointsPerUnit
		&& this.weakTo.containsAll(tmp.weakTo) && this.immuneTo.containsAll(tmp.immuneTo)
		&& this.attackPower == tmp.attackPower && this.attackType == tmp.attackType
		&& this.initiative == tmp.initiative) {
	    return true;
	}

	return false;
    }

    @Override
    public int hashCode() {
	return Objects.hash(type, hitpointsPerUnit, attackPower, attackType, initiative);
    }

    public int getEffPower() {
	return units * attackPower;
    }

    public Type type() {
	return type;
    }

    public int initiative() {
	return initiative;
    }

    public int units() {
	return units;
    }

    public Set<String> immunities() {
	return immuneTo;
    }

    public Set<String> weaknesses() {
	return weakTo;
    }

    public String attackType() {
	return attackType;
    }

    public int attackPower() {
	return attackPower;
    }

    public int hitpoints() {
	return hitpointsPerUnit;
    }

    public int attack(ImmuneSystemGroup target) {
	int damageDone = this.getEffPower();
	if (target.weakTo.contains(this.attackType))
	    damageDone *= 2;
	int deadUnits = damageDone / target.hitpointsPerUnit;
	if(deadUnits > target.units) {
	    deadUnits = target.units;
	}
	target.units -= deadUnits;
	return deadUnits;
    }
}
