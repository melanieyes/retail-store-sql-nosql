package model;

public class Supplier {
    private int suppID;
    private String suppName;
    private String contactP;
    private String phone;
    private String address;
    private String status;

    // Getters and Setters
    public int getSuppID() { return suppID; }
    public void setSuppID(int suppID) { this.suppID = suppID; }

    public String getSuppName() { return suppName; }
    public void setSuppName(String suppName) { this.suppName = suppName; }

    public String getContactP() { return contactP; }
    public void setContactP(String contactP) { this.contactP = contactP; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
