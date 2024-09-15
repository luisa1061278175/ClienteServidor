package co.edu.uniquindio.servidor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServidorLogica {
    private static final String RUTA_ARCHIVO = "src/main/java/co/edu/uniquindio/archivo/Estudiantes.txt";
    private static final String RUTA_LIBROS = "src/main/java/co/edu/uniquindio/archivo/Libros.txt";

    public void iniciarSesion(BufferedReader in, PrintWriter out) throws IOException {
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

    public void cambiarContrasena(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Ingrese su usuario (cédula):");
        System.out.println("Solicitud de cédula enviada al cliente para cambio de contraseña.");

        String usuario = in.readLine();
        System.out.println("Usuario recibido: " + usuario);

        out.println("Ingrese su contraseña actual:");
        System.out.println("Solicitud de contraseña actual enviada al cliente.");

        String contrasenaActual = in.readLine();
        System.out.println("Contraseña actual recibida: " + contrasenaActual);

        if (verificarCredenciales(usuario, contrasenaActual)) {
            out.println("Verificación exitosa.");
            System.out.println("Verificación exitosa para el usuario: " + usuario);

            out.println("Ingrese su nueva contraseña:");
            System.out.println("Solicitud de nueva contraseña enviada al cliente.");

            String nuevaContrasena = in.readLine();
            System.out.println("Nueva contraseña recibida: " + nuevaContrasena);

            boolean exito = modificarContrasena(usuario, nuevaContrasena);

            if (exito) {
                out.println("Contraseña actualizada correctamente.");
                System.out.println("Contraseña actualizada para el usuario: " + usuario);
                // Mostrar el menú principal después de cambiar la contraseña
            } else {
                out.println("Error al actualizar la contraseña.");
                System.out.println("Error al actualizar la contraseña del usuario: " + usuario);
            }
        } else {
            out.println("Credenciales inválidas.");
            System.out.println("Credenciales inválidas para el usuario: " + usuario);
        }
    }

    public void mostrarMenuPrincipal(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Menu: 1. Cambiar Contraseña  2. Iniciar Sesión   3. Salir");
        System.out.println("Menú principal enviado al cliente.");
    }


    public void mostrarMenuLibros(BufferedReader in, PrintWriter out) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_LIBROS));

        out.println("Seleccione el criterio de búsqueda: 1. Autor  2. Género  3. Ver todos los libros");
        String opcionBusqueda = in.readLine();

        List<String> librosFiltrados = new ArrayList<>();

        if ("1".equals(opcionBusqueda)) {
            out.println("Ingrese el autor que desea buscar:");
            String autor = in.readLine();

            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[2].equalsIgnoreCase(autor) && datos[4].equals("true")) {
                    librosFiltrados.add(linea);
                }
            }
        } else if ("2".equals(opcionBusqueda)) {
            out.println("Ingrese el género que desea buscar:");
            String genero = in.readLine();

            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[3].equalsIgnoreCase(genero) && datos[4].equals("true")) {
                    librosFiltrados.add(linea);
                }
            }
        } else if ("3".equals(opcionBusqueda)) {
            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos[4].equals("true")) {
                    librosFiltrados.add(linea);
                }
            }
        } else {
            out.println("Opción inválida.");
            return;
        }

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

    private void reservarLibro(String libroId, List<String> lineas) throws IOException {
        List<String> nuevasLineas = new ArrayList<>();

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(libroId)) {
                datos[4] = "false";
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

    public boolean modificarContrasena(String usuario, String nuevaContrasena) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));
        List<String> nuevasLineas = new ArrayList<>();

        boolean exito = false;

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(usuario)) {
                nuevasLineas.add(usuario + ";" + nuevaContrasena);
                exito = true;
            } else {
                nuevasLineas.add(linea);
            }
        }

        Files.write(Paths.get(RUTA_ARCHIVO), nuevasLineas);
        return exito;
    }
}
