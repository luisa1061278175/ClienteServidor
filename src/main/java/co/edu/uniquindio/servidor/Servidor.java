package co.edu.uniquindio.servidor;


import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor en espera de conexión...");
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String mensaje;
            ModelFactoryServer md = new ModelFactoryServer();

            while (true) {
                // Mostrar menú
                out.println("Menu: 1. Cambiar Contraseña  2. Iniciar Sesión   3. Salir");

                // Leer la opción seleccionada por el cliente
                mensaje = in.readLine();
                System.out.println("Opción recibida: " + mensaje);

                // Opción 1: Cambiar contraseña
                if ("1".equals(mensaje)) {
                    out.println("Ingrese su cédula:");
                    String cedula = in.readLine();

                    out.println("Ingrese su nueva contraseña:");
                    String nuevaContrasena = in.readLine();

                    // Llamar al método para cambiar la contraseña
                    md.cambiarContrasena(cedula, nuevaContrasena);
                    out.println("Contraseña cambiada exitosamente.");

                } else if ("2".equals(mensaje)) {  // Opción 2: Iniciar sesión
                    out.println("Ingrese su usuario (cédula):");
                    String usuario = in.readLine();
                    System.out.println("Usuario recibido: " + usuario);

                    out.println("Ingrese su contraseña:");
                    String contrasena = in.readLine();
                    System.out.println("Contraseña recibida: " + contrasena);

                    boolean credencialesValidas = md.verificarCredenciales(usuario, contrasena);

                    if (credencialesValidas) {
                        out.println("Inicio de sesión exitoso");

                        // Mostrar menú de libros
                        md.mostrarMenuReservarLibro(out, in);
                    } else {
                        out.println("Credenciales inválidas.");
                    }

                } else if ("3".equals(mensaje)) {  // Opción 3: Salir
                    out.println("Saliendo del sistema...");
                    break; // Salir del ciclo
                } else {
                    out.println("Opción no válida.");
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

