package jaxb.xml;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student {
	int id;
	String name;
	ArrayList<Workitem> workitemlist;

	public int getId() {
		return id;
	}
	
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	@XmlElement(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Workitem> getWorkitemlist() {
		return workitemlist;
	}

	@XmlElement(name = "workitem")
	public void setWorkitemlist(ArrayList<Workitem> workitemlist) {
		this.workitemlist = workitemlist;
	}
}
