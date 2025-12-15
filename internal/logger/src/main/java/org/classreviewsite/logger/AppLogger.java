package org.classreviewsite.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 애플리케이션 전역에서 사용할 로거 팩토리
 * Spring에 의존하지 않는 순수 로거 유틸리티
 */
public final class AppLogger {
    
    private AppLogger() {
        throw new AssertionError("Utility class cannot be instantiated");
    }
    
    /**
     * 클래스 기반 로거 생성
     * 
     * @param clazz 로거를 사용할 클래스
     * @return SLF4J Logger 인스턴스
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * 이름 기반 로거 생성
     * 
     * @param name 로거 이름
     * @return SLF4J Logger 인스턴스
     */
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
}
