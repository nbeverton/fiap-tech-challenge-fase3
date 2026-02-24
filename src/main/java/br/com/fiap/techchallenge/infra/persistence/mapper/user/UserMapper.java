package br.com.fiap.techchallenge.infra.persistence.mapper.user;

import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.model.Admin;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.Owner;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.infra.persistence.documents.UserDocument;

public class UserMapper {

    private UserMapper(){}; // prevents instantiating an incorrect domain class


    public static UserDocument toDocument(User user){

        UserDocument document = new UserDocument();

        document.setId(user.getId());
        document.setName(user.getName());
        document.setEmail(user.getEmail());
        document.setLogin(user.getLogin());
        document.setPassword(user.getPassword());
        document.setUserType(user.getUserType().name());

        return document;
    }


    public static User toDomain(UserDocument document) {

        UserType type = UserType.valueOf(document.getUserType());

        return switch (type){
            case OWNER -> new Owner(
                    document.getId(),
                    document.getName(),
                    document.getEmail(),
                    document.getLogin(),
                    document.getPassword()
            );
            case CLIENT -> new Client(
                    document.getId(),
                    document.getName(),
                    document.getEmail(),
                    document.getLogin(),
                    document.getPassword()
            );
            case ADMIN -> new Admin(
                    document.getId(),
                    document.getName(),
                    document.getEmail(),
                    document.getLogin(),
                    document.getPassword()
            );
        };
    }

    public static void updateDocument(User user, UserDocument document) {
        document.setName(user.getName());
        document.setUserType(user.getUserType().name());
        document.setEmail(user.getEmail());
        document.setLogin(user.getLogin());
        document.setPassword(user.getPassword());
    }
}
