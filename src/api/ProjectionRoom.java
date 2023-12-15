package api;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import api.Client.ClientState;

public class ProjectionRoom {

    /**
     * Enumerates the possible states of the projection room.
     */
    enum RoomStates {
        CLOSED,
        PROJECTING,
        EXITING,
        CLEANING,
        OPEN
    }

    /**
     * The current state of the projection room.
     */
    private RoomStates state;

    /**
     * List of clients currently inside the projection room.
     */
    private ArrayList<Client> clientsInRoom;

    /**
     * Semaphore controlling access to the projection room.
     */
    private Semaphore roomEntrance;

    /**
     * Object used for synchronization when clients are watching a film.
     */
    public Object watchingFilm = new Object();

    /**
     * Object used for synchronization when clients are waiting for the next
     * projection.
     */
    public Object waitingNext = new Object();

    /**
     * Constructs a new `ProjectionRoom` object with an initial state of CLOSED.
     * Initializes the list of clients and the room entrance semaphore.
     */
    public ProjectionRoom() {
        setState(RoomStates.CLOSED);
        clientsInRoom = new ArrayList<Client>();
        roomEntrance = new Semaphore(1);
    }

    /**
     * Sets the state of the projection room and performs state-specific actions.
     * 
     * @param etat The new state to set.
     */
    public synchronized void setState(RoomStates etat) {
        this.state = etat;
        printMessage(this.state.name());
        switch (this.state) {
            case CLEANING:
                break;
            case CLOSED:
                break;
            case EXITING:
                synchronized (this.watchingFilm) {
                    this.watchingFilm.notifyAll();
                }
                break;
            case OPEN:
                synchronized (this.waitingNext) {
                    this.waitingNext.notifyAll();
                }
                break;
            case PROJECTING:
                printMessage(clientsInRoom.size() + " clients are enjoying the film.");
                break;
            default:
                break;
        }
        notifyAll();
    }

    /**
     * Retrieves the current state of the projection room.
     * 
     * @return The current state of the projection room.
     */
    public RoomStates getState() {
        return this.state;
    }

    /**
     * Allows a client to enter the projection room, updating the client's state.
     * 
     * @param client The client attempting to enter the projection room.
     */
    public synchronized void enterProjectionRoom(Client client) {
        try {
            roomEntrance.acquire();
            if (this.state == RoomStates.OPEN && clientsInRoom.size() < Cinema.PROJECTION_ROOM_CAPACITY) {
                clientsInRoom.add(client);
                client.setState(ClientState.WatchingFilm);
            } else {
                printMessage("The client " + client.getName() + " have to wait the next projection");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        roomEntrance.release();
    }

    /**
     * Allows a client to exit the projection room, updating the client's state.
     * 
     * @param client The client exiting the projection room.
     */
    public synchronized void exitProjectionRoom(Client client) {
        try {
            roomEntrance.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientsInRoom.remove(client);
        client.setState(ClientState.Exiting);
        printMessage("The client " + client.getName() + " just exit the Projection Room");
        roomEntrance.release();
    }

    /**
     * Prints a message to the console with a specific format.
     * 
     * @param message The message to print.
     */
    private void printMessage(String message) {
        System.out.println("\033[0;33m" + "Projection Room : " + message + "\033[0;37m");
    }
}
