package cz.uhk.fim.bs.pickngo_mobile_be.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public void addNewUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, String name, String email){
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalStateException("user with id "+ userId+ "doesnt exist"));
        if (name !=null && name.length() > 0 && !Objects.equals(user.getName(), name)){
            user.setName(name);
        }
        if (email !=null && email.length() > 0 && !Objects.equals(user.getEmail(), email)){
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
    }
}
