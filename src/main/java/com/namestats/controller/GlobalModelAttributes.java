package com.namestats.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("canonicalUrl")
    public String addCanonicalUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }
}
