/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.convoapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author admin
 */
public class LoginTest {

    @Test
    public void testCheckname() {
        System.out.println("checkname");
        Login instance = new Login("name", "username", "phonenumber", "password");
        boolean expResult = false;
        boolean result = instance.checkname();
        assertEquals(expResult, result);

    }

    @Test
    public void testCheckusername_0args() {
        System.out.println("checkusername");
        Login instance = new Login("name", "username", "phonenumber", "password");
        boolean expResult = false;
        boolean result = instance.checkusername();
        assertEquals(expResult, result);

    }

    @Test
    public void testCheckphonenumber() {
        System.out.println("checkphonenumber");
        Login instance = new Login("name", "username", "phonenumber", "password");
        boolean expResult = false;
        boolean result = instance.checkphonenumber();
        assertEquals(expResult, result);

    }

    @Test
    public void testCheckpassword() {
        System.out.println("checkpassword");
        Login instance = new Login("name", "username", "phonenumber", "password");
        boolean expResult = false;
        boolean result = instance.checkpassword();
        assertEquals(expResult, result);

    }

    @Test
    public void testCheckusername_String_String() {
        System.out.println("checkusername");
        String loginidentifier = "";
        String password = "";
        Login instance = new Login("name", "username", "phonenumber", "password");
        boolean expResult = false;
        boolean result = instance.checkusername();
        assertEquals(expResult, result);

    }

}
