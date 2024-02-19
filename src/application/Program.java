package application;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
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

        System.out.println();

        System.out.println("--- TEST findAll ---");
        sellers = sellerDao.findAll();
        sellers.forEach(System.out::println);

        System.out.println();

        System.out.println("--- TEST insert ---");
        String name = "Aretha";
        String email = "aretha@gmail.com";
        Date birthDate = new Date();
        Double baseSalary = 3000.00;

        seller = new Seller(null, name, email, birthDate, baseSalary, department);

        Integer sellerId = sellerDao.insert(seller);

        System.out.println("sellerId: " + sellerId);

        seller = sellerDao.findById(sellerId);
        System.out.println("Seller in database: " + seller);
    }
}
