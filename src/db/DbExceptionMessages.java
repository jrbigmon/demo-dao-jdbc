package db;

public class DbExceptionMessages {
  private DbExceptionMessages() {
    throw new IllegalStateException("DbExceptionMessages is not initialized");
  }

  public static final String ID_CANNOT_BE_NULL = "Id cannot be null";
}
