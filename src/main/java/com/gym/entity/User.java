package com.gym.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
//    @Column(name="user_id")
    private Long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="user_name")
    private String userName;
    @Column(name="password")
    private String password;
    @Column(name="is_active")
    private boolean isActive;

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
