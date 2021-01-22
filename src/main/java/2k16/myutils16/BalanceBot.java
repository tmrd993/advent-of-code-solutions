package myutils16;

public class BalanceBot {
    
    private Integer low;
    private Integer high;
    private final int ID;
    
    public BalanceBot(int id) {
	this.ID = id;
    }
    
    public boolean hasTwoChips() {
	return low != null && high != null;
    }
    
    public void addChip(int val) {
	if(low == null && high == null) {
	    low = val;
	}
	else {
	    if(low > val) {
		high = low;
		low = val;
	    }
	    else {
		high = val;
	    }
	}
    }
    
    public int getLow() {
	return low;
    }
    
    public int getHigh() {
	return high;
    }
    
    public int id() {
	return ID;
    }
    
    public void deleteChips() {
	low = null;
	high = null;
    }
    
    @Override
    public boolean equals(Object o) {
	if(this == o)
	    return true;
	if(o == null)
	    return false;
	if(!(o instanceof BalanceBot))
	    return false;
	
	BalanceBot tmp = (BalanceBot) o;
	
	return this.id() == tmp.id();
    }


}
