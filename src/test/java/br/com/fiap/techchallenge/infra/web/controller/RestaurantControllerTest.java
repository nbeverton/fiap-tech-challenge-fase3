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
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String codAddreess1,codAddreess2;
    private String codUser1,codUser2;
    private String codRestaurant;
    private String codMenuItem;

    @BeforeEach
    public void setup() throws Exception {
        //Variables to be able to create a restaurant
        codAddreess1 = ObjectFactoryController.createAddress(mockMvc, "Address Restaurant 1");
        codUser1 = ObjectFactoryController.createUser(mockMvc, codAddreess1, "ownerRestaurant1");

        //Created context to another tests
        codAddreess2 = ObjectFactoryController.createAddress(mockMvc, "Address Restaurant 2");
        codUser2 = ObjectFactoryController.createUser(mockMvc, codAddreess2, "ownerRestaurant2");
        codRestaurant = ObjectFactoryController.createRestaurant(mockMvc, "Restaurant Context", codAddreess2, codUser2);
        codMenuItem = ObjectFactoryController.createMenuItem(mockMvc, codRestaurant);

    }

    @Test
    void createRestaurantTest() throws Exception {
        String hash = DigestUtils.md5Hex("restaurant"+System.currentTimeMillis());

        var jsonRequest = """
                {
                  "name": "Restaurante Exemplo %s",
                  "addressId": "%s",
                  "cuisineType": "BRAZILIAN",
                  "openingHours": {
                    "opens": "08:00",
                    "closes": "22:00"
                  },
                  "userId": "%s",
                  "menu": []
                }
                """.formatted(hash,codAddreess1,codUser1);



        var res = mockMvc.perform(
                        post("/restaurants")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        System.out.println(res.getContentAsString());
        Assertions.assertEquals(201,res.getStatus());
    }

    @Test
    void getAllRestaurantsTest() throws Exception {
        var res = mockMvc.perform(
                        get("/restaurants")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());
    }

    @Test
    void getRestaurantByIdTest() throws Exception {
        var res = mockMvc.perform(
                        get("/restaurants/{id}",codRestaurant)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());
    }

    @Test
    void updateRestaurantTest() throws Exception {
        String hash = DigestUtils.md5Hex("restaurant"+System.currentTimeMillis());
        var jsonRequest = """
                {
                  "name": "Restaurante Atualizado %s",
                  "addressId": "%s",
                  "cuisineType": "ITALIAN",
                  "openingHours": {
                    "opens": "10:00",
                    "closes": "23:00"
                  },
                  "userId": "%s",
                  "menu": []
                }
                """.formatted(hash,codAddreess2,codUser2);

        var res = mockMvc.perform(
                        put("/restaurants/{id}",codRestaurant)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200,res.getStatus());
    }

    @Test
    void deleteRestaurantTest() throws Exception {
        var res = mockMvc.perform(
                        delete("/restaurants/{id}",codRestaurant)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(204,res.getStatus());
    }

    @Test
    void createMenuToRestaurantTest() throws Exception {
        var jsonRequest = """
                {
                   "name": "Prato do dia - Segunda feira",
                   "description": "Arroz, feijão, bife e salada",
                   "price": 39.90,
                   "dineInAvailable": true,
                   "imageUrl": "https://example.com/pf.jpg"
                 }
                """;
        var res = mockMvc.perform(
                        post("/restaurants/{restaurantId}/menus", codRestaurant)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(201, res.getStatus());
        System.out.println(res.getContentAsString());
    }

    @Test
    void getMenuToRestaurantTest() throws Exception {
        var res = mockMvc.perform(
                        get("/restaurants/{restaurantId}/menus", codRestaurant)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200, res.getStatus());
    }

    @Test
    void updateMenuToRestaurantTest() throws Exception {
        var jsonRequest = """
                {
                  "name": "Prato Feito - Segunda feira",
                  "description": "Arroz, feijão, bife e salada",
                  "price": 38.90,
                  "dineInAvailable": true,
                  "imageUrl": "https://example.com/pf.jpg"
                }
                """;
        var res = mockMvc.perform(
                        put("/restaurants/{restaurantId}/menus/{menuId}", codRestaurant, codMenuItem)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200, res.getStatus());
    }

    @Test
    void deleteMenuToRestaurantTest() throws Exception {
        var res = mockMvc.perform(
                        delete("/restaurants/{restaurantId}/menus/{menuId}", codRestaurant, codMenuItem)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(200, res.getStatus());
    }


}
