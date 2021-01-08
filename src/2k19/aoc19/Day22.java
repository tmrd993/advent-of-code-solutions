package aoc19;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import myutils19.StaticUtils;

public class Day22 {

    public List<String> rawData;

    public Day22(File inputFile) {
	rawData = StaticUtils.fileToStringList(inputFile);
    }

    public int run1() {
	int targetIndex = 2019;
	int len = 10007;

	for (String instr : rawData) {
	    if (instr.contains("stack")) {
		targetIndex = len - targetIndex - 1;
	    } else if (instr.contains("incr")) {
		int n = Integer.parseInt(instr.substring(instr.lastIndexOf(' ') + 1));
		targetIndex = Math.floorMod(targetIndex * n, len);
	    } else if (instr.contains("cut")) {
		int n = Integer.parseInt(instr.substring(instr.lastIndexOf(' ') + 1));
		targetIndex = Math.floorMod(targetIndex + (n * (-1)), len);
	    }
	}

	return targetIndex;
    }

    public BigInteger run2() {
	BigInteger targetIndex = BigInteger.valueOf(2020);
	BigInteger len = BigInteger.valueOf(119315717514047L);
	BigInteger shuffleCount = BigInteger.valueOf(101741582076661L);

	// coefficients a and b for f(x) = a * x + b. This is the final equation for a
	// full shuffle. f(2019) solves part 1
	BigInteger[] coefficients = getFinalCoefficients(len.longValue());

	// Full shuffle has to be repeated 101 741 582 076 661 times. fs(x) = Ax + B mod
	// len is
	// the solution with A and B being unknown coefficients. If we go through a few
	// steps (compose fs(x) with itself a few times) we notice that fs(x) is a
	// geometric series
	// that means fs(x) can be rewritten as: fs(x) = a^k + ((b*(1 - a^k)) / (1 - a))
	// mod len
	// mod len (formula lifted from Wikipedia:
	// https://en.wikipedia.org/wiki/Geometric_progression#Geometric_series)
	// that means A = a^k mod len and B = ((b*(1 - a^k)) / (1 - a)) mod len
	// a^k mod len.

	// The problem with this approach is that shuffleCount is too large
	// for the BigInteger.pow(int exp) method. That's why the formula for B has to
	// be simplified using modular arithmetic rules. After simplifying: A = a^k mod
	// len and B = (b mod len) * (1 mod len - a^k mod len) mod len * (1 - a)^-1 mod len
	// note that a division turns into multiplying with the modular inverse when dealing with modulo
	BigInteger A = coefficients[0].modPow(shuffleCount, len);
	BigInteger B = coefficients[1].mod(len).multiply(BigInteger.ONE.mod(len).subtract(A).mod(len)).mod(len)
		.multiply(BigInteger.ONE.subtract(coefficients[0]).modInverse(len)).mod(len);
	
	// fs(x) = Ax + B mod len returns the index of x after len shuffles. However,
	// part 2
	// asks for the number at index x. If we plug x = 2020 we get a number that
	// corresponds to the index x has been moved to after the shuffles. That means
	// we are looking for the number that ends up at index x after the shuffles
	// which is obtained by simply inverting the function fs(x) = Ax + B mod len
	// fs(x) says "multiply x by A and add B". The inversion of this function would
	// be "subtract B from x and divide by A" So fs^-1(x) = ((x - B) / A) mod len
	BigInteger result = targetIndex.subtract(B).multiply(A.modInverse(len)).mod(len);

	return result;
    }

    // returns coefficients a and b for the linear congruential equation f(x) = ax +
    // b MOD n with
    // f(2019) being the solution for part 1
    private BigInteger[] getFinalCoefficients(long size) {
	BigInteger[] coeff = getCoefficients(rawData.get(0));
	for (int i = 1; i < rawData.size(); i++) {
	    coeff = compose(rawData.get(i), coeff, size);
	}

	return coeff;
    }

    // returns the current coefficients (a, b) updated with new values from the next
    // instruction eg, composes the instruction (g(x)) with the current coefficients
    // (f(x)) so the result is f(g(x))
    private BigInteger[] compose(String instr, BigInteger[] coefficients, long size) {
	BigInteger a = coefficients[0];
	BigInteger b = coefficients[1];

	BigInteger[] cd = getCoefficients(instr);
	BigInteger c = cd[0];
	BigInteger d = cd[1];

	// composition of two linear congruential equations f(x) = ax + b mod n and g(x)
	// = cx + b mod n:
	// using substitution: g(f(x)) = cax + cb + d mod n or g(f(x)) = Ax + B mod n
	// with A = ac mod n and B = bc + d mod n
	return new BigInteger[] { a.multiply(c).mod(BigInteger.valueOf(size)),
		b.multiply(c).add(d).mod(BigInteger.valueOf(size)) };
    }

    private BigInteger[] getCoefficients(String instr) {
	long a = 0L;
	long b = 0L;
	BigInteger[] coeff = new BigInteger[2];
	// f(x) = - x - 1 MOD n
	if (instr.contains("stack")) {
	    a = -1L;
	    b = -1L;
	}
	// f(x) = incr * x MOD n
	else if (instr.contains("incr")) {
	    a = Long.parseLong(instr.substring(instr.lastIndexOf(' ') + 1));
	    b = 0L;
	}
	// f(x) = x - cut MOD n
	else if (instr.contains("cut")) {
	    a = 1L;
	    long amount = Long.parseLong(instr.substring(instr.lastIndexOf(' ') + 1));
	    b = -amount;

	}
	coeff[0] = BigInteger.valueOf(a);
	coeff[1] = BigInteger.valueOf(b);

	return coeff;
    }

    public static void main(String[] args) {
	Day22 test = new Day22(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 22\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
