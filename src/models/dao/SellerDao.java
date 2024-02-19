package models.dao;

import java.util.List;

import models.entities.Department;
import models.entities.Seller;

public interface SellerDao {
  Integer insert(Seller seller);

  Boolean update(Seller seller);

  void deleteById(Integer id);

  Seller findById(Integer id);

  List<Seller> findAll();

  List<Seller> findByDepartment(Department department);
}
