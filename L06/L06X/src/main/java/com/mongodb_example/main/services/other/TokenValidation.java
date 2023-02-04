package com.mongodb_example.main.services.other;


import com.mongodb_example.main.exceptions.TokenExceptions.Unauthorized;

import java.util.List;
import java.util.Objects;

public class TokenValidation {
    public static Boolean validate(String token, String role){
        if(token == null || Objects.equals(token, "Bearer") || Objects.equals(token, "") || !token.contains(" "))
            throw new Unauthorized();
        token = token.split(" ", 2)[1];

        List<String> roles = null;
        try {
            roles = SOAPRequest.execute_get_roles(token);
        }catch(Exception e){
            System.out.println("SOAP Error catched");
        }
        assert roles != null;
        for(String r: roles){
            if(Objects.equals(r, role))
                return true;
        }
        return false;
    }
}