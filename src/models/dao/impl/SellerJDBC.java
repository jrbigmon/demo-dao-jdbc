package models.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import models.dao.SellerDao;
import models.entities.Department;
import models.entities.Seller;
import utils.SQLQuery;

public class SellerJDBC implements SellerDao {
  public static final String TABLE_NAME = "seller";

  private Connection connection;

  private String[] columnsSelect = new String[] { "seller.*, department.Name as DepartmentName" };
  private String[] columnsInsert = new String[] { "Name", "Email", "BirthDate", "BaseSalary", "DepartmentId" };
  private String[] columnsUpdate = new String[] { "Name", "Email", "BirthDate", "BaseSalary", "DepartmentId" };

  private String[] joins = new String[] { "INNER JOIN department ON department.id = seller.DepartmentId" };

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  public SellerJDBC(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void insert(Seller seller) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(SQLQuery.create(TABLE_NAME, columnsInsert));
      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());
      preparedStatement.setDate(3, new java.sql.Date(sdf.parse(seller.getBirthDate().toString()).getTime()));
      preparedStatement.setDouble(4, seller.getBaseSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());
    } catch (SQLException | ParseException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public void update(Seller seller) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(SQLQuery.updateById(TABLE_NAME, columnsUpdate, seller.getId()));

      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());
      preparedStatement.setDate(3, new java.sql.Date(sdf.parse(seller.getBirthDate().toString()).getTime()));
      preparedStatement.setDouble(4, seller.getBaseSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());

      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public void deleteById(Integer id) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(SQLQuery.deleteById(TABLE_NAME, id));

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DbIntegrityException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public Seller findById(Integer id) {
    Statement statement = null;
    ResultSet result = null;

    try {
      statement = connection.createStatement();

      result = statement.executeQuery(SQLQuery.getById(id, TABLE_NAME, this.columnsSelect,
          this.joins));

      if (result.next()) {
        String name = result.getString("Name");
        String email = result.getString("Email");
        Date birthDate = result.getDate("BirthDate");
        Double baseSalary = result.getDouble("BaseSalary");
        Integer departmentId = result.getInt("DepartmentId");
        String departmentName = result.getString("DepartmentName");
        Department department = new Department(departmentId, departmentName);

        return new Seller(id, name, email, birthDate, baseSalary, department);
      }

      return null;
    } catch (Exception e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(statement);
      DB.closeResultSet(result);
    }
  }

  @Override
  public List<Seller> findAll() {
    Statement stt = null;
    ResultSet result = null;

    try {
      List<Seller> list = new ArrayList<>();

      stt = connection.createStatement();

      result = stt.executeQuery(SQLQuery.getList(TABLE_NAME, this.columnsSelect, null,
          this.joins, null));

      while (result.next()) {
        Integer id = result.getInt("Id");
        String name = result.getString("Name");
        String email = result.getString("Email");
        Date birthDate = result.getDate("BirthDate");
        Double baseSalary = result.getDouble("BaseSalary");
        Integer departmentId = result.getInt("DepartmentId");
        String departmentName = result.getString("DepartmentName");
        Department department = new Department(departmentId, departmentName);

        Seller seller = new Seller(id, name, email, birthDate, baseSalary, department);

        list.add(seller);
      }

      return list;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(stt);
      DB.closeResultSet(result);
    }
  }

}
