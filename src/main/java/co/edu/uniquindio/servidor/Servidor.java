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
                System.out.println("Menú enviado al cliente.");

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

        }
    }

    private void iniciarSesion(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Ingrese su usuario (cédula):");
        System.out.println("Solicitud de cédula enviada al cliente.");

        String usuario = in.readLine();
        System.out.println("Usuario recibido: " + usuario);

        out.println("Ingrese su contraseña:");
        System.out.println("Solicitud de contraseña enviada al cliente.");

        String contrasena = in.readLine();
        System.out.println("Contraseña recibida: " + contrasena);

        if (verificarCredenciales(usuario, contrasena)) {
            out.println("Inicio de sesión exitoso");
            System.out.println("Inicio de sesión exitoso para el usuario: " + usuario);
            mostrarMenuLibros(in, out);
        } else {
            out.println("Credenciales inválidas.");
            System.out.println("Credenciales inválidas para el usuario: " + usuario);
        }
    }

    private void cambiarContrasena(BufferedReader in, PrintWriter out) throws IOException {
        // Solicitar cédula al cliente
        out.println("Ingrese su usuario (cédula):");
        System.out.println("Solicitud de cédula enviada al cliente para cambio de contraseña.");

        // Leer cédula del cliente
        String usuario = in.readLine();
        System.out.println("Usuario recibido: " + usuario);

        // Solicitar contraseña actual al cliente
        out.println("Ingrese su contraseña actual:");
        System.out.println("Solicitud de contraseña actual enviada al cliente.");

        // Leer contraseña actual del cliente
        String contrasenaActual = in.readLine();
        System.out.println("Contraseña actual recibida: " + contrasenaActual);

        // Verificar las credenciales
        if (verificarCredenciales(usuario, contrasenaActual)) {
            // Informar al cliente que la verificación fue exitosa
            out.println("Verificación exitosa."); // Enviar mensaje al cliente indicando éxito
            System.out.println("Verificación exitosa para el usuario: " + usuario);

            // Solicitar nueva contraseña
            out.println("Ingrese su nueva contraseña:");
            System.out.println("Solicitud de nueva contraseña enviada al cliente.");

            // Leer nueva contraseña del cliente
            String nuevaContrasena = in.readLine();
            System.out.println("Nueva contraseña recibida: " + nuevaContrasena);

            // Intentar actualizar la contraseña
            boolean exito = modificarContrasena(usuario, nuevaContrasena);

            if (exito) {
                out.println("Contraseña actualizada correctamente.");
                System.out.println("Contraseña actualizada para el usuario: " + usuario);
                mostrarMenuLibros(in,out);

            } else {
                out.println("Error al actualizar la contraseña.");
                System.out.println("Error al actualizar la contraseña del usuario: " + usuario);
            }
        } else {
            // Informar al cliente que la verificación falló
            out.println("Credenciales inválidas.");
            System.out.println("Credenciales inválidas para el usuario: " + usuario);
        }
    }


    private void mostrarMenuLibros(BufferedReader in, PrintWriter out) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_LIBROS));

        // Preguntar al cliente si desea buscar por autor, género o ver todos los libros
        out.println("Seleccione el criterio de búsqueda: 1. Autor  2. Género  3. Ver todos los libros");
        String opcionBusqueda = in.readLine();

        List<String> librosFiltrados = new ArrayList<>();

        if ("1".equals(opcionBusqueda)) {
            // Búsqueda por autor
            out.println("Ingrese el autor que desea buscar:");
            String autor = in.readLine();

            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[2].equalsIgnoreCase(autor) && datos[4].equals("true")) { // Comparar autor y disponibilidad
                    librosFiltrados.add(linea);
                }
            }
        } else if ("2".equals(opcionBusqueda)) {
            // Búsqueda por género
            out.println("Ingrese el género que desea buscar:");
            String genero = in.readLine();

            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[3].equalsIgnoreCase(genero) && datos[4].equals("true")) { // Comparar género y disponibilidad
                    librosFiltrados.add(linea);
                }
            }
        } else if ("3".equals(opcionBusqueda)) {
            // Mostrar todos los libros disponibles sin filtrar
            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[4].equals("true")) { // Solo mostrar libros disponibles
                    librosFiltrados.add(linea);
                }
            }
        } else {
            out.println("Opción inválida.");
            return;
        }

        // Mostrar los libros filtrados según la búsqueda o todos los disponibles
        if (!librosFiltrados.isEmpty()) {
            out.println("Libros disponibles:");
            for (int i = 0; i < librosFiltrados.size(); i++) {
                String[] datos = librosFiltrados.get(i).split(";");
                out.println((i + 1) + ". " + datos[1] + " - Autor: " + datos[2] + " - Género: " + datos[3]);
            }

            out.println("Ingrese el número del libro que desea reservar:");
            String seleccion = in.readLine();

            int libroSeleccionado = Integer.parseInt(seleccion) - 1;
            if (libroSeleccionado >= 0 && libroSeleccionado < librosFiltrados.size()) {
                String libroId = librosFiltrados.get(libroSeleccionado).split(";")[0];
                reservarLibro(libroId, lineas);
                out.println("Reserva realizada con éxito.");
            } else {
                out.println("Número de libro inválido.");
            }
        } else {
            out.println("No se encontraron libros según los criterios de búsqueda.");
        }
    }

    // Método para actualizar la disponibilidad del libro en el archivo
    private void reservarLibro(String libroId, List<String> lineas) throws IOException {
        List<String> nuevasLineas = new ArrayList<>();

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(libroId)) {
                datos[4] = "false"; // Cambiar disponibilidad a 'false'
                nuevasLineas.add(String.join(";", datos));
            } else {
                nuevasLineas.add(linea);
            }
        }

        Files.write(Paths.get(RUTA_LIBROS), nuevasLineas);
    }


    public static boolean verificarCredenciales(String cedula, String contrasena) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos.length == 2 && datos[0].equals(cedula) && datos[1].equals(contrasena)) {
                System.out.println("Credenciales verificadas para el usuario: " + cedula);
                return true;
            }
        }
        System.out.println("Credenciales inválidas para el usuario: " + cedula);
        return false;
    }

    private boolean modificarContrasena(String usuario, String nuevaContrasena) {
        File archivo = new File("src/main/java/co/edu/uniquindio/archivo/Estudiantes.txt");
        boolean actualizado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenidoArchivo = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].equals(usuario)) {
                    System.out.println("Usuario encontrado: " + usuario + ". Actualizando contraseña...");
                    linea = datos[0] + ";" + nuevaContrasena; // Actualizar línea con nueva contraseña
                    actualizado = true;
                }
                contenidoArchivo.append(linea).append(System.lineSeparator());
            }

            // Escribir el contenido actualizado de nuevo al archivo
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                bw.write(contenidoArchivo.toString());
            }

            if (actualizado) {
                System.out.println("Contraseña actualizada en el archivo para el usuario: " + usuario);
            } else {
                System.out.println("Usuario no encontrado en el archivo.");
            }

            return actualizado;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
