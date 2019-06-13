package edu.roomplanner.builders;

import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

public class PersonEntityBuilder {

    private Long id;
    private String email;
    private String password;
    private UserType type;
    private String firstName;
    private String lastName;

    public PersonEntityBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public PersonEntityBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public PersonEntityBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public PersonEntityBuilder withType(UserType type){
        this.type = type;
        return this;
    }

    public PersonEntityBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }
    public PersonEntityBuilder withLastName(String lastName){
        this.lastName = lastName;
        return this;
    }

    public UserEntity build(){
        UserEntity userEntity = new PersonEntity();
        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setType(type);
        userEntity.setId(id);
        ((PersonEntity) userEntity).setFirstName(firstName);
        ((PersonEntity) userEntity).setLastName(lastName);

        return userEntity;
    }
}
