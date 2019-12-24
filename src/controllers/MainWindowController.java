package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import helpers.CSVwork;
import helpers.Info;
import helpers.Logic;
import helpers.SimpleInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sql.DatabaseHandler;

public class MainWindowController {
    StringBuilder log = new StringBuilder();
    ArrayList<Info> data;
    private ObservableList<Info> tableList;
    private ObservableList<SimpleInfo> graphicList;

    DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tableTab;

    @FXML
    private DatePicker datePicker1;

    @FXML
    private DatePicker datePicker2;

    @FXML
    private Button downloadButton;

    @FXML
    private TableView<Info> infoTabView;

    @FXML
    private TableColumn<Info, String> dateColumn;

    @FXML
    private TableColumn<Info, Number> openColumn;

    @FXML
    private TableColumn<Info, Number> highColumn;

    @FXML
    private TableColumn<Info, Number> lowColumn;

    @FXML
    private TableColumn<Info, Number> closeColumn;

    @FXML
    private Tab graphicPane;

    @FXML
    private CategoryAxis xAxis = new CategoryAxis();

    @FXML
    private NumberAxis yAxis = new NumberAxis();

    @FXML
    private LineChart<String, Number> graphicLineChart = new LineChart<String, Number>(xAxis, yAxis);

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button drawButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label resultLabel;

    @FXML
    void initialize() {

        ObservableList<String> typeDataList = FXCollections.observableArrayList(
                "open", "high", "low", "close"
        );
        comboBox.setItems(typeDataList);

        drawButton.setOnAction(event -> {
            graphicList = dbHandler.getSimpleData(comboBox.getValue(), datePicker1.getValue().toString(), datePicker2.getValue().toString());
            graphicLineChart.getData().clear();

            int n = graphicList.size();
            double[] x = new double[n];
            double[] y = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = i + 1;
                y[i] = graphicList.get(i).getValue();
            }
            Thread firstThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Logic.mnk(x, y);
                }
            });
            firstThread.start();
            Thread secondThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Logic.polynom2nd(x, y);
                }
            });
            secondThread.start();
            Thread thirdThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Logic.exponential(x, y);
                }
            });
            thirdThread.start();

            try {
                firstThread.join();
                secondThread.join();
                thirdThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            XYChart.Series series = new XYChart.Series();
            series.setName(comboBox.getValue());
            XYChart.Series mnkSeries = new XYChart.Series();
            mnkSeries.setName("least square");
            XYChart.Series polynom2ndSeries = new XYChart.Series();
            polynom2ndSeries.setName("nonlinear graphic");
            XYChart.Series exponentialSeries = new XYChart.Series();
            exponentialSeries.setName("exponential graphic");

            ObservableList<XYChart.Data> graphicData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data> mnkData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data> polynom2ndData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data> exponentialData = FXCollections.observableArrayList();

            for (int i = 0; i < n; i++) {
                graphicData.add(new XYChart.Data(graphicList.get(i).getDate(), graphicList.get(i).getValue()));
                mnkData.add(new XYChart.Data(graphicList.get(i).getDate(), Logic.linearFun(x[i])));
                polynom2ndData.add(new XYChart.Data(graphicList.get(i).getDate(), Logic.nonlinearFun(x[i])));
                exponentialData.add(new XYChart.Data(graphicList.get(i).getDate(), Logic.expFun(x[i])));

            }
            series.setData(graphicData);
            mnkSeries.setData(mnkData);
            polynom2ndSeries.setData(polynom2ndData);
            exponentialSeries.setData(exponentialData);


            graphicLineChart.getData().add(series);
            graphicLineChart.getData().add(mnkSeries);
            graphicLineChart.getData().add(polynom2ndSeries);
            graphicLineChart.getData().add(exponentialSeries);

            log.append("Draw graphics for " + comboBox.getValue() + " price and linear/unlinear regression at " + new Date().toString());
        });

        downloadButton.setOnAction(event -> {
            String dateFrom = datePicker1.getValue().toString();
            String dateTo = datePicker2.getValue().toString();
            System.out.println(dateFrom + "\n" + dateTo);
            try {
                CSVwork.downloadCSV(dateFrom, dateTo);
                Thread.currentThread().sleep(2000);
                data = CSVwork.parseCSV();
                dbHandler.addData(data);
                tableList = FXCollections.observableArrayList(data);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            dateColumn.setCellValueFactory(new PropertyValueFactory<Info, String>("date"));
            openColumn.setCellValueFactory(new PropertyValueFactory<Info, Number>("openPrice"));
            highColumn.setCellValueFactory(new PropertyValueFactory<Info, Number>("highPrice"));
            lowColumn.setCellValueFactory(new PropertyValueFactory<Info, Number>("lowPrice"));
            closeColumn.setCellValueFactory(new PropertyValueFactory<Info, Number>("closePrice"));

            infoTabView.setItems(tableList);
            log.append("Data selected from " + dateFrom + " to " + dateTo + " at " + new Date().toString());
        });

        saveButton.setOnAction(event -> {
            dbHandler.addLog(LoginWindowController.getCurrentUserLogin(), log.toString());
            resultLabel.setText("Saved!");
        });
    }
}
