package co.edu.uniquindio.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClienteLogica {
    private BufferedReader in;
    private PrintWriter out;

    public ClienteLogica(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void iniciarSesion(BufferedReader consoleInput) throws IOException {
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

    public void cambiarContrasena(BufferedReader consoleInput) throws IOException {
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

    public void mostrarMenuLibros(BufferedReader consoleInput) throws IOException {
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
}
