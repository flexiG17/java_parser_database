package com.company.People;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Person{
    private int vkId;
    private final String name;
    private final String surname;
    private String city;
    private String birthdate;
    private Gender gender;
    private String photo;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.city = "None";
        this.birthdate = null;
        this.photo = "None";
        this.vkId = -1000;
        gender = Gender.None;
    }

    public String getBirthdate() { return birthdate; }

    public String getCity() { return city; }

    public String getSurname() { return surname; }

    public String getName() { return name; }

    public int getVkId() { return vkId;}

    public void setVkId(int vkId) { this.vkId = vkId; }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }

    public void setCity(String city) { this.city = city; }

    public Gender getGender() { return gender; }

    public void setGender(Gender gender) { this.gender = gender; }

    public void setBirthdate(String birthdate) {
        var regex = new SimpleDateFormat("MM.dd.yyyy");
        try {
            var date = regex.parse(birthdate);
            regex.applyPattern("yyyy-MM-dd");
            this.birthdate = regex.format(date);
        } catch (ParseException e) {
            this.birthdate = null;
        }
    }
}
