import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel {
	
	private DefaultListModel	model1;										// list model
	private DefaultListModel	model2;										// list model
	private JList				list, user;								// lists
	private JTextField			tName			= new JTextField(10);		// name field
	private JButton				bAdd			= new JButton("Add");		// add button
	private JButton				bRemove			= new JButton("Remove");	// remove button
	private JRadioButton		rList			= new JRadioButton("List");		// list radio button
	private JRadioButton		rMessage		= new JRadioButton("Message");	// list radio button
	
	private Mediator med = new Mediator();
	
	public Main() {
		init();
	}
	
	public void init() {
		// initialize model fisiere
		model1 = new DefaultListModel();
		model1.addElement("File1");
		model1.addElement("File2");
		model1.addElement("File3");
		
		// initialize model utilizatori
		model2 = new DefaultListModel();
		model2.addElement("User1");
		model2.addElement("User2");
		model2.addElement("User3");
		
		tName.setName("tname");
		bAdd.setName("bAdd");
		bRemove.setName("bRemove");
		
		// initialize lists, based on the specific model
		list = new JList(model1);
		list.setName("list");
		
		//user = new JList(new ReverseListModel(model));
		user = new JList(model2);
		user.setName("user");		
		
		// init radios
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rList);
		radioGroup.add(rMessage);
		rList.setSelected(true);
		
		// main panel
		JPanel top = new JPanel(new FlowLayout());
		
		JPanel middle = new JPanel(new GridLayout(2,0));
		middle.setName("middle");
		
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setName("bottom");
		
		JPanel right = new JPanel(new GridLayout(1,0));
		right.setPreferredSize(new Dimension(100,getHeight()));
		right.setName("right");

		this.setLayout(new BorderLayout(10,10));
		this.add(top, BorderLayout.NORTH);
		this.add(middle, BorderLayout.CENTER);
		this.add(right,BorderLayout.LINE_END);
		this.add(bottom, BorderLayout.PAGE_END);
		
		// top panel: two radios
		//top.add(rList);
		//top.add(rMessage);
		
		// middle panel: the two lists (scrollable)
		JScrollPane jsp = new JScrollPane(list);
		jsp.setName("scroll");
		middle.add(jsp);
		
		JScrollPane jsp2 = new JScrollPane(user);
		//jsp2.setPreferredSize(new Dimension(450,110));
		jsp2.setName("scroll2");
		right.add(jsp2);
		
        JTable table = new JTable();
        table.setModel(new MyModel());//invoking our custom model
        table.setDefaultRenderer(JLabel.class,  new Renderer());// for the rendering of cell
        JScrollPane jp = new JScrollPane(table);
        TableColumn myCol = table.getColumnModel().getColumn(3);
        myCol.setCellRenderer(new CustomProgressBar());
        middle.add(jp);
		
		// bottom panel: name field, add button, remove button
        /*
		final JTable table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		
		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setName("tabel");
		middle.add(scrollPane);
		*/
		bottom.add(tName);
		bottom.add(bAdd);
		bottom.add(bRemove);
		
		
		// mediator init
		med.registerList(list);
		med.registeruser(user);
		med.registerName(tName);
		med.registerAdd(bAdd);
		med.registerRemove(bRemove);
		
		bAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.add();
				/*
				String text = tName.getText();
				if (text.isEmpty()) {
					JOptionPane.showMessageDialog(
							null, "Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				model.addElement(text);
				*/
			}
		});
		
		bRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.remove();
				/*
				int index = list.getSelectedIndex();
				if (index == -1) {
					JOptionPane.showMessageDialog(
							null, "No item selected!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				model.remove(index);
				*/
			}
		});
		
		rList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.list();
			}
		});
		
		rMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.message();
			}
		});
		
	}
	
	public static void buildGUI() {
		JFrame frame = new JFrame("Swing stuff"); // title
		frame.setContentPane(new Main()); // content
		frame.setSize(500, 500); // width / height
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}


	public static void main(String[] args) {
		// run on EDT (event dispatch thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});
	}

}
