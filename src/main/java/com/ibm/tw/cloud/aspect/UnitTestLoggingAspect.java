package com.ibm.tw.cloud.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import com.ibm.tw.cloud.os.service.UnitTestLoggingService;

@Aspect
@Configuration
public class UnitTestLoggingAspect {
	@Before("execution(* com.ibm.tw.cloud.os.service.impl.UnitTestLoggingServiceImpl.*(..))")
	public void before(JoinPoint joinPoint) {
		UnitTestLoggingService service = (UnitTestLoggingService)joinPoint.getThis();
		service.printLoggingRemark();
	}
}
