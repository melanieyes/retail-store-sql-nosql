package model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Customer {
    private ObjectId id;
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String address;
    private Date joinDate;

    // Default Constructor
    public Customer() {}

    // Parameterized Constructor
    public Customer(ObjectId id, String fName, String lName, String email, String phone, String address, Date joinDate) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = joinDate;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
