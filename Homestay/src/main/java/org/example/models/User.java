package org.example.models;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable,Cloneable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String username;
    private String password;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String role = ERole.CLIENT.getName();
    private String clientIDUser;
    private String status;
    private Bank bank ;


    public User(int id, String name, String username, String password, String gender, String phone, String email, String address, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
    }
    public User(int id, String name, String username, String password, String gender, String phone, String email, String address, String role,Bank bank) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
