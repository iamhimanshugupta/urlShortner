package com.practice.urlshortener.aspect;

import com.practice.urlshortener.annotation.Log;
import com.practice.urlshortener.annotation.NotLog;
import com.practice.urlshortener.model.UrlShortenerLogging;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.stream.IntStream;

@Component
@Aspect
public class UrlShortenerLoggingAspect {

    @Value("${spring.application.name}")
    private String APPLICATION_NAME;

    private static final Logger LOGGER = LoggerFactory.getLogger("COMMON_LOGGER");

    @Pointcut("@annotation(com.practice.urlshortener.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        UrlShortenerLogging urlShortenerLogging = new UrlShortenerLogging();
        Long startTime = System.currentTimeMillis();
        String uniqueId = MDC.get("UNIQUE_ID");

        Object[] objects = proceedingJoinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();

        urlShortenerLogging.setA(APPLICATION_NAME);
        urlShortenerLogging.setE(method.getAnnotation(Log.class).name());
        urlShortenerLogging.setU(uniqueId);

        StringBuilder requestBuilder = new StringBuilder();
        IntStream.range(0,  proceedingJoinPoint.getArgs().length).filter((index) -> {
            return methodSignature.getMethod().getParameters()[index].getAnnotation(NotLog.class) == null;
        }).forEach((index) -> {
            requestBuilder.append(methodSignature.getMethod().getParameters()[index].getName()).append(":").append(proceedingJoinPoint.getArgs()[index] != null ? proceedingJoinPoint.getArgs()[index].toString() : null).append(", ");
        });

        String request = requestBuilder.toString();
        if (request.contains(",")) {
            request = request.substring(0, request.lastIndexOf(","));
        }
        urlShortenerLogging.setReq(request);

        Object o = null;
        boolean ifErrorOccurred = false;
        try {
            o = proceedingJoinPoint.proceed();
            urlShortenerLogging.setRes(o.toString());
        } catch (Throwable throwable) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);

            urlShortenerLogging.setRes(sw.toString());
            ifErrorOccurred = true;
            throw throwable;
        } finally {
            urlShortenerLogging.setT(System.currentTimeMillis() - startTime);

            if(ifErrorOccurred){
                LOGGER.error(urlShortenerLogging.toString());
            }else{
                LOGGER.info(urlShortenerLogging.toString());
            }
        }

        return o;
    }
}
