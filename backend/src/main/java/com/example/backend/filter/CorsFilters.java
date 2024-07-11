package com.example.backend.filter;

import com.example.backend.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 跨域问题
 */
@Component
@Order(Const.ORDER_CORS)
public class CorsFilters extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        this.addCorsHeader(request,response);
        chain.doFilter(request,response);
    }

    private void addCorsHeader(HttpServletRequest request,HttpServletResponse response){
        //允许访问的站点，这里是原始请求的站点
        response.addHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,POTIONS");
        response.addHeader("Access-Control-Allow-Methods","Authorization,Content-Type");
    }
}
