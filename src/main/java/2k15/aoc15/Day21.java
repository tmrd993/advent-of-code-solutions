package aoc15;

import java.util.List;
import java.util.function.BooleanSupplier;

import myutils15.Pair;

public class Day21 {
    // input values
    private final int enemyHp = 109;
    private final int enemyAtk = 8;
    private final int enemyArmor = 2;

    // start values for the player unit
    private final int playerHp = 100;
    private final int playerAtk = 0;
    private final int playerArmor = 0;

    // shop as lists of value, cost pairs
    private final List<Pair<Integer, Integer>> weapons = List.of(new Pair<>(4, 8),
	    new Pair<>(5, 10),
	    new Pair<>(6, 25),
	    new Pair<>(7, 40),
	    new Pair<>(8, 74));
    private final List<Pair<Integer, Integer>> armor = List.of(new Pair<>(0, 0),
	    new Pair<>(1, 13),
	    new Pair<>(2, 31),
	    new Pair<>(3, 53),
	    new Pair<>(4, 75),
	    new Pair<>(5, 102));
    private final List<Pair<Integer, Integer>> rings = List.of(new Pair<>(0, 0),
	    new Pair<>(0, 0),
	    new Pair<>(1, 25),
	    new Pair<>(2, 50),
	    new Pair<>(3, 100),
	    new Pair<>(1, 20),
	    new Pair<>(2, 40),
	    new Pair<>(3, 80)
	    );

    private int run1() {
	int lowestGoldUsed = Integer.MAX_VALUE;
	for(Pair<Integer, Integer> weapon : weapons) {
	    for(Pair<Integer, Integer> armorPiece : armor) {
		for(int i = 0; i < rings.size() - 1; i++) {
		    for(int j = i + 1; j < rings.size(); j++) {
			int weaponPower = weapon.k();
			int armorPower = armorPiece.k();
			if(i < 5) {
			    weaponPower += rings.get(i).k();
			} else {
			    armorPower += rings.get(i).k();
			}
			if(j < 5) {
			    weaponPower += rings.get(j).k();
			} else {
			    armorPower += rings.get(j).k();
			}
			
			int goldUsed = weapon.v() + armorPiece.v() + rings.get(i).v() + rings.get(j).v();
			Unit player = new Unit(playerHp, playerAtk + weaponPower, playerArmor + armorPower);
			Unit enemy = new Unit(enemyHp, enemyAtk, enemyArmor);
			if(playerWins(player, enemy) && goldUsed < lowestGoldUsed) {
			    lowestGoldUsed = goldUsed;
			}
		    }
		}
	    }
	}

	return lowestGoldUsed;
    }

    private int run2() {
	int highestGoldUsed = 0;
	for(Pair<Integer, Integer> weapon : weapons) {
	    for(Pair<Integer, Integer> armorPiece : armor) {
		for(int i = 0; i < rings.size() - 1; i++) {
		    for(int j = i + 1; j < rings.size(); j++) {
			int weaponPower = weapon.k();
			int armorPower = armorPiece.k();
			if(i < 5) {
			    weaponPower += rings.get(i).k();
			} else {
			    armorPower += rings.get(i).k();
			}
			if(j < 5) {
			    weaponPower += rings.get(j).k();
			} else {
			    armorPower += rings.get(j).k();
			}
			
			int goldUsed = weapon.v() + armorPiece.v() + rings.get(i).v() + rings.get(j).v();
			Unit player = new Unit(playerHp, playerAtk + weaponPower, playerArmor + armorPower);
			Unit enemy = new Unit(enemyHp, enemyAtk, enemyArmor);
			if(!playerWins(player, enemy) && goldUsed > highestGoldUsed) {
			    highestGoldUsed = goldUsed;
			}
		    }
		}
	    }
	}

	return highestGoldUsed;
    }
    
    // return true if player wins the battle
    private boolean playerWins(Unit player, Unit enemy) {
	while(player.hp > 0 && enemy.hp > 0) {
	    player.attack(enemy);
	    if(enemy.hp > 0)
		enemy.attack(player);
	}
	
	return player.hp > 0;
    }

    public static void main(String[] args) {
	Day21 test = new Day21();
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

    private static class Unit {
	private int hp;
	private int attack;
	private int armor;

	public Unit(int hp, int attack, int armor) {
	    this.hp = hp;
	    this.attack = attack;
	    this.armor = armor;
	}

	public void attack(Unit target) {
	    int damage = Math.max(1, this.attack - target.armor);
	    target.hp -= damage;
	}
    }
}
