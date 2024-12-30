package edu.upc.prop.clusterxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upc.prop.clusterxx.controller.CtrlPersistence;
/**
 * Clase que representa a los usuarios del sistema.
 * Esta clase se encarga de gestionar los usuarios, permitiendo crear, eliminar y loguear usuarios.
 * Además, permite obtener información de los usuarios como la ruta de datos asociada en presistencia, en este caso, carpetas
 * Es importante recalcar lo siguiente:
 * - La clase Users no tiene ninguna dependencia con la clase CtrlPersistence, por lo que no se puede acceder al sistema de persistencia
 * Por esta razón, tiene que ser la clase que utiliza la clase Users de contemplar la consistencia de los datos entre el sistema
 * de persistencia del usuario y la clase Users.
 *
 * Por esta razón, si usted implementa un sistema de persistencia,
 * Debe seguir esta receta de programación:
 * 1. Comprobar que el usuario que existe antes de crearlo/ no existe al eliminarlo con los métodos alreadyExistsUser y doesNotExistUser
 * 2. En la clase donde se utiliza la clase Users, cree/elimine el usuario en el sistema de persistencia
 * 3. En la clase donde se utiliza la clase Users, cree/elimine el usuario en la clase Users con los métodos createUser y deleteUser
 *
 * Si no se sigue esta receta, la clase Users no podrá garantizar la consistencia de los datos entre el sistema de persistencia y la clase Users.
 *
 * Por otro lado, si no se implementa un sistema de persistencia, la clase Users se encargará de gestionar los usuarios en memoria.
 * Con los métodos createUser y deleteUser, se añadirán y eliminarán usuarios de la base de datos en memoria.
 */
public class Users {

    /**
     * Clase interna que representa a un usuario con su nombre y ruta de carpeta asociada.
     */
    private static class User {
        /**
         * Nombre de usuario.
         */
        String username;

        /**
         * Ruta de la carpeta del usuario.
         */
        String folderPath;

        /**
         * Constructor de la clase User.
         *
         * @param username Nombre de usuario.
         * @param folderPath Ruta de la carpeta del usuario.
         */
        public User(String username, String folderPath) {
            this.username = username;
            this.folderPath = folderPath;
        }
    }

    /**
     * Usuario que está Log-In en el sistema
     */
    String LogInUser;

    /**
     * Base de datos en memoria que almacena los usuarios registrados.
     */
    private final HashMap<String, Users.User> userDatabase = new HashMap<>();

    /**
     * Constructor que inicializa los usuarios desde una lista.
     *
     * @param users Lista de objetos Object[] con información de los usuarios.
     * @throws Exception Si hay un error al cargar los usuarios.
     */
    public Users(List<Object[]> users) throws Exception {

        loadUsers(users);
    }

    /**
     * Carga los usuarios desde una lista de datos.
     *
     * @param users Lista de objetos Object[] con información de los usuarios.
     */
    private void loadUsers(List<Object[]> users) {
        for (Object[] userData : users) {
            String username = (String) userData[0];
            String folderPath = (String) userData[1];
            userDatabase.put(username, new User(username, folderPath));
        }
    }

    /**
     * Crea un nuevo usuario y lo añade a la base de datos.
     *
     * @param folderPath Ruta de la carpeta del usuario.
     * @param username Nombre de usuario.
     */
    public void createUser(String username, String folderPath) {

        // This is optional when the class that uses the class Users has a persistence system
        // because needs to check before creating with alreadyExistsUser method.
        // if not persistence system is used, then this is necessary.
        // for that reason, this is commented.
        if (userDatabase.containsKey(username)) {
            throw new IllegalArgumentException("The user already exists.");
        }

        // ADD IT TO THE RUNNING SYSTEM
        userDatabase.put(username, new User(username, folderPath));

    }

    /**
     * Verifica si un usuario ya existe en el sistema.
     *
     * @param username Nombre de usuario.
     */
    public void alreadyExistsUser(String username) {
        if (userDatabase.containsKey(username)) {
            throw new IllegalArgumentException("The user already exists.");
        }
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param username Nombre de usuario.
     */
    public void deleteUser(String username) {

        // This is optional when the class that uses the class Users has a persistence system
        // because needs to check before deleting user if it doesn't exist with doesNotExistUser method.
        // if not persistence system is used, then this is necessary.
        // for that reason, this is commented.
        if (!userDatabase.containsKey(username)) {
            throw new IllegalArgumentException("The user doesn't exists.");
        }
        // DELETE IT FROM RUNNING SYSTEM
        userDatabase.remove(username);

    }

    /**
     * Verifica si un usuario no existe en el sistema.
     *
     * @param username Nombre de usuario.
     */
    public void doesNotExistUser(String username) {
        if (!userDatabase.containsKey(username)) {
            throw new IllegalArgumentException("The user doesn't exists.");
        }
    }

    /**
     * Simula un inicio de sesión para un usuario existente.
     *
     * @param username Nombre de usuario.
     */
    public void logIn(String username) {

        if (!userDatabase.containsKey(username)) {
            throw new IllegalArgumentException("The user doesn't exists.");
        }

        LogInUser = username;

    }

    /**
     * Obtiene el usuario que está logueado en el sistema.
     *
     * @return Nombre de usuario logueado.
     */
    public String getLogInUser() {
        return LogInUser;
    }

    /**
     * Obtiene la ruta asociada a un usuario.
     *
     * @return Ruta de la carpeta del usuario.
     */
    public String getRouteLoggedIn() {

        if(LogInUser == null) {
            throw new IllegalArgumentException("There is no user log in");
        }

        return userDatabase.get(LogInUser).folderPath;
    }

    /**
     * Devuelve una lista de nombres de usuario como cadenas de texto.
     *
     * @return Lista de nombres de usuario.
     */
    public List<String> usersAsString() {
        return new ArrayList<>(userDatabase.keySet());
    }


    /**
     * Devuelve una lista de strings que contiene el username y folderPath de cada usuario.
     *
     * @return Lista de strings con el formato "[username],[folderPath]".
     */
    public List<String> UserAndFolderPathAsString() {
        List<String> userAndFolderList = new ArrayList<>();

        for (User user : userDatabase.values()) {
            String userInfo = user.username + "," + user.folderPath;
            userAndFolderList.add(userInfo);
        }

        return userAndFolderList;
    }


}

