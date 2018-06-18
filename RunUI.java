import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.net.URL;

public class RunUI extends JFrame implements ActionListener, KeyListener, ChangeListener {
  
	//a bunch of declarations for various UI components
  private JPanel panel;
  private BufferedImage circuitImg; 
  private JButton nextButton, resetButton, errorButton;
  private JButton[] qButtons;
  private JLabel circuitLabel;
  private JLabel[] highlights;
  private JLabel startLabel, finishLabel, startstate, finishstate, outcomeLabel;
  private Qubit[] q;
  private Qubit goalState;
  private JSlider probability;
  private JLabel probText;
  
  //the current stage of progress that the UI is displaying
  private int stage = 0;
  
  
  public RunUI() {
    super("Quantum Error Correction");

    //set up the qubit states as pressable buttons, allowing the user to flip them
    qButtons = new JButton[3];
    q = new Qubit[3];
    q[0] = new Qubit();
    q[1] = new Qubit();
    q[2] = new Qubit();
    goalState = new Qubit();
    
    //set up the array of pink box highlights to show which stage is being executed
    highlights = new JLabel[6];
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    repaint();
  }
  
  //helper function used to load in image files
  public BufferedImage loadImage(File f) {
    BufferedImage i = null;
    boolean success = true;
    System.out.println(f.getPath());
    try {
      i = ImageIO.read(f);
    } catch (IOException e) {
      System.err.println("Load failed.");
      success = false;
    }
    if (success) {
      System.out.println("Load successful.");
    }
    return i;
  }

  //refreshes the UI
  public void repaint() {
    if (panel != null) {
    pack();
    panel.requestFocus();
    panel.repaint();
    }
  }
  
  //resets all of the three qubits to the zero state
  //used when resetting the demo
  private void resetQubits() {
    for (int i = 0; i < 3; i++) {
      q[i].state = q[i].ZERO_STATE;
    }
    updateQubits();
  }
  
  //updates the button text for the qubit based on the current values of the three qubits
  private void updateQubits() {
    for (int i = 0; i < 3; i++) {
      if (q[i].state == q[i].ZERO_STATE) {
        qButtons[i].setText("|0>");
      } else {
        qButtons[i].setText("|1>");
      }
    }
  }
  
  //enables or disables all the qubit buttons
  private void setQubitsEnabled(boolean set) {
    qButtons[0].setEnabled(set);
    qButtons[1].setEnabled(set);
    qButtons[2].setEnabled(set);
  }
  
  //sets up the start state for the top qubit
  //we need to know this in order to tell if an error has occurred
  private void setStartState(boolean active) {
    if (active) {
      if (q[0].state == q[0].ZERO_STATE) {
        startstate.setText("|0>");
      } else {
        startstate.setText("|1>");
      }
      goalState.state = q[0].state;
    } else {
      startstate.setText("");
    }
  }
  
  //compares the finish state with the start state
  //to detect and display if an error has occurred
  private void setFinishState(boolean active) {
    if (active) {
      if (q[0].state == q[0].ZERO_STATE) {
        finishstate.setText("|0>");
      } else {
        finishstate.setText("|1>");
      }
    } else {
      finishstate.setText("");
    }
  }
  
  //enables/disables buttons, applies bit flips/transformations,
  //generally sets up the UI based on the current stage of execution
  private void handleStage() {
    setHighlights();
    if (stage == 0) {
      setStartState(false);
      setFinishState(false);
      resetQubits();
      outcomeLabel.setText("");
    }
    if (stage == 0 || stage == 3) {
      setQubitsEnabled(true);
    } else {
      setQubitsEnabled(false);
    }
    if (stage == 3) {
      errorButton.setEnabled(true);
    } else {
      errorButton.setEnabled(false);
    }
    if (stage == 7) {
      nextButton.setEnabled(false);
    } else {
      nextButton.setEnabled(true);
    }
    
    if (stage == 1) {
      setStartState(true);
      goalState.state = q[0].state;
    }
    
    if (stage == 7) {
      setFinishState(true);
      if (goalState.state == q[0].state) {
        outcomeLabel.setText("Error Corrected!");
      } else {
        outcomeLabel.setText("Error detected...");
      }
    }
    
    if (stage == 1 || stage == 4) {
      q[1].CNOT(q[0]);
    } else if (stage == 2 || stage == 5) {
      q[2].CNOT(q[0]);
    } else if (stage == 6) {
      Qubit[] others = {q[1], q[2]};
      q[0].CNOT(others);
    }
    updateQubits();
  }
  
  //causes all qubits to have a bit flip with probability p
  private void errorQubits(double p) {
    q[0].bitError(p);
    q[1].bitError(p);
    q[2].bitError(p);
  }
  
  //sets the highlight boxes based  on the stage of execution
  private void setHighlights() {
    for (int i = 0; i < highlights.length; i++) {
      highlights[i].setVisible(stage-1 == i);
    }
  }
  
  //handles button presses and slider changes
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == nextButton) {
      stage++;
      handleStage();
      
    } else if (e.getSource() == resetButton) {
      stage = 0;
      handleStage();
      
    } else if (e.getSource() == errorButton) {
      double p = probability.getValue()/100.0;
      System.out.println(p);
      errorQubits(p);
      updateQubits();
    } else if (e.getSource() == qButtons[0]) {
      q[0].bitFlip();
      updateQubits();
    } else if (e.getSource() == qButtons[1]) {
      q[1].bitFlip();
      updateQubits();
    } else if (e.getSource() == qButtons[2]) {
      q[2].bitFlip();
      updateQubits();
    }
  }
  
  //updates the text for the probability slider
  private void updateProbability() {
    int value = probability.getValue();
    probText.setText("Error Probability = " + value + "%");
  }
  
  public void stateChanged(ChangeEvent e) {
    updateProbability();
  }
  
  public void keyTyped(KeyEvent e) {
  }
  
  public void keyPressed(KeyEvent e) {
  }
  
  public void keyReleased(KeyEvent e) {
  }
  
  
  //initially sets up the UI and readies all the graphical components for use
  public void setupBoard() {
    
    panel = new JPanel() {
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(48, 48, 74, 64);
        
        //test areas for highlights
        //will leave for pixel size reference
        /*g2.drawRect(224, 38, 75, 300);
        g2.drawRect(335, 38, 75, 300);
        
        g2.drawRect(440, 38, 210, 300);
        
        g2.drawRect(681, 38, 75, 300);
        g2.drawRect(792, 38, 75, 300);
        g2.drawRect(904, 38, 75, 300);*/
      }
    };
    
    panel.addKeyListener(this);
    
    try {
    circuitImg = ImageIO.read(RunUI.class.getResourceAsStream("/circuit.png"));
    } catch (IOException ex) {
    	
    }
    
    //define buttons and sliders and stuff//
    
    circuitLabel = new JLabel();
    circuitLabel.setIcon(new ImageIcon(circuitImg));
    panel.add(circuitLabel);
    circuitLabel.setBounds(150, -60, 1000, 500);
    
    nextButton = new JButton("Advance");
    nextButton.addActionListener(this);
    panel.add(nextButton);
    nextButton.setBounds(50, 700, 150, 50);
    
    resetButton = new JButton("Reset");
    resetButton.addActionListener(this);
    panel.add(resetButton);
    resetButton.setBounds(1100, 700, 150, 50);
    
    qButtons[0] = new JButton("|0>");
    qButtons[0].addActionListener(this);
    panel.add(qButtons[0]);
    qButtons[0].setBounds(50, 50, 70, 60);
    qButtons[0].setFont(new Font("Arial", Font.PLAIN, 20));
    
    qButtons[1] = new JButton("|0>");
    qButtons[1].addActionListener(this);
    panel.add(qButtons[1]);
    qButtons[1].setBounds(50, 160, 70, 60);
    qButtons[1].setFont(new Font("Arial", Font.PLAIN, 20));
    
    qButtons[2] = new JButton("|0>");
    qButtons[2].addActionListener(this);
    panel.add(qButtons[2]);
    qButtons[2].setBounds(50, 270, 70, 60);
    qButtons[2].setFont(new Font("Arial", Font.PLAIN, 20));
    
    
    errorButton = new JButton("Generate Errors");
    errorButton.addActionListener(this);
    panel.add(errorButton);
    errorButton.setBounds(475, 700, 150, 50);
    errorButton.setEnabled(false);
    
    probability = new JSlider(0, 100, 50);
    probability.addChangeListener(this);
    panel.add(probability);
    probability.setBounds(475, 600, 150, 75);
    
    probText = new JLabel("Error Probability = 50%");
    panel.add(probText);
    probText.setBounds(485, 645, 150, 75);
    
    
    startLabel = new JLabel("Start State");
    panel.add(startLabel);
    startLabel.setBounds(50, 400, 150, 75);
    startLabel.setFont(new Font("Arial", Font.PLAIN, 30));
    
    startstate = new JLabel("");
    panel.add(startstate);
    startstate.setBounds(50, 450, 150, 75);
    startstate.setFont(new Font("Arial", Font.PLAIN, 30));
    
    finishLabel = new JLabel("Final State");
    panel.add(finishLabel);
    finishLabel.setBounds(1100, 400, 150, 75);
    finishLabel.setFont(new Font("Arial", Font.PLAIN, 30));
    
    finishstate = new JLabel("");
    panel.add(finishstate);
    finishstate.setBounds(1100, 450, 150, 75);
    finishstate.setFont(new Font("Arial", Font.PLAIN, 30));
    
    outcomeLabel = new JLabel("");
    panel.add(outcomeLabel);
    outcomeLabel.setBounds(500, 450, 550, 75);
    outcomeLabel.setFont(new Font("Arial", Font.PLAIN, 30));
    
    panel.addKeyListener(this);
    Dimension dim = new Dimension(1300, 800);
    this.setSize(dim);
    panel.setLayout(null);
    panel.setPreferredSize(dim);
    
    for (int i = 0; i < highlights.length; i++) {
	  try {
		  BufferedImage bf = ImageIO.read(RunUI.class.getResourceAsStream("/" + (i+1) + ".png"));
		  highlights[i] = new JLabel();
		  highlights[i].setIcon(new ImageIcon(bf));
	  } catch (IOException ex) {
	    	    	
	  }
      panel.add(highlights[i]);
    }
    
    highlights[0].setBounds(224, 38, 75, 300);
    highlights[1].setBounds(335, 38, 75, 300);
    highlights[2].setBounds(440, 38, 210, 300);
    highlights[3].setBounds(681, 38, 75, 300);
    highlights[4].setBounds(792, 38, 75, 300);
    highlights[5].setBounds(904, 38, 75, 300);
    
    setHighlights();
    
    pack();
    getContentPane().add(panel);
    //getRootPane().setDefaultButton(startButton);
    panel.setVisible(true);
    repaint();
  }
  
  //initializes and runs the UI
  public void init() {
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        setupBoard();
        setVisible(true);
      }
    });
  }
  
}