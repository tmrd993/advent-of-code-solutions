package myutils2k17;

public class Particle {
    private Triple<Integer, Integer, Integer> position;
    private Triple<Integer, Integer, Integer> velocity;
    private Triple<Integer, Integer, Integer> acceleration;

    public Particle(Triple<Integer, Integer, Integer> pos, Triple<Integer, Integer, Integer> vel,
	    Triple<Integer, Integer, Integer> acc) {
	position = pos;
	velocity = vel;
	acceleration = acc;
    }

    public Triple<Integer, Integer, Integer> getPosition() {
	return position;
    }

    public Triple<Integer, Integer, Integer> getVelocity() {
	return velocity;
    }

    public Triple<Integer, Integer, Integer> getAcceleration() {
	return acceleration;
    }

    public boolean hasCollided(Particle that) {
	return this.position.getFirst() == that.position.getFirst()
		&& this.position.getSecond() == that.position.getSecond()
		&& this.position.getThird() == that.position.getThird();
    }

    public void move() {
	velocity.setFirst(velocity.getFirst() + acceleration.getFirst());
	velocity.setSecond(velocity.getSecond() + acceleration.getSecond());
	velocity.setThird(velocity.getThird() + acceleration.getThird());
	position.setFirst(position.getFirst() + velocity.getFirst());
	position.setSecond(position.getSecond() + velocity.getSecond());
	position.setThird(position.getThird() + velocity.getThird());
    }

    @Override
    public String toString() {
	return "p:" + position.toString() + ", " + "v:" + velocity.toString() + ", " + "a:" + acceleration.toString();
    }

}
