package com.javaaidev.text2sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaaidev.text2sql.metadata.DatabaseMetadataHelper;
import com.javaaidev.text2sql.tool.RunSqlQueryTool;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class AppConfiguration {

  @Bean
  public DatabaseMetadataHelper databaseMetadataHelper(DataSource dataSource,
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
  @Description("Query database using SQL")
  public RunSqlQueryTool runSqlQuery(JdbcClient jdbcClient) {
    return new RunSqlQueryTool(jdbcClient);
  }
}
