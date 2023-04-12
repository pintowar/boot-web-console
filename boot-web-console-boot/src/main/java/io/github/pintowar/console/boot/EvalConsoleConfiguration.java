package io.github.pintowar.console.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
@ConditionalOnProperty(value = "web.console.enabled", havingValue = "true")
public class EvalConsoleConfiguration {

    @Bean
    public EvalConsoleHandler evalConsoleHandler(ApplicationContext context) {
        return new EvalConsoleHandler(context);
    }

    @Bean
    RouterFunction<ServerResponse> getEmployeeByIdRoute(EvalConsoleHandler evalConsoleHandler) {
        return route().nest(
                path("/console"), builder -> builder
                        .GET("", evalConsoleHandler::index)
                        .GET("/engines", evalConsoleHandler::engines)
                        .POST("/{engine}/eval", evalConsoleHandler::execute)
        ).build();
    }
}
