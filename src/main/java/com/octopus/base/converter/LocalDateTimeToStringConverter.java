package com.octopus.base.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class LocalDateTimeToStringConverter implements AttributeConverter<LocalDateTime, String> {
    /**
     * LocalDateTime 값을 String 으로 컨버트
     *
     * @param attribute boolean 값
     * @return String true 인 경우 Y 또는 false 인 경우 N
     */
    @Override
    public String convertToDatabaseColumn(LocalDateTime date) {
        log.debug("LocalDateTime :: {}", date);
        return (date != null) ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "";
    }
    
    /**
     * String 을 LocalDateTime 으로 컨버트
     *
     * @param yn Y 또는 N
     * @return Boolean 대소문자 상관없이 Y 인 경우 true, N 인 경우 false
     */
    @Override
    public LocalDateTime convertToEntityAttribute(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime     dateTime  = null;
        
        log.debug("value :: {}", value);
        
        try {
            value = value.replaceAll("-", "").replaceAll("/", "").replaceAll(":", "")
                    .replaceAll(".", "");
            
            dateTime = LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
        
        return dateTime;
    }
}