package com.gobeyond.registration;

public class App {
    public static void main(String[] args) {
        RegistrationService service = new RegistrationService();
        String result = service.register("Faisal", "faisal@gmail.com");
        System.out.println(result);
    }
}