package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import myutils19.StaticUtils;

public class Day8 {
    private final int imageWidth = 25;
    private final int imageHeight = 6;
    private final int transparent = 2;
    private final List<Integer> rawImageData;
    
    public Day8(File input) {
	rawImageData = StaticUtils.digitFileToList(input);
    }
    
    public int run1() {
	List<int[][]> layers = getAllLayers();
	int[][] targetLayer = null;
	int zeroCount = Integer.MAX_VALUE;
	for(int[][] layer : layers) {
	    int count = countDigits(layer, 0);
	    if(count < zeroCount) {
		zeroCount = count;
		targetLayer = layer;
	    }
	}

	int oneDigitCount = countDigits(targetLayer, 1);
	int twoDigitCount = countDigits(targetLayer, 2);
	return oneDigitCount * twoDigitCount;
    }
    
    // prints out the decoded layer, can be a bit difficult to read
    // it's easier if you just draw it on a piece of paper after printing it on the console
    public void run2() {
	int[][] decodedLayer = new int[imageHeight][imageWidth];
	List<int[][]> layers = getAllLayers();
	for(int i = 0; i < imageHeight; i++) {
	    for(int j = 0; j < imageWidth; j++) {
		decodedLayer[i][j] = getCorrectColor(layers, i, j);
	    }
	}
	
	for(int[] width : decodedLayer) {
	    System.out.println(Arrays.toString(width));
	}
    }
    
    private int getCorrectColor(List<int[][]> layers, int i, int j) {
	int targetLayer = 0;
	while(layers.get(targetLayer)[i][j] == transparent) {
	    targetLayer++;
	}
	return layers.get(targetLayer)[i][j];
    }
    
    private int countDigits(int[][] layer, int digit) {
	return (int) Arrays.stream(layer).flatMapToInt(x -> Arrays.stream(x)).filter(n -> n == digit).count();
    }
    
    private List<int[][]> getAllLayers() {
	List<int[][]> layers = new ArrayList<>();
	int rawDataLen = imageWidth * imageHeight;
	for(int i = 0; i < rawImageData.size(); i += rawDataLen) {
	    List<Integer> layerList = rawImageData.subList(i, i + rawDataLen);
	    int index = 0;
	    int[][] layer = new int[imageHeight][imageWidth];
	    for(int j = 0; j < imageHeight; j++) {
		for(int k = 0; k < imageWidth; k++) {
		    layer[j][k] = layerList.get(index++);
		}
	    }
	    layers.add(layer);
	}
	return layers;
    }
    
    public static void main(String[] args) {
	Day8 test = new Day8(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 8\\InputFile.txt"));
	test.run2();
    }
    

}
