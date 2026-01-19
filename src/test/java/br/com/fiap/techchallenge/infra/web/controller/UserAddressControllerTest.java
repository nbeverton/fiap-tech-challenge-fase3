package br.com.fiap.techchallenge.infra.web.controller;


import br.com.fiap.techchallenge.tests.ObjectFactoryController;
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
public class UserAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String codAddreess1,codUser1,codUserAddress;

    @BeforeEach
    public void setup() throws Exception {
        codAddreess1 = ObjectFactoryController.createAddress(mockMvc, "Address Restaurant 1");
        codUser1 = ObjectFactoryController.createUser(mockMvc, codAddreess1, "ownerRestaurant1");
        codUserAddress = ObjectFactoryController.setAddressToUser(mockMvc, codUser1, codAddreess1);
    }

    @Test
    void setAdressToUser() throws Exception {
        var jsonRequest = """
                {
                  "userId": "%s",
                  "addressId": "%s",
                  "type": "HOME",
                  "label": "Casa",
                  "principal": true
                }
                """.formatted(codUser1,codAddreess1);

        var res = mockMvc.perform(
                        post("/user-addresses")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();


        Assertions.assertEquals(201,res.getStatus());
    }

    @Test
    void getUserAddresses() throws Exception {
        var res = mockMvc.perform(
                        get("/user-addresses")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());
    }


    @Test void updateUserAddress() throws Exception {
        var jsonRequest = """
                {
                  "type": "OTHER",
                  "label": "Parque",
                  "principal": true
                }
                
                """;

        var res = mockMvc.perform(
                        put("/user-addresses/{id}",codUserAddress)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());
    }
    @Test
    void deleteUserAddress() throws Exception {

    }
}
