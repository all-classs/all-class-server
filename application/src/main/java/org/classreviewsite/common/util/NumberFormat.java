package org.classreviewsite.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumberFormat {

    public static Double format(Double number){
        log.info("data: {}", String.format("%.1f", number));
        return Double.valueOf(String.format("%.1f", number));
    }
}
