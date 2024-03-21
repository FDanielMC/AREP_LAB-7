package edu.eci.arep;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import static spark.Spark.*;

/**
 * Gestiona los usuarios y sus contraseñas almacenándolos como hashes en un mapa
 * estático. Ofrece métodos para añadir usuarios, verificar contraseñas y
 * obtener el puerto en el que el servidor debe escuchar.
 *
 * @author Daniel Fernando Moreno Cerón
 * @version 1.0 (20/03/2024)
 */
public class UserManager {

    private static final HashMap<String, byte[]> users = new HashMap<>();

    /**
     * Método principal que agrega usuarios y configura el servidor.
     *
     * @param args Argumentos de la línea de comandos.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de
     * seguridad.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        addUser("Alexis", "123456");
        addUser("Jefer", "654321");
        port(getPort());
        secure("certificados/ecikeystore.p12", "areplab7", null, null);
        get("/user", (req, res) -> {
            res.type("application/json");
            boolean result = verifyPassword(req.queryParams("name"), req.queryParams("password"));
            return "{\"result\":" + result + "}";
        });
    }

    /**
     * Encripta una contraseña utilizando el algoritmo SHA-256. Este método
     * inicializa un objeto MessageDigest con el algoritmo SHA-256 y lo emplea
     * para cifrar la contraseña proporcionada.
     *
     * @param password La contraseña a hash.
     * @return El hash de la contraseña.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de
     * seguridad.
     */
    public static byte[] digestPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        return md.digest(bytes);
    }

    /**
     * Comprueba si una contraseña coincide con el hash almacenado en el mapa de
     * usuarios. Este método obtiene el hash de la contraseña proporcionada y lo
     * compara con el hash almacenado en el mapa de usuarios.
     *
     * @param userName El nombre de usuario asociado con el hash de la
     * contraseña.
     * @param password La contraseña a verificar.
     * @return Verdadero si la contraseña coincide con el hash almacenado, falso
     * en caso contrario.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de
     * seguridad.
     */
    public static boolean verifyPassword(String userName, String password) throws NoSuchAlgorithmException {
        byte[] hash = users.get(userName);
        byte[] attemptedHash = digestPassword(password);
        return Arrays.equals(hash, attemptedHash);
    }

    /**
     * Obtiene el puerto en el cual el servidor debe estar a la escucha. Si la
     * variable de entorno "PORT" está establecida, se extrae su valor y se
     * convierte a un entero. En caso contrario, se retorna el puerto escrito.
     *
     * @return El puerto en el que el servidor debe escuchar.
     */
    public static int getPort() {
        System.out.println(System.getenv("PORT"));
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Agrega un usuario al mapa de usuarios con su contraseña hash. Este método
     * agrega un usuario al mapa de usuarios con su contraseña hash utilizando
     * el método hashPassword.
     *
     * @param name El nombre de usuario.
     * @param password La contraseña del usuario.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de
     * seguridad.
     */
    public static void addUser(String name, String password) throws NoSuchAlgorithmException {
        users.put(name, digestPassword(password));
    }
}
