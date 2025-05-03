package com.javaaidev.text2sql.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_ABSENT)
public record ColumnInfo(
    String name,
    String dataType,
    String description) {

}
