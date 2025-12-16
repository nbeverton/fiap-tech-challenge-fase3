package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.enums.UserType;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Client;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Owner;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.documents.UserDocument;

public class UserMapper {

    private UserMapper(){}; // prevents instantiating an incorrect domain class


    public static UserDocument toDocument(User user){

        UserDocument document = new UserDocument();

        document.setId(user.getId());
        document.setName(user.getName());
        document.setEmail(user.getEmail());
        document.setLogin(user.getLogin());
        document.setPassword(user.getPassword());
        document.setUserType(user.getUserType());

        return document;
    }


    public static User toDomain(UserDocument document){

        if(document.getUserType() == UserType.OWNER){

            return new Owner(
                    document.getName(),
                    document.getEmail(),
                    document.getLogin(),
                    document.getPassword()
            );
        }

        if(document.getUserType() == UserType.CLIENT){

            return new Client(
                    document.getName(),
                    document.getEmail(),
                    document.getLogin(),
                    document.getPassword()
            );
        }

        // If a document comes with a null, invalid, or unexpected user type
        throw new IllegalArgumentException(
                "Unsupported user type> " + document.getUserType()
        );

    }
}
