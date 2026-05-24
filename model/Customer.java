/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class Customer {
    private int id;
    private String nama;
    private String nohp;
    private String email;

    public Customer() {
    }

    public Customer(int id, String nama, String nohp, String email) {
        this.id = id;
        this.nama = nama;
        this.nohp = nohp;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return nama;
    }

    public void setName(String nama) {
        this.nama = nama;
    }

    public String getPhoneNumber() {
        return nohp;
    }

    public void setPhoneNumber(String nohp) {
        this.nohp = nohp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getid() {
        return getId();
    }

    public void setid(int id) {
        setId(id);
    }

    public String getnama() {
        return getName();
    }

    public void setnama(String nama) {
        setName(nama);
    }

    public String getnohp() {
        return getPhoneNumber();
    }

    public void setnohp(String nohp) {
        setPhoneNumber(nohp);
    }

    public String getemail() {
        return getEmail();
    }

    public void setemail(String email) {
        setEmail(email);
    }
}
