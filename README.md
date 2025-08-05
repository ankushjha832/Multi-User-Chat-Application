import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {
        final int PORT = 12345;
        // Use a thread pool to manage client connections
        ExecutorService pool = Executors.newFixedThreadPool(10); 

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new task to handle the client
                pool.execute(new ClientHandler(clientSocket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}

class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                // Simple response logic: echo the message back
                writer.println("Server received: " + inputLine); 
                
                if ("exit".equalsIgnoreCase(inputLine)) {
                    System.out.println("Client " + clientSocket.getInetAddress() + " disconnected.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
