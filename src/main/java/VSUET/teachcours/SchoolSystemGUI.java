package VSUET.teachcours;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SchoolSystemGUI {

    public SchoolSystemGUI() {
        SchoolController controller = new SchoolController();
    }

    public void showMainWindow() {
        SwingUtilities.invokeLater(this::initElements);
    }

    private void initElements() {
        JFrame frame = new JFrame("School System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton showAllButton = new JButton("Показать учителей");
        showAllButton.addActionListener(e -> showAllTeachers());
        mainPanel.add(createButtonPanel(showAllButton), gbc);

        gbc.gridy++;
        JButton addButton = new JButton("Добавить учителя");
        addButton.addActionListener(e -> addTeacher());
        mainPanel.add(createButtonPanel(addButton), gbc);

        gbc.gridy++;
        JButton addCourseButton = new JButton("Добавить курс учителю");
        addCourseButton.addActionListener(e -> addCourseTeacher());
        mainPanel.add(createButtonPanel(addCourseButton), gbc);

        gbc.gridy++;
        JButton removeButton = new JButton("Удалить учителя или курс");
        removeButton.addActionListener(e -> removeTeacherOrCourse());
        mainPanel.add(createButtonPanel(removeButton), gbc);

        gbc.gridy++;
        JButton editButton = new JButton("Редактировать учителя или курс");
        editButton.addActionListener(e -> editTeacherOrCourse());
        mainPanel.add(createButtonPanel(editButton), gbc);

        gbc.gridy++;
        JButton showCoursesButton = new JButton("Показать все курсы");
        showCoursesButton.addActionListener(e -> showAllCourses());
        mainPanel.add(createButtonPanel(showCoursesButton), gbc);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createButtonPanel(JButton button) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(button);
        buttonPanel.add(Box.createHorizontalGlue());
        return buttonPanel;
    }

    private void showAllTeachers() {
        JFrame teachersFrame = createFrame("Все учителя");
        JTextArea teachersTextArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(teachersTextArea);
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> teachersFrame.dispose());
        JPanel teachersPanel = new JPanel();
        teachersPanel.add(backButton);
        teachersPanel.add(scrollPane);
        teachersFrame.add(teachersPanel);
        teachersFrame.setVisible(true);

        List<Teacher> teachers = AdministrationDatabase.loadAllTeachers();
        StringBuilder output = new StringBuilder("Учителя:\n");
        for (Teacher teacher : teachers) {
            output.append("Имя: ").append(teacher.getFullName()).append(", Возраст: ").append(teacher.getAge()).append("\n");
            output.append("Курсы:\n");
            for (Course course : AdministrationDatabase.loadTeacherCourses(teacher)) {
                output.append(" - ").append(course.getName()).append(" (").append(course.getHours()).append(" часов)\n");
            }
            output.append("\n");
        }
        teachersTextArea.setText(output.toString());
    }

    private void addTeacher() {
        JFrame addTeacherFrame = createFrame("Добавить учителя");
        JTextField fullNameField = new JTextField(20);
        JTextField ageField = new JTextField(5);
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> {
            String fullName = fullNameField.getText();
            int age = Integer.parseInt(ageField.getText());

            Teacher newTeacher = new Teacher(fullName, age);
            int teacherId = AdministrationDatabase.saveTeacher(newTeacher);

            if (teacherId != -1) {
                JOptionPane.showMessageDialog(addTeacherFrame, "Учитель успешно добавлен! ID учителя: " + teacherId);
            } else {
                JOptionPane.showMessageDialog(addTeacherFrame, "Ошибка при добавлении учителя.");
            }

            addTeacherFrame.dispose();
        });
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> addTeacherFrame.dispose());

        JPanel addTeacherPanel = new JPanel();
        addTeacherPanel.add(new JLabel("Полное имя:"));
        addTeacherPanel.add(fullNameField);
        addTeacherPanel.add(new JLabel("Возраст:"));
        addTeacherPanel.add(ageField);
        addTeacherPanel.add(addButton);
        addTeacherPanel.add(backButton);

        addTeacherFrame.add(addTeacherPanel);
        addTeacherFrame.setVisible(true);
    }

    private void addCourseTeacher() {
        JFrame addCourseFrame = createFrame("Добавить курс учителю");
        JTextField teacherNameField = new JTextField(20);
        JTextField courseNameField = new JTextField(20);
        JTextField hoursField = new JTextField(5);
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> {
            String teacherName = teacherNameField.getText();
            String courseName = courseNameField.getText();
            int hours = Integer.parseInt(hoursField.getText());

            Teacher teacher = AdministrationDatabase.findTeacherByName(teacherName);
            if (teacher == null) {
                JOptionPane.showMessageDialog(addCourseFrame, "Учитель с именем '" + teacherName + "' не найден.");
                return;
            }

            Course newCourse = new Course(courseName, hours);
            int courseId = AdministrationDatabase.saveCourse(newCourse);

            if (courseId != -1) {
                AdministrationDatabase.saveTeacherCourse(teacher.getId(), courseId);
                JOptionPane.showMessageDialog(addCourseFrame, "Курс успешно добавлен учителю!");
            } else {
                JOptionPane.showMessageDialog(addCourseFrame, "Ошибка при добавлении курса.");
            }

            addCourseFrame.dispose();
        });
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> addCourseFrame.dispose());

        JPanel addCoursePanel = new JPanel();
        addCoursePanel.add(new JLabel("Полное имя учителя:"));
        addCoursePanel.add(teacherNameField);
        addCoursePanel.add(new JLabel("Название курса:"));
        addCoursePanel.add(courseNameField);
        addCoursePanel.add(new JLabel("Часы:"));
        addCoursePanel.add(hoursField);
        addCoursePanel.add(addButton);
        addCoursePanel.add(backButton);

        addCourseFrame.add(addCoursePanel);
        addCourseFrame.setVisible(true);
    }


    private void removeTeacherOrCourse() {
        JFrame removeFrame = createFrame("Удалить учителя/курс");
        JTextField teacherNameField = new JTextField(20);
        JTextField courseNameField = new JTextField(20);

        JComboBox<String> operationComboBox = new JComboBox<>(new String[]{"Удалить учителя", "Удалить курс"});

        JButton removeButton = new JButton("Удалить");

        removeButton.addActionListener(e -> {
            String teacherName = teacherNameField.getText();
            String courseName = courseNameField.getText();

            Teacher teacher = AdministrationDatabase.findTeacherByName(teacherName);
            Course course = null;

            if (operationComboBox.getSelectedItem().equals("Удалить учителя")) {
                if (teacher != null) {
                    AdministrationDatabase.removeTeacher(teacher);
                    JOptionPane.showMessageDialog(removeFrame, "Учитель успешно удален!");
                } else {
                    JOptionPane.showMessageDialog(removeFrame, "Учитель с именем '" + teacherName + "' не найден.");
                }
            } else if (operationComboBox.getSelectedItem().equals("Удалить курс")) {
                if (teacher != null) {
                    course = AdministrationDatabase.findCourseByName(teacher, courseName);
                }

                if (course != null) {
                    AdministrationDatabase.removeCourse(course);
                    JOptionPane.showMessageDialog(removeFrame, "Курс успешно удален!");
                } else {
                    JOptionPane.showMessageDialog(removeFrame, "Курс с именем '" + courseName + "' не найден.");
                }
            }

            removeFrame.dispose();
        });

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> removeFrame.dispose());

        JPanel removePanel = new JPanel();
        removePanel.add(new JLabel("Выберите операцию:"));
        removePanel.add(operationComboBox);
        removePanel.add(new JLabel("Полное имя учителя:"));
        removePanel.add(teacherNameField);
        removePanel.add(new JLabel("Название курса (для удаления курса):"));
        removePanel.add(courseNameField);
        removePanel.add(removeButton);
        removePanel.add(backButton);

        removeFrame.add(removePanel);
        removeFrame.setVisible(true);
    }

    private void editTeacherOrCourse() {
        JFrame editFrame = createFrame("Редактировать учителя/курс");
        JTextField teacherNameField = new JTextField(20);
        JTextField courseNameField = new JTextField(20);

        JComboBox<String> operationComboBox = new JComboBox<>(new String[]{"Редактировать учителя", "Редактировать курс"});

        JButton editButton = new JButton("Редактировать");

        editButton.addActionListener(e -> {
            String teacherName = teacherNameField.getText();
            String courseName = courseNameField.getText();

            if (operationComboBox.getSelectedItem().equals("Редактировать учителя")) {
                editTeacherAction(teacherName);
            } else if (operationComboBox.getSelectedItem().equals("Редактировать курс")) {
                editCourseAction(teacherName, courseName);
            }

            editFrame.dispose();
        });

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> editFrame.dispose());

        JPanel editPanel = new JPanel();
        editPanel.add(new JLabel("Выберите операцию:"));
        editPanel.add(operationComboBox);
        editPanel.add(new JLabel("Полное имя учителя:"));
        editPanel.add(teacherNameField);
        editPanel.add(new JLabel("Название курса (для редактирования курса):"));
        editPanel.add(courseNameField);
        editPanel.add(editButton);
        editPanel.add(backButton);

        editFrame.add(editPanel);
        editFrame.setVisible(true);
    }

    private void editTeacherAction(String teacherName) {
        Teacher teacher = AdministrationDatabase.findTeacherByName(teacherName);
        if (teacher != null) {
            JOptionPane.showMessageDialog(null, "Учитель успешно отредактирован!");
        } else {
            JOptionPane.showMessageDialog(null, "Учитель с именем '" + teacherName + "' не найден.");
        }
    }

    private void editCourseAction(String teacherName, String courseName) {
        Teacher teacher = AdministrationDatabase.findTeacherByName(teacherName);
        if (teacher != null) {
            Course course = AdministrationDatabase.findCourseByName(teacher, courseName);
            if (course != null) {
                JOptionPane.showMessageDialog(null, "Курс успешно отредактирован!");
            } else {
                JOptionPane.showMessageDialog(null, "Курс с именем '" + courseName + "' не найден.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Учитель с именем '" + teacherName + "' не найден.");
        }
    }


    private void showAllCourses() {
        JFrame coursesFrame = createFrame("Все курсы");
        JTextArea coursesTextArea = new JTextArea(10, 30);
        JButton backButton = new JButton("Назад");
        JScrollPane scrollPane = new JScrollPane(coursesTextArea);
        backButton.addActionListener(e -> coursesFrame.dispose());
        JPanel coursesPanel = new JPanel();
        coursesPanel.add(backButton);
        coursesPanel.add(scrollPane);
        coursesFrame.add(coursesPanel);
        coursesFrame.setVisible(true);

        List<Course> courses = AdministrationDatabase.loadAllCourses();
        StringBuilder output = new StringBuilder("Курсы:\n");
        for (Course course : courses) {
            output.append("Название: ").append(course.getName()).append(", Часы: ").append(course.getHours()).append("\n");
        }
        coursesTextArea.setText(output.toString());
    }

    private JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 200);
        return frame;
    }
}
