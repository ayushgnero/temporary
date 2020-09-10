"""
Huarongdao is a well-known game in China. The purpose of this game is to move the Cao Cao block out of the board.

Acme is interested in this game, and he invents a similar game. There is a N*M board. Some blocks in this board are movable, while some are fixed. There is only one empty position. In one step, you can move a block to the empty position, and it will take you one second. The purpose of this game is to move the Cao Cao block to a given position. Acme wants to finish the game as fast as possible.

But he finds it hard, so he cheats sometimes. When he cheats, he spends K seconds to pick a block and put it in an empty position. However, he is not allowed to pick the Cao Cao block out of the board .

Note

    Immovable blocks cannot be moved while cheating.
    A block can be moved only in the directions UP, DOWN, LEFT or RIGHT.

Input Format

The first line contains four integers N, M, K, Q separated by a single space. N lines follow.
Each line contains M integers 0 or 1 separated by a single space. If the jth integer is 1, then the block in ith row and jth column is movable. If the jth integer is 0 then the block in ith row and jth column is fixed. Then Q lines follows, each line contains six integers EXi, EYi, SXi, SYi, TXi, TYi separated by a single space. The ith query is the Cao Cao block is in row SXi column SYi, the exit is in TXi, TYi, and the empty position is in row EXi column EYi. It is guaranteed that the blocks in these positions are movable. Find the minimum seconds Acme needs to finish the game. If it is impossible to finish the game, you should answer -1.

Constraints

N,M ≤ 200
1 ≤ Q ≤ 250
10 ≤ K≤ 15
1 ≤ EXi, SXi, TXi≤ N
1 ≤ EYi, SYi,TYi ≤ M

Output Format

You should output Q lines, i-th line contains an integer which is the answer to i-th query.

Sample Input

5 5 12 1
1 1 1 1 1
1 1 1 1 1
0 1 1 1 1
1 1 1 1 1
0 1 0 1 1
1 5 4 3 4 1

Sample Output

20

Explanation

Move the block in (1, 4) to (1, 5);
Move the block in (1, 3) to (1, 4);
Move the block in (1, 2) to (1, 3);
Move the block in (2, 2) to (1, 2);
Move the block in (3, 2) to (2, 2);
Move the block in (4, 2) to (3, 2);
Move the block in (4, 3) to (4, 2);
Move the block in (4, 1) to (4, 3) by cheating;
Move the block in (4, 2) to (4, 1).

So, 1 + 1 + 1 + 1 + 1 + 1 + 1 + 12 + 1 = 20.
"""
import heapq

N, M , K, Q = [int(x) for x in input().strip().split(' ')]
G = []
for n in range(N) :
    G.append([int(x) for x in input().strip().split(' ')])

def getMinPath(S,T,cost, getCostOnly=False) :
    Sx,Sy = S
    Tx,Ty = T
    closed = {}
    opened = [(abs(Sx-Tx)+abs(Tx-Ty),0,S,None)]

    while opened :
        current = heapq.heappop(opened)
        C = current[2]
        Cx,Cy = C
        if C in closed and current[1] >= closed[C][0] :
            continue

        closed[C] = (current[1],current[3])
        if C == T :
            if getCostOnly :
                return current[1]
            else :
                return rebuildPath(closed,T)
        for Dx, Dy in [(-1,0),(1,0),(0,-1),(0,1)] :
            Nx, Ny = Cx+Dx, Cy+Dy
            if Nx > 0 and Ny > 0 and Nx <= N and Ny <= M and G[Nx-1][Ny-1] == 1 and not (Nx,Ny) in closed :
                nextCost = current[1]+cost(C,(Nx,Ny), current[3])
                heapq.heappush(opened,(nextCost+abs(Cx-Tx)+abs(Cx-Ty),nextCost,(Nx,Ny),C))
    if getCostOnly :
        return -1
    else :
        return None

def rebuildPath(closed, target) :
    path = [target]
    while closed[path[-1]][1] :
        path.append(closed[path[-1]][1])
    return path

def costGrid(S,T,E) :
    return 1 # S will always be next to T

def costCao(S,T,E) :
    # Need to move the empty block around to T and next move the Cao block
    global Estart, G
    if not E :
        E = Estart
    G[S[0]-1][S[1]-1] = 2
    path = getMinPath(E,T,costGrid)
    G[S[0]-1][S[1]-1] = 1
    if path and len(path)-1 < K :
        cost = len(path)
    else :
        cost = K + 1
#    print((S,T,E, cost)) ################
    return cost

def getMinTime(Ex, Ey, Sx, Sy, Tx, Ty) :
    if (Ex, Ey) == (Sx, Sy) : # Wrong data, should not happen.
        return -1
    if (Sx, Sy) == (Tx, Ty) :
        return 0

    return getMinPath((Sx,Sy),(Tx,Ty),costCao, getCostOnly=True )

for q in range(Q) :
    Ex, Ey, Sx, Sy, Tx, Ty = [int(x) for x in input().strip().split(' ')]
    Estart = (Ex,Ey)
    print(getMinTime(Ex, Ey, Sx, Sy, Tx, Ty))
    
