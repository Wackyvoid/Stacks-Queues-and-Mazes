// Names: Namita Nair, Ruth Mesfin
// x500s: nair0025, mesfi020

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MyMaze{
    private Cell[][] maze;
    private int rows;
    private int cols;
    private int startRow;
    private int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols]; //creating maze; 2d array of cells
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = new Cell();
            }
        }
        this.rows = rows;
        this.cols = cols;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow) {
        MyMaze newMaze = new MyMaze(rows, cols, startRow, endRow);
        Stack1Gen<int[]> newStack = new Stack1Gen<int[]>(); //initializing stack of cell indices
        int[] startIndex = {startRow, 0};
        newStack.push(startIndex); //adding start of maze to stack
        newMaze.maze[startRow][0].setVisited(true);
        while (!newStack.isEmpty()) { //randomizing direction and checking to make sure new neighbor cell is unvisited
            int[] topIndex = newStack.top();
            int[] neighborCell = {topIndex[0], topIndex[1]};

            int[][] validCells = new int [4][2]; //array where valid neighbors will be put to be randomly accessed
            String[] direction= new String[4]; //keeps track of direction of valid neighbors for removing wall later

            int numValid = 0; //keeps track of number of valid cells
            if ((topIndex[1] + 1) < cols && !newMaze.maze[topIndex[0]][topIndex[1] + 1].getVisited()) { //checks if right neighbor is visited & in bounds
                validCells[numValid]= new int[]{topIndex[0], topIndex[1] + 1};
                direction[numValid]= "right";
                numValid++;
            }
            if ((topIndex[1] - 1) >= 0 && !newMaze.maze[topIndex[0]][topIndex[1] - 1].getVisited()) { //checks if left neighbor is visited & in bounds
                validCells[numValid] = new int[]{topIndex[0], topIndex[1] - 1};
                direction[numValid]= "left";
                numValid++;
            }
            if ((topIndex[0] + 1) < rows && !newMaze.maze[topIndex[0] + 1][topIndex[1]].getVisited()) {//checks if bottom neighbor is visited & in bounds
                validCells[numValid] = new int[]{topIndex[0] + 1, topIndex[1]};
                direction[numValid]= "down";
                numValid++;
            }
            if ((topIndex[0] - 1) >= 0 && !newMaze.maze[topIndex[0] - 1][topIndex[1]].getVisited()) {//checks if above neighbor is visited & in bounds
                validCells[numValid] = new int[]{topIndex[0] - 1, topIndex[1]};
                direction[numValid]= "up";
                numValid++;
            }

            if(numValid == 0){ //if all neighbors are visited, pops index from the stack
                newStack.pop();
            } else {
                double rand = Math.random()*numValid;
                neighborCell = validCells[(int) rand]; //randomly accesses one of the valid neighbor cells

                newStack.push(neighborCell); //adding neighbor cell to stack
                newMaze.maze[neighborCell[0]][neighborCell[1]].setVisited(true); //sets cell to visited


                switch (direction[(int)rand]) { //based on location of neighbor cell, removes wall between neighbor and current cell
                    case "right":
                        newMaze.maze[topIndex[0]][topIndex[1]].setRight(false);
                        continue;
                    case "up":
                        newMaze.maze[neighborCell[0]][neighborCell[1]].setBottom(false);
                        continue;
                    case "down":
                        newMaze.maze[topIndex[0]][topIndex[1]].setBottom(false);
                        continue;
                    case "left":
                        newMaze.maze[neighborCell[0]][neighborCell[1]].setRight(false);
                }
            }

        }

        for (int i = 0; i < rows; i++) { //sets all cells visited to false
            for (int j = 0; j < cols; j++) {
                newMaze.maze[i][j].setVisited(false);
            }
        }

        newMaze.maze[endRow][newMaze.cols-1].setRight(false); //opens the right wall at the end of the maze
        return newMaze;

    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        String newRow;
        String bottomRow;

        for (int z = 0; z < maze[0].length; z++) { //printing implicit border at top of maze
            System.out.print("|---");
        }
        System.out.print("|");
        System.out.print("\n");
        for (int i = 0; i < maze.length; i++) {
            newRow = ""; //middle row of cell, containing visited attribute and right wall
            bottomRow = ""; //bottom row of cell, containing bottom wall attribute
            for (int j = 0; j < maze[0].length; j++) { //checks attributes of each cell in row and prints correct output
                if (j == 0) {
                    newRow = "|";
                    bottomRow = "|";
                }

                if (i == startRow && j == 0) {
                    newRow = " ";
                }

                if (maze[i][j].getVisited()) {
                        newRow += " * ";
                } else if (!maze[i][j].getVisited()) {
                        newRow += "   ";
                }

                if (maze[i][j].getRight()) {
                        newRow += "|";
                } else if (!maze[i][j].getRight()) {
                        newRow += " ";
                }

                if (maze[i][j].getBottom()) {
                        bottomRow += "---|";
                } else if (!maze[i][j].getBottom()) {
                        bottomRow += "   |";
                }

            }
            System.out.println(newRow);
            System.out.println(bottomRow);
        }
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q2Gen<int[]> q = new Q2Gen<int[]>();
        int[] startIndex = {startRow, 0};
        q.add(startIndex);
        maze[startRow][0].setVisited(true);
        while (q.length() > 0) { //removes items from queue and visits valid neighbors until end is found
            int[] currentIndex = q.remove();
            maze[currentIndex[0]][currentIndex[1]].setVisited(true);

            if (currentIndex[0] == endRow && currentIndex[1] == cols - 1) { //ends loop because end of maze has been reached
                System.out.println("Solved.");
                break;
            }

            if ((currentIndex[1] + 1) < cols && !maze[currentIndex[0]][currentIndex[1]].getRight()
                    && !maze[currentIndex[0]][currentIndex[1] + 1].getVisited()) { //checks if there is a right neighbor & that it is unvisited & adds to queue
                int[] neighborCell = new int[]{currentIndex[0], currentIndex[1] + 1};
                q.add(neighborCell);
            }
            if ((currentIndex[1] - 1) >= 0 && !maze[currentIndex[0]][currentIndex[1] - 1].getRight()
                    && !maze[currentIndex[0]][currentIndex[1] - 1].getVisited()) { //checks if there is a left neighbor & that it is unvisited & adds to queue
                int[] neighborCell = new int[]{currentIndex[0], currentIndex[1] - 1};
                q.add(neighborCell);
            }
            if ((currentIndex[0] + 1) < rows && !maze[currentIndex[0]][currentIndex[1]].getBottom()
                    && !maze[currentIndex[0] + 1][currentIndex[1]].getVisited()) {//checks if there is a bottom neighbor & that it is unvisited & adds to queue
                int[] neighborCell = new int[]{currentIndex[0] + 1, currentIndex[1]};
                q.add(neighborCell);
            }
            if ((currentIndex[0] - 1) >= 0 && !maze[currentIndex[0] - 1][currentIndex[1]].getBottom()
                    && !maze[currentIndex[0] - 1][currentIndex[1]].getVisited()) {//checks if there is an above neighbor & that it is unvisited & adds to queue
                int[] neighborCell = new int[]{currentIndex[0] - 1, currentIndex[1]};
                q.add(neighborCell);
            }

        }

        printMaze(); //prints the solved maze

    }

    public static void main(String[] args){
        /* Any testing can be put in this main function */
        MyMaze newMaze = makeMaze(7, 12, 2, 5);
        //newMaze.printMaze();
        newMaze.solveMaze();
        //newMaze.printMaze();
    }
}

//assumptions: rows and cols will both be greater than 0. startrow and endrow are within bounds.
