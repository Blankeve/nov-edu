package com.novedu.nov.common.config;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * @author ：juam
 * @date ：2021/12/10 10:46
 * @description：
 * @modified By：
 * @version:
 */
@Component
public class NovString2DateTimeConverter implements Converter<String, Date> {

    @SneakyThrows
    @Override
    public Date convert(String source) {
        if (source.length() == 0) {
            return null;
        }
        if (source.length() == 10) {
            return DateUtils.parseDate(source, "yyyy-MM-dd");
        } else if (source.length() == 19) {
            return DateUtils.parseDate(source, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }
}
