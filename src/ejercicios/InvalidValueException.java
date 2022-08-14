package ejercicios;

public class InvalidValueException extends Exception {

	private static final long serialVersionUID = -142249735795450327L;

	public InvalidValueException() {
	}

	public InvalidValueException(String message) {
		super(message);
	}

}
