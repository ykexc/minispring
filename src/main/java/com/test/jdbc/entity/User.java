package com.test.jdbc.entity;

import java.util.Date;

/**
 * @author mqz
 */
public class User {

    public String name;

    public Integer id;

    public Date birthday;

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
               "name='" + name + '\'' +
               ", id=" + id +
               ", birthday=" + birthday +
               '}';
    }
}
