package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import api.Client.ClientState;

public class Cinema {

  /**
   * Path to the file containing names for generating client names.
   */
  private static final String NamesSet = "src\\data\\names.csv";

  /**
   * Total number of clients in the cinema.
   */
  static final int CLIENT_COUNT = 8;

  /**
   * Capacity of the projection room.
   */
  static final int PROJECTION_ROOM_CAPACITY = 4;

  /**
   * Capacity of available tickets.
   */
  static final int TICKETS_CAPACITY = 6;

  /**
   * Reference to the ticket office in the cinema.
   */
  static TicketOffice ticketOffice;

  /**
   * Reference to the projection room in the cinema.
   */
  static ProjectionRoom projectionRoom;

  /**
   * List of clients currently in the cinema.
   */
  static ArrayList<Client> clients;

  /**
   * Reference to the cinema employee.
   */
  private static Employe employ;

  /**
   * Semaphore controlling access to the cinema entrance.
   */
  private static Semaphore cinemaEntrance;

  /**
   * The main method to start the cinema system.
   * Initializes the cinema components and starts clients and the employee.
   * 
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args) {
    Cinema cinema = new Cinema();
    printMessage(TICKETS_CAPACITY + " tickets for today.");
    ticketOffice = new TicketOffice();
    projectionRoom = new ProjectionRoom();
    cinemaEntrance = new Semaphore(1);
    cinema.createAndStartClients();
    cinema.createAndStartEmploy();
  }

  /**
   * Creates and starts the cinema employee.
   */
  private void createAndStartEmploy() {
    employ = new Employe(projectionRoom);
    employ.start();
  }

  /**
   * Creates and starts the specified number of clients in the cinema.
   */
  private void createAndStartClients() {
    clients = new ArrayList<Client>();
    for (int i = 0; i < CLIENT_COUNT; i++) {
      Client newClient = new Client(getClientName());
      newClient.start();
    }
  }

  /**
   * Allows a client to enter the cinema, updating the client list and state.
   * 
   * @param client The client entering the cinema.
   */
  public synchronized static void enterCinema(Client client) {
    try {
      cinemaEntrance.acquire();
      clients.add(client);
      client.setState(ClientState.Entering);
      cinemaEntrance.release();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Allows a client to exit the cinema, updating the client list and state.
   * @param client The client exiting the cinema.
   */
  public synchronized static void exitCinema(Client client) {
    try {
      cinemaEntrance.acquire();
      clients.remove(client);
      client.setState(ClientState.Exiting);
      cinemaEntrance.release();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints a message to the console with a specific format.
   * @param message The message to print.
   */
  private static void printMessage(String message) {
    System.out.println("\033[0;37mCinema : " + message);
  }

  /**
   * Generates a random client name by reading from a CSV file.
   * @return A randomly selected client name.
   */
  private String getClientName() {
    try {
      String separator = ",";

      List<String> lines = readCSV(NamesSet);

      int randomIndex = new Random().nextInt(lines.size());
      String randomLine = lines.get(randomIndex);

      // Extraire la première colonne
      return randomLine.split(separator)[0];
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "";
  }

  /**
   * Reads the content of a CSV file and returns a list of lines.
   * @param filePath The path to the CSV file.
   * @return A list containing lines from the CSV file.
   * @throws IOException If an I/O error occurs while reading the file.
   */
  private static List<String> readCSV(String filePath) throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
  }
}