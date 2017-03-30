package com.example.varma.contacts;

/**
 * Created by Varma on 3/29/2017.
 */

public class Contact {
    private String contactName,contactNumber,contactId;


    Contact(){

    }

    Contact(String contactName,String contactNumber, String contactId){
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactId() {
        return contactId;
    }
}
