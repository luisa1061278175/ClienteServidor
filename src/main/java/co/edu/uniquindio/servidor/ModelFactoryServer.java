package co.edu.uniquindio.servidor;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModelFactoryServer {
    private static final String RUTA_ARCHIVO = "src\\main\\java\\co\\edu\\uniquindio\\conexion2Pc\\archivos\\Estudiantes.txt";
    private static final String RUTA_ARCHIVO_LIBROS = "src\\main\\java\\co\\edu\\uniquindio\\conexion2Pc\\archivos\\Libros.txt";

    public void cambiarContrasena(String cedula, String contrasena) {
        if (cedula.isEmpty() || contrasena.isEmpty()) {
            System.out.println("Todos los campos deben estar llenos.");
            return;
        }

        try {
            boolean cambioExitoso = modificarContrasena(cedula, contrasena);
            if (cambioExitoso) {
                System.out.println("Contraseña cambiada exitosamente.");
            } else {
                System.out.println("Error: La cédula no coincide.");
            }
        } catch (IOException e) {
            System.out.println("Error al cambiar la contraseña: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean modificarContrasena(String cedula, String contrasena) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));
        List<String> nuevasLineas = new ArrayList<>();

        boolean encontrado = false;

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(cedula)) {
                nuevasLineas.add(cedula + ";" + contrasena);
                encontrado = true;
            } else {
                nuevasLineas.add(linea);
            }
        }

        if (encontrado) {
            Files.write(Paths.get(RUTA_ARCHIVO), nuevasLineas);
            System.out.println("Contraseña modificada exitosamente.");
        } else {
            System.out.println("Cédula no encontrada.");
        }
        return encontrado;
    }

    public boolean verificarCredenciales(String cedula, String contrasena) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(cedula) && datos[1].equals(contrasena)) {
                return true;
            }
        }
        return false;
    }

    public void mostrarMenuReservarLibro(PrintWriter out, BufferedReader in) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO_LIBROS));
        List<String> librosDisponibles = new ArrayList<>();

        // Mostrar los libros disponibles
        out.println("Libros disponibles:");
        int index = 1;
        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[4].equals("true")) { // Solo mostrar libros disponibles
                out.println(index + ". " + datos[1] + " - Autor: " + datos[2]);
                librosDisponibles.add(datos[0]); // Guardar el id del libro disponible
                index++;
            }
        }

        if (librosDisponibles.isEmpty()) {
            out.println("No hay libros disponibles.");
            return;
        }

        // Solicitar al usuario que ingrese el número del libro que desea reservar
        out.println("Ingrese el número del libro que desea reservar:");
        String seleccion = in.readLine();
        int numeroLibro = Integer.parseInt(seleccion);

        if (numeroLibro < 1 || numeroLibro > librosDisponibles.size()) {
            out.println("Selección inválida.");
        } else {
            String idLibroSeleccionado = librosDisponibles.get(numeroLibro - 1);
            reservarLibro(idLibroSeleccionado);
            out.println("Libro reservado exitosamente.");
        }
    }

    public void reservarLibro(String idLibro) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO_LIBROS));
        List<String> nuevasLineas = new ArrayList<>();

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos[0].equals(idLibro)) {
                datos[4] = "false"; // Actualizar la disponibilidad del libro
            }
            nuevasLineas.add(String.join(";", datos));
        }

        Files.write(Paths.get(RUTA_ARCHIVO_LIBROS), nuevasLineas);
    }
}

