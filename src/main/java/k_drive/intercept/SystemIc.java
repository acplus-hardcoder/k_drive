package k_drive.intercept;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SystemIc implements HandlerInterceptor {

	@Override	//특정 범위의 패키지에 있는 메서드가 실행되기 전에 실행될 공통 메서드
	public boolean preHandle(HttpServletRequest rq, HttpServletResponse rs, Object h)
			throws Exception {
		HttpSession s = rq.getSession();		
		String user = (String)s.getAttribute("login_user");
		System.out.println(user);
		if(!Objects.isNull(user) && user != "")
		{
			System.out.println("Login Pass");
			rq.removeAttribute("message");
			return true;
		}
		else
		{			
			rs.sendRedirect("/k_drive/login");
		}
		//true이면 핵심메서드가 실행, false면 핵심메서드가 실행x
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
