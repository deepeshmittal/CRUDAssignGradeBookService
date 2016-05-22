package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import jaxb.xml.Student;
import jaxb.xml.Students;
import jaxb.xml.Workitem;
import jaxb.xml.Converter;


@Path("/gradebook")
public class GradeBookService {
	
	ArrayList<Student> studentlist;
	Students studentroot;
	
	@Context
    private UriInfo context;
	
	public GradeBookService() throws IOException, JAXBException{
		super();
		readMainXml();
	}
	
	@POST
    @Path("/addstudent")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addStudent(String content) throws IOException {
		Student new_student = new Student();
		Response response = null;
		Integer studentid = null;
		boolean success = false;
		try{
			new_student = (Student) Converter.convertFromXmlToObject(content, Student.class);
			
			
			boolean found = false;
			while(!found){
				Random r = new Random();
				studentid = r.nextInt((9999 - 1000) + 1) + 1000;
				int temp = 0;
				for(Student student : studentlist){
					if(student.getId() == studentid){
						break;
					}
					temp++;
				}
				if (temp==studentlist.size()){
					found = true;
				}
			}
			
			Student curr_student = studentlist.get(0);
			new_student.setWorkitemlist(curr_student.getWorkitemlist());
			new_student.setId(studentid);
			content = Converter.convertFromObjectToXml(new_student, Student.class);
			studentlist.add(new_student);
			writeMainXml();
			success = true;
		}catch(JAXBException e){
			response = Response.status(Response.Status.BAD_REQUEST).build();
		}catch(IOException e){
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}catch(Exception e){
			response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		if(success){
			URI locationURI = URI.create(context.getAbsolutePath() + "/" + Integer.toString(studentid));
			response = Response.status(201).location(locationURI).entity(content).build();
		}

		return response;
	}
	
	@POST
    @Path("/addworkitem")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addWorkitem(String content) throws IOException {
		Workitem new_workitem = new Workitem();
		ArrayList<Workitem> itemlist = new ArrayList<Workitem>();
		Response response = null;
		boolean success = false;
		try{
			new_workitem = (Workitem) Converter.convertFromXmlToObject(content, Workitem.class);
			for(Student student : studentlist){
				itemlist = student.getWorkitemlist();
				itemlist.add(new_workitem);
			}
			writeMainXml();
			success = true;
		}catch(JAXBException e){
			response = Response.status(Response.Status.BAD_REQUEST).build();
		}catch(IOException e){
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}catch(Exception e){
			response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		if(success){
			response = Response.status(201).entity(content).build();
		}

		return response;
	}
	
	@PUT
    @Path("/addgrade/{id}/{itemname}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateGrade(@PathParam("id") String id, @PathParam("itemname") String itemname, String content) throws IOException {
		Workitem request_item = new Workitem();
		Response response;
		boolean studentfound = false;
		boolean itemfound = false;
		try{
			request_item = (Workitem) Converter.convertFromXmlToObject(content, Workitem.class);
			outerloop:
			for(Student student : studentlist){
				if(student.getId() == Integer.parseInt(id)){
					studentfound = true;
					ArrayList<Workitem> itemlist = student.getWorkitemlist();
					for(Workitem item : itemlist){
						if(item.getItemname().equals(itemname)){
							itemfound = true;
							item.setFeedback(request_item.getFeedback());
							item.setGrades(request_item.getGrades());
							break outerloop;
						}
					}
					
				}
			}
			writeMainXml();
		}catch(JAXBException e){
			response = Response.status(Response.Status.BAD_REQUEST).build();
		}catch(IOException e){
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}catch(Exception e){
			response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		if(!studentfound){
			response = Response.status(404).entity(content).build();
		}else if(!itemfound){
			response = Response.status(409).entity(content).build();
		}else{
			response = Response.status(200).entity(content).build();
		}

		return response;
	}
	
	@PUT
    @Path("/updategrade/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateGrade(@PathParam("id") String id, String content) throws IOException {
		Workitem request_item = new Workitem();
		Response response;
		boolean studentfound = false;
		boolean itemfound = false;
		try{
			request_item = (Workitem) Converter.convertFromXmlToObject(content, Workitem.class);
			outerloop:
			for(Student student : studentlist){
				if(student.getId() == Integer.parseInt(id)){
					studentfound = true;
					ArrayList<Workitem> itemlist = student.getWorkitemlist();
					for(Workitem item : itemlist){
						if(item.getItemname().equals(request_item.getItemname())){
							itemfound = true;
							item.setFeedback(request_item.getFeedback());
							item.setGrades(request_item.getGrades());
							break outerloop;
						}
					}
					
				}
			}
			writeMainXml();
		}catch(JAXBException e){
			response = Response.status(Response.Status.BAD_REQUEST).build();
		}catch(IOException e){
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}catch(Exception e){
			response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		if(!studentfound){
			response = Response.status(404).entity(content).build();
		}else if(!itemfound){
			response = Response.status(409).entity(content).build();
		}else{
			response = Response.status(200).entity(content).build();
		}

		return response;
	}
	
	
	@GET
    @Path("/viewgrade/{id}/{itemname}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response viewGrade(@PathParam("id") String id, @PathParam("itemname") String itemname) throws IOException {
		Workitem request_item = new Workitem();
		Student request_student = new Student();
		Response response;
		boolean studentfound = false;
		boolean itemfound = false;
		String response_xml = new String();
		try{
			outerloop:
			for(Student student : studentlist){
				if(student.getId() == Integer.parseInt(id)){
					studentfound = true;
					ArrayList<Workitem> itemlist = student.getWorkitemlist();
					if(itemname.equalsIgnoreCase("NoAssign")){
						itemfound = true;
						request_student.setWorkitemlist(itemlist);
						break outerloop;
					}
					for(Workitem item : itemlist){
						if(item.getItemname().equals(itemname)){
							itemfound = true;
							request_item.setFeedback(item.getFeedback());
							request_item.setGrades(item.getGrades());
							break outerloop;
						}
					}
					
				}
			}
		if(itemname.equalsIgnoreCase("NoAssign")){
			response_xml = Converter.convertFromObjectToXml(request_student, Student.class);
		}else
		response_xml = Converter.convertFromObjectToXml(request_item, Workitem.class);
		}catch(Exception e){
			response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		if(!studentfound){
			response = Response.status(404).build();
		}else if(!itemfound){
			response = Response.status(409).build();
		}else{
			response = Response.status(200).entity(response_xml).build();
		}

		return response;
	}
	
	@DELETE
    @Path("/deletegrade/{id}/{itemname}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteGrade(@PathParam("id") String id, @PathParam("itemname") String itemname) throws IOException {
		Student curr_student = studentlist.get(0);
		Response response;
		boolean studentfound = false;
		boolean itemfound = false;
		try{
			outerloop:
			for(Student student : studentlist){
				if(student.getId() == Integer.parseInt(id)){
					studentfound = true;
					if(itemname.equalsIgnoreCase("NoAssign")){
						itemfound = true;
						student.setWorkitemlist(curr_student.getWorkitemlist());;
						break outerloop;
					}
					ArrayList<Workitem> itemlist = student.getWorkitemlist();
					for(Workitem item : itemlist){
						if(item.getItemname().equals(itemname)){
							itemfound = true;
							item.setGrades("");
							item.setFeedback("");
							break outerloop;
						}
					}
					
				}
			}
		writeMainXml();
		}catch(Exception e){
			response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		if(!studentfound){
			response = Response.status(404).build();
		}else if(!itemfound){
			response = Response.status(409).build();
		}else{
			response = Response.status(204).build();
		}

		return response;
	}
	
	public void readMainXml() throws IOException, JAXBException{
		BufferedReader br = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			File file = new File(classLoader.getResource("Student.xml").getPath());
			br = new BufferedReader(new FileReader(file));
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        studentroot = (Students) Converter.convertFromXmlToObject(sb.toString(), Students.class);
	        studentlist = studentroot.getStudentlist();
	        return ;
	    } finally {
	        br.close();
	    }
	}
	
	public void writeMainXml() throws IOException, JAXBException{
		BufferedWriter bw = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			File file = new File(classLoader.getResource("Student.xml").getPath());
			bw = new BufferedWriter(new FileWriter(file));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Students student = new Students();
		student.setStudentlist(studentlist);
		String xmloutput = Converter.convertFromObjectToXml(student, Students.class);
		
		try {
	        bw.write(xmloutput);
			return ;
	    } finally {
	        bw.close();
	    }
	}
}
