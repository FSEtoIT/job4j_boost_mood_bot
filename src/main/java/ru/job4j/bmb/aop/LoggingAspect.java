package ru.job4j.bmb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    /**
     * Логгер для вывода информации о вызовах методов.
     */
    private static final Logger LOG =
            LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Логирует вызов методов сервисного слоя перед их выполнением.
     *
     * @param joinPoint информация о вызываемом методе и его аргументах
     */
    @Before("execution(* ru.job4j.bmb.services.*.*(..))")
    public void logBefore(final JoinPoint joinPoint) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        LOG.info(
                "Вызов: {}.{} args={}",
                className,
                methodName,
                Arrays.toString(joinPoint.getArgs())
        );
    }
}
