package jaxb.xml;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Students {
	ArrayList<Student> studentlist;

	public ArrayList<Student> getStudentlist() {
		return studentlist;
	}
	
	@XmlElement(name = "student")
	public void setStudentlist(ArrayList<Student> studentlist) {
		this.studentlist = studentlist;
	}

}
