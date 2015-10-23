import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CourseUMD {

	final static long waitTime = 200;
	final static String yearNum = "201601";

	public static void main(String[] args) {
		ArrayList<String> links = new ArrayList<>();
		ArrayList<CourseUMD> courses = new ArrayList<>();
		processTopicsList(links);
		//System.out.println(links);
		for (String link: links) {
			process(link, courses);
			sleep(waitTime);
		}
		System.out.println("\n\nPROCESSING DONE, WRITING TO FILE\n");
		outputToFile(courses,"test");
	}

	public static void outputToFile(ArrayList<CourseUMD> courses, String filename) {
		try {
			FileWriter f = new FileWriter(new File(filename+".json"));
			String s = "[";
			for (int i=0;i<courses.size();i++) {
				s = s+"\n"+courses.get(i).toString();
				if (i<courses.size()-1)
					s = s+",";
			}
			s+="\n]";
			f.write(s);
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static boolean sleep(long wait){
		try {
			Thread.sleep(wait);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static void processTopicsList(ArrayList<String> links) {
		try {
			Document doc = Jsoup.connect("https://ntst.umd.edu/soc/"+yearNum).get();
			Element content = doc.getElementById("course-prefixes-page");
			Elements linkTags = content.getElementsByTag("a");	
			for (Element linkElement: linkTags) {
				links.add(linkElement.attr("abs:href"));
				System.out.println(linkElement.attr("abs:href"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void process(String url, ArrayList<CourseUMD> courseList) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements courseElements = doc.getElementsByClass("course");
			for (Element e:courseElements) {
				courseList.add(new CourseUMD(e));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	String department;
	String courseNumber;
	String title;
	String minCredits;
	String maxCredits;
	String credits;
	String gradeMethodsString;
	String description;
	String coreString;
	String genEdString;
	String link;
	String numCore;
	String numGenEd;
	ArrayList<String> genEds;
	final static String[] gradeMethods = {"Regular","Pass/Fail","Audit"};
	final static String[] core = {"D","CS","HA","HL","HO","IE","LL","PL","LS","MS","PS","SB","SH"};
	final static String[] genEd = {"FSAW","FSAR","FSMA","FSOC","FSPW","DSHS","DSHU","DSNS","DSNL","DSSP","DVCC","DVUP","SCIS"};
	final static List<String> genEdArrayList = Arrays.asList(genEd);

	public CourseUMD(Element course) {
		courseNumber = course.getElementsByClass("course-id").get(0).text();
		department = courseNumber.substring(0,4);
		title = course.getElementsByClass("course-title").get(0).text();
		gradeMethodsString = course.getElementsByClass("grading-method").get(0).text();
		description = "";
		Elements descriptionElements = course.getElementsByClass("approved-course-text");
		for (Element e:descriptionElements){
			description = description+"\n"+e.text();
		}	
		minCredits = "";
		maxCredits = "";
		if (course.getElementsByClass("course-max-credits").size()>0) {
			minCredits = course.getElementsByClass("course-min-credits").get(0).text();
			maxCredits = course.getElementsByClass("course-max-credits").get(0).text();
			credits = minCredits + " - " + maxCredits;
		}
		else {
			minCredits = course.getElementsByClass("course-min-credits").get(0).text();
			maxCredits = minCredits;
			credits = minCredits;
		}

		descriptionElements = course.getElementsByClass("course-text");
		for (Element e:descriptionElements){
			description = description+"\n"+e.text();
		}
		coreString = "";
		genEdString = "";
		genEds = new ArrayList<>();
		Elements genEdElements = course.getElementsByClass("course-subcategory");
		for (Element e:genEdElements){
			genEdString = genEdString+" "+e.text();
			if (genEdArrayList.contains(e.text()))
				genEds.add(e.text());
		}
		link="https://ntst.umd.edu/soc/search?courseId="+courseNumber+"&sectionId=&termId="+yearNum+"&_openSectionsOnly=on&courseLevelFilter=ALL&instructor=&teachingCenter=ALL&courseStartCompare=&courseStartHour=&courseStartMin=&courseStartAM=&courseEndHour=&courseEndMin=&courseEndAM=&creditCompare=&credits=&_classDay1=on&_classDay2=on&_classDay3=on&_classDay4=on&_classDay5=on";
		//		numCore;
		//		numGenEd;
		System.out.println(toString());
	}

	public String jsString(String s){
		return s.replace("\"","\\\"");
	}
	
	@Override
	public String toString() {
		String s = "{";
		s+= " \""+"department"+		"\":"+"\""+department+"\" ";
		s+=", \""+"courseNumber"+	"\":"+"\""+courseNumber+"\"";
		s+=", \""+"title"+			"\":"+"\""+jsString(title)+"\"";
		s+=", \""+"link"+			"\":"+"\""+link+"\"";
		s+=", \""+"credits"+		"\":"+"\""+credits+"\"";
		s+=", \""+"genEdString"+	"\":"+"\""+genEdString+"\"";	
		s+=", \""+"genEds"+			"\":[";
		for (int i=0;i<genEds.size();i++) {
			s+="\""+genEds.get(i)+"\"";
			if (i<genEds.size()-1)
				s+=",";
		}
		s+="]";
		s+="}";
		return s;
	}

}
