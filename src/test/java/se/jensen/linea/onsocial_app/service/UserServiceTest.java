package se.jensen.linea.onsocial_app.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.jensen.linea.onsocial_app.DTO.UserResponseDTO;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;


    @Test
    public void testGetAllUsers() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.setAlias("alias");
        //Act

        User user2 = new User();
        user2.setId(2L);
        user2.setAlias("alias2");
        List<User> users = List.of(user, user2);

//        UserResponseDTO dto1 = UserResponceDtoBuilder.builder().
//                withId(1L).
//                withAlias("alias").
//                build();
//
//
//        UserResponseDTO dto2 = UserResponceDtoBuilder.builder().
//                withId(2L).
//                withAlias("alias2").
//                build();


        when(userRepository.findAll()).thenReturn(users);


        //Act

        List<UserResponseDTO> result = userService.getAllUsers();

        //Assert
        assertEquals(2, result.size());

    }

    @Test
    public void testGetUserByIdOrThrow() {

        User user = new User();
        user.setId(1L);
        user.setAlias("alias");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //Act
        UserResponseDTO result = userService.getUserByIdOrThrow(1L);

        //Assert
        assertEquals(1, result.id(), result.alias());

    }

    @Test
    public void testDeleteUser() {

        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // Act

        boolean result = userService.deleteUser(userId);

        // Assert
        assertTrue(result);
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);

    }


}
