package application;

import java.util.List;

import models.dao.DaoFactory;
import models.dao.SellerDao;
import models.entities.Department;
import models.entities.Seller;

public class Program {
    public static void main(String[] args) throws Exception {
        SellerDao sellerDao = DaoFactory.getSellerDao();

        System.out.println("--- TEST findById ---");
        Seller seller = sellerDao.findById(31);
        System.out.println(seller);

        System.out.println();

        System.out.println("--- TEST findByDepartmentId ---");
        Department department = new Department(1, "Computers");
        List<Seller> sellers = sellerDao.findByDepartment(department);
        sellers.forEach(System.out::println);
    }
}
