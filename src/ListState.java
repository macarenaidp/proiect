public class ListState extends State {
	
	private Mediator med;
	
	public ListState(Mediator med) {
		this.med = med;
	}
	
	@Override
	public void add() {
		med.addList();
	}
	
	@Override
	public void remove() {
		med.removeList();
	}
}
