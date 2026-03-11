package ru.job4j.bmb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* ru.job4j.bmb.services.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        System.out.println(
                "Вызов: " + className + "." + methodName +
                        " args=" + Arrays.toString(joinPoint.getArgs())
        );
    }
}