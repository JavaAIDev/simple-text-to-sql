package io.github.javaaidev.text2sql.controller;

import io.github.javaaidev.text2sql.DatabaseMetadataAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  public ChatController(ChatClient.Builder builder,
      DatabaseMetadataAdvisor databaseMetadataAdvisor) {
    chatClient = builder.defaultAdvisors(databaseMetadataAdvisor).build();
  }

  @PostMapping("/chat")
  public ChatResponse chat(@RequestBody ChatRequest request) {
    return new ChatResponse(
        chatClient.prompt().user(request.input())
            .options(OpenAiChatOptions.builder()
                .withModel(ChatModel.GPT_3_5_TURBO)
                .withTemperature(0.0)
                .withFunction("runSqlQuery")
                .build())
            .call().content());
  }
}
