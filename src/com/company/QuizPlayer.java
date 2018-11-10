package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class QuizPlayer {


    private JTextArea display;
    private JTextArea answer;
    private ArrayList<Qanda> cardList;
    private Iterator cardIterator;
    private Qanda currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;




    public QuizPlayer() {

        // Build UI
        frame = new JFrame("Flash Card Player");
        JPanel mainPanel = new JPanel();
        Font mFont = new Font("Helvetica Neue", Font.BOLD, 23);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display = new JTextArea(10, 20);
        display.setFont(mFont);

        JScrollPane qJScrollPane = new JScrollPane(display);
        qJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nextButton = new JButton("Show Answer");
        mainPanel.add(qJScrollPane);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load Card set");
        loadMenuItem.addActionListener(new OpenMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);
    }


    public static void main (String[] args) {
        // Create the frame on the event dispatching thread
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new QuizPlayer();

            }
        });

    }


    public class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isShowAnswer) {
                // Show the answer since they've seen the question
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next Card");
                isShowAnswer = false;
            }else {
                // show next question
                if (cardIterator.hasNext()) {

                    //we know our arraylist of cards is not empty, so show next card
                    showNextCard();
                }else {

                    // no more cards to show
                    display.setText("That was last card.");
                    nextButton.setEnabled(false);

                }
            }

        }



    }

    public class OpenMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());

        }

    }

    public void loadFile(File file) {

        cardList = new ArrayList<Qanda>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;

            while (( line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("Couldn't read the card file");
            e.printStackTrace();
        }

        // show the first card
        cardIterator = cardList.iterator();
        showNextCard();


    }

    public void makeCard(String lineToParse) {
        //String[] result = lineToParse.split("/");
        StringTokenizer result = new StringTokenizer(lineToParse, "/");
        if (result.hasMoreTokens()) {

            Qanda card = new Qanda(result.nextToken(), result.nextToken());
            cardList.add(card);
            System.out.println("Made a Card" + card.getQuestion());


        }

    }


    private void showNextCard() {

        currentCard = (Qanda) cardIterator.next();


        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;


    }


}
