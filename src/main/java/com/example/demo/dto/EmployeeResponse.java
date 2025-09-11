package com.example.demo.dto;

public class EmployeeResponse {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private boolean active;

    public EmployeeResponse(Integer id, String name, Integer age, String gender, boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EmployeeResponse() {

    }
}
