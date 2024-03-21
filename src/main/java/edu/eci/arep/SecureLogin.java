package edu.eci.arep;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

/**
 * Esta clase implementa un servicio de inicio de sesión que se conecta a través
 * de un servidor web. El servicio de inicio de sesión está diseñado para
 * gestionar las solicitudes de inicio de sesión a través del servidor web. La
 * configuración del servicio permite escuchar en un puerto específico, el cual
 * se determina a partir de la variable de entorno "PORT" si está definida; de
 * lo contrario, se utilizará el puerto especificado. Para garantizar la seguridad
 * de las comunicaciones, el servicio utiliza un certificado. Este servicio de 
 * inicio de sesión ofrece un único punto final en "/login", donde se pueden 
 * proporcionar dos parámetros: "name" (nombre) y "password" (contraseña).
 *
 * @author Daniel Fernando Moreno Cerón
 * @version 1.0 (21/03/2024)
 */
public class SecureLogin {

    /**
     * Método principal que inicia el servicio de inicio de sesión.
     *
     * @param args Argumentos de la línea de comandos.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de
     * seguridad.
     * @throws KeyStoreException Si ocurre un error al acceder al almacén de
     * claves.
     * @throws IOException Si ocurre un error al leer o escribir datos.
     * @throws KeyManagementException Si ocurre un error al gestionar claves.
     * @throws CertificateException Si ocurre un error al cargar certificados.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, CertificateException {
        staticFiles.location("/public");
        port(getPort());
        secure("certificados/ecikeystore.p12", "areplab7", null, null);
        setupTrustedSSL();
        get("/login", (req, res) -> {
            res.type("application/json");
            return accessURL("name=" + req.queryParams("name") + "&password=" + req.queryParams("password"));
        });
    }

    /**
     * Determina el puerto en el cual el servidor debe estar a la escucha. Si la
     * variable de entorno "PORT" está establecida, se extrae su valor y se
     * convierte a un entero. En caso contrario, se retorna el valor escrito.
     *
     * @return El puerto en el que el servidor debe escuchar.
     */
    public static int getPort() {
        System.out.println(System.getenv("PORT"));
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 38000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Configura el contexto SSL predeterminado con un almacén de claves de
     * confianza. Esta función crea un almacén de claves de confianza a partir
     * del archivo "certificados/myTrustStore.p12" utilizando la contraseña
     * "areplab7". Después, genera un contexto SSL utilizando este almacén de
     * claves de confianza y lo asigna como el contexto SSL predeterminado.
     *
     * @throws KeyStoreException Si ocurre un error al acceder al almacén de
     * claves.
     * @throws IOException Si ocurre un error al leer o escribir datos.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de
     * seguridad.
     * @throws KeyManagementException Si ocurre un error al gestionar claves.
     * @throws CertificateException Si ocurre un error al cargar certificados.
     */
    private static void setupTrustedSSL() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        File trustStoreFile = new File("certificados/myTrustStore.p12");
        char[] trustStorePassword = "areplab7".toCharArray();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        for (TrustManager t : tmf.getTrustManagers()) {
            System.out.println(t);
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
    }

    /**
     * Lee el contenido de una URL y retorna la respuesta como una cadena. Este
     * método crea una URL combinando la cadena de consulta provista con la URL
     * base estática. Después, establece una conexión con la URL y obtiene los
     * encabezados de la respuesta. Finalmente, lee el cuerpo de la respuesta y
     * la devuelve como una cadena.
     *
     * @param query La cadena de consulta para construir la URL.
     * @return La respuesta de la URL como una cadena.
     * @throws IOException Si ocurre un error al leer o escribir datos.
     */
    public static String accessURL(String query) throws IOException {
        URL siteURL = new URL("https://localhost:8088/user?" + query);
        URLConnection urlConnection = siteURL.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            if (headerName != null) {
                System.out.print(headerName + ":");
            }
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                System.out.print(value);
            }
            System.out.println("");
        }
        System.out.println("-------message-body------");
        StringBuffer response = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        System.out.println(response);
        System.out.println("GET DONE");
        return response.toString();
    }
}