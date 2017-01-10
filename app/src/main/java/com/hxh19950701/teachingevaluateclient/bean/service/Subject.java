package com.hxh19950701.teachingevaluateclient.bean.service;

import java.util.List;

public class Subject extends TimeMakableRecord {

    private Department department;
    private String name;
    private List<Clazz> classes;

    public Subject() {
    }

    public Subject(Department department, String name) {
        this.department = department;
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(List<Clazz> classes) {
        this.classes = classes;
    }
}
