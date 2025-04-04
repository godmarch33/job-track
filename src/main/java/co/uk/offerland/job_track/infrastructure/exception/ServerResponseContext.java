package co.uk.offerland.job_track.infrastructure.exception;

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;

public class ServerResponseContext implements ServerResponse.Context {
    private final ServerWebExchange exchange;

    public ServerResponseContext(ServerWebExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewResolver> viewResolvers() {
        return Collections.emptyList();
    }
}
