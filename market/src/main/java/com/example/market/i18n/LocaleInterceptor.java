package com.example.market.i18n;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    @Resource(name = "localeHolder")
    LocalHolder localHolder;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver instanceof AcceptHeaderLocaleResolver headerLocaleResolver) {
            localHolder.setCurrentLocale(headerLocaleResolver.resolveLocale(request));
        } else {
            throw new IllegalStateException("Resolver should be of AcceptHeaderLocaleResolver type");
        }

        return true;
    }
}