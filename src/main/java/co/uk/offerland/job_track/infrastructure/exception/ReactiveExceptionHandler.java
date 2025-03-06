//package co.uk.offerland.job_track.infrastructure.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.web.WebProperties;
//import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.*;
//import org.springframework.web.server.ResponseStatusException;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//@Slf4j
//@Component
//public class ReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {
////    private final Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode;
////    private final HttpStatus defaultStatus;
//
////    public ReactiveExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
////                                    ApplicationContext applicationContext) {
////        super(errorAttributes, resources, applicationContext);
////        this.exceptionToStatusCode = exceptionToStatusCode;
////        this.defaultStatus = defaultStatus;
////    }
//
//    @Override
//    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
//        System.err.println("!!!!!!!!!!!!!!!!!!!!!");
//        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
//    }
//
//    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
//        System.err.println("=========================");
//
//        Throwable error = getError(request);
//        log.error("An error has occurred", error);
//        HttpStatus httpStatus;
//        if (error instanceof ResponseStatusException responseStatusException) {
//            httpStatus = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
//        } else if (error instanceof Exception exception) {
////            httpStatus = exceptionToStatusCode.getOrDefault(exception.getClass(), defaultStatus);
//        } else {
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return ServerResponse
//                .status(HttpStatus.ACCEPTED)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(new ApiError(error.getMessage())));
//    }
//}