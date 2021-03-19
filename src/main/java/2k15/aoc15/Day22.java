package aoc15;

import java.util.LinkedList;
import java.util.Queue;

import myutils15.Pair;

public class Day22 {

    private final int enemyHp = 71;
    private final int enemyAtk = 10;

    private final int playerHp = 50;
    private final int playerMana = 500;

    public int run1() {
	Queue<Integer> manaSpentQueue = new LinkedList<>();
	manaSpentQueue.add(0);
	Queue<Pair<Unit, Unit>> fightQueue = new LinkedList<>();
	Unit startPlayer = new Unit(playerHp, playerMana, 0, 0);
	Unit startEnemy = new Unit(enemyHp, 0, enemyAtk, 0);
	fightQueue.add(new Pair<>(startPlayer, startEnemy));

	int leastManaSpent = Integer.MAX_VALUE;
	while (!fightQueue.isEmpty()) {
	    Pair<Unit, Unit> currentFight = fightQueue.poll();
	    int manaSpent = manaSpentQueue.poll();
	    Unit player = currentFight.k();
	    Unit enemy = currentFight.v();

	    Unit.Spell[] spells = Unit.Spell.values();
	    for (Unit.Spell spell : spells) {
		Unit tmpEnemy = Unit.copy(enemy);
		Unit tmpPlayer = Unit.copy(player);

		applyEffects(tmpPlayer, tmpEnemy);

		if (tmpEnemy.hp <= 0) {
		    if (manaSpent < leastManaSpent)
			leastManaSpent = manaSpent;
		    continue;
		}

		// if we can't cast spells, skip this state
		if ((spell.manaCost() > tmpPlayer.mana) || (spell == Unit.Spell.POISON && tmpEnemy.poisonCounter > 0)
			|| (spell == Unit.Spell.RECHARGE && tmpPlayer.rechargeCounter > 0)
			|| (spell == Unit.Spell.SHIELD && tmpPlayer.shieldCounter > 0)) {
		    continue;
		}

		tmpPlayer.castSpell(spell, tmpEnemy);
		int tmpManaSpent = manaSpent + spell.manaCost();

		if (tmpEnemy.hp > 0) {

		    applyEffects(tmpPlayer, tmpEnemy);

		    if (tmpEnemy.hp <= 0) {
			if (tmpManaSpent < leastManaSpent)
			    leastManaSpent = tmpManaSpent;
			continue;
		    }

		    tmpEnemy.attack(tmpPlayer);
		    if (tmpPlayer.hp > 0) {
			fightQueue.add(new Pair<>(tmpPlayer, tmpEnemy));
			manaSpentQueue.add(tmpManaSpent);
		    }
		} else {
		    if (tmpManaSpent < leastManaSpent) {
			leastManaSpent = tmpManaSpent;
		    }
		}
	    }
	}

	return leastManaSpent;
    }

    public int run2() {
	Queue<Integer> manaSpentQueue = new LinkedList<>();
	manaSpentQueue.add(0);
	Queue<Pair<Unit, Unit>> fightQueue = new LinkedList<>();
	Unit startPlayer = new Unit(playerHp, playerMana, 0, 0);
	Unit startEnemy = new Unit(enemyHp, 0, enemyAtk, 0);
	fightQueue.add(new Pair<>(startPlayer, startEnemy));

	int leastManaSpent = Integer.MAX_VALUE;
	while (!fightQueue.isEmpty()) {
	    Pair<Unit, Unit> currentFight = fightQueue.poll();
	    int manaSpent = manaSpentQueue.poll();
	    Unit player = currentFight.k();
	    Unit enemy = currentFight.v();

	    Unit.Spell[] spells = Unit.Spell.values();
	    for (Unit.Spell spell : spells) {
		Unit tmpEnemy = Unit.copy(enemy);
		Unit tmpPlayer = Unit.copy(player);

		tmpPlayer.hp--;
		if (tmpPlayer.hp <= 0) {
		    continue;
		}
		applyEffects(tmpPlayer, tmpEnemy);

		if (tmpEnemy.hp <= 0) {
		    if (manaSpent < leastManaSpent)
			leastManaSpent = manaSpent;
		    continue;
		}

		// if we can't cast spells, skip this state
		if ((spell.manaCost() > tmpPlayer.mana) || (spell == Unit.Spell.POISON && tmpEnemy.poisonCounter > 0)
			|| (spell == Unit.Spell.RECHARGE && tmpPlayer.rechargeCounter > 0)
			|| (spell == Unit.Spell.SHIELD && tmpPlayer.shieldCounter > 0)) {
		    continue;
		}

		tmpPlayer.castSpell(spell, tmpEnemy);
		int tmpManaSpent = manaSpent + spell.manaCost();

		if (tmpEnemy.hp > 0) {
		    applyEffects(tmpPlayer, tmpEnemy);

		    if (tmpEnemy.hp <= 0) {
			if (tmpManaSpent < leastManaSpent)
			    leastManaSpent = tmpManaSpent;
			continue;
		    }

		    tmpEnemy.attack(tmpPlayer);
		    if (tmpPlayer.hp > 0) {
			fightQueue.add(new Pair<>(tmpPlayer, tmpEnemy));
			manaSpentQueue.add(tmpManaSpent);
		    }
		} else {
		    if (tmpManaSpent < leastManaSpent) {
			leastManaSpent = tmpManaSpent;
		    }
		}
	    }
	}

	return leastManaSpent;
    }

    private void applyEffects(Unit player, Unit enemy) {
	if (enemy.poisonCounter > 0) {
	    enemy.hp -= 3;
	    enemy.poisonCounter--;
	}

	if (player.shieldCounter > 0) {
	    player.shieldCounter--;
	    if (player.shieldCounter == 0) {
		player.armor -= 7;
	    }
	}

	if (player.rechargeCounter > 0) {
	    player.mana += 101;
	    player.rechargeCounter--;
	}
    }

    public static void main(String[] args) {
	Day22 test = new Day22();
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

    private static class Unit {
	private enum Spell {
	    MAGIC_MISSILE(53), DRAIN(73), SHIELD(113), POISON(173), RECHARGE(229);

	    private final int manaCost;

	    Spell(int manaCost) {
		this.manaCost = manaCost;
	    }

	    public int manaCost() {
		return manaCost;
	    }
	};

	private int hp;
	private int mana;
	private int atk;
	private int armor;

	private int poisonCounter;
	private int shieldCounter;
	private int rechargeCounter;

	public Unit(int hp, int mana, int atk, int armor) {
	    this.hp = hp;
	    this.mana = mana;
	    this.atk = atk;
	    this.armor = armor;
	}

	public void attack(Unit target) {
	    int damage = Math.max(1, this.atk - target.armor);
	    target.hp -= damage;
	}

	public void castSpell(Spell spell, Unit target) {
	    if (spell == Spell.MAGIC_MISSILE) {
		target.hp -= 4;
		this.mana -= Spell.MAGIC_MISSILE.manaCost();
	    } else if (spell == Spell.DRAIN) {
		target.hp -= 2;
		this.hp += 2;
		this.mana -= Spell.DRAIN.manaCost();
	    } else if (spell == Spell.SHIELD) {
		this.shieldCounter = 6;
		this.armor += 7;
		this.mana -= Spell.SHIELD.manaCost();
	    } else if (spell == Spell.POISON) {
		target.poisonCounter = 6;
		this.mana -= Spell.POISON.manaCost();
	    } else if (spell == Spell.RECHARGE) {
		this.rechargeCounter = 5;
		this.mana -= Spell.RECHARGE.manaCost();
	    }
	}

	public static Unit copy(Unit target) {
	    Unit copy = new Unit(target.hp, target.mana, target.atk, target.armor);
	    copy.poisonCounter = target.poisonCounter;
	    copy.shieldCounter = target.shieldCounter;
	    copy.rechargeCounter = target.rechargeCounter;
	    return copy;
	}
    }

}
