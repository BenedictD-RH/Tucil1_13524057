package stima.modules;

public class Solver {
    public int selX, selY;
    public Board currBoard;
    public int iterations;

    public Solver(Board B) {
        this.currBoard = B;
        this.selX = 0;
        this.selY = 0;
        this.iterations = 0;
    }


    public void solveBoard() {
        long start = System.currentTimeMillis();
        while(!this.noPossibilityLeft()) {
            while(!this.noTilesLeft()) {
                if (this.isQueenPlaceableAtSelected()) {
                    this.placeQueenAtSelected();
                    this.setSelectedTile(0, 0);
                    if (allQueensPlaced()) break;
                }
                this.moveSelectedTile();
            }
            if (allQueensPlaced()) break;
            this.backtrackToLastQueen();
        }
        if (allQueensPlaced()) {
            this.iterations++;
            long finish = System.currentTimeMillis();
            this.currBoard.printBoard();
            System.out.println("Time elapsed : " + (finish - start) + "ms");
            System.out.println("Total iterations : " + this.iterations);

        }
        if (this.noPossibilityLeft()) {
            System.out.println("Not Possible");
        }
    }

    private void moveSelectedTile() {
        this.selX++;
        if (this.selX >= this.currBoard.boardDimension) {
            this.selX = 0;
            this.selY++;
        }
    }

    private void setSelectedTile(int x, int y) {
        this.selX = x;
        this.selY = y;
    }

    private boolean noTilesLeft() {
        return this.selY >= this.currBoard.boardDimension;
    }

    private boolean allQueensPlaced() {
        return this.currBoard.queenAmount == this.currBoard.boardDimension;
    }

    private boolean noPossibilityLeft() {
        return this.noTilesLeft() && this.currBoard.queenAmount == 0;
    }

    private boolean isQueenPlaceableAtSelected() {
        return this.currBoard.isQueenPlaceableAt(this.selX, this.selY);
    }

    private void placeQueenAtSelected() {
        this.currBoard.placeQueen(this.selX, this.selY);
        // for (int i = 0; i < this.currBoard.queenAmount; i++) {
        //     System.out.print("  ");
        // }
        //System.out.println("Placed Queen " + this.currBoard.queenAmount + " at (" + this.selX + ", " + this.selY + ")");
    }

    private void backtrackToLastQueen() {
        Tile prevQueenTile = this.currBoard.lastQueenTile();
        this.currBoard.removeLastQueen();
        this.setSelectedTile(prevQueenTile.x, prevQueenTile.y);
        this.moveSelectedTile();
        this.iterations++;
        // if (this.currBoard.queenAmount <= 10) {
        //     // System.out.println("Moved Queen 1");
        //     for (int i = 0; i < this.currBoard.queenAmount; i++) {
        //         System.out.print("  ");
        //     }
        //     System.out.println("Backtracked to Queen " + (this.currBoard.queenAmount + 1) + " at (" + prevQueenTile.x + ", " + prevQueenTile.y + ", " + prevQueenTile.sign + ")");
        // }
    }
}
