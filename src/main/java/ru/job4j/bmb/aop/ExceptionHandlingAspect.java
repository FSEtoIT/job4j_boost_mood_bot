package ru.job4j.bmb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования исключений в сервисном слое.
 */
@Aspect
@Component
public final class ExceptionHandlingAspect {

    /**
     * Логгер для обработки исключений.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    /**
     * Обрабатывает исключения, выброшенные в сервисном слое.
     *
     * @param joinPoint информация о месте вызова метода
     * @param ex        выброшенное исключение
     */
    @AfterThrowing(
            pointcut = "execution(* ru.job4j.bmb.services.*.*(..))",
            throwing = "ex"
    )
    public void handleException(
            final JoinPoint joinPoint,
            final Exception ex) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        LOGGER.error(
                "Exception in {}.{}(): {}",
                className,
                methodName,
                ex.getMessage(),
                ex
        );
    }
}
