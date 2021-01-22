package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils20.Passport;
import myutils20.StaticUtils;

public class Day4 {
    
    private List<String> rawData;
    
    public Day4(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }
    
    public int run1() {
	return (int) getPassports().stream().filter(p -> p.hasAllFieldsPresent() || p.getPresentDataBitfield() == 0b1111111).count();
    }
    
    public int run2() {
	List<Passport> passports = getPassports();
	passports.stream().forEach(Passport::cleanup);
	
	return (int) passports.stream().filter(p -> p.hasAllFieldsPresent() || p.getPresentDataBitfield() == 0b1111111).count();
    }
    
    // parses the batch file and returns a list of passports which may or may not be valid
    private List<Passport> getPassports() {
	List<Passport> passports = new ArrayList<>();
	
	Passport.Builder ppb = new Passport.Builder();
	
	for(int i = 0; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    
	    // end of line, new passport comes next
	    if(line.length() == 0) {
		Passport passport = ppb.build();
		passports.add(passport);
		ppb = new Passport.Builder();
	    }
	    // line contains data for current passport
	    else {
		String[] ppData = line.split(" ");
		for(String data : ppData) {
		    ppb.setField(data.substring(0, data.indexOf(':')), data.substring(data.indexOf(':') + 1));
		}
	    }
	}
	passports.add(ppb.build());
	
	return passports;
    }

    public static void main(String[] args) {
	Day4 test = new Day4(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 4\\InputFile1.txt"));
	//test.getPassports().stream().forEach(System.out::println);
	System.out.println(test.run2());
	
	
    }

}
