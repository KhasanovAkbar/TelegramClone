package univ.tuit.telegramclone.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;
    private final WSHandshakerInterceptor wsHandshakerInterceptor;

    public WebSocketConfig(WebSocketHandler webSocketHandler, WSHandshakerInterceptor wsHandshakerInterceptor) {
        this.webSocketHandler = webSocketHandler;
        this.wsHandshakerInterceptor = wsHandshakerInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSocketHandler, "/messaging")
                .setAllowedOrigins("*")
                .addInterceptors(wsHandshakerInterceptor);

    }
}
