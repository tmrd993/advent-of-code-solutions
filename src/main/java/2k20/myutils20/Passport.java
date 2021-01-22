package myutils20;

import java.util.regex.Pattern;

// object representing a passport for day 4. fields are all optional (may be 0 or null), validity checks are done by the client
public class Passport {

    private int byr;
    private int iyr;
    private int eyr;
    private String hgt;
    private String hcl;
    private String ecl;
    private String pid;
    private String cid;
    // bitfield representing the fields that currently have a valid value
    private int presentDataBitfield;

    private Passport() {
    }

    public int getByr() {
	return byr;
    }

    public int getIyr() {
	return iyr;
    }

    public int getEyr() {
	return eyr;
    }

    public String getHgt() {
	return hgt;
    }

    public String getHcl() {
	return hcl;
    }

    public String getEcl() {
	return ecl;
    }

    public String getPid() {
	return pid;
    }

    public String getCid() {
	return cid;
    }

    public int getPresentDataBitfield() {
	return presentDataBitfield;
    }

    @Override
    public String toString() {
	return "BYR: " + byr + "\nIYR: " + iyr + "\nEYR: " + eyr + "\nHGT: " + hgt + "\nHCL: " + hcl + "\nECL: " + ecl
		+ "\nPID: " + pid + "\nCID : " + cid + "\nPresent Data: " + Integer.toBinaryString(presentDataBitfield)
		+ "\n";
    }

    public boolean hasAllFieldsPresent() {
	return byr != 0 && iyr != 0 && eyr != 0 && hgt != null && hcl != null && ecl != null && pid != null
		&& cid != null;
    }

    // removes invalid fields (set to null or 0) according to rules of day 4, part 2
    public void cleanup() {
	if (hasAllFieldsPresent() || presentDataBitfield == 0b1111111) {
	    if (byr < 1920 || byr > 2002) {
		byr = 0;
		presentDataBitfield = presentDataBitfield & ~(1 << 0);
	    }

	    if (iyr < 2000 || iyr > 2020) {
		iyr = 0;
		presentDataBitfield = presentDataBitfield & ~(1 << 1);
	    }

	    if (eyr < 2020 || eyr > 2030) {
		eyr = 0;
		presentDataBitfield = presentDataBitfield & ~(1 << 2);
	    }

	    if (!hgt.contains("cm") && !hgt.contains("in")) {
		hgt = null;
		presentDataBitfield = presentDataBitfield & ~(1 << 3);
	    } else {
		int hgtNum = Integer.parseInt(StaticUtils.extractLeftSideDigits(hgt));
		if (hgt.contains("cm") && (hgtNum < 150 || hgtNum > 193)) {
		    hgt = null;
		    presentDataBitfield = presentDataBitfield & ~(1 << 3);
		} else if (hgt.contains("in") && (hgtNum < 59 || hgtNum > 76)) {
		    hgt = null;
		    presentDataBitfield = presentDataBitfield & ~(1 << 3);
		}
	    }

	    Pattern hclPattern = Pattern.compile("#[0-9|a-f]{6}");
	    if (!hclPattern.matcher(hcl).matches()) {
		hcl = null;
		presentDataBitfield = presentDataBitfield & ~(1 << 4);
	    }

	    if (!ecl.equals("amb") && !ecl.equals("blu") && !ecl.equals("brn") && !ecl.equals("gry")
		    && !ecl.equals("grn") && !ecl.equals("hzl") && !ecl.equals("oth")) {
		ecl = null;
		presentDataBitfield = presentDataBitfield & ~(1 << 5);
	    }
	    
	    Pattern pidPattern = Pattern.compile("[0-9]{9}");
	    if(!pidPattern.matcher(pid).matches()) {
		pid = null;
		presentDataBitfield = presentDataBitfield & ~(1 << 6);
	    }
	}
    }

    public static class Builder {
	private int byr;
	private int iyr;
	private int eyr;
	private String hgt;
	private String hcl;
	private String ecl;
	private String pid;
	private String cid;
	private int presentDataBitfield;

	public Builder setBirthYear(int byr) {
	    presentDataBitfield |= 1;
	    this.byr = byr;

	    return this;
	}

	public Builder setIssueYear(int iyr) {
	    presentDataBitfield = presentDataBitfield | 1 << 1;
	    this.iyr = iyr;

	    return this;
	}

	public Builder setExpirationYear(int eyr) {
	    presentDataBitfield = presentDataBitfield | 1 << 2;
	    this.eyr = eyr;

	    return this;
	}

	public Builder setHeight(String hgt) {
	    presentDataBitfield = presentDataBitfield | 1 << 3;
	    this.hgt = hgt;

	    return this;
	}

	public Builder setHairColor(String hcl) {
	    presentDataBitfield = presentDataBitfield | 1 << 4;
	    this.hcl = hcl;

	    return this;
	}

	public Builder setEyeColor(String ecl) {
	    presentDataBitfield = presentDataBitfield | 1 << 5;
	    this.ecl = ecl;

	    return this;
	}

	public Builder setPassportId(String pid) {
	    presentDataBitfield = presentDataBitfield | 1 << 6;
	    this.pid = pid;

	    return this;
	}

	public Builder setCountryId(String cid) {
	    presentDataBitfield = presentDataBitfield | 1 << 7;
	    this.cid = cid;

	    return this;
	}

	public Builder setField(String fieldId, String value) {
	    if (fieldId.equals("byr")) {
		return setBirthYear(Integer.parseInt(value));
	    } else if (fieldId.equals("iyr")) {
		return setIssueYear(Integer.parseInt(value));
	    } else if (fieldId.equals("eyr")) {
		return setExpirationYear(Integer.parseInt(value));
	    } else if (fieldId.equals("hcl")) {
		return setHairColor(value);
	    } else if (fieldId.equals("ecl")) {
		return setEyeColor(value);
	    } else if (fieldId.equals("hgt")) {
		return setHeight(value);
	    } else if (fieldId.equals("pid")) {
		return setPassportId(value);
	    } else if (fieldId.equals("cid")) {
		return setCountryId(value);
	    }

	    throw new IllegalArgumentException("Illegal field or value: " + fieldId + ", " + value);
	}

	public Passport build() {
	    Passport passport = new Passport();

	    passport.byr = this.byr;
	    passport.iyr = this.iyr;
	    passport.eyr = this.eyr;
	    passport.hgt = this.hgt;
	    passport.hcl = this.hcl;
	    passport.ecl = this.ecl;
	    passport.pid = this.pid;
	    passport.cid = this.cid;
	    passport.presentDataBitfield = this.presentDataBitfield;

	    return passport;
	}
    }
}
