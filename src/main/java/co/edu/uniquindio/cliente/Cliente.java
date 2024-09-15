package co.edu.uniquindio.cliente;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public Cliente(String direccionServidor, int puerto) throws IOException {
        socket = new Socket(direccionServidor, puerto);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void iniciarCliente() {
        try {
            mostrarMenu();
        } catch (IOException e) {
            System.out.println("Error durante la comunicación con el servidor: " + e.getMessage());
        }
    }

    private void mostrarMenu() throws IOException {
        String mensajeDelServidor = in.readLine();
        System.out.println("Servidor: " + mensajeDelServidor);

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        String opcion = teclado.readLine();
        out.println(opcion);

        switch (opcion) {
            case "1":
                cambiarContrasena();
                break;
            case "2":
                iniciarSesion();
                break;
            case "3":
                System.out.println("Saliendo...");
                socket.close();
                break;
            default:
                System.out.println("Opción inválida.");
                mostrarMenu();
                break;
        }
    }

    private void iniciarSesion() throws IOException {
        System.out.println(in.readLine()); // "Ingrese su usuario (cédula):"
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        String usuario = teclado.readLine();
        out.println(usuario);

        System.out.println(in.readLine()); // "Ingrese su contraseña:"
        String contrasena = teclado.readLine();
        out.println(contrasena);

        String respuesta = in.readLine();
        System.out.println("Servidor: " + respuesta);

        if ("Inicio de sesión exitoso".equals(respuesta)) {
            mostrarMenuLibros();
        } else {
            mostrarMenu();
        }
    }

    private void cambiarContrasena() throws IOException {
        System.out.println(in.readLine()); // "Ingrese su usuario (cédula):"
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        String usuario = teclado.readLine();
        out.println(usuario);

        System.out.println(in.readLine()); // "Ingrese su contraseña actual:"
        String contrasenaActual = teclado.readLine();
        out.println(contrasenaActual);

        String respuestaVerificacion = in.readLine();
        System.out.println("Servidor: " + respuestaVerificacion);

        if (respuestaVerificacion.contains("exitoso")) {
            System.out.println(in.readLine()); // "Ingrese su nueva contraseña:"
            String nuevaContrasena = teclado.readLine();
            System.out.println("Enviando nueva contraseña: " + nuevaContrasena); // Depuración
            out.println(nuevaContrasena);

            String respuestaActualizacion = in.readLine();
            System.out.println("Servidor: " + respuestaActualizacion);
        } else {
            mostrarMenu();
        }
    }

    private void mostrarMenuLibros() throws IOException {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        String libroMenu;
        while ((libroMenu = in.readLine()) != null) {
            System.out.println("Servidor: " + libroMenu);

            if (libroMenu.contains("Ingrese el número del libro")) {
                String seleccion = teclado.readLine();
                out.println(seleccion);
                String confirmacion = in.readLine();
                System.out.println("Servidor: " + confirmacion);
                break;
            }
        }
        mostrarMenu();
    }

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente("localhost", 12345);
            cliente.iniciarCliente();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
}
