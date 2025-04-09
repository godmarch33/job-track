//package co.uk.offerland.job_track.infrastructure.controller;
//
//import co.uk.offerland.job_track.application.dto.CompanyJobTitleDetailsRequest;
//import co.uk.offerland.job_track.application.dto.CompanyVisaSponsorshipDto;
//import co.uk.offerland.job_track.application.dto.RecognizeJobUrl;
//import co.uk.offerland.job_track.application.dto.RecognizeJobUrlResponse;
//import co.uk.offerland.job_track.application.usecases.job.VisaSponsorshipUseCase;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Mono;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@WebFluxTest(VisaSponsorshipController.class)
//@Import(VisaSponsorshipControllerTest.TestConfig.class)
//class VisaSponsorshipControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//    @Autowired
//    private VisaSponsorshipUseCase visaSponsorshipUseCase;
//
//    @Configuration
//    static class TestConfig {
//        @Bean
//        public VisaSponsorshipUseCase visaSponsorshipUseCase() {
//            return Mockito.mock(VisaSponsorshipUseCase.class);
//        }
//
//        @Bean
//        public VisaSponsorshipController visaSponsorshipController(VisaSponsorshipUseCase useCase) {
//            return new VisaSponsorshipController(useCase);
//        }
//    }
//
//
//    @Test
//    void sponsorship_ShouldReturnVisaSponsorshipDto() {
//        CompanyJobTitleDetailsRequest request = new CompanyJobTitleDetailsRequest("Google", "Software Engineer", "London");
//        CompanyVisaSponsorshipDto response = new CompanyVisaSponsorshipDto("Google", "London", "Senior Software engineer", "Skilled work visa", "100000$", "no");
//
//        Mockito.when(visaSponsorshipUseCase.searchVisa(any()))
//                .thenReturn(Mono.just(response));
//
//        webTestClient.post()
//                .uri("/api/companies/sponsorship")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CompanyVisaSponsorshipDto.class)
//                .isEqualTo(response);
//    }
//
//    @Test
//    void recognize_ShouldReturnRecognizeJobUrlResponse() {
//        RecognizeJobUrl request = new RecognizeJobUrl("https://jobs.example.com/123");
//        RecognizeJobUrlResponse response = new RecognizeJobUrlResponse("Google", "Backend Engineer", "London", "100000$");
//
//        Mockito.when(visaSponsorshipUseCase.recognizeJob(any()))
//                .thenReturn(Mono.just(response));
//
//        webTestClient.post()
//                .uri("/api/companies/recognize")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(RecognizeJobUrlResponse.class)
//                .isEqualTo(response);
//    }
//}
