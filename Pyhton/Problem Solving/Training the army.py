"""
In the magical kingdom of Kasukabe, people strive to possess skillsets. Higher the number of skillset present among the people, the more content people will be.

There are
types of skill set present and initially there exists people possessing skill set, where

.

There are
wizards in the kingdom and they have the ability to transform the skill set of a person into another skill set. Each of the these wizards has two lists of skill sets associated with them, and . He can only transform the skill set of person whose initial skill set belongs to the list to one of the final skill set which belongs to the list . That is, if and

then following transformation can be done by that trainer.

Once a transformation is done, both skill is removed from the respective lists. In the above example, if he perform
transformation on a person, list will be updated to and list will be

. This updated list will be used for further transformations.

Few points to note are:

    One person can possess only one skill set.
    A wizard can perform zero or more transformation as long as they satisfies the above criteria.
    A person can go through multiple transformation of skill set.
    Same class transformation is also possible. That is a person' skill set can be transformed into his current skill set. Eg.

    in the above example.

Your goal is to design a series of transformation which results into maximum number of skill set with non-zero number of people knowing it.

Input Format

The first line contains two numbers,
, where represent the number of skill set and represent the number of wizards.
Next line contains space separated integers, , where represents the number of people with skill. Then follows lines, where each pair of line represent the configuration of each wizard.
First line of the pair will start with the length of list and followed by list in the same line. Similarly second line of the pair starts with the length of list and then the list

.

Constraints

Output Format

The output must consist of one number, the maximum number of distinct skill set that can the people of country learn, after making optimal transformation steps.

Sample Input

3 3
3 0 0
1 1
2 2 3
1 2
1 3
1 1
1 2

Sample Output

2

Explanation

There are
types of skill sets present along with wizards. Initially, all three people know the skill set but no one knows the and

skill sets.

The
wizard's initial lists are: and . Suppose, he performs transformation one any one of person with the skill set, then it's list will be updated to an empty list and list will be .
Now, we have two people knowing the skill set and one person knowing the

skill set.

The
wizard's initial lists are: and . He will use the transformation one of the person with the skill set, then it's lists will also be updated to an empty lists A: and :

.

Now, we have 1 person with
skillset and and 2 people knowing the

skillset.

The
wizard's initial lists are: and . He will transform one of the person with skillset to one using the transformation . It's lists will also be updated to an empty lists A: and : .
At this point, no further transformations are possible and we have achieved our maximum possible answer. Thus, each of the skill set, is known by person.. This means there are three skill sets available in the kingdom.
"""
import collections

class Node:
    def __init__(self, id_):
        self.id = id_
        self.residual_flows = {}

class MaxFlow_LinkedList:
    INF = 100000

    def __init__(self, N_):
        self.N = N_

        self.node_table = []
        for i in range(0, self.N):
            self.node_table.append(Node(i))

        self.source = 0
        self.sink = N_ - 1

        self.max_flow = 0

        self.parent_links = [-1] * self.N

        self.main_flows = []

    def getMainFlows(self):
        net_flows = []
        for [u, v, c] in self.main_flows:
            net_flows.append([u, v, c, self.node_table[v].residual_flows[u]])
        return net_flows

    def addCapacity(self, u, v, c):
        self.node_table[u].residual_flows[v] = c
        self.node_table[v].residual_flows[u] = 0

        self.main_flows.append([u, v, c])

    def addCapacityBoth(self, u, v, c_uv, c_vu):
        self.node_table[u].residual_flows[v] = c_uv
        self.node_table[v].residual_flows[u] = c_vu

        self.main_flows.append([u, v, c_uv])
        self.main_flows.append([v, u, c_vu])

    def bfs(self):
        visited = [False] * self.N

        pending = collections.deque();

        visited[self.source] = True
        pending.append(self.source)
        self.parent_links[self.source] = -1

        while len(pending) > 0:
            curr_node = pending.popleft()

            if curr_node == self.sink:
                return True

            for adj_node, res_flow in self.node_table[curr_node].residual_flows.items():
                if res_flow > 0 and not visited[adj_node]:
                    self.parent_links[adj_node] = curr_node
                    pending.append(adj_node)
                    visited[adj_node] = True

        return False

    def findMaxFlow(self):
        max_total_flow = 0

        while self.bfs():

            # find maximum possible flow in the BFS path
            max_path_flow = MaxFlow_LinkedList.INF
            v = self.sink
            while v != self.source:
                u = self.parent_links[v]
                max_path_flow = min(max_path_flow, self.node_table[u].residual_flows[v])
                v = u

            # modify the residual flows:
            # - remove the flow from residual flows from source to sink
            # - add the flow to residual flows from sink to source

            v = self.sink
            while v != self.source:
                u = self.parent_links[v]
                self.node_table[u].residual_flows[v] -= max_path_flow
                self.node_table[v].residual_flows[u] += max_path_flow
                v = u

            max_total_flow += max_path_flow

        return max_total_flow


[n, k] = list(map(int, input().split()))
C = list(map(int, input().split()))

A = []
B = []

for i in range(0, k):
    a = list(map(int, input().split()))
    b = list(map(int, input().split()))

    A.append(a[1:])
    B.append(b[1:])

def getAIdx(w, i):
    return sum(len(a) for a in A[:w]) + i + 1 + n*2

def getBIdx(w, i):
    return sum(len(a) for a in A) + sum(len(b) for b in B[:w]) + i + 1 + 2*n

# total nodes = 1sink + 1source + 2*N + sum_of_sizes_A + sum_of_sizes_B
total_nodes = 2 + 2 * n + sum(len(a) for a in A) + sum(len(b) for b in B)

flow_network = MaxFlow_LinkedList(total_nodes)

for [i, c] in enumerate(C):
    flow_network.addCapacity(0, i+1, c)
    flow_network.addCapacityBoth(i+1, n+1+i, 1, 1000000)
    flow_network.addCapacity(n+1+i, total_nodes-1, 1)

for w in range(0, len(A)):
    for i in range(0, len(A[w])):
        flow_network.addCapacity(A[w][i], getAIdx(w, i), 1)

for w in range(0, len(B)):
    for i in range(0, len(B[w])):
        flow_network.addCapacity(getBIdx(w, i), n+B[w][i], 1)

for w in range(0, len(A)):
    for i in range(0, len(A[w])):
        for j in range(0, len(B[w])):
            flow_network.addCapacity(getAIdx(w, i), getBIdx(w, j), 1)

print (flow_network.findMaxFlow())
