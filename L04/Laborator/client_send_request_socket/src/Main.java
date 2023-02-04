import java.io.IOException;

public class Main {
    public static String constructSoapMessage() {
        String soapBody = getSoapBody();

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

    public static String getSoapBody() {
        return "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
                " xmlns:service=\"services.db-auth.soap\">" +
                "<soap11env:Body><service:login><service:username>Tudisie</service:username>" +
                "<service:password>Parola123</service:password>" +
                "</service:login>" +
                "</soap11env:Body>" +
                "</soap11env:Envelope>" +
                "\r\n";
    }

    public static void main(String[] args) throws IOException {
        ClientSocket client = new ClientSocket();
        client.startConnection("127.0.0.1", 8000);
        String response = client.sendMessage(constructSoapMessage());
        System.out.println(response);
    }
}