"""
The travelling salesman has a map containing m*n squares. He starts from the top left corner and visits every cell exactly once and returns to his initial position (top left). The time taken for the salesman to move from a square to its neighbor might not be the same. Two squares are considered adjacent if they share a common edge and the time taken to reach square b from square a and vice-versa are the same. Can you figure out the shortest time in which the salesman can visit every cell and get back to his initial position?

Input Format

The first line of the input is 2 integers m and n separated by a single space. m and n are the number of rows and columns of the map.
Then m lines follow, each of which contains (n – 1) space separated integers. The jth integer of the ith line is the travel time from position (i,j) to (i,j+1) (index starts from 1.)
Then (m-1) lines follow, each of which contains n space integers. The jth integer of the ith line is the travel time from position (i,j) to (i + 1, j).

Constraints

1 ≤ m, n ≤ 10
Times are non-negative integers no larger than 10000.

Output Format

Just an integer contains the minimal time to complete his task. Print 0 if its not possible to visit each cell exactly once.

Sample Input

2 2
5
8
6 7

Sample Output

26

Explanation

As its a 2*2 square, all cells are visited. 5 + 7 + 8 + 6 = 26
"""
INF = 10 ** 9

m = True, False, None
TT, TF, TN, FT, FF, FN, NT, NF, NN = ((i, j) for i in m for j in m)

m, n = map(int, input().split())
row = [list(map(int, input().split())) for i in range(m)]
column = [list(map(int, input().split())) for j in range(m - 1)]

def minimize(t, v):
    global current, INF
    current[t] = min(v, current.get(t, INF))

if m & n & 1:
    ans = 0
else:
    ans = INF
    previous, current = {}, {NN[:1] * (m + n): 0}
    for i in range(m):
        for j in range(n):
            previous, current, k = current, {}, m + j - 1 - i
            for state, value in previous.items():
                l, x, r = state[:k], state[k: k + 2], state[k + 2:]
                if x == NN:
                    if i + 1 < m and j + 1 < n:
                        minimize(l + TF + r, value)
                elif x == NT or x == NF:
                    value += column[i - 1][j]
                    if j + 1 < n:
                        minimize(state, value)
                    if i + 1 < m:
                        minimize(l + x[::-1] + r, value)
                elif x == FN or x == TN:
                    value += row[i][j - 1]
                    if j + 1 < n:
                        minimize(l + x[::-1] + r, value)
                    if i + 1 < m:
                        minimize(state, value)
                else:
                    value += row[i][j - 1] + column[i - 1][j]
                    if x == TF:
                        if i + 1 == m and j + 1 == n:
                            ans = min(ans, value)
                    elif x == FT:
                        minimize(l + NN + r, value)
                    elif x == TT:
                        count = 1
                        index = -1
                        while count:
                            index += 1
                            count += 1 if r[index] == TT[0] else -1 if r[index] == FF[0] else 0
                        minimize(l + NN + r[:index] + TT[:1] + r[index + 1:], value)
                    else:
                        count = -1
                        index = k
                        while count:
                            index -= 1
                            count += 1 if l[index] == TT[0] else -1 if l[index] == FF[0] else 0
                        minimize(l[:index] + FF[:1] + l[index + 1:] + NN + r, value)
print(ans)
