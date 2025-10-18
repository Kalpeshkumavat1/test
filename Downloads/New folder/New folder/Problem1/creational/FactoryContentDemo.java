package creational;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// === Product Interface ===
interface Content {
    void display();
    String getTitle();
    String getType();
    String getDescription();
}

// === Concrete Products ===
class VideoLesson implements Content {
    private String title;
    public VideoLesson(String title) { this.title = title; }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Video Lesson"; }
    
    @Override
    public String getDescription() { return "Interactive video content with playback controls"; }
    
    @Override
    public void display() {
        System.out.println("[Video] Playing lesson: " + title);
    }
}

class QuizModule implements Content {
    private String title;
    public QuizModule(String title) { this.title = title; }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Quiz Module"; }
    
    @Override
    public String getDescription() { return "Interactive quiz with multiple choice questions"; }
    
    @Override
    public void display() {
        System.out.println("[Quiz] Starting quiz: " + title);
    }
}

class Article implements Content {
    private String title;
    public Article(String title) { this.title = title; }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Article"; }
    
    @Override
    public String getDescription() { return "Text-based content with rich formatting"; }
    
    @Override
    public void display() {
        System.out.println("[Article] Reading article: " + title);
    }
}

class Podcast implements Content {
    private String title;
    public Podcast(String title) { this.title = title; }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Podcast"; }
    
    @Override
    public String getDescription() { return "Audio content for listening on-the-go"; }
    
    @Override
    public void display() {
        System.out.println("[Podcast] Playing episode: " + title);
    }
}

class InteractiveLab implements Content {
    private String title;
    public InteractiveLab(String title) { this.title = title; }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public String getType() { return "Interactive Lab"; }
    
    @Override
    public String getDescription() { return "Hands-on laboratory simulation"; }
    
    @Override
    public void display() {
        System.out.println("[Lab] Starting interactive lab: " + title);
    }
}

// === Factory (Creator) ===
class ContentFactory {
    public static Content createContent(String type, String title) {
        switch (type.toLowerCase()) {
            case "video":
                return new VideoLesson(title);
            case "quiz":
                return new QuizModule(title);
            case "article":
                return new Article(title);
            case "podcast":
                return new Podcast(title);
            case "lab":
                return new InteractiveLab(title);
            default:
                throw new IllegalArgumentException("[ERROR] Unknown content type: " + type);
        }
    }
    
    public static String[] getAvailableTypes() {
        return new String[]{"Video", "Quiz", "Article", "Podcast", "Lab"};
    }
}

// === GUI Application ===
public class FactoryContentDemo {
    private static JTextArea outputArea;
    private static JList<Content> contentList;
    private static DefaultListModel<Content> contentListModel;
    private static JLabel contentInfoLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Factory Pattern: Content Creation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Create components
            outputArea = new JTextArea(15, 50);
            outputArea.setEditable(false);
            outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane outputScrollPane = new JScrollPane(outputArea);

            contentListModel = new DefaultListModel<>();
            contentList = new JList<>(contentListModel);
            contentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            contentList.setCellRenderer(new ContentListCellRenderer());
            JScrollPane contentScrollPane = new JScrollPane(contentList);

            contentInfoLabel = new JLabel("Select content to see details", JLabel.CENTER);
            contentInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Content creation controls
            JLabel typeLabel = new JLabel("Content Type:");
            JComboBox<String> typeCombo = new JComboBox<>(ContentFactory.getAvailableTypes());
            
            JLabel titleLabel = new JLabel("Title:");
            JTextField titleField = new JTextField(20);
            
            JButton createBtn = new JButton("Create Content");
            JButton displayBtn = new JButton("Display Selected");
            JButton clearBtn = new JButton("Clear All");
            JButton createRandomBtn = new JButton("Create Random Content");

            // Layout
            JPanel topPanel = new JPanel(new FlowLayout());
            topPanel.add(typeLabel);
            topPanel.add(typeCombo);
            topPanel.add(titleLabel);
            topPanel.add(titleField);
            topPanel.add(createBtn);

            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(new JLabel("Created Content (Factory Pattern)", JLabel.CENTER), BorderLayout.NORTH);
            leftPanel.add(contentScrollPane, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(contentInfoLabel, BorderLayout.NORTH);
            rightPanel.add(new JLabel("Console Output", JLabel.CENTER), BorderLayout.CENTER);
            rightPanel.add(outputScrollPane, BorderLayout.SOUTH);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(displayBtn);
            buttonPanel.add(createRandomBtn);
            buttonPanel.add(clearBtn);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setDividerLocation(400);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Event handlers
            createBtn.addActionListener(e -> {
                String type = (String) typeCombo.getSelectedItem();
                String title = titleField.getText().trim();
                
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a title!");
                    return;
                }
                
                try {
                    Content content = ContentFactory.createContent(type.toLowerCase(), title);
                    contentListModel.addElement(content);
                    titleField.setText("");
                    outputArea.append("Created " + type + ": " + title + "\n");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            });

            createRandomBtn.addActionListener(e -> {
                String[] types = ContentFactory.getAvailableTypes();
                String[] titles = {
                    "Introduction to Programming", "Advanced Mathematics", "Science Fundamentals",
                    "History of Technology", "Creative Writing", "Data Analysis", "Machine Learning Basics"
                };
                
                Random rand = new Random();
                String randomType = types[rand.nextInt(types.length)];
                String randomTitle = titles[rand.nextInt(titles.length)] + " - " + randomType;
                
                try {
                    Content content = ContentFactory.createContent(randomType.toLowerCase(), randomTitle);
                    contentListModel.addElement(content);
                    outputArea.append("Created random " + randomType + ": " + randomTitle + "\n");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            });

            displayBtn.addActionListener(e -> {
                Content selected = contentList.getSelectedValue();
                if (selected != null) {
                    outputArea.append("\n=== Content Display ===\n");
                    selected.display();
                    outputArea.append("Type: " + selected.getType() + "\n");
                    outputArea.append("Description: " + selected.getDescription() + "\n\n");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select content to display!");
                }
            });

            clearBtn.addActionListener(e -> {
                contentListModel.clear();
                outputArea.setText("");
                outputArea.append("=== Factory Pattern Demo ===\n");
                outputArea.append("1. Select content type from dropdown\n");
                outputArea.append("2. Enter a title and click 'Create Content'\n");
                outputArea.append("3. Use 'Create Random Content' for quick demo\n");
                outputArea.append("4. Select content and click 'Display Selected'\n");
                outputArea.append("5. Notice how factory creates different content types\n\n");
            });

            // Content selection listener
            contentList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    Content selected = contentList.getSelectedValue();
                    if (selected != null) {
                        updateContentInfo(selected);
                        outputArea.append("Selected: " + selected.getType() + " - " + selected.getTitle() + "\n");
                    }
                }
            });

            // Initial setup
            outputArea.append("=== Factory Pattern Demo ===\n");
            outputArea.append("1. Select content type from dropdown\n");
            outputArea.append("2. Enter a title and click 'Create Content'\n");
            outputArea.append("3. Use 'Create Random Content' for quick demo\n");
            outputArea.append("4. Select content and click 'Display Selected'\n");
            outputArea.append("5. Notice how factory creates different content types\n\n");

            frame.setVisible(true);
        });
    }

    private static void updateContentInfo(Content content) {
        contentInfoLabel.setText("<html><b>" + content.getType() + "</b><br>" + 
                                "<b>Title:</b> " + content.getTitle() + "<br>" +
                                "<b>Description:</b> " + content.getDescription() + "</html>");
    }

    // Custom cell renderer for content list
    private static class ContentListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Content) {
                Content content = (Content) value;
                setText(content.getType() + ": " + content.getTitle());
                
                // Set different colors based on content type
                switch (content.getType()) {
                    case "Video Lesson":
                        setForeground(Color.BLUE);
                        break;
                    case "Quiz Module":
                        setForeground(new Color(0, 128, 0));
                        break;
                    case "Article":
                        setForeground(Color.BLACK);
                        break;
                    case "Podcast":
                        setForeground(new Color(128, 0, 128));
                        break;
                    case "Interactive Lab":
                        setForeground(new Color(255, 140, 0));
                        break;
                }
            }
            
            return this;
        }
    }
}