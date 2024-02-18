package utils;

public class Utils {
  private Utils() {
    throw new IllegalStateException("Utils is not initialized");
  }

  public static Double incrementPercent(Double value, Double percent) {
    return value + value * percent;
  }
}
