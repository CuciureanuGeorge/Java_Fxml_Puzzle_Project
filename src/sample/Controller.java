package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import repository.Item;
import repository.Repository;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Controller {
    @FXML
    public Label timeLabel;
    @FXML
    public TextField nGiving;
    @FXML
    private ImageView missingPiece;
    @FXML
    private GridPane gridImage;
    @FXML
    private Button startPuzzle;
    @FXML
    private ImageView mainImage;
    @FXML
    private Button nextImage;

    private Repository repo;
    private int c, r;
    private List<Item> items;
    private int n;
    private List<Image> winningList;
    private final long ONE_SECOND = 1000000000;
    private final StringProperty second = new SimpleStringProperty("0");
    private AnimationTimer timer;
    private long lastTime;

    public Controller() {
        try {
            repo = new Repository();
        } catch (IOException ex) {
            System.out.println("Error!");
        }
    }

    public void go_next(MouseEvent mouseEvent) {
        this.mainImage.setImage(repo.getCurrentImage());
        //System.out.println("Button pressed");
    }
    public boolean checkWin()
    {
        List<Image> currentPic = new ArrayList<>();
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                if(i==r && j==c)
                    continue;
                else
                    currentPic.add(getImageFromGrid(i, j));
        for(int i=0; i<n*n-1; i++)
            System.out.println(currentPic.get(i));
        //System.out.println("Space");
        for(int i=0; i<n*n-1; i++)
            System.out.println(winningList.get(i));
        winningList.remove(missingPiece.getImage());
        for(int i=0; i<n*n-1; i++)
            if(winningList.get(i)==missingPiece.getImage())
                continue;
            else
                if(currentPic.get(i)!=winningList.get(i))
                return false;
        //System.out.println(currentPic.size());
        //System.out.println(winningList.size());
        return true;
    }

    private Image getImageFromGrid(int x, int y)
    {
        for (Node node : gridImage.getChildren()) {
            if (gridImage.getRowIndex(node) != null && gridImage.getColumnIndex(node) != null && gridImage.getRowIndex(node) == x && gridImage.getColumnIndex(node) == y) {
                if (node instanceof ImageView) {
                    ImageView im = (ImageView) node;
                    return im.getImage();
                }
            }
        }
        return null;
    }


    public void start_puzzle(MouseEvent mouseEvent) {
        lastTime=0;
        second.set("0");
        timeLabel.textProperty().bind(second);
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0)
                    lastTime = now;
                if(now - lastTime > ONE_SECOND){
                    second.set(String.valueOf(Integer.parseInt(second.get())+1));
                    lastTime = now;
                    if(timeLabel.getText().equals("600"))
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setContentText("You won!");
                        timer.stop();
                        alert.showAndWait();
                    }
                    //System.out.println(now);
                }
            }
        };
        timer.start();

        ImageView imv = null;

        gridImage.getRowConstraints().remove(0);
        gridImage.getRowConstraints().remove(0);
        gridImage.getRowConstraints().remove(0);
        gridImage.getColumnConstraints().remove(0);
        gridImage.getColumnConstraints().remove(0);
        gridImage.getColumnConstraints().remove(0);


        //-this is where we size the puzzle size:3x3
        this.n=3;

        int sz = 500 / n;

        for (int i = 0; i < n; i++) {
            RowConstraints con = new RowConstraints();
            con.setPrefHeight(sz);
            gridImage.getRowConstraints().add(con);

            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(sz);
            gridImage.getColumnConstraints().add(col);

        }


        try {
            int count = 0;
            List<Image> imgs = repo.splitImage(n);

            this.items = new ArrayList<>();
            for (int i = 0; i < imgs.size(); i++)
            {
                items.add(new Item(imgs.get(i)));
            }
            winningList=imgs;

            Collections.shuffle(items);

            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    imv = new ImageView();
                    imv.setImage(items.get(count++).getImage());
                    items.get(count - 1).setRow(i);
                    items.get(count - 1).setCol(j);
                    imv.setFitHeight(this.gridImage.getHeight() / n);
                    imv.setFitWidth(this.gridImage.getWidth() / n);
                    this.gridImage.add(imv, j, i);
                }

            ObservableList<Node> childrens = gridImage.getChildren();

            for (Node node : childrens) {
                if (gridImage.getRowIndex(node) != null && gridImage.getColumnIndex(node) != null && gridImage.getRowIndex(node) == 0 && gridImage.getColumnIndex(node) == 0) {
                    if (node instanceof ImageView) {
                        ImageView im = (ImageView) node;
                        missingPiece.setImage(im.getImage());
                        gridImage.getChildren().remove(node);
                        c = 0;
                        r = 0;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    public void keyPressed(KeyEvent keyEvent) {
        //System.out.println(keyEvent.getCharacter());
        if (keyEvent.getCharacter().equals("w")) {
            ObservableList<Node> nodes = gridImage.getChildren();
            for (Node node : nodes) {
                if (gridImage.getRowIndex(node) != null && gridImage.getColumnIndex(node) != null && gridImage.getRowIndex(node) == r+1 && gridImage.getColumnIndex(node) == c) {
                    ImageView imv = (ImageView)node;
                    gridImage.getChildren().remove(node);
                    gridImage.add(imv, c, r);
                    r++;
                    //System.out.println(r);
                    //System.out.println(c);
                    break;
                }
            }
        }
        else if(keyEvent.getCharacter().equals("a"))
        {
            ObservableList<Node> nodes = gridImage.getChildren();
            for (Node node : nodes) {
                if (gridImage.getRowIndex(node) != null && gridImage.getColumnIndex(node) != null && gridImage.getRowIndex(node) == r && gridImage.getColumnIndex(node) == c+1) {

                    ImageView imv = (ImageView)node;
                    gridImage.getChildren().remove(node);
                    gridImage.add(imv, c, r);
                    c++;
                    //System.out.println(r);
                    //System.out.println(c);
                    break;
                }
            }
        }
        else if(keyEvent.getCharacter().equals("s"))
        {
            ObservableList<Node> nodes = gridImage.getChildren();
            for (Node node : nodes) {
                if (gridImage.getRowIndex(node) != null && gridImage.getColumnIndex(node) != null && gridImage.getRowIndex(node) == r-1  && gridImage.getColumnIndex(node) == c) {
                    ImageView imv = (ImageView)node;
                    gridImage.getChildren().remove(node);
                    gridImage.add(imv, c, r);
                    r--;
                    //System.out.println(r);
                   //System.out.println(c);
                    break;
                }
            }

        }
        else if ((keyEvent.getCharacter().equals("d")))
        {
            ObservableList<Node> nodes = gridImage.getChildren();
            for (Node node : nodes) {
                if (gridImage.getRowIndex(node) != null && gridImage.getColumnIndex(node) != null && gridImage.getRowIndex(node) == r  && gridImage.getColumnIndex(node) == c-1) {
                    ImageView imv = (ImageView)node;
                    gridImage.getChildren().remove(node);
                    gridImage.add(imv, c, r);
                    c--;
                    //System.out.println(r);
                    //System.out.println(c);
                    break;
                }
            }

        }
        if(checkWin()==true)
        {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("You won!");
            //timer.stop();
            //alert.showAndWait();
        }
    }


}