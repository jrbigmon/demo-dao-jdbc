package models.dao;

import db.DB;
import models.dao.impl.SellerJDBC;

public class DaoFactory {
  private DaoFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static SellerDao getSellerDao() {
    return new SellerJDBC(DB.getConnection());
  }
}
