package se.jensen.linea.onsocial_app.mapper;

import org.springframework.stereotype.Component;
import se.jensen.linea.onsocial_app.DTO.UserRequestDTO;
import se.jensen.linea.onsocial_app.DTO.UserResponseDTO;
import se.jensen.linea.onsocial_app.model.User;

@Component
public class UserMapper {

    public User fromDto(UserRequestDTO userRequestDTO) {
        User user = new User();
        valuesToUser(user, userRequestDTO);
        return user;
    }

    public static User valuesToUser(User user, UserRequestDTO userRequestDTO) {
        user.setAlias(userRequestDTO.alias());
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());
        user.setEmail(userRequestDTO.email());
        user.setPassword(userRequestDTO.password());
        user.setProfilePicture(userRequestDTO.profilePicture());
        return user;
    }

    public UserResponseDTO userToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getAlias(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePicture(),
                user.getRole()
        );
    }
}
