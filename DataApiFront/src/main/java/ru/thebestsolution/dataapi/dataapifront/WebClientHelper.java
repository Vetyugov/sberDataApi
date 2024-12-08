package ru.thebestsolution.dataapi.dataapifront;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import ru.thebestsolution.dataapi.dataapifront.api.GenerateModelDataJsonRequest;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class WebClientHelper {
    private final WebResource webResource;

    public WebClientHelper() {
        Client client = Client.create(new DefaultClientConfig());
        // Создаем экземпляр WebClient, указывая базовый URL (если нужно)
        this.webResource = client.resource("http://localhost:8080");
    }

    public String sendGetRequest(GenerateModelDataJsonRequest request) throws IOException {

        //писать результат сериализации будем во Writer(StringWriter)
        StringWriter writer = new StringWriter();
        //это объект Jackson, который выполняет сериализацию
        ObjectMapper mapper = new ObjectMapper();
        // сама сериализация: 1-куда, 2-что
        mapper.writeValue(writer, request);


        ClientResponse clientResponse = webResource
                .path("v1")
                .path("generate")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, writer.toString());

        return clientResponse.getEntity(String.class);
    }
}
