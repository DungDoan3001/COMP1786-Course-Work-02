package com.dungdoan.contactappcw;

public class ContactConstants {
    public static final String TABLE_NAME = "contact_table";
    public static final String CONTACT_ID = "contact_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_PHONE_NUMBER = "contact_phone_number";
    public static final String CONTACT_ADDRESS = "contact_address";
    public static final String CONTACT_EMAIL = "contact_email";
    public static final String CONTACT_IMAGE = "contact_image";
    public static final String CREATE_TABLE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_NAME, CONTACT_ID, CONTACT_NAME, CONTACT_PHONE_NUMBER, CONTACT_ADDRESS, CONTACT_EMAIL, CONTACT_IMAGE
    );
    public static final String DROP_TABLE_QUERY = String.format(
            "DROP TABLE IF EXISTS %s", TABLE_NAME
    );
}
