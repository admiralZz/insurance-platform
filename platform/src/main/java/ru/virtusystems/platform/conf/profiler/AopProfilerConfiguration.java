package ru.virtusystems.platform.conf.profiler;

import lombok.Setter;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Configuration
@ConfigurationProperties(prefix = "app.profiling")
@ConditionalOnProperty(
        prefix = "app.profiling",
        name = "enable",
        havingValue = "true"
)
@EnableAspectJAutoProxy
public class AopProfilerConfiguration {

    private boolean enable;
    private List<String> pointcuts;

    @Bean
    public Advisor performanceMonitorAdvisor() {
        var pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(formExpressions());
        return new DefaultPointcutAdvisor(pointcut, new CustomPerformanceMonitorInterceptor());
    }

    private String formExpressions() {
        return pointcuts
                .stream()
                .map(this::formExpression)
                .collect(Collectors.joining(" || "));
    }

    private String formExpression(String pointcut) {
        return pointcut.endsWith(")") ? execForMethod(pointcut) : execForPackageClass(pointcut);
    }

    private String execForMethod(String pointcut) {
        return String.format("execution(public * %s(..))", pointcut.replace("()", ""));
    }

    private String execForPackageClass(String pointcut) {
        return String.format("execution(public * %s..*(..))", pointcut);
    }
}
