# Morse Code Automata (Mealy & Moore Models)

This repository contains the design and simulation of two **Deterministic Finite Automata (DFA)** created to decode Morse code into Latin characters. The projects were developed using the **JFLAP** tool.

## Project Files

The repository includes two main `.jff` files:

1. **MealyMorse(ics23078).jff**
   **Model Type**: Mealy Machine.
   **Logic**: Outputs (letters) are generated during transitions based on the input symbols.
   **Structure**: Features a streamlined set of states (q0 to q26) where transitions for dots (`.`), dashes (`-`), and termination signals (`s`/`S`) trigger the character output.

2. **MooreMorse(ics23078).jff**
   **Model Type**: Moore Machine.
   **Logic**: Outputs are determined by the state itself rather than the transition.
   **Structure**: Utilizes a more detailed state network (Q0 to Q78) to handle the sequential nature of Morse signals and map them to specific output states.

## 🛠 How to Use

To view or simulate these automata:
1. Download the **JFLAP** executable from [jflap.org](http://www.jflap.org/).
2. Open JFLAP and go to `File` -> `Open`.
3. Select either the Mealy or Moore `.jff` file.
4. To test inputs, navigate to `Input` -> `Step with State` and enter a Morse sequence (e.g., `.-s` for 'A').

## ℹ️ Technical Specifications
* **Input Alphabet**: 
    * `.` : Dot
    * `-` : Dash
    * `s` /  : Termination signal to finalize the current letter
    * `S` : Space signal
* **Output Alphabet**: Latin characters A-Z.
