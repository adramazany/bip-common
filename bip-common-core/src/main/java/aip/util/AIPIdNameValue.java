package aip.util;

public class AIPIdNameValue {
	Integer id;
	String name;
	String value;
	
	
	
	public AIPIdNameValue(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public AIPIdNameValue(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public AIPIdNameValue(Integer id, String name, String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
