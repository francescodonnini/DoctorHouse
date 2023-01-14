package ispw.uniroma2.doctorhouse;

public class IrrecoverableError extends Error {
    private final Exception cause;

    public IrrecoverableError(Exception cause) {
        this.cause = cause;
    }

    @Override
    public Exception getCause() {
        return cause;
    }
}
