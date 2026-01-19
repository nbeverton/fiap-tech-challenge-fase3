package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.tests.ObjectFactoryController;
import org.apache.commons.codec.digest.DigestUtils;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String codAddreess1,codAddreess2;
    private String codUser1,codUser2;
    private String codUserAddress;

    @BeforeEach
    public void setup() throws Exception {

        // Cria um endereço e um usuário para os testes
        codAddreess1 = ObjectFactoryController.createAddress(mockMvc,"Endereço teste 1");
        codUser1 = ObjectFactoryController.createUser(mockMvc,codAddreess1,"testuser 1");

        codAddreess2 = ObjectFactoryController.createAddress(mockMvc,"Endereço teste 2");
        codUser2 = ObjectFactoryController.createUser(mockMvc,codAddreess2,"testuser 2");
        codUserAddress = ObjectFactoryController.setAddressToUser(mockMvc, codUser2, codAddreess2);
    }


    @Test
    public void createUser() throws Exception {
        // Implement test logic here
        String hash = DigestUtils.md5Hex("meu-texto"+System.currentTimeMillis());
        var jsonRequest = """
                {
                  "name": "Usuário Tech",
                  "userType": "OWNER",
                  "email": "user.tech@example.com",
                  "login": "user.tech%s",
                  "password": "SenhaForte123!",
                  "addressId": "%s",
                  "addressType": "HOME",
                  "label": "casa"
                }
                """.formatted(hash,codAddreess1);

        var res = mockMvc.perform(
                post("/users")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(201,res.getStatus());

    }

    @Test
    void getAllUsers() throws Exception {
        var res = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        Assertions.assertEquals(200,res.getStatus());

    }

    @Test
    void getUserById() throws Exception {

        var res = mockMvc.perform(
                get("/users/{id}",codUser1)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn()
                .getResponse();
        Assertions.assertEquals(200,res.getStatus());
    }

    @Test
    void updateUser() throws Exception {

        String hash = DigestUtils.md5Hex("meu-texto"+System.currentTimeMillis());

        var jsonRequest = """
                {
                  "id": "%s",
                  "name": "Usuário Tech",
                  "userType": "OWNER",
                  "email": "user.tech@example.com",
                  "login": "user.tech%s",
                  "password": "SenhaForte123!",
                  "addressId": "%s",
                  "addressType": "HOME",
                  "label": "casa"
                }
                """.formatted(codUser1,hash,codAddreess1);

        var res = mockMvc.perform(
                        put("/users/{id}",codUser1)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());

    }

    @Test
    void deleteUser() throws Exception {
        var res = mockMvc.perform(
                        delete("/users/{id}",codUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();

        Assertions.assertEquals(204,res.getStatus());
    }

    @Test
    void createUserAddress() throws Exception {
        var jsonRequest = """
                {
                    "addressId": "%s",
                    "addressType": "HOME",
                    "label": "Minha casa"
                }
                """.formatted(codAddreess1);

        var res = mockMvc.perform(
                        post("/users/{userId}/addresses",codUser1)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(201,res.getStatus());


    }
    @Test
    void updateUserAddress() throws Exception {
        ObjectFactoryController.setAddressToUser(mockMvc,codUser2,codAddreess2);
        var jsonUpdateRequest = """
                {
                  "type": "OTHER",
                  "label": "Parque",
                  "principal": true
                }
                """;

        var updateRes = mockMvc.perform(
                        put("/users/{userId}/addresses/{userAddressId}",codUser2,codUserAddress)
                                .content(jsonUpdateRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,updateRes.getStatus());

    }



}
