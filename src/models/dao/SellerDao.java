package models.dao;

import java.util.List;

import models.entities.Seller;

public interface SellerDao {
  void insert(Seller seller);

  void update(Seller seller);

  void deleteById(Integer id);

  Seller findById(Integer id);

  List<Seller> findAll();

  List<Seller> findByDepartment(Integer departmentId);
}
