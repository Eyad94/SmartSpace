package smartspace.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
@Aspect
public class PerfoemanceMonitorAspect {
	@Autowired
	public PerfoemanceMonitorAspect() {
		super();
	}
	
	@Around("@annotation(smartspace.aop.PerformanceMonitor)")
	public Object measureTime(ProceedingJoinPoint pjp) throws Throwable{
		
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName(); 
		long startTime = System.currentTimeMillis();
		
		
		// invoke method
		try {
			Object rv = pjp.proceed();
			return rv;
		} catch (Throwable e) {
			throw e;
		}finally {
			long elapsed = System.currentTimeMillis() - startTime;
			System.err.println("********* " + className + "." + methodName + "() - " + elapsed + "ms");
		}
	}

}
