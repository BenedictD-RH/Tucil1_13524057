package stima.modules;

public class Solver {
    public int selX, selY;
    public Board currBoard;
    public int iterations;
    public String processState;
    public long timeElapsed;

    public Solver(Board B) {
        this.currBoard = B;
        this.selX = 0;
        this.selY = 0;
        this.iterations = 0;
        this.processState = "";
        this.timeElapsed = 0;
    }

    public void moveSelectedTile() {
        this.selX++;
        if (this.selX >= this.currBoard.boardDimension) {
            this.selX = 0;
            this.selY++;
        }
    }

    public void setSelectedTile(int x, int y) {
        this.selX = x;
        this.selY = y;
    }

    public boolean noTilesLeft() {
        return this.selY >= this.currBoard.boardDimension;
    }

    public boolean allQueensPlaced() {
        return this.currBoard.queenAmount == this.currBoard.boardDimension;
    }

    public boolean noPossibilityLeft() {
        return this.noTilesLeft() && this.currBoard.queenAmount == 0;
    }

    public boolean isQueenPlaceableAtSelected() {
        return this.currBoard.isQueenPlaceableAt(this.selX, this.selY);
    }

    public void placeQueenAtSelected() {
        this.currBoard.placeQueen(this.selX, this.selY);
        // for (int i = 0; i < this.currBoard.queenAmount; i++) {
        //     System.out.print("  ");
        // }
        //System.out.println("Placed Queen " + this.currBoard.queenAmount + " at (" + this.selX + ", " + this.selY + ")");
    }

    public void backtrackToLastQueen() {
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
