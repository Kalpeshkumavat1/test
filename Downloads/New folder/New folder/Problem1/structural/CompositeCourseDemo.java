package structural;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// === Component Interface ===
interface CurriculumComponent {
    void showDetails(String prefix);
    String getTitle();
    String getType();
    java.util.List<CurriculumComponent> getChildren();
}

// === Leaf: CurriculumLesson (renamed to avoid name clash in package) ===
class CurriculumLesson implements CurriculumComponent {
    private String title;
    public CurriculumLesson(String title) {
        this.title = title;
    }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Lesson"; }
    
    @Override
    public java.util.List<CurriculumComponent> getChildren() { return new ArrayList<>(); }
    
    @Override
    public void showDetails(String prefix) {
        System.out.println(prefix + "- Lesson: " + title);
    }
}

// === Composite: Module ===
class Module implements CurriculumComponent {
    private String title;
    private java.util.List<CurriculumComponent> components = new ArrayList<>();

    public Module(String title) {
        this.title = title;
    }

    public void addComponent(CurriculumComponent c) {
        components.add(c);
    }

    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Module"; }
    
    @Override
    public java.util.List<CurriculumComponent> getChildren() { return components; }

    @Override
    public void showDetails(String prefix) {
        System.out.println(prefix + "Module: " + title);
        for (CurriculumComponent c : components) {
            c.showDetails(prefix + "   ");
        }
    }
}

// === Composite: Course ===
class CourseComposite implements CurriculumComponent {
    private String title;
    private java.util.List<CurriculumComponent> components = new ArrayList<>();

    public CourseComposite(String title) {
        this.title = title;
    }

    public void addComponent(CurriculumComponent c) {
        components.add(c);
    }

    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Course"; }
    
    @Override
    public java.util.List<CurriculumComponent> getChildren() { return components; }

    @Override
    public void showDetails(String prefix) {
        System.out.println(prefix + "Course: " + title);
        for (CurriculumComponent c : components) {
            c.showDetails(prefix + "   ");
        }
    }
}

// === Tree Model for JTree ===
class CurriculumTreeModel implements TreeModel {
    private CurriculumComponent root;
    private java.util.List<javax.swing.event.TreeModelListener> listeners = new ArrayList<>();

    public CurriculumTreeModel(CurriculumComponent root) {
        this.root = root;
    }

    @Override
    public Object getRoot() { return root; }

    @Override
    public Object getChild(Object parent, int index) {
        return ((CurriculumComponent) parent).getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((CurriculumComponent) parent).getChildren().size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((CurriculumComponent) node).getChildren().isEmpty();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((CurriculumComponent) parent).getChildren().indexOf(child);
    }

    @Override
    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
        listeners.remove(l);
    }
}

// === Custom Tree Cell Renderer ===
class CurriculumTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        CurriculumComponent comp = (CurriculumComponent) value;
        String type = comp.getType();
        
        // Set different icons and colors based on type
        if (type.equals("Course")) {
            setIcon(UIManager.getIcon("FileView.directoryIcon"));
            setForeground(Color.BLUE);
        } else if (type.equals("Module")) {
            setIcon(UIManager.getIcon("FileView.directoryIcon"));
            setForeground(new Color(0, 128, 0));
        } else {
            setIcon(UIManager.getIcon("FileView.fileIcon"));
            setForeground(Color.BLACK);
        }
        
        setText(comp.getType() + ": " + comp.getTitle());
        return this;
    }
}

// === GUI Application ===
public class CompositeCourseDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Composite Pattern: Course Structure");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Create the course structure
            CurriculumLesson l1 = new CurriculumLesson("Intro to Algebra");
            CurriculumLesson l2 = new CurriculumLesson("Quadratic Equations");
            CurriculumLesson l3 = new CurriculumLesson("Newton's Laws");
            CurriculumLesson l4 = new CurriculumLesson("Work & Energy");
            CurriculumLesson l5 = new CurriculumLesson("Chemical Reactions");
            CurriculumLesson l6 = new CurriculumLesson("Organic Chemistry");

            Module m1 = new Module("Mathematics Basics");
            m1.addComponent(l1);
            m1.addComponent(l2);

            Module m2 = new Module("Physics Fundamentals");
            m2.addComponent(l3);
            m2.addComponent(l4);

            Module m3 = new Module("Chemistry Essentials");
            m3.addComponent(l5);
            m3.addComponent(l6);

            CourseComposite course = new CourseComposite("STEM Foundation Course");
            course.addComponent(m1);
            course.addComponent(m2);
            course.addComponent(m3);

            // Create tree
            JTree tree = new JTree(new CurriculumTreeModel(course));
            tree.setCellRenderer(new CurriculumTreeCellRenderer());
            tree.setRootVisible(true);
            tree.setShowsRootHandles(true);
            tree.expandRow(0); // Expand root by default

            JScrollPane treeScrollPane = new JScrollPane(tree);

            // Create text area for console output
            JTextArea consoleOutput = new JTextArea(10, 40);
            consoleOutput.setEditable(false);
            consoleOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane consoleScrollPane = new JScrollPane(consoleOutput);

            // Create buttons
            JButton showStructureBtn = new JButton("Show Structure (Console)");
            JButton expandAllBtn = new JButton("Expand All");
            JButton collapseAllBtn = new JButton("Collapse All");
            JButton addLessonBtn = new JButton("Add New Lesson");

            // Layout
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(new JLabel("Course Structure Tree (Composite Pattern)", JLabel.CENTER), BorderLayout.NORTH);
            leftPanel.add(treeScrollPane, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(new JLabel("Console Output", JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(consoleScrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(showStructureBtn);
            buttonPanel.add(expandAllBtn);
            buttonPanel.add(collapseAllBtn);
            buttonPanel.add(addLessonBtn);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setDividerLocation(400);

            frame.add(splitPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Event handlers
            showStructureBtn.addActionListener(e -> {
                consoleOutput.setText("=== Course Structure (Composite Pattern) ===\n\n");
                course.showDetails("");
                consoleOutput.append("\n=== Pattern Explanation ===\n");
                consoleOutput.append("• Course, Module, and Lesson all implement CurriculumComponent\n");
                consoleOutput.append("• Course and Module are composites (contain other components)\n");
                consoleOutput.append("• Lesson is a leaf (no children)\n");
                consoleOutput.append("• All components can be treated uniformly\n");
                consoleOutput.append("• Tree structure allows hierarchical organization\n");
            });

            expandAllBtn.addActionListener(e -> {
                for (int i = 0; i < tree.getRowCount(); i++) {
                    tree.expandRow(i);
                }
            });

            collapseAllBtn.addActionListener(e -> {
                for (int i = tree.getRowCount() - 1; i >= 0; i--) {
                    tree.collapseRow(i);
                }
            });

            addLessonBtn.addActionListener(e -> {
                String lessonTitle = JOptionPane.showInputDialog(frame, "Enter lesson title:");
                if (lessonTitle != null && !lessonTitle.trim().isEmpty()) {
                    // Add to the first module for demo
                    m1.addComponent(new CurriculumLesson(lessonTitle));
                    tree.updateUI(); // Refresh the tree
                    consoleOutput.append("Added lesson: " + lessonTitle + " to " + m1.getTitle() + "\n");
                }
            });

            // Tree selection listener
            tree.addTreeSelectionListener(e -> {
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    CurriculumComponent selected = (CurriculumComponent) path.getLastPathComponent();
                    consoleOutput.append("Selected: " + selected.getType() + " - " + selected.getTitle() + "\n");
                    consoleOutput.append("Children count: " + selected.getChildren().size() + "\n");
                }
            });

            // Initial console output
            consoleOutput.append("=== Composite Pattern Demo ===\n");
            consoleOutput.append("• Click on tree nodes to see details\n");
            consoleOutput.append("• Use 'Show Structure' to see console output\n");
            consoleOutput.append("• Use 'Add New Lesson' to add components dynamically\n");
            consoleOutput.append("• Expand/Collapse to navigate the tree\n\n");

            frame.setVisible(true);
        });
    }
}