package co.edu.uniquindio.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorAplicacion {
    private ServerSocket serverSocket;
    private ServidorLogica logica;

    public ServidorAplicacion(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        logica = new ServidorLogica();
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

                logica.mostrarMenuPrincipal(in, out);


                String opcion = in.readLine();
                System.out.println("Opción recibida del cliente: " + opcion);

                switch (opcion) {
                    case "1":

                        logica.cambiarContrasena(in, out);
                        break;
                    case "2":

                        logica.iniciarSesion(in, out);
                        break;
                    case "3":

                        out.println("Saliendo...");
                        System.out.println("El cliente seleccionó salir.");
                        salir = true;
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
