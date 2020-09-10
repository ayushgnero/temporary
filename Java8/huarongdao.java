import java.io.*;
import java.util.*;

public class Solution {

  public static void main(String[] args) throws NumberFormatException, IOException {
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    int m = scanner.nextInt();
    int k = scanner.nextInt();
    int q = scanner.nextInt();
    boolean moveable[][] = new boolean[n][m];
    for (int row = 0; row < n; row++) {
      for (int col = 0; col < m; col++) {
        moveable[row][col] = scanner.nextInt() == 1;
      }
    }
    Huarongdao hua = new Huarongdao(n,m,k, moveable);
    Map<List<Integer>,Integer> cache = new HashMap<>();
    for (int query = 0; query < q; query++) {
      int emptyRow = scanner.nextInt()-1;
      int emptyCol = scanner.nextInt()-1;
      int caoCaoRow = scanner.nextInt()-1;
      int caoCaoCol = scanner.nextInt()-1;
      int exitRow = scanner.nextInt()-1;
      int exitCol = scanner.nextInt()-1;
      int result;
      if (cache.containsKey(Arrays.asList(emptyRow,emptyCol,caoCaoRow,caoCaoCol,exitRow,exitCol))) {
        result = cache.get(Arrays.asList(emptyRow,emptyCol,caoCaoRow,caoCaoCol,exitRow,exitCol));
      } else {
        result = hua.query(emptyRow, emptyCol, caoCaoRow, caoCaoCol, exitRow, exitCol);
        cache.put(Arrays.asList(emptyRow,emptyCol,caoCaoRow,caoCaoCol,exitRow,exitCol), result);
      }
      System.out.println(result);
    }
  }

  public static class Huarongdao {
    private static final int UP = 0;
    private static final int RIGHT = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private int n;
    private int m;
    private int k;
    private boolean[][] moveable; // row, col
    private byte[][][][] moveAround; // row, col, from, to

    public Huarongdao(int n, int m, int k, boolean[][] moveable) {
      this.n = n;
      this.m = m;
      this.k = k;
      this.moveable = moveable;

      moveAround = new byte[n][m][4][4];
      for (int row = 0; row < n; row++) {
        for (int col = 0; col < m; col++) {
          calcMoveAround(row,col, moveAround[row][col]);
        }
      }
    }

    private void calcMoveAround(int row, int col, byte[][] bs) {
      if (row != 0 && moveable[row-1][col]) {
        calcMoveAround(row,col,row-1,col, bs[UP]);
      }
      if (col != m-1 && moveable[row][col+1]) {
        calcMoveAround(row,col,row,col+1, bs[RIGHT]);
      }
      if (row != n-1 && moveable[row+1][col]) {
        calcMoveAround(row,col,row+1,col, bs[DOWN]);
      }
      if (col != 0 && moveable[row][col-1]) {
        calcMoveAround(row,col,row,col-1, bs[LEFT]);
      }
    }

    // Dijkstra
    // K = 10     K = 11/12
    // YRRR...    YYRRR....
    // .RXR...    Y.RXR....
    // .RXR...    ..RXR....
    // .RRC...    ..RXR....
    //            ..RRC....
    // 7x7
    //            9x9
    //
    // C: Center, X: Blocked, Y: Can not be part of route as cheating is shorter
    private void calcMoveAround(int row, int col, int fromRow, int fromCol, byte[] bs) {
      int diameter = (k+1)/2*2-3;// Long enough away from center to be able to find dists shorter than cheating dist
      int centerPos = (diameter-1)/2;
      byte dist[][] = new byte[diameter][diameter];
      int rowOffset = row-centerPos;
      int colOffset = col-centerPos;
      byte cheatLength = (byte) k;
      for (int rowI = 0; rowI < diameter; rowI++) {
        for (int colI = 0; colI < diameter; colI++) {
          dist[rowI][colI] = isValidAndMoveable(rowI+rowOffset, colI+colOffset) ? cheatLength : -1;
        }
      }
      dist[row-rowOffset][col-colOffset] = -1; // Can not be used
      dist[fromRow-rowOffset][fromCol-colOffset] = 0; // Starting point
      Queue<Pos> queue = new ArrayDeque<>();
      queue.add(new Pos(fromRow-rowOffset,fromCol-colOffset));
      int edgesHit = 0;
      while (!queue.isEmpty()) {
        Pos curPos = queue.remove();
        byte curLength = dist[curPos.row][curPos.col];
        if ((k & ~1) == curLength) { break;}
        if (addMoveAroundPos(dist, cheatLength, queue, curLength, curPos.col, curPos.row-1, centerPos)) { if (++edgesHit == 3) break;}
        if (addMoveAroundPos(dist, cheatLength, queue, curLength, curPos.col, curPos.row+1, centerPos)) { if (++edgesHit == 3) break;}
        if (addMoveAroundPos(dist, cheatLength, queue, curLength, curPos.col-1, curPos.row, centerPos)) { if (++edgesHit == 3) break;}
        if (addMoveAroundPos(dist, cheatLength, queue, curLength, curPos.col+1, curPos.row, centerPos)) { if (++edgesHit == 3) break;}
      }
      bs[UP]    = dist[centerPos-1][centerPos];
      bs[RIGHT] = dist[centerPos]  [centerPos+1];
      bs[DOWN]  = dist[centerPos+1][centerPos];
      bs[LEFT]  = dist[centerPos]  [centerPos-1];
    }

    private boolean addMoveAroundPos(byte[][] dist, byte cheatLength, Queue<Pos> queue, byte curLength, int col, int row, int centerPos) {
      if (0<=col && col < dist.length && 0 <= row && row < dist.length && dist[row][col] > curLength+1) {
        dist[row][col] = (byte) (curLength+1);
        queue.add(new Pos(row,col));
        return isNeighbour(row,col, centerPos);
      }
      return false;
    }

    private boolean isNeighbour(int row, int col, int centerPos) {
      return Math.abs(row-centerPos) + Math.abs(col-centerPos) == 1;
    }

    private boolean isValidAndMoveable(int row, int col) {
      return 0 <= row && row < n && 0 <= col && col < m && moveable[row][col];
    }

    private int getPositionId(int row, int col, int dir) {
      return (dir+1)*n*m+row*m+col;
    }
    public int query(int emptyRow, int emptyCol, int caoCaoRow, int caoCaoCol, int exitRow, int exitCol) {
      if (caoCaoRow == exitRow && caoCaoCol == exitCol) { return 0; }
      boolean visited[] = new boolean[5*n*m];
      Heap2 heap = new Heap2(5*n*m, k+1);
      heap.insert(getPositionId(emptyRow,emptyCol,-1), 0);
      if (isValidAndMoveable(caoCaoRow-1, caoCaoCol)) { heap.insert( getPositionId(caoCaoRow, caoCaoCol, UP), k); }
      if (isValidAndMoveable(caoCaoRow, caoCaoCol+1)) { heap.insert( getPositionId(caoCaoRow, caoCaoCol, RIGHT), k); }
      if (isValidAndMoveable(caoCaoRow+1, caoCaoCol)) { heap.insert( getPositionId(caoCaoRow, caoCaoCol, DOWN), k); }
      if (isValidAndMoveable(caoCaoRow, caoCaoCol-1)) { heap.insert( getPositionId(caoCaoRow, caoCaoCol, LEFT), k); }
      while (!heap.isEmpty()) {
        int distV = heap.getMinVal();
        int v = heap.removeMin();
        if (v > n*m && v % (n*m) == getPositionId(exitRow,exitCol,-1)) {
          return distV;
        }
        visited[v] = true;
        int dir = v / (n*m) - 1;
        int row = (v % (n*m)) / m;
        int col = (v % (n*m)) % m;
//        System.err.printf("Row: %3d Col: %3d Dir: %d Dist: %3d%n", row, col, dir, distV);
        if (dir == -1) {
          if (Math.abs(row-caoCaoRow) + Math.abs(col-caoCaoCol) == 1) {
            int newDir;
            if (row < caoCaoRow) {
              newDir = UP;
            } else if (row > caoCaoRow) {
              newDir = DOWN;
            } else if (col < caoCaoCol) {
              newDir = LEFT;
            } else {
              newDir = RIGHT;
            }
            addPos(heap, visited, caoCaoRow, caoCaoCol, newDir, distV);
          } else {
            if (distV == k-1) {
              continue; // Do not search further with empty space - just cheat if CaoCao has not been reached
            }
            addPos(heap, visited, row-1, col, -1, distV+1);
            addPos(heap, visited, row+1, col, -1, distV+1);
            addPos(heap, visited, row, col-1, -1, distV+1);
            addPos(heap, visited, row, col+1, -1, distV+1);

          }
        } else {
          switch (dir) {
          case UP:
            addPos(heap, visited, row-1, col, DOWN, distV+1);
            break;
          case RIGHT:
            addPos(heap, visited, row, col+1, LEFT, distV+1);
            break;
          case DOWN:
            addPos(heap, visited, row+1, col, UP, distV+1);
            break;
          case LEFT:
            addPos(heap, visited, row, col-1, RIGHT, distV+1);
            break;
          }
          addRotations(heap, visited, row, col, dir, distV);
        }
    }
    return -1; // Not reachable
  }
  private void addRotations(Heap2 heap, boolean[] visited, int row, int col, int dir, int dist) {
    byte[] distToOtherDirs = moveAround[row][col][dir];
    for (int newDir = 0; newDir < 4; newDir++) {
      if (dir != newDir && distToOtherDirs[newDir] != -1) {
        addPos(heap,visited,row,col,newDir,dist + distToOtherDirs[newDir]);
      }
    }
  }

  private void addPos(Heap2 heap, boolean[] visited, int row, int col, int dir, int dist) {
    if (isValidAndMoveable(row, col)) {
      int posId = getPositionId(row, col, dir);
      if (!visited[posId]) {
        if (!heap.containsNode(posId)) {
          heap.insert(posId, dist);
        } else if (heap.getVal(posId) > dist) {
          heap.setVal(posId, dist);
        }
      }
    }
  }

    }
  private static class Pos {
    int row;
    int col;
    public Pos(int row, int col) {
      this.row = row;
      this.col = col;
    }
  }

  public static class Heap2 {
    private int size;
    private int heaps[][];
    private int heapsSize[];
    private int firstHeap;
    private int firstHeapVal;
    private int nodeVal[];
    private int nodeLoc[];

    public Heap2(int n, int span) {
      heaps = new int[span][64];
      heapsSize = new int[span];
      firstHeap = firstHeapVal = 0;
      nodeLoc = new int[n];
      nodeVal = new int[n];
      Arrays.fill(nodeLoc, -1);
      size = 0;
    }

    protected int compare(int v1, int v2) {
      return Integer.compare(v1, v2);
    }

    public boolean isEmpty() {
      return size == 0;
    }

    public int size() {
      return size;
    }

    public void insert(int node, int val) {
      rotateIfNeeded(val);
      int heap = (val-firstHeapVal+firstHeap) % heaps.length;
      if (heaps[heap].length == heapsSize[heap]) {
        heaps[heap] = Arrays.copyOf(heaps[heap], 2*heapsSize[heap]);
      }
      heaps[heap][heapsSize[heap]] = node;
      nodeVal[node] = val;
      nodeLoc[node] = heapsSize[heap];
      heapsSize[heap]++;
      size++;
    }

    private void rotateIfNeeded(int val) {
      if (size == 0) {
        firstHeapVal = val;
      } else {
        while (heapsSize[firstHeap] == 0 && firstHeapVal < val) {
          firstHeap++;
          firstHeapVal++;
          if (firstHeap == heaps.length) {
            firstHeap = 0;
          }
        }
      }
    }

    public void setVal(int node, int val) {
      remove(node);
      insert(node,val);
    }

    public int getVal(int node) {
      return nodeVal[node];
    }

    /**
     * @return value of removed node
     */
     public int remove(int node) {
       int heap = (nodeVal[node]-firstHeapVal+firstHeap) % heaps.length;
       int loc = nodeLoc[node];
       if (heapsSize[heap] != loc+1) {
         heaps[heap][loc] = heaps[heap][heapsSize[heap]-1];
         nodeLoc[heaps[heap][loc]] = loc;
       }
       heapsSize[heap]--;
       nodeLoc[node] = -1;
       size--;
       return nodeVal[node];
     }

     public int getMinVal() {
       return nodeVal[getMinNode()];
     }

     public int getMinNode() {
       rotateIfNeeded(Integer.MAX_VALUE);
       return heaps[firstHeap][0];
     }

     /**
      * @return node number
      */
     public int removeMin() {
       int node = getMinNode();
       remove(node);
       return node;
     }


     public boolean containsNode(int node) {
       return nodeLoc[node] != -1;
     }
  }
}
