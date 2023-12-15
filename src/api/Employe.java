package api;

import api.ProjectionRoom.RoomStates;

public class Employe extends Thread {

  /**
   * The projection room associated with the employee.
   */
  ProjectionRoom room;

  /**
   * Constructs an `Employe` object with the specified projection room.
   * 
   * @param room The projection room to associate with the employee.
   */
  public Employe(ProjectionRoom room) {
    this.room = room;
  }

  public void run() {
    try {
      openRoom();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens the projection room, starts the projection, and performs subsequent
   * actions.
   * 
   * @throws InterruptedException If the thread is interrupted during sleep.
   */
  void openRoom() throws InterruptedException {
    if (Cinema.clients.size() == 0) {
      printMessage("No clients anymore");
      closeRoom();
      return;
    }

    printMessage("Open the projection room");
    this.room.setState(RoomStates.OPEN);
    Thread.sleep(3000);
    startProjection();
  }

  /**
   * Starts the film projection in the projection room and initiates the waiting
   * process.
   * 
   * @throws InterruptedException If the thread is interrupted during sleep.
   */
  void startProjection() throws InterruptedException {
    this.room.setState(RoomStates.PROJECTING);
    Thread.sleep(5000);
    waitUntilEmpty();
  }

  /**
   * Waits until all clients exit the projection room and triggers the cleaning
   * process.
   * 
   * @throws InterruptedException If the thread is interrupted during sleep.
   */
  void waitUntilEmpty() throws InterruptedException {
    this.room.setState(RoomStates.EXITING);
    printMessage("Waiting all the clients to exit the room");
    cleanRoom();
  }

  /**
   * Cleans the projection room and prepares it for the next cycle of operations.
   * 
   * @throws InterruptedException If the thread is interrupted during sleep.
   */
  void cleanRoom() throws InterruptedException {
    this.room.setState(RoomStates.CLEANING);
    Thread.sleep(2000);
    openRoom();
  }

  /**
   * Closes the projection room, transitioning it to the CLOSED state.
   * 
   * @throws InterruptedException If the thread is interrupted during sleep.
   */
  void closeRoom() throws InterruptedException {
    this.room.setState(RoomStates.CLOSED);
  }

  /**
   * Prints a message to the console with a specific format.
   * 
   * @param message The message to print.
   */
  private void printMessage(String message) {
    System.out.println("\033[0;31m" + "Employ : " + message + "\033[0;37m");
  }
}
