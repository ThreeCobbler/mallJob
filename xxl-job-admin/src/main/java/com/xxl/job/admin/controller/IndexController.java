package com.xxl.job.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xxl.job.admin.controller.annotation.PermessionLimit;
import com.xxl.job.admin.core.model.User;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.service.IUserRedis;
import com.xxl.job.admin.service.IUserService;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {

	@Resource
	private XxlJobService xxlJobService;

	@Autowired
	private IUserService userService;

	@Autowired
	IUserRedis userRedis;

	public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

	@RequestMapping("/")
	public String index(Model model) {

		Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);

		return "index";
	}

    @RequestMapping("/chartInfo")
	@ResponseBody
	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> chartInfo = xxlJobService.chartInfo(startDate, endDate);
        return chartInfo;
    }
	
	@RequestMapping("/toLogin")
	@PermessionLimit(limit=false)
	public String toLogin(Model model, HttpServletRequest request) {
		if (this.ifLogin(request)) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember) throws JsonProcessingException {
		// valid
		if (this.ifLogin(request)) {
			return ReturnT.SUCCESS;
		}

		// param
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
			return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
		}
		boolean ifRem = (StringUtils.isNotBlank(ifRemember) && "on".equals(ifRemember))?true:false;

		User user = userService.findByNameAndPassword(userName, password);
		// do login
//		boolean loginRet = PermissionInterceptor.login(response, userName, password, ifRem);
		if (user == null) {
			return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
		}
		String tokenTmp = DigestUtils.md5Hex(userName + "_" + password);
		tokenTmp = new BigInteger(1, tokenTmp.getBytes()).toString(16);
		CookieUtil.set(response,LOGIN_IDENTITY_KEY,tokenTmp,ifRem);
		userRedis.addToken(tokenTmp,user);
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		if (this.ifLogin(request)) {
			String token = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
			CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
			userRedis.delete(token);
		}
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/help")
	public String help() {

		/*if (!PermissionInterceptor.ifLogin(request)) {
			return "redirect:/toLogin";
		}*/

		return "help";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	public boolean ifLogin(HttpServletRequest request)  {
		String token = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		User user = null;
		try {
			user = userRedis.getUser(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (user == null) {
			return false;
		}
		return true;
	}
	
}
