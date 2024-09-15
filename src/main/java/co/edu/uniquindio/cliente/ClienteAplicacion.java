package co.edu.uniquindio.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteAplicacion {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ClienteLogica logica;

    public ClienteAplicacion(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        logica = new ClienteLogica(in, out); // Instancia la lógica del cliente
    }

    public void iniciarCliente() {
        try {
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            boolean salir = false;

            while (!salir) {
                String menu = in.readLine();
                System.out.println(menu);

                String opcion = consoleInput.readLine();
                out.println(opcion);

                switch (opcion) {
                    case "1":
                        logica.cambiarContrasena(consoleInput);
                        break;
                    case "2":
                        logica.iniciarSesion(consoleInput);
                        break;
                    case "3":
                        salir = true;
                        break;
                    default:
                        System.out.println(in.readLine()); // Mostrar mensaje de opción inválida
                        break;
                }
            }

            cerrarConexion();
        } catch (IOException e) {
            System.out.println("Error durante la comunicación con el servidor: " + e.getMessage());
        }
    }

    private void cerrarConexion() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) {
        try {
            ClienteAplicacion cliente = new ClienteAplicacion("localhost", 12345); // Cambia "localhost" por la IP del servidor si es en otra máquina
            cliente.iniciarCliente();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
}
