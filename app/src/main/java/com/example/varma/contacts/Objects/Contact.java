package com.example.varma.contacts.Objects;


import com.example.varma.contacts.Extra.Utils;

public class Contact {
    private String contactName, contactNumber, contactId, contactFirstLetter;
    private int contactColor = 0;

    public Contact() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
        contactFirstLetter = Utils.getFirstLetter(contactName);

    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {

        contactNumber = contactNumber.replaceAll(" ", "");
        contactNumber = contactNumber.replaceAll("\\+91", "");


        this.contactNumber = contactNumber;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactFirstLetter() {
        return contactFirstLetter;
    }

    public int getContactColor() {
        return contactColor;
    }

    public void setContactColor(int contactColor) {
        this.contactColor = contactColor;
    }
}
