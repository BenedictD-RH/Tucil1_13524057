package stima.modules;

import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.Scanner;
import java.util.Arrays;

public class Board {
    public Tile[][] boardMatr = new Tile[26][26];
    public int boardDimension;
    public Tile[] queenTiles = new Tile[26];
    public int queenAmount;
    public String uniqueSigns;

    public Board(int dim) {
        this.boardDimension = dim;
        this.queenAmount = 0;
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                this.boardMatr[i][j] = new Tile(' ',j,i);
            }
            this.queenTiles[i] = new Tile(' ',-1,-1);
        }
        this.uniqueSigns = "";
    }

    public Board(Board B) {
        this.boardDimension = B.boardDimension; 
        this.queenAmount = B.queenAmount;
        for (int i = 0; i < B.boardDimension; i++) {
            for (int j = 0; j < B.boardDimension; j++) {
                this.boardMatr[i][j] = new Tile(B.boardMatr[i][j].sign,j,i);
            }
            this.queenTiles[i] = new Tile(B.queenTiles[i].sign, -1,-1);
        }
    }

    public Tile tileAt(int x, int y) {
        return this.boardMatr[y][x];
    }

    public void updateTileSign(int x, int y, char newSign) {
        this.boardMatr[y][x] = new Tile(newSign, x, y);
        this.updateUniqueSigns();
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

    public void updateUniqueSigns() {
        this.uniqueSigns = "";
        for (int i = 0; i < this.boardDimension; i++) {
            for (int j = 0; j < this.boardDimension; j++) {
                if (!this.uniqueSigns.contains(Character.toString(this.tileAt(j,i).sign)) && this.tileAt(j,i).sign != ' ') {
                    this.uniqueSigns += this.tileAt(j,i).sign;
                }
            }
        }
        char[] cArray = this.uniqueSigns.toCharArray();
        Arrays.sort(cArray);
        this.uniqueSigns = new String(cArray);
    }

    public int amountOfSignOnBoard(char sign) {
        int count = 0;
        for (int i = 0; i < this.boardDimension; i++) {
            for (int j = 0; j < this.boardDimension; j++) {
                if (this.tileAt(j, i).sign == sign) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isSignOnBoard(char sign) {
        return amountOfSignOnBoard(sign) > 0;
    }
    
    public boolean isBoardEmpty() {
        return this.uniqueSigns == "";
    }

    public boolean isBoardValid() {
        return (this.uniqueSigns.length() == this.boardDimension) && !this.isSignOnBoard(' ');
    }

    public static Board readBoard(File myObj) {
        Board B = new Board(9);
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
        } catch (FileNotFoundException e) {

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
