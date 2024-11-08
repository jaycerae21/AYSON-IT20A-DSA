/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package todo;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

public class Todo {

    private static LinkedList<String> todoList = new LinkedList<>();
    private static LinkedList<String> completedTasks = new LinkedList<>();
    private static Stack<String> undoStack = new Stack<>();

    public static void main(String[] args) {
        // Apply a custom UI theme
        UIManager.put("OptionPane.background", new Color(238, 238, 238));
        UIManager.put("Panel.background", new Color(238, 238, 238));
        UIManager.put("Button.background", new Color(102, 153, 255));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));

        boolean running = true;

        while (running) {
            // Create a styled panel for buttons
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Define options as buttons with a consistent design
            String[] options = {"Add Task", "Mark Task as Done", "Undo", "View To-Do List", "View Completed Tasks", "Exit"};
            Dimension buttonSize = new Dimension(200, 40);

            for (String option : options) {
                JButton button = new JButton(option);
                button.setPreferredSize(buttonSize);
                button.setMaximumSize(buttonSize);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 102, 204), 2, true),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));

                button.addActionListener(e -> {
                    switch (option) {
                        case "Add Task" -> addTask();
                        case "Mark Task as Done" -> markTaskAsDone();
                        case "Undo" -> undoLastAction();
                        case "View To-Do List" -> viewToDoList();
                        case "View Completed Tasks" -> viewCompletedTasks();
                        case "Exit" -> {
                            JOptionPane.showMessageDialog(null, "Goodbye!");
                            System.exit(0);
                        }
                    }
                });

                panel.add(button);
                panel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
            }

            // Display the custom panel with JOptionPane
            JOptionPane.showMessageDialog(null, panel, "To-Do List Manager", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private static void addTask() {
        String task = JOptionPane.showInputDialog(null, "Enter the task to add:", "Add Task", JOptionPane.QUESTION_MESSAGE);
        if (task != null && !task.trim().isEmpty()) {
            todoList.add(task);
            undoStack.push("add:" + task);
            JOptionPane.showMessageDialog(null, "Task added: " + task, "Task Added", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Task cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void markTaskAsDone() {
        if (todoList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks in the to-do list.", "Empty List", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder list = new StringBuilder("Current To-Do List:\n");
        for (int i = 0; i < todoList.size(); i++) {
            list.append(i + 1).append(". ").append(todoList.get(i)).append("\n");
        }

        String input = JOptionPane.showInputDialog(null, list + "\nEnter the number of the task to mark as done:", "Mark Task as Done", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                int taskNumber = Integer.parseInt(input) - 1;
                if (taskNumber >= 0 && taskNumber < todoList.size()) {
                    String completedTask = todoList.remove(taskNumber);
                    completedTasks.add(completedTask);
                    undoStack.push("done:" + completedTask);
                    JOptionPane.showMessageDialog(null, "Task marked as done: " + completedTask, "Task Completed", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid task number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void undoLastAction() {
        if (undoStack.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No actions to undo.", "Undo Action", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String action = undoStack.pop();
        String[] parts = action.split(":", 2);
        String actionType = parts[0];
        String task = parts[1];

        if (actionType.equals("add")) {
            todoList.remove(task);
            JOptionPane.showMessageDialog(null, "Undo: Added task '" + task + "' removed from the list.", "Undo Action", JOptionPane.INFORMATION_MESSAGE);
        } else if (actionType.equals("done")) {
            completedTasks.remove(task);
            todoList.add(task);
            JOptionPane.showMessageDialog(null, "Undo: Task '" + task + "' marked as not done.", "Undo Action", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void viewToDoList() {
        if (todoList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "To-Do List is empty.", "To-Do List", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder list = new StringBuilder("Current To-Do List:\n");
            for (int i = 0; i < todoList.size(); i++) {
                list.append(i + 1).append(". ").append(todoList.get(i)).append("\n");
            }
            JOptionPane.showMessageDialog(null, list.toString(), "To-Do List", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void viewCompletedTasks() {
        if (completedTasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No completed tasks.", "Completed Tasks", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder list = new StringBuilder("Completed Tasks:\n");
            for (String task : completedTasks) {
                list.append("- ").append(task).append("\n");
            }
            JOptionPane.showMessageDialog(null, list.toString(), "Completed Tasks", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
