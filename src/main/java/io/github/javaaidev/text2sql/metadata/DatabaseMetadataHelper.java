package io.github.javaaidev.text2sql.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import javax.sql.DataSource;

public class DatabaseMetadataHelper {

  private final DataSource dataSource;
  private final ObjectMapper objectMapper;

  public DatabaseMetadataHelper(DataSource dataSource,
      ObjectMapper objectMapper) {
    this.dataSource = dataSource;
    this.objectMapper = objectMapper;
  }

  public String extractMetadataJson() throws SQLException {
    var metadata = extractMetadata();
    try {
      return objectMapper.writeValueAsString(metadata);
    } catch (JsonProcessingException e) {
      return Objects.toString(metadata);
    }
  }

  public DatabaseMetadata extractMetadata() throws SQLException {
    var metadata = dataSource.getConnection().getMetaData();
    var tablesInfo = new ArrayList<TableInfo>();
    try (var tables = metadata.getTables(null, null, null,
        new String[]{"TABLE"})) {
      while (tables.next()) {
        var tableName = tables.getString("TABLE_NAME");
        var tableDescription = tables.getString("REMARKS");
        var tableCatalog = tables.getString("TABLE_CAT");
        var tableSchema = tables.getString("TABLE_SCHEM");
        var columnsInfo = new ArrayList<ColumnInfo>();
        try (var columns = metadata.getColumns(null, null, tableName, null)) {
          while (columns.next()) {
            var columnName = columns.getString("COLUMN_NAME");
            var datatype = columns.getString("TYPE_NAME");
            var columnDescription = columns.getString("REMARKS");
            columnsInfo.add(
                new ColumnInfo(columnName, datatype, columnDescription)
            );
          }
        }
        tablesInfo.add(
            new TableInfo(
                tableName,
                tableDescription,
                tableCatalog,
                tableSchema,
                columnsInfo
            )
        );
      }
    }
    return new DatabaseMetadata(tablesInfo);
  }
}
