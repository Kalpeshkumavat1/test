package behavioural;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// === Observer Interfaces ===
interface Observer {
    String getId();
    void update(String subjectName, String message);
}

interface Subject {
    void subscribe(Observer o);
    void unsubscribe(String observerId);
    void notifyObservers(String message);
}

// === Concrete Observers ===
class Student implements Observer {
    private final String id;
    private final String name;
    private JTextArea output;

    public Student(String id, String name, JTextArea output) {
        this.id = id;
        this.name = name;
        this.output = output;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void update(String subjectName, String message) {
        output.append("[Student Notify] " + name + " <- " + subjectName + ": " + message + "\n");
    }
}

class Parent implements Observer {
    private final String id;
    private final String name;
    private JTextArea output;

    public Parent(String id, String name, JTextArea output) {
        this.id = id;
        this.name = name;
        this.output = output;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void update(String subjectName, String message) {
        output.append("[Parent Notify] " + name + " <- " + subjectName + ": " + message + "\n");
    }
}

// === Concrete Subject ===
class Course implements Subject {
    private final String title;
    private final Map<String, Observer> observers = new LinkedHashMap<>();
    private JTextArea output;

    public Course(String title, JTextArea output) {
        this.title = title;
        this.output = output;
    }

    @Override
    public void subscribe(Observer o) {
        if (observers.containsKey(o.getId())) {
            output.append("[WARN] Already subscribed: " + o.getId() + "\n");
            return;
        }
        observers.put(o.getId(), o);
        output.append("[INFO] " + o.getId() + " subscribed to " + title + "\n");
    }

    @Override
    public void unsubscribe(String observerId) {
        if (!observers.containsKey(observerId)) {
            output.append("[WARN] Observer not found: " + observerId + "\n");
            return;
        }
        observers.remove(observerId);
        output.append("[INFO] " + observerId + " unsubscribed from " + title + "\n");
    }

    public void postAnnouncement(String author, String message) {
        if (message == null || message.trim().isEmpty()) {
            output.append("[ERROR] Empty announcement not allowed\n");
            return;
        }
        String fullMessage = author + ": " + message;
        output.append("\n[ANNOUNCEMENT] " + title + " -> " + fullMessage + "\n");
        notifyObservers(fullMessage);
    }

    @Override
    public void notifyObservers(String message) {
        if (observers.isEmpty()) {
            output.append("[INFO] No subscribers to notify.\n");
            return;
        }
        for (Observer obs : observers.values()) {
            obs.update(title, message);
        }
    }
}

// === GUI Application ===
public class CourseObserverGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Course Notifications (Observer Pattern)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JTextArea output = new JTextArea();
            output.setEditable(false);
            JScrollPane scroll = new JScrollPane(output);

            JTextField studentNameField = new JTextField(10);
            JButton addStudentBtn = new JButton("Subscribe Student");
            JTextField parentNameField = new JTextField(10);
            JButton addParentBtn = new JButton("Subscribe Parent");

            JTextField announcementField = new JTextField(20);
            JButton postBtn = new JButton("Post Announcement");

            JPanel topPanel = new JPanel();
            topPanel.add(new JLabel("Student:"));
            topPanel.add(studentNameField);
            topPanel.add(addStudentBtn);
            topPanel.add(new JLabel("Parent:"));
            topPanel.add(parentNameField);
            topPanel.add(addParentBtn);

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(new JLabel("Announcement:"));
            bottomPanel.add(announcementField);
            bottomPanel.add(postBtn);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(scroll, BorderLayout.CENTER);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            Course course = new Course("Math 101", output);

            addStudentBtn.addActionListener(e -> {
                String name = studentNameField.getText().trim();
                if (!name.isEmpty()) {
                    course.subscribe(new Student("S-" + name, name, output));
                    studentNameField.setText("");
                }
            });

            addParentBtn.addActionListener(e -> {
                String name = parentNameField.getText().trim();
                if (!name.isEmpty()) {
                    course.subscribe(new Parent("P-" + name, name, output));
                    parentNameField.setText("");
                }
            });

            postBtn.addActionListener(e -> {
                String msg = announcementField.getText().trim();
                if (!msg.isEmpty()) {
                    course.postAnnouncement("Teacher", msg);
                    announcementField.setText("");
                }
            });

            frame.setVisible(true);
        });
    }
}
