package com.example.kursach.repo;



import com.example.kursach.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Integer>
{
    User findByLogin(String login);

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    User findByEmail(String email);
    User findByLoginAndEmail(String login, String email);
    public User findByResetPasswordToken(String token);

}
