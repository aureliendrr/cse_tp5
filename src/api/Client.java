package api;

import api.ProjectionRoom.RoomStates;

public class Client extends Thread {

  /**
   * Enumerates the possible states of a client.
   */
  enum ClientState {
    DontHaveTicket,
    HaveTicket,
    WatchingFilm,
    Entering,
    Exiting
  }

  /**
   * The current state of the client.
   */
  private ClientState state;

  /**
   * Sets the new state of the client and prints a corresponding message.
   * 
   * @param newState The new state to set for the client.
   */
  public void setState(ClientState newState) {
    this.state = newState;

    String message = "";
    switch (this.state) {
      case DontHaveTicket:
        message = "didn't get a ticket.";
        break;
      case Exiting:
        message = "is exiting the cinema";
        break;
      case HaveTicket:
        message = "has just bought his ticket, he is heading towards the projection room.";
        break;
      case WatchingFilm:
        message = "just enter the Projection Room";
        break;
      case Entering:
        message = "is entering the cinema";
        break;
      default:
        break;
    }

    printMessage(message);
  }

  /**
   * Constructs a new `Client` object with the specified name.
   * 
   * @param name The name to assign to the client.
   */
  public Client(String name) {
    this.setName(name);
  }

  public void run() {
    try {
      behaviour();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Defines the behavior of the client, including entering, buying a ticket, and
   * watching a film.
   * 
   * @throws InterruptedException If the thread is interrupted during sleep.
   */
  private void behaviour() throws InterruptedException {
    Cinema.enterCinema(this);
    Cinema.ticketOffice.tryBuyTicket(this);

    if (this.state == ClientState.DontHaveTicket) {
      Cinema.exitCinema(this);
    }

    while (this.state == ClientState.HaveTicket) {
      Cinema.projectionRoom.enterProjectionRoom(this);
      if (this.state == ClientState.WatchingFilm) {
        synchronized (Cinema.projectionRoom.watchingFilm) {
          Cinema.projectionRoom.watchingFilm.wait();
          Cinema.projectionRoom.exitProjectionRoom(this);
          Thread.sleep(500);
          Cinema.exitCinema(this);
        }
      } else {
        synchronized (Cinema.projectionRoom.waitingNext) {
          Cinema.projectionRoom.waitingNext.wait();
        }
      }
    }
  }

  /**
   * Prints a message to the console with a specific format.
   * 
   * @param message The message to print.
   */
  private void printMessage(String message) {
    System.out.println("\033[0;32m" + getName() + " (" + this.getId() + ") " + message + "\033[0;37m");
  }
}
