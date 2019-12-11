package provider;

import java.io.IOException;

public class ProviderHandler {

    public String handle(String input) throws IOException {
        if (input.startsWith("0")) {
            return "20020191212";
        } else {
            return "40420191212";
        }
    }
}
