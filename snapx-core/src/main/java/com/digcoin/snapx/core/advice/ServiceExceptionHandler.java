package com.digcoin.snapx.core.advice;

import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.error.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class ServiceExceptionHandler {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceClassMethod() {

    }

    @Pointcut("execution(public * *(..))")
    public void publicMethodOnly() {

    }

    @Around("serviceClassMethod() && publicMethodOnly()")
    public Object logServiceException(ProceedingJoinPoint pjp) {

        try {

            return pjp.proceed();

        } catch (BusinessException e) {
            throw e;

        } catch (Throwable e) {
            Signature signature = pjp.getSignature();
            Logger log = LoggerFactory.getLogger(signature.getDeclaringType());
            log.error("{} args:[{}]", signature.getName(), Arrays.toString(pjp.getArgs()), e);
            throw CommonError.UNEXPECT_ERROR.withCause(e);
        }

    }
}
