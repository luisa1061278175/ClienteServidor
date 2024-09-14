package co.edu.uniquindio.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private ServerSocket serverSocket;

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en el puerto: " + puerto);
    }

    public void iniciarServidor() {
        while (true) {
            try (Socket cliente = serverSocket.accept()) {
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

                manejarCliente(in, out);
            } catch (IOException e) {
                System.out.println("Error manejando el cliente: " + e.getMessage());
            }
        }
    }

    private void manejarCliente(BufferedReader in, PrintWriter out) {
        try {
            out.println("Menu: 1. Cambiar Contraseña  2. Iniciar Sesión   3. Salir");
            String opcion = in.readLine();
            System.out.println("Opción recibida del cliente: " + opcion);

            if ("2".equals(opcion)) {
                iniciarSesion(in, out);
            } else if ("1".equals(opcion)) {
                cambiarContrasena(in, out);
            } else if ("3".equals(opcion)) {
                out.println("Saliendo...");
            } else {
                out.println("Opción inválida");
            }
        } catch (IOException e) {
            System.out.println("Error durante la comunicación con el cliente: " + e.getMessage());
        }
    }

    private void iniciarSesion(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Ingrese su usuario (cédula):");
        String usuario = in.readLine();
        System.out.println("Usuario recibido: " + usuario);

        out.println("Ingrese su contraseña:");
        String contrasena = in.readLine();
        System.out.println("Contraseña recibida: " + contrasena);

        if (verificarCredenciales(usuario, contrasena)) {
            out.println("Inicio de sesión exitoso");
        } else {
            out.println("Credenciales inválidas.");
        }
    }

    private void cambiarContrasena(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Ingrese su usuario (cédula):");
        String usuario = in.readLine();
        System.out.println("Usuario recibido: " + usuario);

        out.println("Ingrese su contraseña actual:");
        String contrasenaActual = in.readLine();
        System.out.println("Contraseña actual recibida: " + contrasenaActual);

        if (verificarCredenciales(usuario, contrasenaActual)) {
            out.println("Ingrese su nueva contraseña:");
            String nuevaContrasena = in.readLine();
            System.out.println("Nueva contraseña recibida: " + nuevaContrasena);

            actualizarContrasena(usuario, nuevaContrasena);
            out.println("Contraseña actualizada correctamente.");
        } else {
            out.println("Credenciales inválidas.");
        }
    }

    private boolean verificarCredenciales(String usuario, String contrasena) {
        // Simular verificación de credenciales
        return "1".equals(usuario) && "1".equals(contrasena);
    }

    private void actualizarContrasena(String usuario, String nuevaContrasena) {
        // Simular actualización de contraseña
        System.out.println("Contraseña actualizada para el usuario " + usuario);
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor(12345);
            servidor.iniciarServidor();
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
