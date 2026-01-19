package br.com.fiap.techchallenge.infra.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String codeAddress;

    @BeforeEach
    void setup() throws Exception {
        var jsonRequest = """
                {
                  "postalCode": "01234-567",
                  "streetName": "Rua A",
                  "streetNumber": "123",
                  "additionalInfo": "Sala 2",
                  "neighborhood": "Centro",
                  "city": "São Paulo",
                  "stateProvince": "SP",
                  "country": "Brazil"
                }
                """;
        var r = mockMvc.perform(post("/addresses")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        System.out.println(r.getContentAsString());

        codeAddress = r.getContentAsString()
                .split(",")[0]
                .split(":")[1]
                .replaceAll("\"", "");


    }


    @Test
    public void CreateAddress() throws Exception {
       var jsonRequest = """
                {
                  "postalCode": "01234-567",
                  "streetName": "Rua A",
                  "streetNumber": "123",
                  "additionalInfo": "Sala 2",
                  "neighborhood": "Centro",
                  "city": "São Paulo",
                  "stateProvince": "SP",
                  "country": "Brazil"
                }
                """;
        var res = mockMvc.perform(
                post("/addresses")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(201,res.getStatus());

    }

    @Test
    void getAllAddress() throws Exception{

        var res = mockMvc.perform(
                get("/addresses")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());

    }

    @Test
    void getAddressById() throws Exception{
       // var codAddress = "696832bfcab6dad436840fe3";
        var res = mockMvc.perform(
                get("/addresses/{id}",codeAddress)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());

    }

    @Test
    void deleteAddressById() throws Exception{
        //var codAddress = "6968354430aacc943f70e764";

        var res = mockMvc.perform(
                delete("/addresses/{id}",codeAddress)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(204,res.getStatus());

    }

    @Test
    void updateAddressById() throws Exception{
        //var codAddress = "69683e815444b4f05b21b205";

        var jsonRequest = """
                {
                  "postalCode": "98765-432",
                  "streetName": "Rua B",
                  "streetNumber": "456",
                  "additionalInfo": "Apto 3",
                  "neighborhood": "Bairro Novo",
                  "city": "Rio de Janeiro",
                  "stateProvince": "RJ",
                  "country": "Brazil"
                }
                """;

        var res = mockMvc.perform(
                put("/addresses/{id}",codeAddress)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());

    }

}
