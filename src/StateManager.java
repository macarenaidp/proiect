public class StateManager {
	
	private ListState		listState;
	private MessageState	messageState;
	private State			crtState;
	
	public StateManager(Mediator med) {
		listState		= new ListState(med);
		messageState	= new MessageState(med);
		
		crtState = listState;
	}
	
	public void add() {
		crtState.add();
	}
	
	public void remove() {
		crtState.remove();
	}
	
	public void setListState() {
		crtState = listState;
	}
	
	public void setMessageState() {
		crtState = messageState;
	}
}
