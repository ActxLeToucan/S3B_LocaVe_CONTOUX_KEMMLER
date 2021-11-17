/**
 * interface sujet a observer
 */
public interface Sujet {
    /**
     * enregistre un nouvel observateur
     * @param observateur
     *          nouvel observateur
     */
    public void enregistrerObservateur(Observateur observateur);

    /**
     * supprime un observateur
     * @param observateur
     *          observateur a supprimer
     */
    public void supprimerObservateur(Observateur observateur);

    /**
     * notifie les observateurs
     */
    public void notifierObservateurs();
}
