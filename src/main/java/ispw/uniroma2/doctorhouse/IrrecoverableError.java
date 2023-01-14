package ispw.uniroma2.doctorhouse;

public class IrrecoverableError extends Error {
    public IrrecoverableError(Exception cause) {
        super(cause);
    }
}
