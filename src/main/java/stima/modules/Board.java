package stima.modules;

import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.Scanner;

public class Board {
    public Tile[][] boardMatr = new Tile[9][9];
    public int boardDimension;
    public Tile[] queenTiles = new Tile[9];
    public int queenAmount;

    public Board(int dim) {
        this.boardDimension = dim;
        this.queenAmount = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.boardMatr[i][j] = new Tile(' ',j,i);
            }
            this.queenTiles[i] = new Tile(' ',-1,-1);
        }
    }

    public Tile tileAt(int x, int y) {
        return this.boardMatr[y][x];
    }

    public void updateTileSign(int x, int y, char newSign) {
        this.boardMatr[y][x] = new Tile(newSign, x, y);
    }

    public void placeQueen(int x, int y) {
        this.queenTiles[this.queenAmount] = this.tileAt(x, y);
        this.queenAmount++;
    }

    public Tile lastQueenTile() {
        return this.queenTiles[this.queenAmount - 1];
    }

    public void removeLastQueen() {
        this.queenAmount--;
    }

    public boolean isQueenAtRow(int y) {
        boolean found = false;
        for (int i = 0; i < this.queenAmount; i++) {
            found = y == this.queenTiles[i].y;
            if (found) break;
        }
        return found;
    }

    public boolean isQueenAt(int x, int y) {
        boolean found = false;
        for (int i = 0; i < this.queenAmount; i++) {
            found = this.queenTiles[i].isAt(x, y);
            if (found) break;
        }
        return found;
    }

    public boolean isQueenPlaceableAt(int x, int y) {
        boolean placeable = true;
        Tile currentTile = this.tileAt(x, y);
        for (int i = 0; i < this.queenAmount; i++) {
            placeable = placeable && !Tile.isNextTo(this.queenTiles[i], currentTile) 
                        && !Tile.isSameCol(this.queenTiles[i], currentTile)
                        && !Tile.isSameRow(this.queenTiles[i], currentTile)
                        && !Tile.isSameSign(this.queenTiles[i], currentTile);
            if (!placeable) {
                break;
            }
        }
        return placeable;
    }

    public static Board readBoard(String inputFile) {
        Board B = new Board(9);
        File myObj = new File("test.txt");
        try (Scanner myReader = new Scanner(myObj)) {
            int dim = -1;
            int n = 0;
            boolean readFail = false;
            String uniqueSigns = "";
            while (myReader.hasNextLine()) {
                String boardRow = myReader.nextLine();
                if (dim == -1) {
                    dim = boardRow.length();
                    B = new Board(dim);
                }
                readFail = (n >= dim) || (boardRow.length() != dim);
                if (readFail) {
                    System.out.println("Dimensions do not match.");
                    B = new Board(9);
                    break;
                }
                for (int m = 0; m < dim; m++) {
                    B.updateTileSign(m, n, boardRow.charAt(m));
                    boolean found = false;
                    for (int k = 0; k < uniqueSigns.length(); k++) {
                        if (boardRow.charAt(m) == uniqueSigns.charAt(k)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) uniqueSigns += boardRow.charAt(m);
                }
                n++;
            }
            if (uniqueSigns.length() != dim) {
                System.out.println("Not enough tile signs.");
                B = new Board(9);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return B;
    }

    public void printBoard() {
        for (int i = 0; i < this.boardDimension; i++) {
            for (int j = 0; j < this.boardDimension; j++) {
                if (this.isQueenAt(j, i)) {
                    System.out.print('#');
                }
                else {
                    System.out.print(this.tileAt(j, i).sign);
                }   
            }
            System.out.print('\n');
        }
    }

    public void printQueens() {
        System.out.println("Queens : ");
        for (int i = 0; i < this.queenAmount; i++) {
            System.out.println("Queen " + (i + 1) + " (" + this.queenTiles[i].x + ", " + this.queenTiles[i].y + ", " + this.queenTiles[i].sign + ")");
        }
    }
}
