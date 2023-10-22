import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorApp extends JFrame {
    private JTextField inputField;
    private JButton[] numberButtons;
    private JButton addButton, subtractButton, multiplyButton, divideButton, equalsButton, clearButton;
    private JRadioButton traditionalRadio, rpnRadio;

    private boolean isTraditional = true;
    private String currentOperator = "";
    private double firstOperand = 0;

    public CalculatorApp() {
        setTitle("Calcolatrice");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(280, 40));
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setEditable(false);

        traditionalRadio = new JRadioButton("Tradizionale");
        rpnRadio = new JRadioButton("RPN");
        traditionalRadio.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(traditionalRadio);
        group.add(rpnRadio);

        JPanel radioPanel = new JPanel();
        radioPanel.add(traditionalRadio);
        radioPanel.add(rpnRadio);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4));
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(Integer.toString(i));
            buttonPanel.add(numberButtons[i]);
        }

        addButton = new JButton("+");
        subtractButton = new JButton("-");
        multiplyButton = new JButton("*");
        divideButton = new JButton("/");
        equalsButton = new JButton("=");
        clearButton = new JButton("C");

        buttonPanel.add(addButton);
        buttonPanel.add(subtractButton);
        buttonPanel.add(multiplyButton);
        buttonPanel.add(divideButton);
        buttonPanel.add(equalsButton);
        buttonPanel.add(clearButton);

        setLayout(new FlowLayout());
        add(inputField);
        add(radioPanel);
        add(buttonPanel);

        traditionalRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isTraditional = true;
            }
        });

        rpnRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isTraditional = false;
            }
        });

        for (int i = 0; i < 10; i++) {
            final int digit = i;
            numberButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inputField.setText(inputField.getText() + digit);
                }
            });
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("+");
            }
        });

        subtractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("-");
            }
        });

        multiplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("*");
            }
        });

        divideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("/");
            }
        });

        equalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateResult();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInput();
            }
        });
    }

    private void performOperation(String operator) {
        currentOperator = operator;
        firstOperand = Double.parseDouble(inputField.getText());
        clearInput();
    }

    private void calculateResult() {
        double secondOperand = Double.parseDouble(inputField.getText());
        double result = 0;

        if (isTraditional) {
            switch (currentOperator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "*":
                    result = firstOperand * secondOperand;
                    break;
                case "/":
                    result = firstOperand / secondOperand;
                    break;
            }
        } else {
            RPNCalculator rpnCalculator = new RPNCalculator();
            rpnCalculator.push(firstOperand);
            rpnCalculator.push(secondOperand);

            switch (currentOperator) {
                case "+":
                    result = rpnCalculator.add();
                    break;
                case "-":
                    result = rpnCalculator.subtract();
                    break;
                case "*":
                    result = rpnCalculator.multiply();
                    break;
                case "/":
                    result = rpnCalculator.divide();
                    break;
            }
        }

        inputField.setText(Double.toString(result));
    }

    private void clearInput() {
        inputField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CalculatorApp calculator = new CalculatorApp();
                calculator.setVisible(true);
            }
        });
    }
}

class RPNCalculator {
    private Stack<Double> stack;

    public RPNCalculator() {
        stack = new Stack<>();
    }

    public void push(double value) {
        stack.push(value);
    }

    public double add() {
        if (stack.size() < 2)
            throw new IllegalArgumentException("Insufficient operands");

        double operand2 = stack.pop();
        double operand1 = stack.pop();
        return operand1 + operand2;
    }

    public double subtract() {
        if (stack.size() < 2)
            throw new IllegalArgumentException("Insufficient operands");

        double operand2 = stack.pop();
        double operand1 = stack.pop();
        return operand1 - operand2;
    }

    public double multiply() {
        if (stack.size() < 2)
            throw new IllegalArgumentException("Insufficient operands");

        double operand2 = stack.pop();
        double operand1 = stack.pop();
        return operand1 * operand2;
    }

    public double divide() {
        if (stack.size() < 2)
            throw new IllegalArgumentException("Insufficient operands");

        double operand2 = stack.pop();
        double operand1 = stack.pop();

        if (operand2 == 0)
            throw new ArithmeticException("Division by zero");

        return operand1 / operand2;
    }
}