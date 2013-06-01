
package project.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import javax.swing.*;
import project.messages.*;


/**
 * GUI design inspired by p2p app http://cs.berry.edu/~nhamid/p2p/framework-java.html 
 * by Nadeem Abdul Hamid
 *  
 * @author Sergei Tsimbalist
 *
 */
@SuppressWarnings("serial")
public class GUI extends JFrame{

	private static final int FRAME_WIDTH = 700, FRAME_HEIGHT = 600;
	private JPanel filesPanel, usersPanel, logsPanel, lowerFilesPanel, lowerPeersPanel;
	private DefaultListModel<String> filesModel, usersModel, logsModel;
	private JList<String> filesList, usersList, logsList;
	private JButton downloadFilesButton, shareFilesButton, searchFilesButton, setNameButton, refreshDataButton;
	private JTextField shareTextField, searchTextField, setNameTextField;
	private Client client;
	
	public static void main(String[] args) throws IOException{

		new GUI("localhost", 8888);

	}


	private GUI(String server, int port){
		
		client = new Client(server, port);
		

		downloadFilesButton = new JButton("Download");
		downloadFilesButton.addActionListener(new DownloadListener());
		refreshDataButton = new JButton("Refresh data");
		refreshDataButton.addActionListener(new RefreshDataListener());
		shareFilesButton = new JButton("Share");
		shareFilesButton.addActionListener(new ShareListener());
		searchFilesButton = new JButton("Search");
		searchFilesButton.addActionListener(new SearchListener());
		setNameButton = new JButton("Set Name");
		setNameButton.addActionListener(new SetNameListener());
		shareTextField = new JTextField(15);
		searchTextField = new JTextField(15);
		setNameTextField = new JTextField(15);
		setupFrame(this);

		(new Thread() { public void run() {  }}).start();

		new javax.swing.Timer(3000, new RefreshListener()).start();
		

		
	}

	
	private void setupFrame(JFrame frame){


		frame = new JFrame(client.getGUILabel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel upperPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel lowerPanel = new JPanel();	
		upperPanel.setLayout(new GridLayout(1, 2));
		//upperPanel.setPreferredSize(new Dimension(FRAME_WIDTH, (FRAME_HEIGHT * 1/2)));
		midPanel.setLayout(new GridLayout(1, 2));
		lowerPanel.setLayout(new GridLayout(1, 0));

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		filesModel = new DefaultListModel<String>();
		filesList = new JList<String>(filesModel);
		usersModel = new DefaultListModel<String>();
		usersList = new JList<String>(usersModel);
		logsModel = new DefaultListModel<String>();
		logsList = new JList<String>(logsModel);
		filesPanel = initPanel(new JLabel("Files"), filesList, 300, 200, false);
		usersPanel = initPanel(new JLabel("Users"), usersList, 115, 200, false);
		logsPanel = initPanel(new JLabel("Logs"), logsList, 500, 200, true);
		lowerFilesPanel = new JPanel();
		lowerPeersPanel = new JPanel();

		filesPanel.add(downloadFilesButton);
		filesPanel.add(refreshDataButton);
		
		lowerFilesPanel.add(shareTextField);
		lowerFilesPanel.add(shareFilesButton);
		lowerFilesPanel.add(searchTextField);
		lowerFilesPanel.add(searchFilesButton);
		
		

		lowerPeersPanel.add(setNameTextField);
		lowerPeersPanel.add(setNameButton);

		upperPanel.add(filesPanel);
		upperPanel.add(usersPanel);
		midPanel.add(lowerFilesPanel);
		midPanel.add(lowerPeersPanel);
		lowerPanel.add(logsPanel);


		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		
		refreshDataButton.setVisible(false);
		setNameTextField.setVisible(false);
		setNameButton.setVisible(false);
		searchTextField.setVisible(false);
		searchFilesButton.setVisible(false);

		frame.setVisible(true);

	}

	
	private JPanel initPanel(JLabel textField,JList<String> list, int x, int y, boolean bottom){
		JPanel panel = new JPanel();
		panel.add(textField);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		if(bottom){
			scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
		        public void adjustmentValueChanged(AdjustmentEvent e) {  
		            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
		        }
		    });
		}
		scrollPane.setPreferredSize(new Dimension(x, y));
		panel.add(scrollPane);
		return panel;
	}
	
	private void updateLogsList() {

		Iterator<String> logs = client.getLogger().iterator();
		String log;
		int index = 0;
		while(logs.hasNext()){
			log = logs.next();
			if(index >= logsModel.getSize()){
				logsModel.addElement(log);
			}
			index++;
			
		}	
	}

	
	private void updateFileList() {
		filesModel.removeAllElements();
		if(client.getFilesOnServer() != null){
			String element;
			for (String filename : client.getFilesOnServer().keySet()) {
				element = filename + " : "+ formatSize(client.getFilesOnServer().get(filename).getSize());
				filesModel.addElement(element);
			}
		}
	}


	private void updateUserList(){
		usersModel.removeAllElements();
		if(client.getUsersOnServer() != null){
			for (String username : client.getUsersOnServer()) {
				usersModel.addElement(username);
			}
		}
	}

	
	class DownloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(filesList.getSelectedValue() != null){
				String selected = filesList.getSelectedValue().toString();
				String filename = selected.substring(0, selected.indexOf(':') - 1);
				
				String sub = (selected.substring(selected.indexOf(':') + 1,selected.length()));
				String owner = sub.substring(0, sub.indexOf(':')).trim();
				
				System.out.println(filename + ' ' + owner);
				
				client.getOut().addMessage(new DownloadMessage(filename, owner));
			}
		}
	}

	class ShareListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			
			String dir = shareTextField.getText().trim();
			
			if(!client.isInitDir()){
				client.setDirectory("share " + dir);
				client.setState(0);
			}else{
				if (!dir.equals("")) {
					client.parseMessage("share " + dir);
				}
				shareTextField.requestFocusInWindow();
				
				updateFileList();
			}
			
			
			
		}
	}

	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String search = searchTextField.getText().trim();
			client.getOut().addMessage(new FilesQuery(search));

			searchTextField.requestFocusInWindow();
			searchTextField.setText("");
		}
	}
	
	class SetNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = setNameTextField.getText().trim();
			if(name.length() > 0){
				if(name.indexOf(" ") != -1 || name.indexOf(":") != -1 ){
					client.getLogger().add("Illegal name, don't use whitespaces or colons");
				}
				else if(client.getClientState() == 0 || client.getClientState() == 2){
					client.getOut().addMessage(new InitName(name));
					client.setState(1);
					client.setName(name);
				}
			}
			
			setNameTextField.requestFocusInWindow();
			setNameTextField.setText("");
			
			
			
			
		}
	}


	class RefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if(client.isInitDir() && client.getClientState() != 3){
				setNameTextField.setVisible(true);
				setNameButton.setVisible(true);
			}
			if(client.getClientState() == 3){
				setNameTextField.setVisible(false);
				setNameButton.setVisible(false);
				searchTextField.setVisible(true);
				searchFilesButton.setVisible(true);
				refreshDataButton.setVisible(true);
				client.getOut().addMessage(new WhoMessage());
			}
			updateFileList();
			updateUserList();
			updateLogsList();
			
		}
	}
	
	class RefreshDataListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			client.getOut().addMessage(new FilesQuery());
			client.getOut().addMessage(new WhoMessage());
			updateFileList();
			updateUserList();
			updateLogsList();
			
		}
	}
	
	public static String formatSize(long bytes) {
	    int unit = 1000;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    char pre = ("kMGTPE").charAt(exp-1);
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}



	
}
