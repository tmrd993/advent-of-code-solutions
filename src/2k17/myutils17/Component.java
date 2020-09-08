package myutils2k17;

import java.util.Objects;

public class Component {
    private final int LEFT_PIN_TYPE;
    private final int RIGHT_PIN_TYPE;
    private final int ID;
    private Component left;
    private Component right;

    public Component(int leftPin, int rightPin, int id) {
	this.LEFT_PIN_TYPE = leftPin;
	this.RIGHT_PIN_TYPE = rightPin;
	this.ID = id;
	left = null;
	right = null;
    }

    public Component(Component that) {
	this.LEFT_PIN_TYPE = that.LEFT_PIN_TYPE;
	this.RIGHT_PIN_TYPE = that.RIGHT_PIN_TYPE;
	this.left = that.left;
	this.right = that.right;
	this.ID = that.ID;
    }

    public int leftPinType() {
	return LEFT_PIN_TYPE;
    }

    public int rightPinType() {
	return RIGHT_PIN_TYPE;
    }

    public void setLeft(Component that) {
	this.left = that;
    }

    public void setRight(Component that) {
	this.right = that;
    }

    public Component left() {
	return left;
    }

    public Component right() {
	return right;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null) {
	    return false;
	}
	if (!(o instanceof Component)) {
	    return false;
	}

	Component tmp = (Component) o;

	return this.ID == tmp.ID && this.LEFT_PIN_TYPE == tmp.LEFT_PIN_TYPE
		&& this.RIGHT_PIN_TYPE == tmp.RIGHT_PIN_TYPE;

    }
    
    @Override
    public int hashCode() {
	return Objects.hash(ID, LEFT_PIN_TYPE, RIGHT_PIN_TYPE);
    }
    
    @Override
    public String toString() {
	return LEFT_PIN_TYPE + "/" + RIGHT_PIN_TYPE;
    }

}
