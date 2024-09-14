package co.edu.uniquindio.cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ModelFactoryCliente {

    public static void cambiarContrasenaLogic(BufferedReader in, BufferedReader userInput, PrintWriter out) throws IOException {
        System.out.println(in.readLine()); // Solicita cédula
        String cedula = userInput.readLine();
        out.println(cedula);

        System.out.println(in.readLine()); // Solicita nueva contraseña
        String nuevaContrasena = userInput.readLine();
        out.println(nuevaContrasena);
        System.out.println("Nueva contraseña enviada.");

        System.out.println(in.readLine()); // Respuesta del servidor
    }

    public static void iniciarSesionLogic(BufferedReader in, BufferedReader userInput, PrintWriter out) throws IOException {
        System.out.println(in.readLine()); // Solicita usuario
        String usuario = userInput.readLine();
        out.println(usuario);

        System.out.println(in.readLine()); // Solicita contraseña
        String contrasena = userInput.readLine();
        out.println(contrasena);
        System.out.println("Datos enviados");

        // Leer respuesta del servidor para verificar inicio de sesión
        String respuesta = in.readLine();
        System.out.println(respuesta);

        if (respuesta.contains("Inicio de sesión exitoso")) {
            // Mostrar menú de reserva de libros
            while ((respuesta = in.readLine()) != null) {
                System.out.println(respuesta);
                if (respuesta.contains("Ingrese el número del libro")) {
                    String seleccion = userInput.readLine();
                    out.println(seleccion);
                    // Mostrar confirmación de reserva
                    respuesta = in.readLine();
                    System.out.println(respuesta);
                    break;
                }
            }
        }
    }

    public static void mostrarMenu(BufferedReader in, BufferedReader userInput, PrintWriter out) throws IOException {
        String respuesta = in.readLine(); // Leer el menú de opciones
        System.out.println(respuesta);

        String opcion = userInput.readLine();
        out.println(opcion); // Enviar la opción seleccionada

        switch (opcion) {
            case "1":
                // Cambiar contraseña
                cambiarContrasenaLogic(in, userInput, out);
                break;
            case "2":
                // Iniciar sesión
                iniciarSesionLogic(in, userInput, out);
                break;
            case "3":
                // Salir
                System.out.println("Saliendo de la aplicación...");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }
}

