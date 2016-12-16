package com.aqib.www.ipms;

/**
 * Created by Admin on 12/2/2016.
 */
public class Test {

    public String test_id;

    public String testName;


    public String price;

    public String room;

    public Test() {

    }

    public Test(String test_id, String name, String price, String room) {
        this.test_id = test_id;
        this.testName = name;
        this.price = price;
        this.room = room;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}