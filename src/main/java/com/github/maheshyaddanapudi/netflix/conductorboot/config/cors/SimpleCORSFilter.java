package com.github.maheshyaddanapudi.netflix.conductorboot.config.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;

@Component
@Profile(Constants.EXTERNAL_ADFS)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if(Constants.OPTIONS.equalsIgnoreCase(request.getMethod()))
        {
            if(null==response.getHeaders("Access-Control-Allow-Origin") || response.getHeaders("Access-Control-Allow-Origin").isEmpty())
                response.setHeader("Access-Control-Allow-Origin", "*");
            if(null==response.getHeaders("Access-Control-Allow-Methods") || response.getHeaders("Access-Control-Allow-Methods").isEmpty())
                response.setHeader("Access-Control-Allow-Methods", "GET,POST,PATCH,PUT,DELETE,OPTIONS,HEAD");
            if(null==response.getHeaders("Access-Control-Allow-Headers") || response.getHeaders("Access-Control-Allow-Headers").isEmpty())
                response.setHeader("Access-Control-Allow-Headers", "*");

            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
            chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
