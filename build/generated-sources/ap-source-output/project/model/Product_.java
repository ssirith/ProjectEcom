package project.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import project.model.Orderdetail;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-10-25T22:28:10")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile SingularAttribute<Product, String> productcode;
    public static volatile ListAttribute<Product, Orderdetail> orderdetailList;
    public static volatile SingularAttribute<Product, String> productname;
    public static volatile SingularAttribute<Product, Integer> productprice;
    public static volatile SingularAttribute<Product, String> producttype;

}