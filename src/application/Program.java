package application;

import java.util.Date;

import models.entities.Department;
import models.entities.Seller;

public class Program {
    public static void main(String[] args) throws Exception {
        Department dp = new Department(1, "Sales");
        Seller seller = new Seller(1, "vagner", "vagner.junior@email", new Date(), 3000.00, dp);

        System.out.println(dp);
        System.out.println(seller);
    }
}
