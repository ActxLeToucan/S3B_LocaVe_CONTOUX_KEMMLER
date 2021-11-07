public class TriggerInvalidException extends Throwable {
    public TriggerInvalidException(String triggerName) {
        super(triggerName + " n'est pas un nom de trigger valide.");
    }
}
