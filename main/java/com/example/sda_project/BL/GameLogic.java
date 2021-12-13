package com.example.sda_project.BL;

public class GameLogic {

    public void Step(Grid grid) {
        int[][] next = new int[100][100];

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                int neighbors = countAliveNeighbors(i, j,grid);

                if (neighbors == 3) {
                    next[i][j] = 1;
                }else if (neighbors < 2 || neighbors > 3) {
                    next[i][j] = 0;
                }else {
                    next[i][j] = grid.getGridArray(i , j);
                }
            }
        }
        grid.setGridArray(next);
    }

    public int countAliveNeighbors(int i, int j,Grid grid) {
        int sum = 0;
        int iStart = i == 0 ? 0 : -1;
        int iEndInclusive = i == 99 ? 0 : 1;
        int jStart = j == 0 ? 0 : -1;
        int jEndInclusive = j == 99 ? 0 : 1;

        for (int k = iStart; k <= iEndInclusive; k++) {
            for (int l = jStart; l <= jEndInclusive; l++) {
                sum += grid.getGridArray(i + k,l + j);
            }
        }

        sum -= grid.getGridArray(i , j);

        return sum;
    }
}
