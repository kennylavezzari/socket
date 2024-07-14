package com.mycompany.sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static List<Integer> sharedList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        int port = 10; // Puerto donde el servidor escuchará

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

                while (true) {
                    try {
                        // Leer número aleatorio del cliente
                        int number = input.readInt();
                        if (number == -1) {
                            System.out.println("Cliente desconectado.");
                            break;
                        }
                        System.out.println("Número recibido del cliente: " + number);

                        // Agregar el número a la lista compartida
                        synchronized (sharedList) {
                            sharedList.add(number);
                        }

                        // Enviar la lista actualizada al cliente
                        output.writeObject(new ArrayList<>(sharedList));
                        output.flush();

                        System.out.println("Lista actualizada enviada al cliente: " + sharedList);
                    } catch (EOFException e) {
                        System.out.println("Conexión terminada por el cliente.");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
