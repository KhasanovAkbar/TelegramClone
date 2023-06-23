package univ.tuit.telegramclone.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.message.broker.constants.KafkaConstants;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.UserRepository;

import java.util.Map;

@Component
public class WSHandshakerInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WSHandshakerInterceptor.class);
    private final CacheService cacheService;
    private final UserRepository userRepository;

    public WSHandshakerInterceptor(CacheService cacheService, UserRepository userRepository) {
        this.cacheService = cacheService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        LOGGER.info("handshake URl for WS session: "+request.getURI().getPath() +" ? "+request.getURI().getQuery());
        String[] parameters = request.getURI().getQuery().split("=");

        if (parameters.length == 2 && parameters[0].equals("accessToken")){
            String accessToken = parameters[1];

            Long senderUserId = 0L;
            String senderId = cacheService.getUserIdByAccessToken(accessToken);

            if (senderId ==null){
                User sender = userRepository.findByToken(accessToken);
                if (sender != null){
                    senderUserId = sender.getUserId();
                }
            }else {
                senderUserId = Long.valueOf(senderId);
            }
            LOGGER.info("Handshake found userId: "+senderUserId);

            if (senderUserId == 0L){
                LOGGER.info("Handshake failed: user not found for given accessToken");
                attributes.put(KafkaConstants.HEADER, "Handshake failed: user not found for given accessToken");
                return false;
            }

            attributes.put(KafkaConstants.HEADER, "Handshake successful");
            LOGGER.info("Handshake successful");
            return true;
        }
        attributes.put(KafkaConstants.HEADER, "Handshake failed: accessToken not found");
        LOGGER.info("Handshake failed: accessToken not found");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
