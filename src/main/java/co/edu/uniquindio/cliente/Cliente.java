package co.edu.uniquindio.cliente;
//"192.168.1.59", 1234


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

        if ("2".equals(opcion)) {
            iniciarSesion();
        } else if ("1".equals(opcion)) {
            cambiarContrasena();
        } else if ("3".equals(opcion)) {
            System.out.println("Saliendo...");
            socket.close();
        } else {
            System.out.println("Opción inválida.");
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
            out.println(nuevaContrasena);

            String respuestaActualizacion = in.readLine();
            System.out.println("Servidor: " + respuestaActualizacion);
        }
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
