package com.example;

import java.util.Objects;

/**
 * Abstract base class representing a human entity within the academic institution.
 * Enforces fundamental identity attributes, data encapsulation, and polymorphic role definitions.
 */
public abstract class Person {
    private int id;
    private String name;
    private String email;
    private String phone;

    public Person() {
    }

    public Person(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Polymorphic method to obtain the institutional classification of the person.
     */
    public abstract String getRoleDescription();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
