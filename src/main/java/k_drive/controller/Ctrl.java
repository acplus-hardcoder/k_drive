package k_drive.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import k_drive.maps.Mappable;
import net.sf.json.JSONArray;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Ctrl {
	@Autowired
	Connection con;

	@Autowired
	Mappable km;	

	@Autowired
	SqlSessionFactory sqlsession;
	
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String homePage(ModelMap m, HttpServletRequest rq) {
		HttpSession s = rq.getSession();
		m.addAttribute("user_num", s.getAttribute("login_user_num"));
		m.addAttribute("user_name", s.getAttribute("login_user"));
		return "home";
	}
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String loginPage(ModelMap m, HttpServletRequest rq) {
		HttpSession s = rq.getSession();
		m.addAttribute("user_num", s.getAttribute("login_user_num"));
		m.addAttribute("user_name", s.getAttribute("login_user"));
		return "login";
	}
	
	@RequestMapping(value = { "/member_join" }, method = RequestMethod.GET)
	public String memberPage(ModelMap m, HttpServletRequest rq) {
		HttpSession s = rq.getSession();
		m.addAttribute("user_num", s.getAttribute("login_user_num"));
		m.addAttribute("user_name", s.getAttribute("login_user"));
		System.out.println("member_join");
		return "member_join";
	}
	
	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	public String logout(ModelMap m, HttpServletRequest rq) {
		HttpSession s = rq.getSession();				
		s.setAttribute("login_user", "");				
		s.setAttribute("login_user_num", "");
		m.addAttribute("user_num", "");
		m.addAttribute("user_name", "");
		return "home";
	}
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public String checkLogin(@RequestParam String id, @RequestParam String pwd,
			HttpServletRequest rq, HttpServletResponse rs, ModelMap m)	
	{
		List<HashMap<String, String>> user = km.idCheck(id);
		System.out.println(user);
		m.addAttribute("user_num", "");
		m.addAttribute("user_name", "");
		if(user.isEmpty())
		{			
			rq.setAttribute("message", "아이디가 없습니다.");
			return "login";
		}
		else 
		{
			if(user.get(0).get("M_PWD").equals(pwd))
			{
				HttpSession s = rq.getSession();				
				s.setAttribute("login_user", user.get(0).get("M_USERID"));				
				s.setAttribute("login_user_num", user.get(0).get("M_NUM"));				
				return "redirect:web_hard";				
			}
			else
			{
				rq.setAttribute("message", "패스워드가 다릅니다.");				
				return "login";
			}
		}
	}

	@RequestMapping(value = { "/web_hard" }, method = RequestMethod.GET)
	public String k_drivePage(ModelMap m, HttpServletRequest rq) {
		HttpSession s = rq.getSession();		
		String user_num = s.getAttribute("login_user_num").toString();
		String user_name = s.getAttribute("login_user").toString();
		System.out.println(user_num);
		JSONArray mapResult = JSONArray.fromObject(km.getKdriveList(Integer.parseInt(user_num)));		
		m.addAttribute("klist", mapResult);
		m.addAttribute("user_num", user_num);
		m.addAttribute("user_name", user_name);
		System.out.println(user_num + ":" + user_name);
		return "web_hard";
	}
	
	@RequestMapping(value = { "/down" }, method = RequestMethod.GET)
	public void down(HttpServletRequest rq, HttpServletResponse rs, String filename, String dbname, int table)
	{
		String uppath = String.format("C:\\sts-bundle\\ws\\k_drive\\src\\main\\webapp\\static\\upfolder_%s\\", table);
		// 파일열기
		String path = uppath + dbname; //full경로
	    String fileName = filename; //파일명
	    String fileNameEncoding = null;
	    try {
			fileNameEncoding = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 
	    File file = new File(path);
	 
	    FileInputStream fileInputStream = null;
	    ServletOutputStream servletOutputStream = null;
	 
	    try {
	    	rs.setContentType("application/x-download");
	        rs.setHeader("Content-Disposition","attachment;filename=\"" + fileNameEncoding + "\"");	        
	        rs.setHeader("Content-Transfer-Encoding", "binary;");
	 
	        fileInputStream = new FileInputStream(file);
	        servletOutputStream = rs.getOutputStream();
	 
	        byte b [] = new byte[1024];
	        int data = 0;
	 
	        while((data=(fileInputStream.read(b, 0, b.length))) != -1) {	             
	            servletOutputStream.write(b, 0, data);	             
	        }	 
	        servletOutputStream.flush();	         
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if(servletOutputStream!=null) {
	            try {
	                servletOutputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        if(fileInputStream!=null) {
	            try {
	                fileInputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
}