package project.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import project.model.Account;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-16T10:17:27")
@StaticMetamodel(Card.class)
public class Card_ { 

    public static volatile SingularAttribute<Card, String> cvc;
    public static volatile SingularAttribute<Card, Account> accountid;
    public static volatile SingularAttribute<Card, Integer> cardid;
    public static volatile SingularAttribute<Card, Integer> cardnumber;
    public static volatile SingularAttribute<Card, Date> exp;

}