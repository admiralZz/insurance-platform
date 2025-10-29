package ru.virtusystems.platform.conf.profiler;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.util.StopWatch;

public class CustomPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {
    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
        String name = this.createInvocationTraceName(invocation);
        var stopWatch = new StopWatch(name);
        stopWatch.start(name);

        Object result;
        try {
            result = invocation.proceed();
        } finally {
            stopWatch.stop();
            this.writeToLog(logger, String.format("Profiling: %s. Running time: %f sec", name, stopWatch.getTotalTimeSeconds()));
        }

        return result;
    }
}
