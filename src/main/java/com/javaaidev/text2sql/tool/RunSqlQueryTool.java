package com.javaaidev.text2sql.tool;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.CollectionUtils;

/**
 * Use {@linkplain JdbcClient} to run SQL query and output result in CSV
 * format
 */
public class RunSqlQueryTool implements
    Function<RunSqlQueryRequest, RunSqlQueryResponse> {

  private final JdbcClient jdbcClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(
      RunSqlQueryTool.class);

  public RunSqlQueryTool(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public RunSqlQueryResponse apply(RunSqlQueryRequest request) {
    try {
      LOGGER.info("SQL query to run [{}]", request.query());
      return new RunSqlQueryResponse(runQuery(request.query()), null);
    } catch (Exception e) {
      return new RunSqlQueryResponse(null, e.getMessage());
    }
  }

  private String runQuery(String query) throws IOException {
    var rows = jdbcClient.sql(query)
        .query().listOfRows();
    if (CollectionUtils.isEmpty(rows)) {
      return "";
    }
    var fields = rows.getFirst().keySet().stream().sorted().toList();
    var printer = CSVFormat.DEFAULT.builder()
        .setHeader(fields.toArray(new String[0]))
        .setSkipHeaderRecord(false)
        .setRecordSeparator('\n')
        .build();
    var builder = new StringBuilder();
    for (Map<String, Object> row : rows) {
      printer.printRecord(builder,
          fields.stream().map(row::get).toArray());
    }
    return builder.toString();
  }
}
