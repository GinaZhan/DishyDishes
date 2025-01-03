package com.example.backend.Components;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter implements Filter{

    @Override
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain) throws IOException,ServletException{
        HttpServletRequest request=(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) res;

        String header=request.getHeader("Authorization");
        if(header==null||!header.startsWith("Bearer ")){
            HttpServletRequest httpRequest=(HttpServletRequest) req;
            httpRequest.setAttribute("uid","");
            chain.doFilter(request,response);
            return;
        }

        String token=header.substring(7);
        try{
            String uid=JWT.require(Algorithm.HMAC256("DishyDishesJSONWebTokenEncodeAlgorithmSecret"))
                    .withIssuer("DishyDishes")
                    .build()
                    .verify(token)
                    .getSubject();
            request.setAttribute("uid",uid);
            chain.doFilter(request,response);
        }catch(Exception e){
            request.setAttribute("uid","");
            chain.doFilter(request,response);
        }
        return;
    }
}
