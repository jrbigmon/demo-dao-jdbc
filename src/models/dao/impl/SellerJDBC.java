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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      preparedStatement = connection
          .prepareStatement(SQLQuery.updateById(TABLE_NAME, columnsUpdate, seller.getId()));

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
        return this.instantiateSeller(result, this.instantiateDepartment(result));
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
    Statement statement = null;
    ResultSet result = null;

    try {
      List<Seller> list = new ArrayList<>();

      statement = connection.createStatement();

      result = statement.executeQuery(SQLQuery.getList(TABLE_NAME, this.columnsSelect, null,
          this.joins, null, null));

      Map<Integer, Department> mapDepartment = new HashMap<>();

      while (result.next()) {
        Department instantiatedDepartment = mapDepartment.get(result.getInt("DepartmentId"));

        if (instantiatedDepartment == null) {
          instantiatedDepartment = this.instantiateDepartment(result);
          mapDepartment.put(instantiatedDepartment.getId(), instantiatedDepartment);
        }

        list.add(this.instantiateSeller(result, instantiatedDepartment));
      }

      return list;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(statement);
      DB.closeResultSet(result);
    }
  }

  @Override
  public List<Seller> findByDepartment(Department department) {
    ResultSet result = null;
    Statement statement = null;

    try {
      List<Seller> list = new ArrayList<>();

      statement = connection.createStatement();

      result = statement
          .executeQuery(
              SQLQuery.getList(TABLE_NAME, columnsSelect, "WHERE DepartmentId = " + department.getId(), this.joins,
                  null, "ORDER BY Name ASC"));

      Map<Integer, Department> mapDepartment = new HashMap<>();

      while (result.next()) {
        Department instantiatedDepartment = mapDepartment.get(result.getInt("DepartmentId"));

        if (instantiatedDepartment == null) {
          instantiatedDepartment = this.instantiateDepartment(result);
          mapDepartment.put(instantiatedDepartment.getId(), instantiatedDepartment);
        }

        list.add(this.instantiateSeller(result, instantiatedDepartment));
      }

      return list;
    } catch (Exception e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeResultSet(result);
      DB.closeStatement(statement);
    }
  }

  private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Integer departmentId = resultSet.getInt("DepartmentId");
    String departmentName = resultSet.getString("DepartmentName");
    return new Department(departmentId, departmentName);
  }

  private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
    Integer id = resultSet.getInt("Id");
    String name = resultSet.getString("Name");
    String email = resultSet.getString("Email");
    Date birthDate = resultSet.getDate("BirthDate");
    Double baseSalary = resultSet.getDouble("BaseSalary");

    return new Seller(id, name, email, birthDate, baseSalary, department);
  }

}
