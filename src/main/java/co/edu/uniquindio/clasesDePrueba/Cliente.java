package co.edu.uniquindio.clasesDePrueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Cliente(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
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
                        cambiarContrasena(consoleInput);
                        break;
                    case "2":
                        iniciarSesion(consoleInput);
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

    private void iniciarSesion(BufferedReader consoleInput) throws IOException {
        System.out.println(in.readLine()); // Solicitud de usuario
        String usuario = consoleInput.readLine();
        out.println(usuario);

        System.out.println(in.readLine()); // Solicitud de contraseña
        String contrasena = consoleInput.readLine();
        out.println(contrasena);

        String respuesta = in.readLine();
        System.out.println(respuesta);

        if (respuesta.equals("Inicio de sesión exitoso")) {
            mostrarMenuLibros(consoleInput);
        }
    }

    private void cambiarContrasena(BufferedReader consoleInput) throws IOException {
        System.out.println(in.readLine()); // Solicitud de cédula
        String usuario = consoleInput.readLine();
        out.println(usuario);

        System.out.println(in.readLine()); // Solicitud de contraseña actual
        String contrasenaActual = consoleInput.readLine();
        out.println(contrasenaActual);

        String respuesta = in.readLine();
        System.out.println(respuesta);

        if (respuesta.equals("Verificación exitosa.")) {
            System.out.println(in.readLine()); // Solicitud de nueva contraseña
            String nuevaContrasena = consoleInput.readLine();
            out.println(nuevaContrasena);

            System.out.println(in.readLine()); // Resultado de la actualización
            mostrarMenuLibros(consoleInput);
        }
    }

    private void mostrarMenuLibros(BufferedReader consoleInput) throws IOException {
        System.out.println(in.readLine()); // Selección de criterio de búsqueda
        String opcionBusqueda = consoleInput.readLine();
        out.println(opcionBusqueda);

        switch (opcionBusqueda) {
            case "1": // Búsqueda por autor
                System.out.println(in.readLine()); // Solicitud de autor
                String autor = consoleInput.readLine();
                out.println(autor);
                break;
            case "2": // Búsqueda por género
                System.out.println(in.readLine()); // Solicitud de género
                String genero = consoleInput.readLine();
                out.println(genero);
                break;
            case "3": // Ver todos los libros
                // No se requiere entrada adicional, simplemente continúa
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        // Mostrar lista de libros
        String respuesta;
        while (!(respuesta = in.readLine()).isEmpty()) {
            if (respuesta.equals("Ingrese el número del libro que desea reservar:")) {
                System.out.println(respuesta);
                break;
            }
            System.out.println(respuesta);
        }

        // Seleccionar libro para reservar
        String seleccion = consoleInput.readLine();
        out.println(seleccion);

        // Recibir respuesta de la reserva
        System.out.println(in.readLine());
    }

    private void cerrarConexion() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente("localhost", 12345); // Cambia "localhost" por la IP del servidor si es en otra máquina
            cliente.iniciarCliente();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
}
