"""
In order to entertain themselves during the long flight, Alex and Fedor invented the following very simple two players game. The game is:

    First, Alex draws some graph with bidirectional weighted edges. There are possibly multiple edges (probably, with different or same weights) in this graph.

    Then Fedor picks some spanning tree of this graph. If the tree appears to be the minimal spanning tree, then the winner is Fedor. Otherwise, the winner is Alex.

We consider two trees different if the sets of the numbers of edges that are included in these trees are different. We consider two sets
and different if there is at least one element that is present in and not present in

or vice versa.

We should also mention that the graphs with enormous number of spanning trees upset Alex, as well as Fedor, so they will never have a graph that has more than

spanning trees.

At some point, Fedor became too lazy to look for minimal spanning trees and now he just picks some arbitrary spanning tree from the Alex's graph. Each spanning tree has equal probability to be picked by Fedor. What is the probability of Fedor's victory now?

Input Format

The first line of input consists of two single space separated integers
and

- the number of nodes in Alex's graph and the number of edges in that graph, respectively.

Then there are
lines, where the line has three numbers with the meaning that the edge with the number connects the nodes and and has the weight of

.

Constraints

    The graph is always connected.

Output Format

Output the probability of Fedor's victory, if he picks the spanning tree randomly, as an irreducible fraction.

Sample Input

4 4
1 2 1
2 3 4
3 4 3
4 1 2

Sample Output

1/4

"""
#!/bin/python3

import os
import sys
from decimal import *

#
# Complete the alexFedor function below.
#

#
# For the weighted graph, <name>:
#
# 1. The number of nodes is <name>_nodes.
# 2. The number of edges is <name>_edges.
# 3. An edge exists between <name>_from[i] to <name>_to[i] and the weight of the edge is <name>_weight[i].
#
#
def alexFedor(graph_nodes, graph_from, graph_to, graph_weight):
    #
    # Write your code here.
    #
    getcontext().prec = 30

    def gcd(x, y):
        return gcd(y, x % y) if y else x

    def find(x):
        if x != pre[x]:
            pre[x] = find(pre[x])
        return pre[x]

    def join(_x, _y):
        pre[find(_x)] = find(_y)

    def gauss(n0):
        if n0 <= 1:
            return 1
        n0 -= 1
        res = 1
        d = [[0]*n0 for _ in range(n0)]
        for i in range(n0):
            for j in range(n0):
                d[i][j] = Decimal(a[i][j])
        for i in range(n0):
            if d[i][i] == 0:
                for j in range(i+1, n0):
                    if d[j][i] != 0:
                        for k in range(i,n0):
                            d[i][k], d[j][k] = d[j][k], d[i][k]
                        break
            for j in range(i+1, n0):
                tmp = d[j][i]
                for k in range(n0):
                    d[j][k] -= d[i][k] / d[i][i] * tmp
            res *= d[i][i]
        return int(abs(res)+Decimal(0.5))

    graph_from = [x-1 for x in graph_from]
    graph_to = [x-1 for x in graph_to]
    n, m = graph_nodes, len(graph_from)
    #a = ([[0] * n]) * n
    a = [[0] * n for _ in range(n)]
    for i in range(m):
        x, y = graph_from[i], graph_to[i]
        a[x][x] += 1
        a[y][y] += 1
        a[x][y] -= 1
        a[y][x] -= 1
    al_count = gauss(n)
    min_count = 1
    idx = list(range(m))
    pre = list(range(n))
    idx.sort(key=lambda _x: graph_weight[_x])
    l = 0
    while l < m:
        r = l + 1
        while r < m and graph_weight[idx[l]] == graph_weight[idx[r]]:
            r += 1
        for i in range(l, r):
            graph_from[idx[i]] = find(graph_from[idx[i]])
            graph_to[idx[i]] = find(graph_to[idx[i]])
        for i in range(l, r):
            join(graph_from[idx[i]], graph_to[idx[i]])
        idx[l:r] = sorted(idx[l:r], key=lambda _x: find(graph_from[_x]))
        ll = l
        while ll < r:
            rr = ll + 1
            while rr < r and find(graph_from[idx[ll]]) == find(graph_from[idx[rr]]):
                rr += 1
            # calculate ways with pathes id from ll to rr
            v = [0] * n
            for i in range(ll, rr):
                v[graph_from[idx[i]]] = v[graph_to[idx[i]]] = 1
            for i in range(1, n):
                v[i] += v[i-1]
            for i in range(v[-1]):
                for j in range(v[-1]):
                    a[i][j] = 0
            for i in range(ll, rr):
                x, y = v[graph_from[idx[i]]] - 1, v[graph_to[idx[i]]] - 1
                a[x][x] += 1
                a[y][y] += 1
                a[x][y] -= 1
                a[y][x] -= 1
            min_count *= gauss(v[-1])
            ll = rr
        l = r
    for i in range(1, n):
        if find(i) != find(0):
            min_count = 0
    _gcd = gcd(al_count, min_count)
    if _gcd:
        al_count //= _gcd
        min_count //= _gcd
    return str(min_count) + '/' + str(al_count)

if __name__ == '__main__':
    fptr = open(os.environ['OUTPUT_PATH'], 'w')

    graph_nodes, graph_edges = map(int, input().split())

    graph_from = [0] * graph_edges
    graph_to = [0] * graph_edges
    graph_weight = [0] * graph_edges

    for graph_itr in range(graph_edges):
        graph_from[graph_itr], graph_to[graph_itr], graph_weight[graph_itr] = map(int, input().split())

    result = alexFedor(graph_nodes, graph_from, graph_to, graph_weight)

    fptr.write(result + '\n')

    fptr.close()
