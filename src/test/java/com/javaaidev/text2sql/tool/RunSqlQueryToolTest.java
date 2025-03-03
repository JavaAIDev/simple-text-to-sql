package com.javaaidev.text2sql.tool;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.ResultQuerySpec;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;

class RunSqlQueryToolTest {
  @Test
  void testCsvFormat() {
    var mock = Mockito.mock(JdbcClient.class);
    var resultQuerySpecMock = Mockito.mock(ResultQuerySpec.class);
    when(resultQuerySpecMock.listOfRows()).thenReturn(List.of(
        Map.of("v1", "Hello", "v2", 123),
        Map.of("v1", "World","v2", 456)
    ));
    var statementSpecMock = Mockito.mock(StatementSpec.class);
    when(statementSpecMock.query()).thenReturn(resultQuerySpecMock);
    when(mock.sql(any())).thenReturn(statementSpecMock);
    var tool = new RunSqlQueryTool(mock);
    var response = tool.apply(new RunSqlQueryRequest("select 1"));
    assertEquals("Hello,123\nWorld,456\n", response.result());
  }
}