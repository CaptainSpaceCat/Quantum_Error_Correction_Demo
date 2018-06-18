import java.lang.Math;

//simple representation of qubit, includes the |0>, |1>, |+>, and |-> states
//along with transitions between them
public class Qubit {
  
  public int ZERO_STATE = 0;
  public int ONE_STATE = 1;
  public int PLUS_STATE = 2;
  public int MINUS_STATE = 3;
  
  //current state of this qubit
  public int state;
  
  public Qubit() {
    state = ZERO_STATE;
  }
  
  public Qubit(int s) {
    state = s;
  }
  
  public String toString() {
    String result = "|";
    if (state == ZERO_STATE) {
      result += "0";
    } else if (state == ONE_STATE) {
      result += "1";
    } else if (state == PLUS_STATE) {
      result += "+";
    } else if (state == MINUS_STATE) {
      result += "-";
    } 
    result += ">";
    return result;
  }
  
  public void setState(int s) {
    state = s;
  }
  
  public void bitFlip() {
    if (state == ZERO_STATE) {
      state = ONE_STATE;
    } else if (state == ONE_STATE) {
      state = ZERO_STATE;
    }
  }
  
  public void phaseFlip() {
    if (state == PLUS_STATE) {
      state = MINUS_STATE;
    } else if (state == MINUS_STATE) {
      state = PLUS_STATE;
    }
  }
  
  //flips this qubit's bit with probability p
  public void bitError(double probability) {
    if (Math.random() <= probability) {
      bitFlip();
    }
  }
  
  //flips this qubit's phase with probability p
  public void phaseError(double probability) {
    if (Math.random() <= probability) {
      phaseFlip();
    }
  }
  
  public void hadamard() {
    if (state == ZERO_STATE) {
      state = PLUS_STATE;
    } else if (state == ONE_STATE) {
      state = MINUS_STATE;
    } else if (state == PLUS_STATE) {
      state = ZERO_STATE;
    } else if (state == MINUS_STATE) {
      state = ONE_STATE;
    } 
  }
  
  //applies a CNOT gate to this qubit, as if the other qubit is the source state
  public void CNOT(Qubit other) {
    if (other.state == ONE_STATE) {
      bitFlip();
    } else if (state == MINUS_STATE) {
      other.phaseFlip();
    }
  }
  
  //applies a CNOT gate to this qubit, as if  the other qubits passed are all source states
  public void CNOT(Qubit[] others) {
    boolean flag = true;
    for (int i = 0; i < others.length; i++) {
      if (!(others[i].state == ONE_STATE)) {
        flag = false;
        break;
      }
    }
    if (flag) {
      bitFlip();
    }
  }
  
}