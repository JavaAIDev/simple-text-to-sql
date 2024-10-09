package io.github.javaaidev.text2sql;

import io.github.javaaidev.text2sql.metadata.DatabaseMetadataHelper;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.core.Ordered;

/**
 * Advisor to update system text of the prompt
 */
public class DatabaseMetadataAdvisor implements CallAroundAdvisor {

  private static final String DEFAULT_SYSTEM_TEXT = """
      You are a Postgres expert. Please help to generate a Postgres query, then run the query to answer the question. The output should be in tabular format.
      
      ===Tables
      {table_schemas}
      """;

  private final DatabaseMetadataHelper databaseMetadataHelper;

  public DatabaseMetadataAdvisor(
      DatabaseMetadataHelper databaseMetadataHelper) {
    this.databaseMetadataHelper = databaseMetadataHelper;
  }

  @Override
  public AdvisedResponse aroundCall(AdvisedRequest advisedRequest,
      CallAroundAdvisorChain chain) {
    var systemParams = Optional.ofNullable(advisedRequest.systemParams())
        .orElseGet(HashMap::new);
    var tableSchemas = "";
    try {
      tableSchemas = databaseMetadataHelper.extractMetadataJson();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    systemParams.put("table_schemas", tableSchemas);
    var request = AdvisedRequest.from(advisedRequest)
        .withSystemText(DEFAULT_SYSTEM_TEXT)
        .withSystemParams(systemParams)
        .build();
    return chain.nextAroundCall(request);
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
