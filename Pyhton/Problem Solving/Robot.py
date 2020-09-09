"""
You have two arrays of integers, and , where both have

number of elements. Consider the following function:

score = 0

int Go(step, energy) {
    if (step == N) {
        score += V[step];
        return (score);
    }
    else {
        int way = random(1, 2);
        if (way == 1) {
            score += V[step];
        }
        else {
            energy = P[step];
        }
        if (energy > 0) {
            Go(step + 1, energy - 1);
        }
        else {
            KillTheWorld();
        }
    }
}

What is the maximum possible value of score that we can get in the end, if we call
?.
Note that the function should never invoke KillTheWorld function. And

generates a random integer from set [1, 2].
It is guaranteed there will be a solution that wont kill the world.

Input Format

The first line contains an integer N. Each of the following N lines contains a pair of integers. The i-th line contains a pair of numbers,

, separated by space.

Constraints



Output Format

Derive the maximum score given by return (score);.

Sample Input

4
4 2
0 2
4 0
3 4

Sample Output

7

Explanation

In the best case, the first and second function call in Go variable
will take value 2, while in the other calls it will be equal to 1 then the final score will be equal to the value of 7.
"""
import sys
from operator import itemgetter

N = int(input())

V, P = [None] * N, [None] * N
for i in range(N):
   v_item, p_item = input().split()
   V[i] = int(v_item)
   P[i] = int(p_item)

games = []
for i in range(N):
   maxVal = -1
   games.sort(key=itemgetter(1))
   for j in range(len(games) - 1, -1, -1):
      game = games[j]
      if game[1] == 0:
         del games[0:j+1]
         break

      if maxVal < game[0]:
         maxVal = game[0]
      else:
         del games[j]


      game[0] += V[i]
      game[1] += -1
   if maxVal == -1:
      maxVal = 0
   games.append([maxVal, P[i]])
print(max(games, key=itemgetter(0))[0])
