package myutils20;

import java.util.ArrayList;
import java.util.List;

public class CustomsGroup {
    
    private List<CustomsPerson> answers;
    
    public CustomsGroup() {
	answers = new ArrayList<>();
    }
    
    public void addPerson(CustomsPerson person) {
	answers.add(person);
    }
    
    public List<CustomsPerson> getPeople() {
	return answers;
    }

}
