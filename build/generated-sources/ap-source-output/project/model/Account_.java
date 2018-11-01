package project.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import project.model.Card;
import project.model.Orderlist;
import project.model.Profile;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-10-25T22:28:10")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, String> activatecode;
    public static volatile ListAttribute<Account, Card> cardList;
    public static volatile SingularAttribute<Account, Integer> accountid;
    public static volatile SingularAttribute<Account, String> password;
    public static volatile SingularAttribute<Account, Date> activationdate;
    public static volatile ListAttribute<Account, Profile> profileList;
    public static volatile SingularAttribute<Account, String> email;
    public static volatile ListAttribute<Account, Orderlist> orderlistList;

}