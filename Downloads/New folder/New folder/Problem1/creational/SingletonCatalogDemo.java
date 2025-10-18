package creational;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// === Singleton Class ===
class CourseCatalog {
    private static CourseCatalog instance;  // static instance
    private Map<String, String> courses;    // courseId -> courseName
    private JTextArea output;               // GUI output reference

    // private constructor (no outside instantiation)
    private CourseCatalog() {
        courses = new LinkedHashMap<>();
    }

    // public static method to get the single instance
    public static synchronized CourseCatalog getInstance() {
        if (instance == null) {
            instance = new CourseCatalog();
            System.out.println("[INFO] New CourseCatalog instance created.");
        }
        return instance;
    }

    // Set output reference for GUI
    public void setOutput(JTextArea output) {
        this.output = output;
    }

    // CRUD operations
    public void addCourse(String id, String name) {
        if (courses.containsKey(id)) {
            String message = "[WARN] Course with ID " + id + " already exists.";
            System.out.println(message);
            if (output != null) output.append(message + "\n");
            return;
        }
        courses.put(id, name);
        String message = "[INFO] Course added: " + id + " - " + name;
        System.out.println(message);
        if (output != null) output.append(message + "\n");
    }

    public void removeCourse(String id) {
        if (!courses.containsKey(id)) {
            String message = "[ERROR] Course ID not found: " + id;
            System.out.println(message);
            if (output != null) output.append(message + "\n");
            return;
        }
        String removedName = courses.remove(id);
        String message = "[INFO] Course removed: " + id + " - " + removedName;
        System.out.println(message);
        if (output != null) output.append(message + "\n");
    }

    public void listCourses() {
        String message = "\n[COURSE CATALOG]";
        System.out.println(message);
        if (output != null) output.append(message + "\n");
        
        if (courses.isEmpty()) {
            String emptyMessage = "  (no courses available)";
            System.out.println(emptyMessage);
            if (output != null) output.append(emptyMessage + "\n");
            return;
        }
        courses.forEach((id, name) -> {
            String courseMessage = "  " + id + ": " + name;
            System.out.println(courseMessage);
            if (output != null) output.append(courseMessage + "\n");
        });
    }

    public Map<String, String> getCourses() {
        return new LinkedHashMap<>(courses);
    }

    public int getCourseCount() {
        return courses.size();
    }

    public boolean hasCourse(String id) {
        return courses.containsKey(id);
    }
}

// === GUI Application ===
public class SingletonCatalogDemo {
    private static JTextArea outputArea;
    private static JList<String> courseList;
    private static DefaultListModel<String> courseListModel;
    private static JTextField courseIdField;
    private static JTextField courseNameField;
    private static JLabel instanceInfoLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Singleton Pattern: Course Catalog Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);

            // Create components
            outputArea = new JTextArea(15, 50);
            outputArea.setEditable(false);
            outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane outputScrollPane = new JScrollPane(outputArea);

            courseListModel = new DefaultListModel<>();
            courseList = new JList<>(courseListModel);
            courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane courseScrollPane = new JScrollPane(courseList);

            instanceInfoLabel = new JLabel("Singleton Instance Info", JLabel.CENTER);
            instanceInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Course management controls
            JLabel idLabel = new JLabel("Course ID:");
            courseIdField = new JTextField(10);
            
            JLabel nameLabel = new JLabel("Course Name:");
            courseNameField = new JTextField(20);
            
            JButton addBtn = new JButton("Add Course");
            JButton removeBtn = new JButton("Remove Selected");
            JButton listBtn = new JButton("List All Courses");
            JButton clearBtn = new JButton("Clear All");
            JButton testSingletonBtn = new JButton("Test Singleton");
            JButton addSampleBtn = new JButton("Add Sample Courses");

            // Layout
            JPanel topPanel = new JPanel(new FlowLayout());
            topPanel.add(idLabel);
            topPanel.add(courseIdField);
            topPanel.add(nameLabel);
            topPanel.add(courseNameField);
            topPanel.add(addBtn);

            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(new JLabel("Course Catalog (Singleton Pattern)", JLabel.CENTER), BorderLayout.NORTH);
            leftPanel.add(courseScrollPane, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(instanceInfoLabel, BorderLayout.NORTH);
            rightPanel.add(new JLabel("Console Output", JLabel.CENTER), BorderLayout.CENTER);
            rightPanel.add(outputScrollPane, BorderLayout.SOUTH);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(removeBtn);
            buttonPanel.add(listBtn);
            buttonPanel.add(testSingletonBtn);
            buttonPanel.add(addSampleBtn);
            buttonPanel.add(clearBtn);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setDividerLocation(400);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Initialize singleton and set output reference
            CourseCatalog catalog = CourseCatalog.getInstance();
            catalog.setOutput(outputArea);

            // Event handlers
            addBtn.addActionListener(e -> {
                String id = courseIdField.getText().trim();
                String name = courseNameField.getText().trim();
                
                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both ID and name!");
                    return;
                }
                
                catalog.addCourse(id, name);
                updateCourseList();
                courseIdField.setText("");
                courseNameField.setText("");
            });

            removeBtn.addActionListener(e -> {
                String selected = courseList.getSelectedValue();
                if (selected != null) {
                    String id = selected.split(":")[0].trim();
                    catalog.removeCourse(id);
                    updateCourseList();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a course to remove!");
                }
            });

            listBtn.addActionListener(e -> {
                catalog.listCourses();
            });

            testSingletonBtn.addActionListener(e -> {
                CourseCatalog catalog1 = CourseCatalog.getInstance();
                CourseCatalog catalog2 = CourseCatalog.getInstance();
                
                outputArea.append("\n=== Singleton Test ===\n");
                outputArea.append("catalog1 == catalog2 ? " + (catalog1 == catalog2) + "\n");
                outputArea.append("catalog1.hashCode() = " + catalog1.hashCode() + "\n");
                outputArea.append("catalog2.hashCode() = " + catalog2.hashCode() + "\n");
                outputArea.append("Both references point to the same instance!\n\n");
            });

            addSampleBtn.addActionListener(e -> {
                catalog.addCourse("C101", "Mathematics Basics");
                catalog.addCourse("C102", "Physics Fundamentals");
                catalog.addCourse("C103", "Chemistry Essentials");
                catalog.addCourse("C104", "Computer Science");
                catalog.addCourse("C105", "English Literature");
                updateCourseList();
            });

            clearBtn.addActionListener(e -> {
                catalog.getCourses().clear();
                updateCourseList();
                outputArea.setText("");
                outputArea.append("=== Singleton Pattern Demo ===\n");
                outputArea.append("1. Add courses using ID and name fields\n");
                outputArea.append("2. Use 'Add Sample Courses' for quick demo\n");
                outputArea.append("3. Select courses and click 'Remove Selected'\n");
                outputArea.append("4. Use 'Test Singleton' to verify single instance\n");
                outputArea.append("5. Notice how all operations use the same instance\n\n");
            });

            // Course selection listener
            courseList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selected = courseList.getSelectedValue();
                    if (selected != null) {
                        outputArea.append("Selected: " + selected + "\n");
                    }
                }
            });

            // Update instance info
            updateInstanceInfo();

            // Initial setup
            outputArea.append("=== Singleton Pattern Demo ===\n");
            outputArea.append("1. Add courses using ID and name fields\n");
            outputArea.append("2. Use 'Add Sample Courses' for quick demo\n");
            outputArea.append("3. Select courses and click 'Remove Selected'\n");
            outputArea.append("4. Use 'Test Singleton' to verify single instance\n");
            outputArea.append("5. Notice how all operations use the same instance\n\n");

            frame.setVisible(true);
        });
    }

    private static void updateCourseList() {
        courseListModel.clear();
        CourseCatalog catalog = CourseCatalog.getInstance();
        Map<String, String> courses = catalog.getCourses();
        
        for (Map.Entry<String, String> entry : courses.entrySet()) {
            courseListModel.addElement(entry.getKey() + ": " + entry.getValue());
        }
        
        updateInstanceInfo();
    }

    private static void updateInstanceInfo() {
        CourseCatalog catalog = CourseCatalog.getInstance();
        instanceInfoLabel.setText("<html><b>Singleton Instance</b><br>" +
                                "Hash Code: " + catalog.hashCode() + "<br>" +
                                "Courses: " + catalog.getCourseCount() + "</html>");
    }
}