public class Main {
  
  public static void main(String[] args) {
    RunUI ui = new RunUI();
    ui.init();
  }
  
  
  
  
  //all of the below is testing code used to ensure the logic in the Qubit class is implemented correctly
  //no need to use this in the final version, will leave here just for reference
  public static void bitTest() {
    Qubit q0 = new Qubit();
    Qubit q1 = new Qubit();
    Qubit q2 = new Qubit();
    
    q0.bitError(.5);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    q1.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    q2.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    double probability = .1;
    q0.bitError(probability);
    q1.bitError(probability);
    q2.bitError(probability);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    q1.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    q2.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    Qubit[] others = {q1, q2};
    q0.CNOT(others);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
  }
  
  public static void phaseTest() {
    Qubit q0 = new Qubit();
    Qubit q1 = new Qubit();
    Qubit q2 = new Qubit();
    
    q0.bitError(.5);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    q1.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    q2.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    q0.hadamard();
    q1.hadamard();
    q2.hadamard();
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    double probability = .1;
    q0.phaseError(probability);
    q1.phaseError(probability);
    q2.phaseError(probability);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    q0.hadamard();
    q1.hadamard();
    q2.hadamard();
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    q1.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    q2.CNOT(q0);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
    
    Qubit[] others = {q1, q2};
    q0.CNOT(others);
    System.out.println(q0.toString() + " " + q1.toString() + " " + q2.toString());
  }
  
}