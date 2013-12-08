package finData;

public class AttrNode {
	private String name;
	private String value;
	
	public AttrNode(String name, String value) {
		setName(name);
        setValue(value);
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "Name:" + name + ", Value:" + value;
    }
}