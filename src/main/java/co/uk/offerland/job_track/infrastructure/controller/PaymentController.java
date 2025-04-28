package co.uk.offerland.job_track.infrastructure.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @PostMapping(value = "/create-checkout-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, String>> createCheckoutSession(@AuthenticationPrincipal Jwt jwt) {
        return Mono.fromCallable(() -> {
            String email = jwt.getClaim("email");
            String fullName = jwt.getClaim("name");
            String userId = jwt.getClaim("sub");
            SessionCreateParams.LineItem item = SessionCreateParams.LineItem.builder()
                    .setPrice("price_1XXXXX")
                    .setQuantity(1L)
                    .build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .addAllLineItem(List.of(item))
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl("https://xpatjob.co.uk/api/payment/success")
                    .setCancelUrl("https://xpatjob.co.uk/api/payment/cancel")
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("checkoutUrl", session.getUrl());

            return response;
        });
    }

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> handleStripeWebhook(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .reduce(new StringBuilder(), (builder, dataBuffer) -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return builder.append(new String(bytes, StandardCharsets.UTF_8));
                })
                .flatMap(payload -> {
                    String sigHeader = exchange.getRequest().getHeaders().getFirst("Stripe-Signature");

                    try {
                        Event event = Webhook.constructEvent(payload.toString(), sigHeader, stripeSecretKey);
                        if ("checkout.session.completed".equals(event.getType())) {
                            Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                            String customerId = session.getCustomer();
                            String email = session.getCustomerDetails().getEmail();

                            // TODO: Update your DB or trigger business logic here
                        }

                        return Mono.just(ResponseEntity.ok("Received"));
                    } catch (SignatureVerificationException e) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature"));
                    }
//                    catch (StripeException e) {
//                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Stripe error"));
//                    }
                });
    }
}
