package co.edu.uniquindio.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private ServerSocket serverSocket;
    private static final String RUTA_ARCHIVO = "src/main/java/co/edu/uniquindio/archivo/Estudiantes.txt";
    private static final String RUTA_LIBROS = "src/main/java/co/edu/uniquindio/archivo/Libros.txt";

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
        boolean salir = false;
        try {
            while (!salir) {
                out.println("Menu: 1. Cambiar Contraseña  2. Iniciar Sesión   3. Salir");
                String opcion = in.readLine();
                System.out.println("Opción recibida del cliente: " + opcion);

                switch (opcion) {
                    case "1":
                        cambiarContrasena(in, out);
                        break;
                    case "2":
                        iniciarSesion(in, out);
                        break;
                    case "3":
                        out.println("Saliendo...");
                        salir = true; // Termina el bucle para cerrar la conexión
                        break;
                    default:
                        out.println("Opción inválida");
                        break;
                }
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
            mostrarMenuLibros(in,out);
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

            boolean exito = modificarContrasena(usuario, nuevaContrasena);
            if (exito) {
                out.println("Contraseña actualizada correctamente.");
            } else {
                out.println("Error al actualizar la contraseña.");
            }
        } else {
            out.println("Credenciales inválidas.");
        }
    }

    private void mostrarMenuLibros(BufferedReader in, PrintWriter out) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_LIBROS));
        List<String> librosDisponibles = new ArrayList<>();
        List<String> nuevasLineas = new ArrayList<>();

        // Mostrar los libros disponibles
        out.println("Libros disponibles:");
        int index = 1;
        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[4].equals("true")) { // Solo mostrar libros disponibles
                out.println(index + ". " + datos[1] + " - Autor: " + datos[2]);
                librosDisponibles.add(datos[0]); // Guardar el id del libro disponible
                nuevasLineas.add(linea); // Guardar línea original
                index++;
            }
        }
        out.println("Ingrese el número del libro que desea reservar:");

        // Leer la entrada del usuario para la reserva
        String seleccion = in.readLine();
        int libroSeleccionado = Integer.parseInt(seleccion) - 1;

        if (libroSeleccionado >= 0 && libroSeleccionado < librosDisponibles.size()) {
            String libroId = librosDisponibles.get(libroSeleccionado);

            // Actualizar el archivo para marcar el libro como no disponible
            boolean libroReservado = false;
            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[0].equals(libroId)) {
                    // Cambiar disponibilidad del libro a 'false'
                    nuevasLineas.add(libroId + ";" + datos[1] + ";" + datos[2] + ";" + datos[3] + ";false");
                    libroReservado = true;
                } else {
                    nuevasLineas.add(linea); // Mantener línea original
                }
            }

            if (libroReservado) {
                Files.write(Paths.get(RUTA_LIBROS), nuevasLineas);
                out.println("Reserva realizada con éxito.");
            } else {
                out.println("Error al realizar la reserva. El libro no está disponible.");
            }
        } else {
            out.println("Número de libro inválido.");
        }
    }


    public static boolean verificarCredenciales(String cedula, String contrasena) throws IOException {
        // Leer todas las líneas del archivo
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));

        // Iterar sobre cada línea para buscar coincidencia
        for (String linea : lineas) {
            String[] datos = linea.split(";");
            // Verificar si la cédula y la contraseña coinciden
            if (datos.length == 2 && datos[0].equals(cedula) && datos[1].equals(contrasena)) {
                return true; // Credenciales válidas
            }
        }
        return false; // Credenciales inválidas
    }

    public static boolean modificarContrasena(String cedula, String nuevaContrasena) throws IOException {
        // Leer todas las líneas del archivo
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));
        List<String> nuevasLineas = new ArrayList<>();

        // Bandera para verificar si se encontró la cédula
        boolean encontrado = false;

        // Iterar sobre cada línea para encontrar la cédula
        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(cedula)) {
                // Si la cédula coincide, cambiar la contraseña
                nuevasLineas.add(cedula + ";" + nuevaContrasena);
                encontrado = true;
            } else {
                // Si no coincide, mantener la línea original
                nuevasLineas.add(linea);
            }
        }

        if (encontrado) {
            Files.write(Paths.get(RUTA_ARCHIVO), nuevasLineas);
            System.out.println("Contraseña actualizada en el archivo.");
        } else {
            System.out.println("Cédula no encontrada para actualizar la contraseña.");
        }
        return encontrado;
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
