"""
There is a horizontal row of cubes. The length of each cube is given. You need to create a new vertical pile of cubes. The new pile should follow these directions: if is on top of then

.

When stacking the cubes, you can only pick up either the leftmost or the rightmost cube each time. Print "Yes" if it is possible to stack the cubes. Otherwise, print "No". Do not print the quotation marks.

Input Format

The first line contains a single integer
, the number of test cases.
For each test case, there are lines.
The first line of each test case contains , the number of cubes.
The second line contains

space separated integers, denoting the sideLengths of each cube in that order.

Constraints


Output Format

For each test case, output a single line containing either "Yes" or "No" without the quotes.

Sample Input

2
6
4 3 2 1 3 4
3
1 3 2

Sample Output

Yes
No

Explanation

In the first test case, pick in this order: left -
, right - , left - , right - , left - , right - .
In the second test case, no order gives an appropriate arrangement of vertical cubes. will always come after either or .
"""
import sys

def test_cubes(cubes):
    t_cube = 0

    if cubes[0] > cubes[len(cubes)-1]:
        t_cube = cubes[0]
        cubes.pop(0)
    else:
        t_cube = cubes[len(cubes)-1]
        cubes.pop(len(cubes)-1)

    while len(cubes) > 0:
        if t_cube == cubes[0]:
            t_cube = cubes.pop(0)
        elif t_cube == cubes[len(cubes)-1]:
            t_cube = cubes.pop(len(cubes)-1)
        elif (cubes[0] > cubes[len(cubes)-1]) and (t_cube >= cubes[0]):
            t_cube = cubes.pop(0)
        elif (cubes[0] < cubes[len(cubes)-1]) and (t_cube >= cubes[len(cubes)-1]):
            t_cube = cubes.pop(len(cubes)-1)
        elif (cubes[0] == cubes[len(cubes)-1]):
            t_cube = cubes.pop(0)
        else:
            return "No"
    return "Yes"

num_of_tests = sys.stdin.readline().strip()
num_of_tests = int(num_of_tests)

for i in range(0, num_of_tests):
    sys.stdin.readline()
    cubes = list(map(int, sys.stdin.readline().strip().split(' ')))
    print(test_cubes(cubes))
