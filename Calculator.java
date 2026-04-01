package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends Application {

    TextField inputNumber1;
    TextField inputNumber2;
    Label displayResult;
    TextArea historyDisplayArea;

       //  الملف الي رح يتخزين الهيستوري فيه
 private static final String HISTORY_FILE_NAME = "history.txt";

    @Override
    public void start(Stage primaryStage) {
        inputNumber1 = new TextField();
        inputNumber2 = new TextField();
        inputNumber1.setPromptText("Number 1");
        inputNumber2.setPromptText("Number 2");

        displayResult = new Label("Result: ");
        
        Button buttonAdd = new Button("+");
        Button buttonSubtract = new Button("-");
        Button buttonMultiply = new Button("*");
        Button buttonDivide = new Button("/");
        Button buttonClear = new Button("Clear");
        Button buttonShowHistory = new Button("History");

        historyDisplayArea = new TextArea();
        historyDisplayArea.setEditable(false);
        historyDisplayArea.setPrefHeight(100);

              //احذف الملف القديم قبل الرن
  deleteHistoryFile();

        buttonAdd.setOnAction(e -> calculate("+"));
        buttonSubtract.setOnAction(e -> calculate("-"));
        buttonMultiply.setOnAction(e -> calculate("*"));
        buttonDivide.setOnAction(e -> calculate("/"));
        buttonClear.setOnAction(e -> clearFields());
        buttonShowHistory.setOnAction(e -> showHistory());

        HBox inputBox = new HBox(10, inputNumber1, inputNumber2);
        HBox buttonsBox = new HBox(10, buttonAdd, buttonSubtract, buttonMultiply, buttonDivide);
        HBox bottomButtonsBox = new HBox(10, buttonClear, buttonShowHistory);

        VBox mainLayout = new VBox(15,
                inputBox,
                buttonsBox,
                displayResult,
                bottomButtonsBox,
                new Label("History:"),
                historyDisplayArea
        );

        mainLayout.setPadding(new Insets(15));

        Scene scene = new Scene(mainLayout, 300, 300);
        primaryStage.setTitle("JavaFX Calculator With History");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculate(String operation) {
        try {
            double number1 = Double.parseDouble(inputNumber1.getText());
            double number2 = Double.parseDouble(inputNumber2.getText());
            double resultValue = 0;

            switch (operation) {
                case "+":
                    resultValue = number1 + number2;
                    break;
                case "-":
                    resultValue = number1 - number2;
                    break;
                case "*":
                    resultValue = number1 * number2;
                    break;
                case "/":
                    if (number2 == 0) {
                        displayResult.setText("Cannot divide by zero!");
                        return;
                    }
                    resultValue = number1 / number2;
                    break;
            }

            String historyRecord = number1 + " " + operation + " " + number2 + " = " + resultValue;
            displayResult.setText("Result: " + resultValue);

            saveToFile(historyRecord);

        } catch (NumberFormatException e) {
            displayResult.setText("Invalid input!");
        }
    }

    private void clearFields() {
        inputNumber1.clear();
        inputNumber2.clear();
        displayResult.setText("Result: ");
    }

    private void showHistory() {
        historyDisplayArea.clear();
                // قراءة الهيستوري من الملف وعرضه
        List<String> historyList = readHistoryFromFile();
        for (String record : historyList) {
            historyDisplayArea.appendText(record + "\n");
        }
    }
    //  علشان نحفظ عملية حسابية في الملف
    private void saveToFile(String record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE_NAME, true))) {
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
        // علشان نقرا  كل العمليات المحفوظة من الملف
    private List<String> readHistoryFromFile() {
        List<String> historyList = new ArrayList<>();
        File file = new File(HISTORY_FILE_NAME);
        
        if (!file.exists()) {
            return historyList;
        }// إذا كان الملف غير موجود، نرجع قائمة فارغة
        
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyList.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return historyList;
    }
    //  لنحذف ملف الهيستوري لما نعمل رن
    private void deleteHistoryFile() {
        File file = new File(HISTORY_FILE_NAME);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Old history file deleted. Starting fresh.");
            } else {
                System.err.println("Could not delete old history file.");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}