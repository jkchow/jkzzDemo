package com.jkzz.lambda;


import java.util.ArrayList;
import java.util.List;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-28 17:59
 **/
public class ComparatorTest {
    public static void main(String[] args) {
        List<Person> personList = Person.createShortList();
    }



}


enum Gender {MALE, FEMALE}

class Person {
    private String givenName;
    private String surName;
    private int age;
    private Gender gender;
    private String eMail;
    private String phone;
    private String address;

    public static List<Person> createShortList() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Bob","Baker",21,Gender.MALE,"bob.baker@example.com","201-121-4678","44 4th St, Smallville, KS 12333"));
        people.add(new Person("Jane","Doe",25,Gender.FEMALE,"jane.doe@example.com","202-123-4678","33 3rd St, Smallville, KS 12333"));
        people.add(new Person("John","Doe",25,Gender.MALE,"john.doe@example.com","202-123-4678","33 3rd St, Smallville, KS 12333"));
        people.add(new Person("James","Johnson",45,Gender.MALE,"james.johnson@example.com","333-456-1233","201 2nd St, New York, NY 12111"));
        return people;
    }

    @Override
    public String toString() {
        return "Person{" +
                "givenName='" + givenName + '\'' +
                ", surName='" + surName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", eMail='" + eMail + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Person() {
    }

    public Person(String givenName, String surName, int age, Gender gender, String eMail, String phone, String address) {
        this.givenName = givenName;
        this.surName = surName;
        this.age = age;
        this.gender = gender;
        this.eMail = eMail;
        this.phone = phone;
        this.address = address;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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
    //通过静态内部类等方式实现的零件无序话构造  建造者模式
    public static class Builder {
        private int    age     = 0;
        private int    safeID  = 0;
        private String name    = null;
        private String address = null;



        public Builder(String name) {
            this.name = name;
        }

        public Builder age(int val) {
            age = val;
            return this;
        }

        public Builder safeID(int val) {
            safeID = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Person build() { // 构建，返回一个新对象
            return new Person(this);
        }
    }

    private Person(Builder b) {
        /*age = b.age;
        safeID = b.safeID;
        name = b.name;
        address = b.address;*/

    }

}