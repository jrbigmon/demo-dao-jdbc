package application;

import models.dao.DaoFactory;
import models.dao.SellerDao;
import models.entities.Seller;

public class Program {
    public static void main(String[] args) throws Exception {
        SellerDao sellerDao = DaoFactory.getSellerDao();

        Seller seller = sellerDao.findById(31);

        System.out.println(seller);
    }
}
