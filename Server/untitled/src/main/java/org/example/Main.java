package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        int num = 30;
        byte b = (byte) (num & 0xFF);
        int num2 = b & 0xFF;
        System.out.println(num2);
    }
}