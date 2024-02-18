package utils;

import java.util.Arrays;

public class SQLQuery {
  protected SQLQuery() {
    throw new IllegalStateException("SQLQuery is not initialized");
  }

  protected static String replaceBrackets(String value) {
    return value.replace("[", "").replace("]", "");
  }

  protected static String getReplacements(Integer columnsLength) {
    String replacements = "(";

    for (int i = 0; i < columnsLength; i++) {
      replacements += "?";
      if (i != columnsLength - 1) {
        replacements += ",";
      }
    }

    return replacements += ")";
  }

  protected static String getReplacementsToUpdate(String[] columns) {
    Integer columnsLength = columns.length;
    String replacements = "";

    for (int i = 0; i < columnsLength; i++) {
      replacements += columns[i] + " = ?";
      if (i != columnsLength - 1) {
        replacements += ", ";
      }
    }

    return replacements;
  }

  public static String getList(String tableName, String[] columns, String where, String[] joins, Integer limit) {
    if (tableName == null) {
      throw new IllegalArgumentException("tableName is required");
    }

    String columnsString = columns != null ? replaceBrackets(Arrays.toString(columns)) : "*";
    String joinsString = joins != null ? replaceBrackets(Arrays.toString(joins)) : "";
    String whereString = where != null ? where.replace(";", "") : "";
    String limitString = limit != null ? "limit " + limit : "";

    return "SELECT " + columnsString + " FROM " + tableName + " " + joinsString + " " + whereString + " " + limitString;
  }

  public static String create(String tableName, String[] columns) {
    if (tableName == null) {
      throw new IllegalArgumentException("tableName is required");
    }

    return "INSERT INTO "
        + tableName
        + " "
        + "(" + replaceBrackets(Arrays.toString(columns)) + ")"
        + " "
        + "VALUES "
        + getReplacements(columns.length)
        + ";";
  }

  public static String updateById(String tableName, String[] columns, int id) {
    if (tableName == null) {
      throw new IllegalArgumentException("tableName is required");
    }

    return "UPDATE "
        + tableName
        + " SET "
        + getReplacementsToUpdate(columns)
        + " WHERE "
        + "id = "
        + id;
  }

  public static String deleteById(String tableName, int id) {
    return "DELETE FROM "
        + tableName
        + " WHERE id = "
        + id;
  }

  public static String getById(Integer id, String tableName, String[] columns, String[] joins) {
    if (tableName == null) {
      throw new IllegalArgumentException("tableName cannot be null");
    }

    String columnsString = columns != null ? replaceBrackets(Arrays.toString(columns)) : "*";
    String joinsString = joins != null ? replaceBrackets(Arrays.toString(joins)) : "";
    String whereString = "WHERE " + tableName + ".id = " + id;

    return "SELECT " + columnsString + " FROM " + tableName + " " + joinsString + " " + whereString;
  }
}
