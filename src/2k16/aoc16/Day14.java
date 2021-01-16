package aoc16;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {

    private final String salt = "ihaygndm";
    
    public int run(boolean part2) {
	Map<String, Integer> quintupleIndexMapping = new HashMap<>();
	Map<Integer, String> tripleIndexMapping = new HashMap<>();
	List<String> hashList = new ArrayList<>();

	int saltIndex = 0;
	for (int i = 0; i <= 1000; i++) {
	    String hash = "";
	    if (part2) {
		hash = stretchedMd5(salt + saltIndex++);
	    } else {
		hash = md5(salt + saltIndex++);
	    }

	    hashList.add(hash);

	    String trip = "";
	    if ((trip = getTriple(hash)) != null) {
		tripleIndexMapping.put(i, trip);
	    }

	    String quint = "";
	    if ((quint = getQuintuple(hash)) != null) {
		quintupleIndexMapping.put(quint, i);
	    }
	}

	int keys = 0;
	int targetIndex = 0;
	int currentIndex = 0;
	while (keys < 64) {
	    if (tripleIndexMapping.containsKey(currentIndex)) {
		String quint = tripleIndexMapping.get(currentIndex).substring(0, 2)
			+ tripleIndexMapping.get(currentIndex);
		if (quintupleIndexMapping.containsKey(quint) && quintupleIndexMapping.get(quint) < (currentIndex + 1000)
			&& quintupleIndexMapping.get(quint) > currentIndex) {
		    keys++;
		    targetIndex = currentIndex;
		}
	    }

	    String nextHash = "";
	    if (part2) {
		nextHash = stretchedMd5(salt + saltIndex);
	    } else {
		nextHash = md5(salt + saltIndex);
	    }

	    String trip = "";
	    if ((trip = getTriple(nextHash)) != null) {
		tripleIndexMapping.put(saltIndex, trip);
	    }

	    String quint = "";
	    if ((quint = getQuintuple(nextHash)) != null) {
		quintupleIndexMapping.put(quint, saltIndex);
	    }

	    hashList.add(nextHash);

	    saltIndex++;
	    currentIndex++;
	}

	return targetIndex;
    }

    private String getTriple(String hash) {
	for (int i = 0; i < hash.length() - 2; i++) {
	    if (hash.charAt(i) == hash.charAt(i + 1) && hash.charAt(i) == hash.charAt(i + 2)) {
		return hash.charAt(i) + "" + hash.charAt(i) + hash.charAt(i);
	    }
	}

	return null;
    }

    private String getQuintuple(String hash) {
	for (int i = 0; i < hash.length() - 4; i++) {
	    if (hash.charAt(i) == hash.charAt(i + 1) && hash.charAt(i) == hash.charAt(i + 2)
		    && hash.charAt(i) == hash.charAt(i + 3) && hash.charAt(i) == hash.charAt(i + 4)) {
		return hash.charAt(i) + "" + hash.charAt(i) + hash.charAt(i) + hash.charAt(i) + hash.charAt(i);
	    }
	}
	return null;
    }

    private String md5(String toHash) {
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    toHash = toHexString(md.digest(toHash.getBytes()));
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}

	return toHash;
    }

    private String stretchedMd5(String toHash) {
	for (int i = 0; i < 2017; i++) {
	    toHash = md5(toHash);
	}
	return toHash;
    }

    public String toHexString(byte[] messageDigest) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < messageDigest.length; i++) {
	    if ((0xff & messageDigest[i]) < 0x10) {
		sb.append('0');
	    }
	    sb.append(Integer.toHexString(0xff & messageDigest[i]));
	}
	return sb.toString();
    }

    public static void main(String[] args) {
	Day14 test = new Day14();
	System.out.println(test.run(true));

    }

}
