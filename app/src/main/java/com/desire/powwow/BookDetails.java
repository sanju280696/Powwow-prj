package com.desire.powwow;


public class BookDetails {
    private String mbookName;
    private String mbookOwner;
    private String mContactNo;
    private String mbranch;

    public BookDetails(String bookName, String bookOwner, String ContactNo, String branch) {
        mbookName = bookName;
        mbookOwner = bookOwner;
        mContactNo = ContactNo;
        mbranch = branch;
    }

    public String getBookName() {
        return mbookName;
    }

    public String getOwnerName() {
        return mbookOwner;
    }

    public String getBranch() {
        return mbranch;
    }

    public String getContact() {
        return mContactNo;
    }
}
