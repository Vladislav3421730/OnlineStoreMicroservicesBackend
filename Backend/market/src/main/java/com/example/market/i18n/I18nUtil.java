package com.example.market.i18n;

import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class I18nUtil {

    MessageSource messageSource;

    @Resource(name = "localeHolder")
    LocalHolder localHolder;

    public String getMessage(String code, String... args){
        return messageSource.getMessage(code, args, localHolder.getCurrentLocale());
    }

    public void setLocale(Locale locale) {
        localHolder.setCurrentLocale(locale);
    }

}