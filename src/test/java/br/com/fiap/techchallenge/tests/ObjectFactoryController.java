package br.com.fiap.techchallenge.tests;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ObjectFactoryController {

    public static String createAddress(MockMvc mockMvc, String nameAddress) throws Exception {
        String hashPostalCode = DigestUtils.md5Hex("meu-postalCode"+System.currentTimeMillis());
        String hashStreetName = DigestUtils.md5Hex("meu-Street"+System.currentTimeMillis());

        var jsonRequestAddress = """
                {
                  "postalCode": "01234-%s",
                  "streetName": "%s %s",
                  "streetNumber": "123",
                  "additionalInfo": "Sala 2",
                  "neighborhood": "Centro",
                  "city": "São Paulo",
                  "stateProvince": "SP",
                  "country": "Brazil"
                }
                """.formatted(hashPostalCode,nameAddress,hashStreetName);

        var rAddress = mockMvc.perform(post("/addresses")
                        .content(jsonRequestAddress)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        return rAddress.getContentAsString()
                .split(",")[0]
                .split(":")[1]
                .replaceAll("\"", "");
    }


    public static String createUser(MockMvc mockMvc, String codAddress, String login) throws Exception {
        String hash = DigestUtils.md5Hex("meu-texto"+System.currentTimeMillis());

        var jsonRequest = """
                {
                  "name": "Usuário Tech",
                  "userType": "OWNER",
                  "email": "user.tech@example.com",
                  "login": "%s.tech%s",
                  "password": "SenhaForte123!",
                  "addressId": "%s",
                  "addressType": "HOME",
                  "label": "casa"
                }
                """.formatted(login,hash,codAddress);

        var rUser = mockMvc.perform(
                        post("/users")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        return rUser.getContentAsString()
                .split(",")[0]
                .split(":")[1]
                .replaceAll("\"", "");


    }


    public static String createRestaurant(MockMvc mockMvc, String restaurantTName, String codAddreess, String codUser) throws Exception {
        String hash = DigestUtils.md5Hex("restaurant"+System.currentTimeMillis());

        var jsonRequest = """
                {
                  "name": "%s %s",
                  "addressId": "%s",
                  "cuisineType": "BRAZILIAN",
                  "openingHours": {
                    "opens": "08:00",
                    "closes": "22:00"
                  },
                  "userId": "%s",
                  "menu": []
                }
                """.formatted(restaurantTName,hash,codAddreess,codUser);



        var res = mockMvc.perform(
                        post("/restaurants")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        return res.getContentAsString()
                .split(",")[0]
                .split(":")[1]
                .replaceAll("\"", "");
    }

    public static String createMenuItem (MockMvc mockMvc, String codRestaurant) throws Exception {
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


        return res.getContentAsString()
                .split("\\{")[3]
                .split(",")[0]
                .split(":")[1]
                .replaceAll("\"", "");
    }

    public static String setAddressToUser (MockMvc mockMvc, String codUser,String codAddreess) throws Exception {
        var jsonRequest = """
                {
                  "userId": "%s",
                  "addressId": "%s",
                  "type": "HOME",
                  "label": "Casa",
                  "principal": true
                }
                """.formatted(codUser,codAddreess);

        var res = mockMvc.perform(
                        post("/user-addresses")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();


        return  res.getContentAsString()
                .split(",")[0]
                .split(":")[1]
                .replaceAll("\"", "");

    }
}
