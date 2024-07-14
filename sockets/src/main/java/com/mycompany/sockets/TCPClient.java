package com.mycompany.sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Dirección del servidor
        int port = 10; // Puerto del servidor

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.print("Ingrese un número (-1 para salir): ");
                int number = scanner.nextInt();

                // Enviar el número al servidor
                output.writeInt(number);
                output.flush();

                if (number == -1) {
                    break;
                }

                System.out.println("Número enviado al servidor: " + number);

                // Recibir lista actualizada del servidor
                List<Integer> updatedList = (List<Integer>) input.readObject();
                System.out.println("Lista actualizada recibida del servidor: " + updatedList);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
