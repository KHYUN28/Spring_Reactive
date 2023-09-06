package com.itvillage.book.v4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;

import javax.validation.Validator;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

//BookdHandler.java
@Configuration("bookRouterV4")
public class BookRouter {
    @Bean
    public RouterFunction<?> routeBookV4(BookHandler handler) { // Mono<HandlerFunction<T>> route(ServerRequest request);
        return route() //  org.springframework.web.reactive.function.server.RouterFunctions의 route()
                // 얘랑 RouterFunction는 다른 얘
                // Builder POST(String pattern, HandleFunction<ServerResponse> handlerFunction)
                // Mono<T> handle(ServerRequest request); <- defined by HandlerFunction interface
                // handler::createBook have implemented for handle method
                .POST("/v4/books", handler::createBook) // handle을 구현한 얘
                .PATCH("/v4/books/{book-id}", handler::updateBook)
                .GET("/v4/books", handler::getBooks)
                .GET("/v4/books/{book-id}", handler::getBook)
                .build(); // return : the built router function
    }

//    @Bean
//    public RouterFunction<?> helloRouterFunction() {
//        return route(GET("/hello"),
//                request -> ok().body(just("Hello World!"), String.class))
//                .andRoute(Get("/bye"),
//                        request -> ok().body(just("See ya!"), String.class));
//    }

    @Bean
    public Validator javaxValidator() {
        return new LocalValidatorFactoryBean();
    }
}
