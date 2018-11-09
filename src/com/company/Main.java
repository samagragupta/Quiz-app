package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    //Instance variables
    private JTextArea question;
    private JTextArea answer;
    private ArrayList<Qanda> cardList;
    private JFrame frame;

    public Main(){

        //Setup Window
        frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create a Panel
        JPanel mainPanel = new JPanel();

        //Font
        Font newfont = new Font("Helvetica Neue",Font.BOLD,21);

        //Question Area
        question = new JTextArea(6,20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(newfont);

        //Make it scrollable
        JScrollPane qJScrollPane = new JScrollPane(question);
        qJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        qJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Answer area
        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(newfont);

        //Make it scrollable
        JScrollPane aJScrollPane = new JScrollPane(answer);
        aJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        aJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        //Next card Button
        JButton nextButton = new JButton("Next Question");

        //Create a few labels
        JLabel qJLabel = new JLabel("Question");
        JLabel aJLabel = new JLabel("Answer");

        // Add components to mainPanel
        mainPanel.add(qJLabel);
        mainPanel.add(qJScrollPane);
        mainPanel.add(aJLabel);
        mainPanel.add(aJScrollPane);
        mainPanel.add(nextButton);

        //Add menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);


        frame.setJMenuBar(menuBar);

        cardList = new ArrayList<Qanda>(); //create an arrayList of Quizcards objects

        //Add actionListener
        nextButton.addActionListener(new NextCardListener());
        newMenuItem.addActionListener(NewMenuListener);
        saveMenuItem.addActionListener(new SavaMenuListener());

        //Add to the Frame
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 600);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        // Create the frame on the event dispatching thread
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Main();

            }
        });

    }

    public class NextCardListener implements ActionListener{
        @Override
        public void actionPerformed (ActionEvent e){
            Qanda card = new Qanda(question.getText(),answer.getText());
            cardList.add(card);
            clearCard();
        }
    }

    public class SavaMenuListener implements ActionListener{
        @Override
        public void actionPerformed (ActionEvent e) {
            Qanda card = new Qanda(question.getText(), answer.getText());
            cardList.add(card);

            //Save file dialog
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    public class NewMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardList.clear();
            clearCard();

        }
    }

    private void saveFile(File selectedFile) {
        try {

            //We'll use BufferedWriter Class for efficiency reasons
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

            Iterator<Qanda> cardIterator = cardList.iterator();
            while (cardIterator.hasNext()) {
                Qanda card = (Qanda) cardIterator.next();
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");

            }
            writer.close(); // need to always close when done!

        } catch (IOException e) {
            System.out.println("Couldn't write to file");
            e.printStackTrace();
        }
    }

    public void clearCard(){
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

}
