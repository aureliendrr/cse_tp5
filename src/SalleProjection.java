public class SalleProjection {

  Client[] clientsAvecBillet;

  enum Etats {
    CLOSED,
    PROJECTING,
    EXITING,
    CLEANING,
    OPEN
  }

  Etats etat;

  public void SetEtat(Etats etat) {
    this.etat = etat;
  }

  public Etats getEtat() {
    return this.etat;
  }

}
