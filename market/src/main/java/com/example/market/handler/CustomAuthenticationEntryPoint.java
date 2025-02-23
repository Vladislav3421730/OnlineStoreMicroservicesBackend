package com.example.market.handler;

import com.example.market.dto.AppErrorDto;
import com.example.market.i18n.I18nUtil;
import com.example.market.util.Messages;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final I18nUtil i18nUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String acceptLanguageHeader = request.getHeader("Accept-Language");
        Locale locale = Locale.forLanguageTag(acceptLanguageHeader != null ? acceptLanguageHeader : "en");

        i18nUtil.setLocale(locale);

        response.getWriter().write(new ObjectMapper()
                .writeValueAsString(new AppErrorDto(i18nUtil.getMessage(Messages.USER_ERROR_UNAUTHENTICATED), 401)));
    }
}
