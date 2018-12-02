package com.maimaicn.goods.configurations.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.password.PasswordUtils;
import com.maimaicn.utils.permission.Subject;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * 登陆拦截器
 *
 * @param
 * @return
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    protected Logger log = LogManager.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        if (!(handler instanceof HandlerMethod)) {
            return true;//如果不是映射到方法直接通过
        }
        HandlerMethod handlerMe = (HandlerMethod) handler;
        if (handlerMe.getMethodAnnotation(AuthLogin.class) != null) {
            Long loginMemberId = getLoginMemberId(request);
            if (loginMemberId == null) {
                outPrint(response, jsonData(request, 1, "用户未登录"));
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    //返回前端字符串
    private static void outPrint(HttpServletResponse response, String str) throws IOException {
        PrintWriter out = response.getWriter();
        out.write(str);
        out.flush();
    }


    protected Long getLoginMemberId(HttpServletRequest request) {
        return Subject.getSubject().getMemberId();
    }

    private String jsonData(HttpServletRequest request, Integer messCode, Object data) {
        try {
            Map<String, Object> map = new HashMap();
            map.put("code", messCode);
            map.put("info", data);
            ObjectMapper om = new ObjectMapper();
            String resultJSON = om.writeValueAsString(map);
            String jsonpCallback = request.getParameter("jsonpCallback");
            return jsonpCallback != null && !"".equals(jsonpCallback) ? jsonpCallback + "(" + resultJSON + ");" : resultJSON;
        } catch (IOException var8) {
            this.log.error("json转换错误", var8);
            return "{\"code\":\"+300+\" , \"info\":\"json转换错误\"}";
        }
    }
}
