package students;

import javax.servlet.http.Cookie;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.ServletUtils;

public class WelcomePage extends HttpServlet{
	private static String file = null;
	public static String sessionId = null;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		file = config.getInitParameter("student_list");
		if (file == null || file.length() == 0) {
			throw new ServletException();
		}
		System.out.println("Loaded init param student_info with " + file);
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		   	response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		    out.println("<HTML><HEAD><TITLE> Welcome</TITLE></HEAD><BODY>");
		    sessionId = ServletUtils.processCookie(request,"sessionid");
		    if (sessionId == null) {
		      sessionId = ServletUtils.generateUniqueId();
		      newSession(request, response, out);
		    } else if(ServletUtils.getSessionInformation(sessionId)==null){
		    	newSession(request, response, out);
		    } else{
		    	StudentInfo st = ServletUtils.getSessionInformation(sessionId);
		    	out.println("<h1>Welcome "+st.getFirstName()+" "+st.getLastName()+"!</h1>");
		    }
	}

	private void newSession(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		  Cookie c = new Cookie("sessionid", sessionId);
		  String id =  ServletUtils.processCookie(request,"id");
		  id = ((id == null) ? "" : id); 
		  response.addCookie(c);
		  out.println("<h1>Welcome New Student</h1>");
		  out.println("<form name=\"welcomeForm\" method=\"post\" action=\"welcome\">");
		  out.println("<label>ID: </label>");
		  out.println("<input type=\"text\" name=\"id\" value=\""+id+"\"><br><br>");
		  out.println("<a href=\"javascript:document.welcomeForm.submit();\">Next</a>");
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String fName = ServletUtils.processCookie(request,"fName");
		String lName = ServletUtils.processCookie(request,"lName");
		fName = ((fName == null) ? "" : fName); 
		lName = ((lName == null) ? "" : lName); 
		out.println("<HTML><HEAD><TITLE> Information Page</TITLE></HEAD><BODY>");
		out.println("<h1>Enter Your Name</h1>");
		out.println("<form name=\"nameForm\" action=\"name\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+request.getParameter("id")+"\">");
		out.println("<label>First Name:</label><input type=\"text\" name=\"fName\" value=\""+fName+"\"><br>"
				+ "<label>Last Name: </label><input type=\"text\" name=\"lName\" value=\""+lName+"\"><br>");
		out.println("<a href=\"welcome\">Previous</a><br>"
				+ "<a href=\"javascript:document.nameForm.submit();\">Next</a>");
		out.println("</FORM></BODY></HTML>");
		Cookie cookie1 = new Cookie("id", request.getParameter("id"));
		response.addCookie(cookie1);
	}
}
