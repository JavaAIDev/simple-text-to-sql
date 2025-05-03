package com.javaaidev.text2sql.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_ABSENT)
public record TableInfo(
    String name,
    String description,
    String catalog,
    String schema,
    List<ColumnInfo> columns) {

}
