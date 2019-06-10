package org.seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(CustomRequestMappingHandlerMapping.class);
    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
    @Override
     protected HandlerMethod handleNoMatch(Set<RequestMappingInfo> requestMappingInfos,
                                           String lookupPath, HttpServletRequest request) throws ServletException {
        //没有匹配到路径,则进行版本降级,继续匹配; 如果已经是最低版本,则执行super.handleNoMatch(requestMappingInfos, lookupPath, request)
        //不存在版本号
        log.info("接口版本降级处理");
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURL());
        //存在版本号
        if(m.find()){
            //路径的版本号
            Integer version = Integer.valueOf(m.group(1));
            log.info("存在版本号:{}",version);
            //已是最小版本,完成所有版本降级匹配,未找到
            if(version == 1) {
                return customerHandleNoMatch(requestMappingInfos, lookupPath, request);
            }else {
                //不是最小版本,重组lookupPath
                lookupPath = lookupPath.replace(version.toString(), String.valueOf(version-1));
                log.info("降级后的lookupPath:{}",lookupPath);
                try {
                    return lookupHandlerMethod(lookupPath, request);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("接口版本降级异常:{}",e.getMessage());
                }
            }
        }else {
            //不存在版本号,获取最新版本
            log.info("不存在版本号");
        }
        return null;
     }

    protected HandlerMethod customerHandleNoMatch(Set<RequestMappingInfo> requestMappingInfos,
                                          String lookupPath, HttpServletRequest request) throws ServletException {
        return super.handleNoMatch(requestMappingInfos, lookupPath, request);
    }

}
