import javax.swing.JOptionPane;

public class MessageState extends State {

	private Mediator med;
	
	public MessageState(Mediator med) {
		this.med = med;
	}
	
	@Override
	public void add() {
		JOptionPane.showMessageDialog(
				null, "Add pressed", "Message", JOptionPane.INFORMATION_MESSAGE);
	}
}
