package com.javaaidev.text2sql.tool;

/**
 * Response of SQL query
 *
 * @param result Success result
 * @param error  Error result
 */
public record RunSqlQueryResponse(String result, String error) {

}
