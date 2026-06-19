package com.javaaidev.text2sql;

import com.javaaidev.text2sql.metadata.DatabaseMetadataHelper;
import com.javaaidev.text2sql.tool.RunSqlQueryRequest;
import com.javaaidev.text2sql.tool.RunSqlQueryResponse;
import com.javaaidev.text2sql.tool.RunSqlQueryTool;
import javax.sql.DataSource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class AppConfiguration {

  @Bean
  public DatabaseMetadataHelper databaseMetadataHelper(
      DataSource dataSource,
      ObjectMapper objectMapper) {
    return new DatabaseMetadataHelper(dataSource, objectMapper);
  }

  @Bean
  public DatabaseMetadataAdvisor databaseMetadataAdvisor(
      DatabaseMetadataHelper databaseMetadataHelper) {
    return new DatabaseMetadataAdvisor(databaseMetadataHelper);
  }

  @Bean
  public JdbcClient jdbcClient(DataSource dataSource) {
    return JdbcClient.create(dataSource);
  }

  @Bean
  public RunSqlQueryTool runSqlQuery(JdbcClient jdbcClient) {
    return new RunSqlQueryTool(jdbcClient);
  }

  @Bean
  public FunctionToolCallback<RunSqlQueryRequest, RunSqlQueryResponse> runSqlQueryToolCallback(RunSqlQueryTool runSqlQueryTool) {
    return FunctionToolCallback.builder("runSqlQuery", runSqlQueryTool)
        .description("Query database using SQL")
        .inputType(RunSqlQueryRequest.class)
        .build();
  }
}
