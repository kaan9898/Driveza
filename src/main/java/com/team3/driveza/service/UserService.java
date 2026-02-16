package main.java.com.team3.driveza.service;

import com.team3.driveza.model.Users;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<Users> getAllUsers() {
        return new ArrayList<>();
    }

    public Users getUserById(Long id) {
        return new Users();
    }

    public Users createUser(Users user) {
        return user;
    }

    public Users updateUser(Long id, Users user) {
        return user;
    }

    public void deleteUser(Long id) {
    }
}