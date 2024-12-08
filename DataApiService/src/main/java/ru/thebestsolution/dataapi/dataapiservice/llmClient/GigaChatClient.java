package ru.thebestsolution.dataapi.dataapiservice.llmClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.thebestsolution.dataapi.dataapiservice.config.AppConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GigaChatClient {
    private final WebClient llmWebClient;
    private final AppConfig appConfig;

    public GigaChatClient(AppConfig appConfig, WebClient llmWebClient) {
        this.appConfig = appConfig;
        this.llmWebClient = llmWebClient;
    }

    public String sendMessage(String userMessage) {

        Map<String, Object> systemMessageSetting = new HashMap<>();
        systemMessageSetting.put("role", "system");
        systemMessageSetting.put("content", "Пришли в ответ только JSON");

        Map<String, Object> userMessageSetting = new HashMap<>();
        userMessageSetting.put("role", "user");
        userMessageSetting.put("content", userMessage);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "GigaChat:latest");
        requestBody.put("temperature", 0.87);
        requestBody.put("messages", List.of(systemMessageSetting, userMessageSetting));
        requestBody.put("n", 1);
        requestBody.put("max_tokens", 512);
        requestBody.put("presence_penalty", 1.07);
        requestBody.put("stream", false);
        requestBody.put("update_interval", 0);

        Mono<String> mono = llmWebClient
                .post()
                .uri("https://gigachat.devices.sberbank.ru/api/chat/completitions")
                .header("Authorization", "Bearer " + appConfig.getAccessToken())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
        String response = mono.block();
        System.out.println("Ответ: " + response);
        return response;

    }
}
