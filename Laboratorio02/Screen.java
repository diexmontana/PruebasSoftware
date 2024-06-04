package ATMCaseStudy;
import java.util.ArrayList;
import java.util.List;
//Screen.java
// Represents the screen of the ATM

public class Screen {
	
	public void dispalyDollarAmount(double amount) {
		System.out.printf("$%,.2f", amount);
	}
	
	private List<String> messages;

    public Screen() {
        messages = new ArrayList<>();
    }

    public void displayMessage(String message) {
        System.out.print(message);
        messages.add(message);
    }

    public void displayMessageLine(String message) {
        System.out.println(message);
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }

    public List<String> getMessages() {
        return messages;
    }
}// end class screen