import javax.swing.*;
import javax.swing.event.*;

public class ReverseListModel extends AbstractListModel implements ListDataListener {
	
	private ListModel innerModel;
	
	public ReverseListModel(ListModel innerModel) {
		this.innerModel = innerModel;
		innerModel.addListDataListener(this);
	}

	@Override
	public Object getElementAt(int index) {
		int newIndex = getSize() - 1 - index;
		return innerModel.getElementAt(newIndex);
	}

	@Override
	public int getSize() {
		return innerModel.getSize();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		int index0 = e.getIndex0(), index1 = e.getIndex1();
		System.out.println("contentsChanged: " + index0 + " - " + index1);
		fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		int index0 = e.getIndex0(), index1 = e.getIndex1();
		System.out.println("intervalAdded: " + index0 + " - " + index1 + " : " + getSize());
		fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		int index0 = e.getIndex0(), index1 = e.getIndex1();
		System.out.println("intervalRemoved: " + index0 + " - " + index1 + " : " + getSize());
		fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
	}

}
