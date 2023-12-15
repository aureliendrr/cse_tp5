package api;

import java.util.concurrent.Semaphore;

import api.Client.ClientState;

public class TicketOffice {

  /**
   * Semaphore controlling access to the ticket office.
   */
  Semaphore semaphore;

  /**
   * The number of tickets remaining in the ticket office.
   */
  int ticketsRemaining;

  public TicketOffice() {
    semaphore = new Semaphore(1);
    ticketsRemaining = Cinema.TICKETS_CAPACITY;
  }

  /**
   * Attempts to sell a ticket to the specified client, updating the ticket count
   * and client state.
   * 
   * @param client The client attempting to buy a ticket.
   */
  public void tryBuyTicket(Client client) {
    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (ticketsRemaining > 0) {
      ticketsRemaining--;
      client.setState(ClientState.HaveTicket);
    } else {
      client.setState(ClientState.DontHaveTicket);
    }
    printMessage("Number of tickets remained : " + ticketsRemaining);
    semaphore.release();
  }

  /**
   * Prints a message to the console with a specific format.
   * 
   * @param message The message to print.
   */
  private void printMessage(String message) {
    System.out.println("\033[0;34m" + "Ticket Office : " + message + "\033[0;37m");
  }
}
