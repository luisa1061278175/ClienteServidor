package co.edu.uniquindio.cliente;


import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.1.59", 1234)) {
            ModelFactoryCliente modelFactoryCliente = new ModelFactoryCliente();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String respuesta;
            while ((respuesta = in.readLine()) != null) {
                System.out.println(respuesta);

                modelFactoryCliente.mostrarMenu(in, userInput, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
