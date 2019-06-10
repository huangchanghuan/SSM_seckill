package org.seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVesrsionCondition implements RequestCondition<ApiVesrsionCondition> {
    private static final Logger logger = LoggerFactory.getLogger(ApiVesrsionCondition.class);
    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");

    private int apiVersion;

    public ApiVesrsionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVesrsionCondition combine(ApiVesrsionCondition other) {
        logger.debug("====combine");
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVesrsionCondition(other.getApiVersion());
    }

    @Override
    public ApiVesrsionCondition getMatchingCondition(HttpServletRequest request) {
        logger.debug("====getMatchingCondition");
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURL());
        if(m.find()){
            //路径的版本号
            Integer version = Integer.valueOf(m.group(1));
            // 如果请求的版本号大于配置版本号， 则满足
            if(version >= this.apiVersion){
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVesrsionCondition other, HttpServletRequest request) {
        logger.debug("====compareTo");
        // 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
    }

    public int getApiVersion() {
        logger.debug("====getApiVersion");
        return apiVersion;
    }

    public static void main(String[] args) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher("http://localhost:8080/appserver/v2/time/now");
        if(m.find()){
            //路径的版本号
            Integer version = Integer.valueOf(m.group(1));
            logger.debug(version.toString());
        }else {
            logger.debug("no");
        }

    }
}
