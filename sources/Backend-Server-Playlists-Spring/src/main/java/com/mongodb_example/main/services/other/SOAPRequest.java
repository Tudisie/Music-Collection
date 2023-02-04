package com.mongodb_example.main.services.other;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SOAPRequest {

    public static String constructSoapMessage(String jws_obj) {
        String soapBody = getSoapBody(jws_obj);

        StringBuilder soapMessage = new StringBuilder();
        soapMessage.append("POST / HTTP/1.1\r\n");
        soapMessage.append("Host: 127.0.0.1\r\n");
        soapMessage.append("Connection: close\r\n"
        );
        soapMessage.append("Content-Type: text/xml\r\n");
        soapMessage.append("Content-Length: " + soapBody.length() + "\r\n");
        soapMessage.append("\r\n");
        soapMessage.append(soapBody);

        return soapMessage.toString();
    }

    public static String getSoapBody(String jws_obj) {
        return "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
                " xmlns:service=\"services.db-auth.soap\">" +
                "<soap11env:Body><service:get_roles><service:jws_obj>"+jws_obj+"</service:jws_obj>" +
                "</service:get_roles>" +
                "</soap11env:Body>" +
                "</soap11env:Envelope>" +
                "\r\n";
    }

    public static List<String> execute_get_roles(String jws_obj) throws IOException {
        ClientSocket client = new ClientSocket();
        client.startConnection("127.0.0.1", 8000);
        String response = client.sendMessage(constructSoapMessage(jws_obj));

        String[] items = response.split("<tns:get_rolesResult>");
        List<String> roles = new ArrayList<>();
        for(int i = 1; i < items.length; ++i)
            roles.add(items[i].split("</tns:get_rolesResult>")[0]);
        return roles;
    }
}
