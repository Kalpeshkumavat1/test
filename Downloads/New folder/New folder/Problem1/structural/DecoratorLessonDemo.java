package structural;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// === Component Interface ===
interface Lesson {
    void display();
    String getTitle();
    String getDescription();
}

// === Concrete Component ===
class BasicLesson implements Lesson {
    private String title;
    public BasicLesson(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getDescription() { return "Basic lesson content"; }

    @Override
    public void display() {
        System.out.println("Lesson: " + title);
    }
}

// === Base Decorator ===
abstract class LessonDecorator implements Lesson {
    protected Lesson decoratedLesson;
    public LessonDecorator(Lesson lesson) {
        this.decoratedLesson = lesson;
    }
    
    @Override
    public String getTitle() {
        return decoratedLesson.getTitle();
    }
    
    @Override
    public void display() {
        decoratedLesson.display();
    }
}

// === Concrete Decorators ===
class ProgressDecorator extends LessonDecorator {
    public ProgressDecorator(Lesson lesson) {
        super(lesson);
    }
    
    @Override
    public String getDescription() {
        return decoratedLesson.getDescription() + " + Progress Tracking";
    }
    
    @Override
    public void display() {
        super.display();
        System.out.println("  -> [Progress Tracker Enabled]");
    }
}

class GamificationDecorator extends LessonDecorator {
    public GamificationDecorator(Lesson lesson) {
        super(lesson);
    }
    
    @Override
    public String getDescription() {
        return decoratedLesson.getDescription() + " + Gamification";
    }
    
    @Override
    public void display() {
        super.display();
        System.out.println("  -> [Gamification: Earn Points & Badges]");
    }
}

class QuizDecorator extends LessonDecorator {
    public QuizDecorator(Lesson lesson) {
        super(lesson);
    }
    
    @Override
    public String getDescription() {
        return decoratedLesson.getDescription() + " + Quiz";
    }
    
    @Override
    public void display() {
        super.display();
        System.out.println("  -> [Quiz Available at End]");
    }
}

class VideoDecorator extends LessonDecorator {
    public VideoDecorator(Lesson lesson) {
        super(lesson);
    }
    
    @Override
    public String getDescription() {
        return decoratedLesson.getDescription() + " + Video Content";
    }
    
    @Override
    public void display() {
        super.display();
        System.out.println("  -> [Video Content Available]");
    }
}

class DiscussionDecorator extends LessonDecorator {
    public DiscussionDecorator(Lesson lesson) {
        super(lesson);
    }
    
    @Override
    public String getDescription() {
        return decoratedLesson.getDescription() + " + Discussion Forum";
    }
    
    @Override
    public void display() {
        super.display();
        System.out.println("  -> [Discussion Forum Enabled]");
    }
}

// === GUI Application ===
public class DecoratorLessonDemo {
    private static JTextArea outputArea;
    private static JList<String> lessonList;
    private static DefaultListModel<String> lessonListModel;
    private static JList<String> decoratorList;
    private static DefaultListModel<String> decoratorListModel;
    private static Lesson currentLesson;
    private static JLabel lessonInfoLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Decorator Pattern: Lesson Enhancement");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);

            // Create components
            outputArea = new JTextArea(15, 50);
            outputArea.setEditable(false);
            outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane outputScrollPane = new JScrollPane(outputArea);

            lessonListModel = new DefaultListModel<>();
            lessonList = new JList<>(lessonListModel);
            lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane lessonScrollPane = new JScrollPane(lessonList);

            decoratorListModel = new DefaultListModel<>();
            decoratorList = new JList<>(decoratorListModel);
            decoratorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane decoratorScrollPane = new JScrollPane(decoratorList);

            lessonInfoLabel = new JLabel("Select a lesson to see its features", JLabel.CENTER);
            lessonInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Buttons
            JButton createBasicBtn = new JButton("Create Basic Lesson");
            JButton addDecoratorBtn = new JButton("Add Selected Decorator");
            JButton removeDecoratorBtn = new JButton("Remove Selected Decorator");
            JButton displayLessonBtn = new JButton("Display Lesson (Console)");
            JButton clearBtn = new JButton("Clear All");

            // Text field for lesson title
            JTextField lessonTitleField = new JTextField(20);
            JButton addLessonBtn = new JButton("Add Lesson");

            // Layout
            JPanel topPanel = new JPanel(new FlowLayout());
            topPanel.add(new JLabel("Lesson Title:"));
            topPanel.add(lessonTitleField);
            topPanel.add(addLessonBtn);

            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(new JLabel("Available Lessons", JLabel.CENTER), BorderLayout.NORTH);
            leftPanel.add(lessonScrollPane, BorderLayout.CENTER);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(lessonInfoLabel, BorderLayout.NORTH);
            centerPanel.add(new JLabel("Available Decorators", JLabel.CENTER), BorderLayout.CENTER);
            centerPanel.add(decoratorScrollPane, BorderLayout.SOUTH);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(new JLabel("Console Output", JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(outputScrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(createBasicBtn);
            buttonPanel.add(addDecoratorBtn);
            buttonPanel.add(removeDecoratorBtn);
            buttonPanel.add(displayLessonBtn);
            buttonPanel.add(clearBtn);

            // Main layout
            JSplitPane leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, centerPanel);
            leftSplit.setDividerLocation(300);
            JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, rightPanel);
            mainSplit.setDividerLocation(600);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(mainSplit, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Initialize decorators list
            String[] decorators = {
                "Progress Tracking", "Gamification", "Quiz", "Video Content", "Discussion Forum"
            };
            for (String decorator : decorators) {
                decoratorListModel.addElement(decorator);
            }

            // Event handlers
            addLessonBtn.addActionListener(e -> {
                String title = lessonTitleField.getText().trim();
                if (!title.isEmpty()) {
                    lessonListModel.addElement(title);
                    lessonTitleField.setText("");
                    outputArea.append("Added lesson: " + title + "\n");
                }
            });

            createBasicBtn.addActionListener(e -> {
                String selected = lessonList.getSelectedValue();
                if (selected != null) {
                    currentLesson = new BasicLesson(selected);
                    updateLessonInfo();
                    outputArea.append("Created basic lesson: " + selected + "\n");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a lesson first!");
                }
            });

            addDecoratorBtn.addActionListener(e -> {
                if (currentLesson == null) {
                    JOptionPane.showMessageDialog(frame, "Please create a lesson first!");
                    return;
                }
                
                String selectedDecorator = decoratorList.getSelectedValue();
                if (selectedDecorator != null) {
                    switch (selectedDecorator) {
                        case "Progress Tracking":
                            currentLesson = new ProgressDecorator(currentLesson);
                            break;
                        case "Gamification":
                            currentLesson = new GamificationDecorator(currentLesson);
                            break;
                        case "Quiz":
                            currentLesson = new QuizDecorator(currentLesson);
                            break;
                        case "Video Content":
                            currentLesson = new VideoDecorator(currentLesson);
                            break;
                        case "Discussion Forum":
                            currentLesson = new DiscussionDecorator(currentLesson);
                            break;
                    }
                    updateLessonInfo();
                    outputArea.append("Added decorator: " + selectedDecorator + "\n");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a decorator!");
                }
            });

            removeDecoratorBtn.addActionListener(e -> {
                if (currentLesson == null) {
                    JOptionPane.showMessageDialog(frame, "No lesson to modify!");
                    return;
                }
                
                // For simplicity, we'll reset to basic lesson
                // In a real implementation, you'd need to track the decorator chain
                String title = currentLesson.getTitle();
                currentLesson = new BasicLesson(title);
                updateLessonInfo();
                outputArea.append("Reset to basic lesson: " + title + "\n");
            });

            displayLessonBtn.addActionListener(e -> {
                if (currentLesson != null) {
                    outputArea.append("\n=== Lesson Display ===\n");
                    currentLesson.display();
                    outputArea.append("Description: " + currentLesson.getDescription() + "\n\n");
                } else {
                    JOptionPane.showMessageDialog(frame, "No lesson to display!");
                }
            });

            clearBtn.addActionListener(e -> {
                currentLesson = null;
                updateLessonInfo();
                outputArea.setText("");
                outputArea.append("=== Decorator Pattern Demo ===\n");
                outputArea.append("1. Add lesson titles using the text field\n");
                outputArea.append("2. Select a lesson and click 'Create Basic Lesson'\n");
                outputArea.append("3. Select decorators and click 'Add Selected Decorator'\n");
                outputArea.append("4. Use 'Display Lesson' to see the result\n");
                outputArea.append("5. Notice how decorators wrap around the base lesson\n\n");
            });

            // Lesson selection listener
            lessonList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selected = lessonList.getSelectedValue();
                    if (selected != null) {
                        outputArea.append("Selected lesson: " + selected + "\n");
                    }
                }
            });

            // Initial setup
            outputArea.append("=== Decorator Pattern Demo ===\n");
            outputArea.append("1. Add lesson titles using the text field\n");
            outputArea.append("2. Select a lesson and click 'Create Basic Lesson'\n");
            outputArea.append("3. Select decorators and click 'Add Selected Decorator'\n");
            outputArea.append("4. Use 'Display Lesson' to see the result\n");
            outputArea.append("5. Notice how decorators wrap around the base lesson\n\n");

            frame.setVisible(true);
        });
    }

    private static void updateLessonInfo() {
        if (currentLesson != null) {
            lessonInfoLabel.setText("<html><b>" + currentLesson.getTitle() + "</b><br>" + 
                                  currentLesson.getDescription() + "</html>");
        } else {
            lessonInfoLabel.setText("Select a lesson to see its features");
        }
    }
}
