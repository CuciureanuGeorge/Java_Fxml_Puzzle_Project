package repository;

import javafx.scene.image.Image;

public class Item {
    private Image image;
    private int row;
    private int col;

    public Item()
    {
    }

    public Item(Image image, int row, int col)
    {
        this.image=image;
        this.row=row;
        this.col=col;
    }

    public void setRow(int row)
    {
        this.row=row;
    }

    public void setCol(int col)
    {
        this.col=col;
    }

    public Item(Image image)
    {
        this.image=image;
    }

    public int getRow()
    {
        return this.row;
    }

    public int getCol()
    {
        return this.col;
    }

    public Image getImage()
    {return image;}

}
