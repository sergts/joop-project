/*
	File: FileShareApp.java
	Copyright 2007 by Nadeem Abdul Hamid, Patrick Valencia

	Permission to use, copy, modify, and distribute this software and its
	documentation for any purpose and without fee is hereby granted, provided
	that the above copyright notice appear in all copies and that both the
	copyright notice and this permission notice and warranty disclaimer appear
	in supporting documentation, and that the names of the authors or their
	employers not be used in advertising or publicity pertaining to distri-
	bution of the software without specific, written prior permission.

	The authors and their employers disclaim all warranties with regard to
	this software, including all implied warranties of merchantability and
	fitness. In no event shall the authors or their employers be liable for 
	any special, indirect or consequential damages or any damages whatsoever 
	resulting from loss of use, data or profits, whether in an action of 
	contract, negligence or other tortious action, arising out of or in 
	connection with the use or performance of this software, even if 
	advised of the possibility of such damage.

	Date		Author				Changes
	Feb 07 2007	Nadeem Abdul Hamid	Add to project (from source by P. Valencia)
 */


package project.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

import project.*;
import project.client.*;
import project.messages.*;



/**
 * The GUI for a simple peer-to-peer file sharing
 * application. 
 * 
 * @author Nadeem Abdul Hamid
 */
@SuppressWarnings("serial")
public class GUI extends JFrame
{

	private static final int FRAME_WIDTH = 700, FRAME_HEIGHT = 500;

	private JPanel filesPanel, peersPanel, logsPanel;
	private JPanel lowerFilesPanel, lowerPeersPanel;
	private DefaultListModel filesModel, peersModel, logsModel;
	private JList filesList, peersList, logsList;


	private JButton downloadFilesButton, shareFilesButton, searchFilesButton;
	private JButton removePeersButton, refreshPeersButton, setNameButton;

	private JTextField shareTextField, searchTextField;
	private JTextField setNameTextField;

	private Client client;


	private GUI(){
		client = new Client();
		

		downloadFilesButton = new JButton("Download");
		downloadFilesButton.addActionListener(new DownloadListener());
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

	
	private void setupFrame(JFrame frame)
	{


		frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new BorderLayout());


		JPanel upperPanel = new JPanel();
		JPanel lowerPanel = new JPanel();
		JPanel lowestPanel = new JPanel();
				
		
		upperPanel.setLayout(new GridLayout(1, 2));
		upperPanel.setPreferredSize(new Dimension(FRAME_WIDTH, (FRAME_HEIGHT * 1/2)));
		lowerPanel.setLayout(new GridLayout(1, 2));
		lowestPanel.setLayout(new GridLayout(1, 0));


		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		filesModel = new DefaultListModel();
		filesList = new JList(filesModel);
		peersModel = new DefaultListModel();
		peersList = new JList(peersModel);
		logsModel = new DefaultListModel();
		logsList = new JList(logsModel);
		filesPanel = initPanel(new JLabel("Available Files"), filesList, 250, 200);
		peersPanel = initPanel(new JLabel("Peer List"), peersList, 150, 200);
		logsPanel = initPanel(new JLabel("Logs"), logsList, 500, 100);
		lowerFilesPanel = new JPanel();
		lowerPeersPanel = new JPanel();

		filesPanel.add(downloadFilesButton);
		

		lowerFilesPanel.add(shareTextField);
		lowerFilesPanel.add(shareFilesButton);
		lowerFilesPanel.add(searchTextField);
		lowerFilesPanel.add(searchFilesButton);	

		lowerPeersPanel.add(setNameTextField);
		lowerPeersPanel.add(setNameButton);

		upperPanel.add(filesPanel);
		upperPanel.add(peersPanel);
		lowerPanel.add(lowerFilesPanel);
		lowerPanel.add(lowerPeersPanel);
		lowestPanel.add(logsPanel);



		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(lowerPanel, BorderLayout.CENTER);
		frame.add(lowestPanel, BorderLayout.SOUTH);

		frame.setVisible(true);

	}

	
	private JPanel initPanel(JLabel textField,JList list, int x, int y){
		JPanel panel = new JPanel();
		panel.add(textField);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(x, y));
		panel.add(scrollPane);
		return panel;
	}
	
	

	
	private void updateFileList() {
		filesModel.removeAllElements();
		if(client.getFilesOnServer() != null){
			for (String filename : client.getFilesOnServer().keySet()) {
					filesModel.addElement(filename);
			}
		}
	}


	private void updatePeerList(){
		peersModel.removeAllElements();
		if(client.getUsersOnServer() != null){
			for (String username : client.getUsersOnServer()) {
				peersModel.addElement(username);
			}
		}
	}

	
	class DownloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(filesList.getSelectedValue() != null){
				String selected = filesList.getSelectedValue().toString();
				String filename = selected.substring(0, selected.indexOf(':')).trim();
				String owner = selected.substring(selected.indexOf(':') + 1, selected.length()).trim();
				client.getOut().addMessage(new DownloadMessage(filename, owner));
			}
		}
	}

	class ShareListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			
			String dir = shareTextField.getText().trim();
			
			if(!client.isInitDir()){
				System.out.println("reading");
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
				if(client.getClientState() == 0 || client.getClientState() == 2){
					client.getOut().addMessage(new InitName(name));
					client.setState(1);
				}
			}
			
			setNameTextField.requestFocusInWindow();
			setNameTextField.setText("");
			
			
			
			
		}
	}


	class RefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if(client.getClientState() == 3){
				setNameTextField.setVisible(false);
				setNameButton.setVisible(false);
				client.getOut().addMessage(new FilesQuery());
				client.getOut().addMessage(new WhoMessage());
			}
			updateFileList();
			updatePeerList();
			
		}
	}



	public static void main(String[] args) throws IOException
	{

		new GUI();

	}

}
