package com.javaaidev.text2sql.controller;

import com.javaaidev.chatagent.model.ChatAgentRequest;
import com.javaaidev.chatagent.model.ChatAgentResponse;
import com.javaaidev.chatagent.springai.ModelAdapter;
import com.javaaidev.text2sql.DatabaseMetadataAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  public ChatController(ChatClient.Builder builder,
      DatabaseMetadataAdvisor databaseMetadataAdvisor) {
    chatClient = builder.defaultAdvisors(databaseMetadataAdvisor).build();
  }

  @PostMapping("/chat")
  public Flux<ServerSentEvent<ChatAgentResponse>> chat(@RequestBody ChatAgentRequest request) {
    return ModelAdapter.toStreamingResponse(
        chatClient.prompt()
            .messages(ModelAdapter.fromRequest(request).toArray(new Message[0]))
            .tools("runSqlQuery")
            .stream()
            .chatResponse());
  }
}
