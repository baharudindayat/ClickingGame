package com.example.taa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


public class  MainController implements Initializable {
    @FXML private VBox MainUI;
    @FXML private VBox StartGameUI;
    @FXML private VBox NextLevelUI;
    @FXML private VBox GameoverUI;
    @FXML private VBox LeaderBoardUI;
    @FXML private Canvas GameCanvas;
    @FXML private Button StartButton;
    @FXML private Button ExitButton;
    @FXML private Button LeaderBoardButton;
    @FXML private Button StartGameButton;
    @FXML private Button CancelGameButton;
    @FXML private Button NextLevelButton;
    @FXML private Button GameOverbutton;
    @FXML private Button closenamebutton;
    @FXML private ChoiceBox LevelChoiceBox;
    @FXML private TextField NameTextField;
    @FXML private Label ScoreLabel;
    @FXML private Label LevelLabel;
    @FXML private Label TimerLabel;
    @FXML private TableView<Player> RankTableView;


    private int score;
    private int level;
    private int count;
    private int timer;
    private int index;
    private double opacity;
    private int[] flag;
    private Timeline timeline;
    private IntegerProperty timeSeconds;
    private Sprite background;
    private Sprite killed;
    private ArrayList<Sprite> object;
    AnimationTimer waktu;

    private GraphicsContext gc;
    private Long lastNanoTime;

    private void drawMainScene() {
        gc = GameCanvas.getGraphicsContext2D();
        background = new Sprite();
        background.setImage(Objects.requireNonNull(getClass().getResource("bg.jpg")).toString());
        gc.clearRect(0, 0, 1360, 709);
        gc.drawImage(background.getImage(), 0, 0);
    }

    private class AnimTimer extends AnimationTimer {
        @Override
        public void handle(long currentNanoTime) {
            double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
            lastNanoTime = currentNanoTime;

            gc.clearRect(0, 0, 1360, 709);
            gc.drawImage(background.getImage(), 0, 0);
            for (int i = 0; i < level; i++) {
                if (flag[i] == 1) {
                    object.get(i).setVelocity(0, 0);

                    if (LevelChoiceBox.getValue() == "MEDIUM") {
                        double speed = 150;
                        int randDirection = (int) (Math.random() * 4);
                        if (randDirection == 0)
                            object.get(i).addVelocity(-speed, 0);
                        else if (randDirection == 1)
                            object.get(i).addVelocity(speed, 0);
                        else if (randDirection == 2)
                            object.get(i).addVelocity(0, -speed);
                        else
                            object.get(i).addVelocity(0, speed);

                    } else if (LevelChoiceBox.getValue() == "HARD") {
                        double speed = 200;
                        int randDirection = (int) (Math.random() * 4);
                        if (randDirection == 0)
                            object.get(i).addVelocity(-speed, speed);
                        else if (randDirection == 1)
                            object.get(i).addVelocity(speed, speed);
                        else if (randDirection == 2)
                            object.get(i).addVelocity(speed, -speed);
                        else
                            object.get(i).addVelocity(-speed, -speed);

                    } else if (LevelChoiceBox.getValue() == "EXPERT") {
                        double speed = 300;
                        int randDirection = (int) (Math.random() * 5);
                        if (randDirection == 0)
                            object.get(i).addVelocity(-speed, speed);
                        else if (randDirection == 1)
                            object.get(i).addVelocity(speed, speed);
                        else if (randDirection == 2)
                            object.get(i).addVelocity(speed, -speed);
                        else
                            object.get(i).addVelocity(-speed, -speed);
                    }
                    object.get(i).update(GameCanvas.getWidth(), GameCanvas.getHeight(), elapsedTime);
                    gc.drawImage(object.get(i).getImage(),
                            object.get(i).getPositionX(),
                            object.get(i).getPositionY());
                }
            }
            if (index >= 0 && opacity > 0) {
                gc.setGlobalAlpha(opacity);
                gc.drawImage(killed.getImage(), object.get(index).getPositionX(), object.get(index).getPositionY());
                opacity -= 0.01;
                if (opacity <= 0) {
                    index = -1;
                    opacity = 0.0;
                }
                gc.setGlobalAlpha(1.0);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LevelChoiceBox.setItems(FXCollections.observableArrayList("EASY", "MEDIUM", "HARD","EXPERT"));
        LevelChoiceBox.setValue("EXPERT");

        score = 0;
        level = 1;
        count = 0;
        timer = 0;
        index = 0;
        opacity = 0;
        StartButton.setText("START ? ");
        timeSeconds = new SimpleIntegerProperty(timer);
        TimerLabel.textProperty().bind(timeSeconds.asString());
        LevelLabel.setText("LEVEL : " + level);
        ScoreLabel.setText("SCORE : " + score);
        object = new ArrayList<>();

        drawMainScene();
        waktu = new AnimTimer();

        StartGameUI.setVisible(false);
        NextLevelUI.setVisible(false);
        GameoverUI.setVisible(false);
        LeaderBoardUI.setVisible(false);

    }

    private void playGame() {
        index = -1;
        opacity = 1.0;

        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(timer);
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(timer + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
        timeline.setOnFinished((e) -> {
            GameoverUI.setVisible(true);
            MainUI.setDisable(true);
            waktu.stop();
        });
        double width = GameCanvas.getWidth();
        double height = GameCanvas.getHeight();
        gc = GameCanvas.getGraphicsContext2D();
        lastNanoTime = System.nanoTime();

        gc.clearRect(0, 0, 1360, 709);
        background = new Sprite();
        background.setImage(Objects.requireNonNull(getClass().getResource("bg.jpg")).toString());
        background.setPosition(0, 0);
        gc.drawImage(background.getImage(), 0, 0);

        object.clear();
        flag = new int[level];
        LevelLabel.setText("LEVEL: " + level);
        for (int i = 0; i < level; i++) {
            Sprite objectt = new Sprite();
            objectt.setImage(Objects.requireNonNull(getClass().getResource("object1.png")).toString());
            double px = (width - 50) * Math.random();
            double py = (height - 50) * Math.random();
            objectt.setPosition(px, py);
            gc.drawImage(objectt.getImage(), objectt.getPositionX(), objectt.getPositionY());
            object.add(objectt);
            flag[i] = 1;
        }

        killed = new Sprite();
        killed.setImage(Objects.requireNonNull(getClass().getResource("kill.png")).toString());
        GameCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        for (int i = 0; i < level; i++) {
                            if (flag[i] == 1) {
                                Rectangle2D rect = object.get(i).getBoundary();
                                if (e.getX() > rect.getMinX() && e.getX() < rect.getMaxX() && e.getY() > rect.getMinY() && e.getY() < rect.getMaxY()) {
                                    flag[i] = 0;
                                    score += 10;
                                    count += 1;
                                    index = i;
                                    opacity = 1.0;
                                    ScoreLabel.setText("Score: " + score);

                                    if (count == level) {
                                        count = 0;
                                        NextLevelUI.setVisible(true);
                                        MainUI.setDisable(true);
                                        if (timeline != null) {
                                            timeline.stop();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                waktu.start();
    }

    @FXML
    protected void onStartClicked() {
        StartGameUI.setVisible(true);
        MainUI.setDisable(true);
        score = 0;
        level = 1;
        count = 0;
        timer = 10 * (int) Math.ceil((double) level / 5);
        LevelLabel.setText("Level : " + level);
        ScoreLabel.setText("Score : " + score);
        timeSeconds = new SimpleIntegerProperty(timer);
        TimerLabel.textProperty().bind(timeSeconds.asString());
    }

    @FXML
    protected void onStartGameClicked() {
        StartGameUI.setVisible(false);
        MainUI.setDisable(false);
        StartButton.setDisable(true);
        playGame();
    }
    @FXML
    protected void onCloseNameClicked(){
        StartGameUI.setVisible(false);
        MainUI.setDisable(false);
    }
    @FXML
    protected void onCancelGameClicked() {
        StartButton.setDisable(false);
        MainUI.setDisable(false);
        NextLevelUI.setVisible(false);
        String names = "(name, difficulty, level, score)";
        String values = "('" + NameTextField.getText() + "', '" + LevelChoiceBox.getValue().toString() + "', '" + LevelLabel.getText().split(" ")[1] + "', '" + ScoreLabel.getText().split(" ")[1] + "')";
        String dbCommand = "INSERT INTO player " + names + "VALUES" + values;
        DBConnector dbConn = new DBConnector();
        dbConn.insert(dbCommand);

        waktu.stop();
        drawMainScene();
    }
    @FXML
    protected void onLeaderBoardClicked(){
        MainUI.setDisable(true);
        LeaderBoardUI.setVisible(true);
        RankTableView.getItems().clear();
        RankTableView.getColumns().clear();

        String dbCommand = "SELECT * FROM player WHERE difficulty='" + LevelChoiceBox.getValue().toString() + "' ORDER BY score DESC";
        DBConnector dbConn =  new DBConnector();
        dbConn.start(dbCommand);

        int countPlayer = 0;
        ObservableList<Player> ListPlayers = FXCollections.observableArrayList();
        for (ObservableList<String> dataRow : dbConn.getData()){
            Player player = new Player(dataRow.get(1), dataRow.get(2), Integer.parseInt(dataRow.get(3)), Integer.parseInt(dataRow.get(4)));
            ListPlayers.add(player);
            countPlayer +=1;
            if (countPlayer == 10) break;
        }

        String[] listHeaders =  {"name", "difficulty", "level", "score"};
        for (String header : listHeaders){
            TableColumn<Player, String> column = new TableColumn<>(header);
            column.setCellValueFactory(new PropertyValueFactory<>(header));
            RankTableView.getColumns().addAll(column);
        }
        RankTableView.setItems(ListPlayers);
    }

    @FXML
    protected void onNextLevelClicked() {
        NextLevelUI.setVisible(false);
        MainUI.setDisable(false);
        level += 1;
        timer = 10 * (int) Math.ceil((double) level / 5);
        playGame();
    }

    @FXML
    protected void onGameOverCLicked() {
        GameoverUI.setVisible(false);
        MainUI.setDisable(false);
        StartButton.setDisable(false);

        String names = "(name, difficulty, level, score)";
        String values = "('" + NameTextField.getText() + "', '" + LevelChoiceBox.getValue().toString() + "', '" + LevelLabel.getText().split(" ")[1] + "', '" + ScoreLabel.getText().split(" ")[1] + "')";
        String dbCommand = "INSERT INTO player " + names + "VALUES" + values;
        DBConnector dbConn = new DBConnector();
        dbConn.insert(dbCommand);

        waktu.stop();
        drawMainScene();
    }

    @FXML
    protected void onCloseRankClicked() {
        LeaderBoardUI.setVisible(false);
        MainUI.setDisable(false);
    }
}