package LocaVe;

public class DateInvalidFormatException extends Throwable {
    public DateInvalidFormatException() {
        super("Mauvais format de date, utilisez YYYY-MM-DD.");
    }
}
