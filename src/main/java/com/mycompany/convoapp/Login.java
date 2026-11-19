/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.convoapp;

/**
 *
 * @author admin
 */
public class Login {
   
    
    public String name ;
    public String username;
    public String phonenumber;
    public String password;
   
   public Login (String name, String username,String phonenumber,String password){
       this.name=name;
       this.username=username;
       this.phonenumber=phonenumber;
       this.password=password;
   
   }

   
   public boolean checkname(){
       
       return name.length()>=5;
       
   }// always separate after a class
     public  boolean checkusername(){ 
         return username.contains("_")&& username.length()>=5;
         
           
       }// the regex expressions that show what is needed
     public boolean checkphonenumber(){
 
         return phonenumber!= null && phonenumber.matches("\\+27\\d{9}");
     
         
     }
     
  public boolean checkpassword() {
    // Requires: Min 8 characters, 1+ uppercase, 1+ lowercase, 1+ digit, 1+ special characteristic
    return password != null && password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");


}
     public boolean checkusername(String loginidentifier, String password) {
    // compares the entered username, phonenumber and password with the registered ones
    return (this.username.equals(loginidentifier) || this.phonenumber.equals(loginidentifier)) 
           && this.password.equals(password);
}
 
     
     }






