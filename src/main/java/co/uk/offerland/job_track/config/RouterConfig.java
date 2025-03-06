//package co.uk.offerland.job_track.config;
//
//import co.uk.offerland.job_track.application.dto.user.ListJobs;
//import co.uk.offerland.job_track.application.dto.user.UserCreateRequest;
//import co.uk.offerland.job_track.application.usecases.user.AssignJobToUserUseCase;
//import co.uk.offerland.job_track.application.usecases.user.CreateUserUseCase;
//import co.uk.offerland.job_track.infrastructure.exception.ApiError;
//import co.uk.offerland.job_track.infrastructure.exception.UserNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
//import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//
//@Slf4j
//@Configuration
//public class RouterConfig {
//
//    @Autowired
//    private CreateUserUseCase userService;
//    @Autowired
//    private AssignJobToUserUseCase assignJobToUserUseCase;
//
//    @Bean
//    public RouterFunction<ServerResponse> userRoutes() {
//        return route(POST("/api/users/register"), this::registerUser)
//                .andRoute(GET("/api/users/{userId}/jobs"), this::getUserJobs);
//    }
//
//    private Mono<ServerResponse> registerUser(ServerRequest request) {
//        return request.bodyToMono(UserCreateRequest.class)
//                .doOnNext(req -> log.info("Income create user request: {}", req))
//                .flatMap(req -> userService.createNewUser(req.getSource()))
//                .flatMap(response -> ServerResponse.ok().bodyValue(response));
//    }
//
//
//    public Mono<ServerResponse> getUserJobs(ServerRequest request) {
//        UUID userId = UUID.fromString(request.pathVariable("userId"));
//        log.info("Income get jobs request userId:[{}]", userId);
//
//        return assignJobToUserUseCase.getJobsByUserId(userId)
//                .doOnNext(jobs -> log.info("Get jobs for userId:[{}], jobs:[{}]", userId, jobs))
//                .flatMap(jobs -> ServerResponse.ok().bodyValue(jobs));
//    }
//
//    private RouterFunction<ServerResponse> errorRoutes() {
//        return route()
//                .onError(UserNotFoundException.class, this::handleResourceNotFoundException)
////                .onError(BadRequestException.class, this::handleBadRequestException)
////                .onError(Exception.class, this::handleGenericException)
//                .build();
//    }
//
////    @Autowired
////    private CreateUserUseCase userService;
////
////    @Bean
////    public RouterFunction<ServerResponse> routeFunction() {
////        return RouterFunctions
////                .route(RequestPredicates.GET("/api/users/register"), userService::createNewUser)
////                .andRoute(RequestPredicates.POST("/resource"), this::createResource);
////    }
////
////    public Mono<ServerResponse> getResource(ServerRequest request) {
////        String id = request.pathVariable("id");
////        return Mono.error(new ResourceNotFoundException("Resource not found with id: " + id));
////    }
////
////    public Mono<ServerResponse> createResource(ServerRequest request) {
////        return Mono.error(new BadRequestException("Invalid request to create resource"));
////    }
////
//
//    //
//    private Mono<ServerResponse> handleResourceNotFoundException(Throwable throwable, ServerRequest request) {
//        return ServerResponse.status(HttpStatus.NOT_FOUND)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(new ApiError(HttpStatus.NOT_FOUND, throwable.getMessage()));
//    }
////
////    private Mono<ServerResponse> handleBadRequestException(Throwable throwable, ServerRequest request) {
////        return ServerResponse.status(HttpStatus.BAD_REQUEST)
////                .contentType(MediaType.TEXT_PLAIN)
////                .bodyValue(throwable.getMessage());
////    }
////
////    private Mono<ServerResponse> handleGenericException(Throwable throwable, ServerRequest request) {
////        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                .contentType(MediaType.TEXT_PLAIN)
////                .bodyValue("An unexpected error occurred: " + throwable.getMessage());
////    }
//}
