package project.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import project.model.Account;
import project.model.Orderdetail;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-16T10:17:27")
@StaticMetamodel(Orderlist.class)
public class Orderlist_ { 

    public static volatile SingularAttribute<Orderlist, Account> accountid;
    public static volatile SingularAttribute<Orderlist, Integer> ordernumber;
    public static volatile SingularAttribute<Orderlist, Date> orderdate;
    public static volatile SingularAttribute<Orderlist, Orderdetail> orderdetail;

}