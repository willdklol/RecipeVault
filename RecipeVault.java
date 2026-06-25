package recipevault;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class RecipeVault extends JFrame {
    private ArrayList<String> recipes = new ArrayList<>();

    private JTextField nameField;
    private JTextField categoryField;
    private JTextArea ingredientsArea;
    private JTextArea instructionsArea;
    private DefaultListModel<String> recipeListModel;
    private JList<String> recipeList;

    public RecipeVault() {
        setTitle("Recipe Vault");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        nameField = new JTextField();
        categoryField = new JTextField();
        ingredientsArea = new JTextArea(4, 20);
        instructionsArea = new JTextArea(4, 20);

        recipeListModel = new DefaultListModel<>();
        recipeList = new JList<>(recipeListModel);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        formPanel.add(new JLabel("Recipe Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Ingredients:"));
        formPanel.add(new JScrollPane(ingredientsArea));
        formPanel.add(new JLabel("Instructions:"));
        formPanel.add(new JScrollPane(instructionsArea));

        JButton addButton = new JButton("Add Recipe");
        JButton deleteButton = new JButton("Delete Selected");
        JButton searchButton = new JButton("Search");
        JButton saveButton = new JButton("Save Recipes");
        JButton loadButton = new JButton("Load Recipes");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        add(formPanel, BorderLayout.WEST);
        add(new JScrollPane(recipeList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addRecipe());
        deleteButton.addActionListener(e -> deleteRecipe());
        searchButton.addActionListener(e -> searchRecipe());
        saveButton.addActionListener(e -> saveRecipes());
        loadButton.addActionListener(e -> loadRecipes());
    }

    private void addRecipe() {
        String recipe = "Name: " + nameField.getText()
                + " | Category: " + categoryField.getText()
                + " | Ingredients: " + ingredientsArea.getText()
                + " | Instructions: " + instructionsArea.getText();

        recipes.add(recipe);
        recipeListModel.addElement(recipe);

        nameField.setText("");
        categoryField.setText("");
        ingredientsArea.setText("");
        instructionsArea.setText("");
    }

    private void deleteRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();

        if (selectedIndex >= 0) {
            recipes.remove(selectedIndex);
            recipeListModel.remove(selectedIndex);
        }
    }

    private void searchRecipe() {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter recipe name or category:");

        if (searchTerm == null || searchTerm.isEmpty()) {
            return;
        }

        recipeListModel.clear();

        for (String recipe : recipes) {
            if (recipe.toLowerCase().contains(searchTerm.toLowerCase())) {
                recipeListModel.addElement(recipe);
            }
        }
    }

    private void saveRecipes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("recipes.txt"))) {
            for (String recipe : recipes) {
                writer.println(recipe);
            }

            JOptionPane.showMessageDialog(this, "Recipes saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving recipes.");
        }
    }

    private void loadRecipes() {
        recipes.clear();
        recipeListModel.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("recipes.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                recipes.add(line);
                recipeListModel.addElement(line);
            }

            JOptionPane.showMessageDialog(this, "Recipes loaded successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No saved recipes found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecipeVault app = new RecipeVault();
            app.setVisible(true);
        });
    }
}