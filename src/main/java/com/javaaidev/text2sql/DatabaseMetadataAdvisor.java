package com.javaaidev.text2sql;

import com.javaaidev.text2sql.metadata.DatabaseMetadataHelper;
import java.sql.SQLException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final String tableSchemas;
  private static final Logger LOGGER = LoggerFactory.getLogger(
      DatabaseMetadataAdvisor.class);

  public DatabaseMetadataAdvisor(
      DatabaseMetadataHelper databaseMetadataHelper) {
    this.databaseMetadataHelper = databaseMetadataHelper;
    this.tableSchemas = getDatabaseMetadata();
    LOGGER.info("Loaded database metadata: {}", this.tableSchemas);
  }

  private String getDatabaseMetadata() {
    try {
      return databaseMetadataHelper.extractMetadataJson();
    } catch (SQLException e) {
      LOGGER.error("Failed to load database metadata", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public AdvisedResponse aroundCall(AdvisedRequest advisedRequest,
      CallAroundAdvisorChain chain) {
    var systemParams = new HashMap<>(advisedRequest.systemParams());
    systemParams.put("table_schemas", tableSchemas);
    var request = AdvisedRequest.from(advisedRequest)
        .systemText(DEFAULT_SYSTEM_TEXT)
        .systemParams(systemParams)
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
