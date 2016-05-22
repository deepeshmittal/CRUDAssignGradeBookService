package jaxb.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Workitem {
	String itemname;
	String percent;
	String feedback;
	String grades;
	
	public String getItemname() {
		return itemname;
	}
	
	@XmlAttribute
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	
	public String getPercent() {
		return percent;
	}
	@XmlElement(name = "percent")
	public void setPercent(String percent) {
		this.percent = percent;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	@XmlElement
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	public String getGrades() {
		return grades;
	}
	
	@XmlElement
	public void setGrades(String grades) {
		this.grades = grades;
	}
}
