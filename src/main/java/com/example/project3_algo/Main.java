package com.example.project3_algo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {
    private Pane pane;
    private Graph graph;
    private ChoiceBox<Vertex> sourceBox;
    private TextField txMaster;
    private ChoiceBox<Vertex> destinationBox;
    private final Double minX = -180.0;
    private final Double maxY = 90.0;
    private final Double maxX = 180.0;
    private final Double minY = -90.0;
    private final Double maxWindowX = 1150.0;
    private final Double maxWindowY = 575.0;
    private static int lock = 0;
    private Dijkstra dijkstra;
    private TextArea textArea;
    private TextField textField;
    private List<Line> Linelist = new ArrayList<>();
    private final List<Polygon> arrowHeadList = new ArrayList<>();
    static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Image image = new Image("file:world-map.jpg");
        ImageView imageView = new ImageView(image);
        imageView.prefHeight(image.getHeight());
        imageView.prefWidth(image.getWidth());
        imageView.setPickOnBounds(true);
        pane = new Pane(imageView);

        VBox vb = new VBox(30);
        vb.setAlignment(Pos.TOP_CENTER);
        vb.setPadding(new Insets(50, 50, 50, 50));

        txMaster = new TextField();
        txMaster.setPrefSize(100, 50);
        txMaster.setEditable(false);
        txMaster.setStyle("-fx-font-size:30px;-fx-text-fill:red");

        HBox hb1 = new HBox(5);
        hb1.setAlignment(Pos.CENTER);
        Label sourceLabel = new Label("Source:");
        sourceLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));

        sourceBox = new ChoiceBox<>();
        sourceBox.setStyle("-fx-font: 20px \"Serif\"; -fx-border-color: black; -fx-background-color:tan ");

        hb1.getChildren().addAll(sourceLabel, sourceBox);

        Label destinationLabel = new Label("Destination:");
        destinationLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));

        destinationBox = new ChoiceBox<>();
        destinationBox.setStyle("-fx-font: 20px \"Serif\"; -fx-border-color: black; -fx-background-color:tan ");

        HBox hb2 = new HBox(5);
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().addAll(destinationLabel, destinationBox);

        VBox pathBox = new VBox(20);
        pathBox.setAlignment(Pos.CENTER);
        Label pathLabel = new Label("Path:");
        pathLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));

        textArea = new TextArea();
        textArea.setPrefSize(120, 200);
        textArea.setEditable(false);
        textArea.setStyle("-fx-border-color : black;");
        Font font = Font.font("Monospaced", 16);
        textArea.setFont(font);

        Label disLabel = new Label("Distance");
        disLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));

        textField = new TextField();
        textField.setEditable(false);
        textField.setPrefSize(100, 30);
        textField.setStyle("-fx-border-color : black;");
        textField.setFont(font);

        Button runBut = new Button("Run");
        runBut.setPrefSize(150, 30);
        runBut.setStyle("-fx-font: 20px \"Serif\"; -fx-border-color: black; -fx-background-color:tan ");
        runBut.setOnAction(e -> runTheAction());

        pathBox.getChildren().addAll(pathLabel, textArea, disLabel, textField);

        runBut.setDisable(true);

        sourceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkRunButtonState(runBut));

        destinationBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkRunButtonState(runBut));

        Button restBut = new Button("Rest");
        restBut.setPrefSize(150, 30);
        restBut.setStyle("-fx-font: 20px \"Serif\"; -fx-border-color: black; -fx-background-color:tan ");
        restBut.setOnAction(q -> clearMap());

        vb.getChildren().addAll(hb1, hb2, runBut, pathBox, restBut);
        vb.setStyle("-fx-background-color:lightblue");

        readFromFile();

        dijkstra = new Dijkstra(graph);

        pane.setMinSize(image.getWidth(), image.getHeight());

        Label title = new Label("World Map ");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));


        VBox map = new VBox(20);
        map.getChildren().addAll(title,pane);
        map.setAlignment(Pos.CENTER);


        SplitPane sp = new SplitPane(map, vb);
        Scene scene = new Scene(sp, screenSize.getWidth(), screenSize.getHeight());

        stage.setScene(scene);
        stage.show();
    }

    private void checkRunButtonState(Button runBut) {
        runBut.setDisable(sourceBox.getSelectionModel().getSelectedItem() == null || destinationBox.getSelectionModel().getSelectedItem() == null);
    }

    private void readFromFile() {
        try (Scanner scan = new Scanner(new File("test.txt"))) {
            graph = new Graph();
            int vertexNumber = Integer.parseInt(scan.nextLine().split(",")[0]);

            for (int i = 0; i < vertexNumber; i++) {
                String[] s = scan.nextLine().split(",");
                String name = s[0];
                Double lat = Double.parseDouble(s[1]);
                Double lon = Double.parseDouble(s[2]);
                Vertex vertex = new Vertex(lat, lon, name);
                graph.add(vertex);
                newLocationAdd(vertex);
            }

            while (scan.hasNextLine()) {
                String[] s = scan.nextLine().split(",");
                Vertex v1 = graph.get(new Vertex(s[0]));
                Vertex v2 = graph.get(new Vertex(s[1]));
                graph.addEdge(v1, v2);
//                graph.addEdge(v2, v1);

            }
        } catch (FileNotFoundException | NumberFormatException e) {
            showAlert("Error reading file: " + e.getMessage());
        }
    }

    private void newLocationAdd(Vertex vertex) {
        sourceBox.getItems().add(vertex);
        destinationBox.getItems().add(vertex);

        double x = ((vertex.getLongitude() - minX) / (maxX - minX) * maxWindowX);
        double y = ((maxY - vertex.getLatitude()) / (maxY - minY) * maxWindowY);

        Label label = new Label(vertex.getLocation());
        label.setLayoutX(x + 10);
        label.setLayoutY(y - 5);
        label.setFont(new Font(8));
        label.setStyle("-fx-text-fill: red;");
        pane.getChildren().add(label);

        Circle circle = new Circle(x, y, 4);
        circle.setStyle("-fx-fill:red");
        circle.setId(vertex.getLocation());
        pane.getChildren().add(circle);
        circle.setOnMouseEntered(e -> txMaster.setText(circle.getId()));
        circle.setOnMouseExited(e -> txMaster.setText(""));
        circle.setOnMouseClicked(e -> handleCircleClick(circle));
    }

    private void handleCircleClick(Circle circle) {
        if (lock == 0) {
            circle.setFill(Color.BLACK);
            lock++;
            sourceBox.getSelectionModel().select(graph.get(new Vertex(circle.getId())));
        } else if (lock == 1) {
            if (circle.getFill().equals(Color.BLACK)) {
                lock = 0;
                sourceBox.getSelectionModel().select(null);
                destinationBox.getSelectionModel().select(null);
                resetCircleColors();
            } else {
                circle.setFill(Color.BLACK);
                lock++;
                destinationBox.getSelectionModel().select(graph.get(new Vertex(circle.getId())));
            }
        } else if (lock == 2) {
            if (circle.getFill().equals(Color.BLACK)) {
                lock = 0;
                sourceBox.getSelectionModel().select(null);
                destinationBox.getSelectionModel().select(null);
                resetCircleColors();
            }
        }
    }

    private void resetCircleColors() {
        pane.getChildren().forEach(node -> {
            if (node instanceof Circle) {
                ((Circle) node).setFill(Color.RED);
            }
        });
    }

    private void addArrowHead(Line line) {
        double arrowLength = 10;
        double arrowWidth = 7;

        double ex = line.getStartX();
        double ey = line.getStartY();
        double sx = line.getEndX();
        double sy = line.getEndY();

        double angle = Math.atan2(ey - sy, ex - sx);

        double x1 = ex - arrowLength * Math.cos(angle - Math.PI / 6);
        double y1 = ey - arrowLength * Math.sin(angle - Math.PI / 6);
        double x2 = ex - arrowLength * Math.cos(angle + Math.PI / 6);
        double y2 = ey - arrowLength * Math.sin(angle + Math.PI / 6);

        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(ex, ey, x1, y1, x2, y2);
        arrowHead.setFill(Color.BLACK);

        pane.getChildren().add(arrowHead);
        arrowHeadList.add(arrowHead);
    }

    private void runTheAction() {
        Vertex source = sourceBox.getSelectionModel().getSelectedItem();
        Vertex destination = destinationBox.getSelectionModel().getSelectedItem();
        if (source != null && destination != null) {
            List<Vertex> list = dijkstra.getShortestPath(source, destination);
            if (list.isEmpty()) {
                showAlert("There is no path from " + source.getLocation() + " to " + destination.getLocation());
                textArea.setText("");
                textField.setText("");
            } else {
                displayPath(list);
            }
        }

        if (dijkstra.getCost(destination) == Double.MAX_VALUE) {
            clearMap();
            textArea.setText("There is no path from " + source.getLocation() + " to " + destination.getLocation());
            textField.setText("");
//            assert source != null;
//            assert destination != null;
//            showAlert("There is no path from " + source.getLocation() + " to " + destination.getLocation());
        }
    }

    private void displayPath(List<Vertex> list) {
        StringBuilder s = new StringBuilder();
        for (int i = list.size() - 1; i >= 0; i--) {
            s.append("-->").append(list.get(i)).append("\n");
        }
        textArea.setText(s.toString());
        textField.setText(String.format("%.2f", dijkstra.getCost(list.get(0))) + " Km");

        clearLines();
        for (int i = 0; i < list.size() - 1; i++) {
            Line line = createLine(list.get(i), list.get(i + 1));
            line.setOnMouseClicked(e -> clearMap());
            pane.getChildren().add(line);
            Linelist.add(line);
            addArrowHead(line);
        }
    }

    private Line createLine(Vertex start, Vertex end) {
        Line line = new Line();
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(3);

        double x1 = ((start.getLongitude() - minX) / (maxX - minX) * maxWindowX);
        double y1 = ((maxY - start.getLatitude()) / (maxY - minY) * maxWindowY);
        double x2 = ((end.getLongitude() - minX) / (maxX - minX) * maxWindowX);
        double y2 = ((maxY - end.getLatitude()) / (maxY - minY) * maxWindowY);
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        return line;
    }

    private void clearLines() {
        if (!Linelist.isEmpty()) {
            for (Line line : Linelist) {
                pane.getChildren().remove(line);
            }
        }
        Linelist.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Path Found");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearMap() {
        sourceBox.getSelectionModel().clearSelection();
        destinationBox.getSelectionModel().clearSelection();
        textArea.setText("");
        textField.setText("");
        clearLines();
        clearArrowHeads();
        lock = 0;
        readFromFile();
    }

    private void clearArrowHeads() {
        if (!arrowHeadList.isEmpty()) {
            for (Polygon arrowHead : arrowHeadList) {
                pane.getChildren().remove(arrowHead);
            }
        }
        arrowHeadList.clear();
    }
}
