package dat.security.dao;

import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import io.javalin.validation.ValidationException;

/**
 * Purpose: To handle security with the database
 * Author: Thomas Hartmann
 */
public interface ISecurityDAO {
    UserDTO getVerifiedUser(String username, String password) throws ValidationException, dat.exceptions.ValidationException;
    User createUser(String username, String password);
}
