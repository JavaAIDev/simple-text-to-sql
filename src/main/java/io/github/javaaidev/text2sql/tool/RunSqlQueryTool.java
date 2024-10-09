package io.github.javaaidev.text2sql.tool;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.CollectionUtils;

/**
 * Use {@linkplain JdbcClient} to run SQL query and output result in CSV format
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

  private String runQuery(String query) {
    var rows = jdbcClient.sql(query)
        .query().listOfRows();
    if (CollectionUtils.isEmpty(rows)) {
      return "";
    }
    var fields = rows.getFirst().keySet();
    var builder = new StringBuilder();
    builder.append(String.join(", ", fields));
    for (Map<String, Object> row : rows) {
      var rowString = fields.stream().map(row::get).filter(Objects::nonNull)
          .map(Object::toString)
          .collect(Collectors.joining(", "));
      builder.append(rowString);
    }
    return builder.toString();
  }
}
