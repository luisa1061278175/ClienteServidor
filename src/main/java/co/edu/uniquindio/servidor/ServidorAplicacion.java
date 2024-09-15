package co.edu.uniquindio.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorAplicacion {
    private ServerSocket serverSocket;
    private ServidorLogica logica;

    public ServidorAplicacion(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        logica = new ServidorLogica(); // Instancia de la lógica del servidor
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
        boolean salir = false;
        try {
            while (!salir) {
                // Esperar y enviar el menú
                logica.mostrarMenuPrincipal(in, out);

                // Leer la opción seleccionada por el cliente
                String opcion = in.readLine();
                System.out.println("Opción recibida del cliente: " + opcion);

                switch (opcion) {
                    case "1":
                        // Cambiar contraseña
                        logica.cambiarContrasena(in, out);
                        break;
                    case "2":
                        // Iniciar sesión
                        logica.iniciarSesion(in, out);
                        break;
                    case "3":
                        // Salir
                        out.println("Saliendo...");
                        System.out.println("El cliente seleccionó salir.");
                        salir = true; // Termina el bucle para cerrar la conexión
                        break;
                    default:
                        out.println("Opción inválida");
                        System.out.println("Opción inválida seleccionada.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error en la comunicación: " + e.getMessage());
        }
    }



    public static void main(String[] args) {
        try {
            ServidorAplicacion servidor = new ServidorAplicacion(12345);
            servidor.iniciarServidor();
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
