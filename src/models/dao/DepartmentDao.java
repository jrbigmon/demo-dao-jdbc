package models.dao;

import java.util.List;

import models.entities.Department;

public interface DepartmentDao {
  void insert(Department department);

  void update(Department department);

  void deleteById(Integer id);

  Department findById(Integer id);

  List<Department> findAll();
}
