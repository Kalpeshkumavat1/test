package behavioural;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// === Strategy Interface ===
interface QuestionStrategy {
    String nextQuestion(java.util.List<String> questions, int currentIndex, int score);
}

// === Concrete Strategies ===
class SequentialStrategy implements QuestionStrategy {
    @Override
    public String nextQuestion(java.util.List<String> questions, int currentIndex, int score) {
        if (currentIndex + 1 < questions.size()) {
            return questions.get(currentIndex + 1);
        }
        return "No more questions.";
    }
}

class RandomStrategy implements QuestionStrategy {
    private Random rand = new Random();
    @Override
    public String nextQuestion(java.util.List<String> questions, int currentIndex, int score) {
        return questions.get(rand.nextInt(questions.size()));
    }
}

class AdaptiveStrategy implements QuestionStrategy {
    @Override
    public String nextQuestion(java.util.List<String> questions, int currentIndex, int score) {
        // Simple adaptive logic: if score is high, skip ahead to harder questions
        if (score > 70 && currentIndex + 2 < questions.size()) {
            return questions.get(currentIndex + 2); // jump ahead
        }
        return currentIndex + 1 < questions.size() ? questions.get(currentIndex + 1) : "No more questions.";
    }
}

// === Context Class ===
class QuizEngine {
    private QuestionStrategy strategy;
    private java.util.List<String> questions;
    private int currentIndex = -1;
    private int score = 0;
    private JTextArea output;

    public QuizEngine(java.util.List<String> questions, JTextArea output) {
        this.questions = questions;
        this.output = output;
    }

    public void setStrategy(QuestionStrategy strategy) {
        this.strategy = strategy;
        output.append("[INFO] Strategy set to: " + strategy.getClass().getSimpleName() + "\n");
    }

    public void setScore(int score) {
        this.score = score;
        output.append("[INFO] Score updated to: " + score + "\n");
    }

    public void startQuiz() {
        currentIndex = -1;
        output.append("[INFO] Quiz started!\n");
    }

    public void nextQuestion() {
        if (strategy == null) {
            output.append("[ERROR] No strategy selected!\n");
            return;
        }
        currentIndex++;
        String q = strategy.nextQuestion(questions, currentIndex, score);
        output.append("[Q" + (currentIndex + 1) + "] " + q + "\n");
    }
}

// === GUI Application ===
public class StrategyQuizDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quiz Strategy Pattern Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);

            // Create components
            JTextArea output = new JTextArea(15, 50);
            output.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(output);

            JLabel titleLabel = new JLabel("Strategy Pattern: Quiz Engine", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel strategyLabel = new JLabel("Select Strategy:");
            JComboBox<String> strategyCombo = new JComboBox<>(new String[]{
                "Sequential Strategy", "Random Strategy", "Adaptive Strategy"
            });

            JLabel scoreLabel = new JLabel("Current Score:");
            JSpinner scoreSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 10));

            JButton setStrategyBtn = new JButton("Set Strategy");
            JButton startQuizBtn = new JButton("Start Quiz");
            JButton nextQuestionBtn = new JButton("Next Question");
            JButton resetBtn = new JButton("Reset");

            // Layout
            JPanel topPanel = new JPanel(new FlowLayout());
            topPanel.add(strategyLabel);
            topPanel.add(strategyCombo);
            topPanel.add(setStrategyBtn);
            topPanel.add(scoreLabel);
            topPanel.add(scoreSpinner);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(startQuizBtn);
            buttonPanel.add(nextQuestionBtn);
            buttonPanel.add(resetBtn);

            frame.add(titleLabel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(topPanel, BorderLayout.WEST);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Initialize quiz
            java.util.List<String> questions = Arrays.asList(
                "Q1: 2 + 2 = ?",
                "Q2: Capital of France?",
                "Q3: Solve 5*6",
                "Q4: Who wrote 'Hamlet'?",
                "Q5: Derivative of x^2?",
                "Q6: What is the speed of light?",
                "Q7: Who painted the Mona Lisa?",
                "Q8: What is 15% of 200?"
            );

            final QuizEngine[] quiz = {new QuizEngine(questions, output)};

            // Event handlers
            setStrategyBtn.addActionListener(e -> {
                String selected = (String) strategyCombo.getSelectedItem();
                switch (selected) {
                    case "Sequential Strategy":
                        quiz[0].setStrategy(new SequentialStrategy());
                        break;
                    case "Random Strategy":
                        quiz[0].setStrategy(new RandomStrategy());
                        break;
                    case "Adaptive Strategy":
                        quiz[0].setStrategy(new AdaptiveStrategy());
                        break;
                }
            });

            startQuizBtn.addActionListener(e -> {
                quiz[0].startQuiz();
                nextQuestionBtn.setEnabled(true);
            });

            nextQuestionBtn.addActionListener(e -> {
                quiz[0].nextQuestion();
            });

            resetBtn.addActionListener(e -> {
                output.setText("");
                quiz[0] = new QuizEngine(questions, output);
                nextQuestionBtn.setEnabled(false);
                output.append("=== Strategy Pattern Demo ===\n");
                output.append("1. Select a strategy from dropdown\n");
                output.append("2. Set current score (for adaptive strategy)\n");
                output.append("3. Click 'Set Strategy' to apply\n");
                output.append("4. Click 'Start Quiz' to begin\n");
                output.append("5. Click 'Next Question' to see strategy in action\n\n");
            });

            // Initial setup
            nextQuestionBtn.setEnabled(false);
            output.append("=== Strategy Pattern Demo ===\n");
            output.append("1. Select a strategy from dropdown\n");
            output.append("2. Set current score (for adaptive strategy)\n");
            output.append("3. Click 'Set Strategy' to apply\n");
            output.append("4. Click 'Start Quiz' to begin\n");
            output.append("5. Click 'Next Question' to see strategy in action\n\n");

            frame.setVisible(true);
        });
    }
}