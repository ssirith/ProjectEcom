package project.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import project.model.Orderdetail;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-16T10:17:27")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile SingularAttribute<Product, String> productcode;
    public static volatile ListAttribute<Product, Orderdetail> orderdetailList;
    public static volatile SingularAttribute<Product, String> productname;
    public static volatile SingularAttribute<Product, Integer> productprice;
    public static volatile SingularAttribute<Product, String> producttype;

}