package com.coffee.back.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.coffee.back.commons.dto.UserDTO;
import com.coffee.back.commons.enums.UserType;
import com.coffee.back.commons.exception.BadRequestException;
import com.coffee.back.commons.exception.UserAuthenticationException;
import com.coffee.back.dao.UserDAO;
import com.coffee.back.dao.WorkerDAO;
import com.coffee.back.service.UserService;

/**
 * Clase {@code ProductService} encargada de ejecutar la logica de negocio.
 *
 * @author mont
 */
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private UserDAO userDAO;
    private WorkerDAO workerDAO;

    @Override
    public UserDTO iniciarSesion(UserDTO userDTO) throws UserAuthenticationException {
        logger.log(Level.INFO, "Service: Método iniciarSesion() se ha iniciado");

        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO es nullo");
        }

        UserDTO user = null;
        try {
            user = this.userDAO.getUserByNickName(userDTO.getUserName());
        } catch (BadRequestException ex) {
            throw new UserAuthenticationException("Usuario incorrecto, campos incorrectos");
        }
        
        System.out.println();
        System.out.println(userDTO.getPassword());
        System.out.println(user.getPassword());
        System.out.println();
        if (userDTO.getPassword().equals(user.getPassword())) {
            logger.log(Level.INFO, "Service: Usuario autenticado, recuperando datos");
            UserType workerRole = this.workerDAO.getRoleNameByWorkerId(user.getWorkerId());
            logger.log(Level.INFO, "Service: Usuario autenticado, datos recuperados");
            user.setUserType(workerRole);
            return user;
        }
        throw new UserAuthenticationException("El usuario " + user.getUserName() + " es incorrecto");
    }

    @Override
    public boolean cerrarSesion(UserDTO userDTO) {
        if (userDTO.getUserType().equals(UserType.UKNOWN)) {
            try {
                UserDTO userLogOut = this.userDAO.getUserByNickName(userDTO.getUserName());
                UserType usertypeLogOut = this.workerDAO.getRoleNameByWorkerId(userLogOut.getWorkerId());
                return usertypeLogOut == UserType.ADMINISTRADOR;
            } catch (BadRequestException ex) {
            }
        }
        return userDTO.getUserType() == UserType.ADMINISTRADOR;
    }

    @Override
    public String altaUsuario(UserDTO userDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String bajaUsuario(String nickName) {
        logger.log(Level.INFO, "UserServiceImpl: Iniciando método bajaUsuario()");
        boolean statusOperation = false;
        try {
            statusOperation = this.userDAO.delete(this.userDAO.getUserByNickName(nickName).getUserName());
        } catch (BadRequestException e) {
            return nickName + " actualmente no existe";
        }

        logger.log(Level.INFO, "UserServiceImpl: Finalizando método bajaUsuario()");
        return statusOperation ? "Usuario " + nickName + " ha sido eliminado exitosamente" : ""
                + "Usuario " + nickName + " actualmente no registrado";
    }

    @Override
    public String modificarUsuario(UserDTO userDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param userDAO the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * @param workerDAO the workerDAO to set
     */
    public void setWorkerDAO(WorkerDAO workerDAO) {
        this.workerDAO = workerDAO;
    }

}

