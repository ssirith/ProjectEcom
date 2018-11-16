package project.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import project.model.Account;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-11-16T10:17:26")
@StaticMetamodel(Profile.class)
public class Profile_ { 

    public static volatile SingularAttribute<Profile, String> fname;
    public static volatile SingularAttribute<Profile, Account> accountid;
    public static volatile SingularAttribute<Profile, String> lname;
    public static volatile SingularAttribute<Profile, String> address;
    public static volatile SingularAttribute<Profile, Integer> profileid;
    public static volatile SingularAttribute<Profile, String> tel;

}