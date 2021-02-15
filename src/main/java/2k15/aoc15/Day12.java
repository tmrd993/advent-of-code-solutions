package aoc15;

import java.io.File;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import myutils15.StaticUtils;

public class Day12 {

    private final String rawInput;
    private final Pattern NUMBER = Pattern.compile("(-?\\d+)");

    public Day12(File input) {
	rawInput = StaticUtils.fileToStringList(input).stream().collect(Collectors.joining());
    }

    public int run1() {
	Matcher matcher = NUMBER.matcher(rawInput);
	int sum = 0;
	while (matcher.find()) {
	    sum += Integer.parseInt(matcher.group());
	}
	return sum;
    }

    public int run2() {
	JsonReader reader = Json.createReader(new StringReader(rawInput));
	JsonObject rootObject = reader.readObject();
	reader.close();

	Queue<JsonStructure> queue = new LinkedList<>();
	queue.add(rootObject);

	int sum = 0;
	while (!queue.isEmpty()) {
	    if (queue.peek() instanceof JsonObject) {
		JsonObject currentObject = (JsonObject) queue.poll();
		if (hasRedAttribute(currentObject)) {
		    continue;
		}
		
		for (Entry<String, JsonValue> entry : currentObject.entrySet()) {
		    if (entry.getValue().getValueType() == ValueType.NUMBER) {
			sum += Integer.parseInt(entry.getValue().toString());
		    } else if (entry.getValue().getValueType() == ValueType.OBJECT) {
			queue.add(entry.getValue().asJsonObject());
		    } else if (entry.getValue().getValueType() == ValueType.ARRAY) {
			queue.add(entry.getValue().asJsonArray());
		    }
		}
	    }

	    else {
		JsonArray currentArray = (JsonArray) queue.poll();
		for(JsonValue value : currentArray) {
		    if (value.getValueType() == ValueType.NUMBER) {
			sum += Integer.parseInt(value.toString());
		    } else if (value.getValueType() == ValueType.OBJECT) {
			queue.add(value.asJsonObject());
		    } else if (value.getValueType() == ValueType.ARRAY) {
			queue.add(value.asJsonArray());
		    }
		}
	    }
	}

	return sum;
    }

    private boolean hasRedAttribute(JsonObject object) {
	for (Entry<String, JsonValue> entry : object.entrySet()) {
	    if (entry.getValue().getValueType() == ValueType.STRING && "\"red\"".equals(entry.getValue().toString())) {
		return true;
	    }
	}

	return false;
    }

    public static void main(String[] args) {
	Day12 test = new Day12(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 12\\InputFile1.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());

    }

}
