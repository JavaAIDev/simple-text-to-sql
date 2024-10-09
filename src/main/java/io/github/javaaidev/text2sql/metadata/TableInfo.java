package io.github.javaaidev.text2sql.metadata;

import java.util.List;

public record TableInfo(String name, String description, String catalog,
                        String schema, List<ColumnInfo> columns) {

}
