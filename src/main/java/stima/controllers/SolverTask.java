package stima.controllers;

import javafx.concurrent.Task;

import stima.modules.*;

public class SolverTask extends Task<Integer>{

    private Solver solver;
    
    public SolverTask(Solver S) {
        this.solver = S;
    }

    @Override
    protected Integer call() throws Exception {
        long start = System.currentTimeMillis();
       solver.processState = "Processing...";
        while(!solver.noPossibilityLeft() && !solver.allQueensPlaced()) {
            while(!solver.noTilesLeft() && !solver.allQueensPlaced()) {
                if (solver.isQueenPlaceableAtSelected()) {
                    solver.placeQueenAtSelected();
                    solver.setSelectedTile(0, 0);
                }
                else {
                    solver.moveSelectedTile();
                }
            }
            if (solver.allQueensPlaced()) break;
            solver.backtrackToLastQueen();
            solver.timeElapsed = (System.currentTimeMillis() - start);
            updateValue(solver.iterations);
        }
        if (solver.allQueensPlaced()) {
            solver.iterations++;
            solver.processState = "Answer Found";
        }
        if (solver.noPossibilityLeft()) {
            solver.processState = "Answer not Found";
        }
        return solver.iterations;
    }
    
}
