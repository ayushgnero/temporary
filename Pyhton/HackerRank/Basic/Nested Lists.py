"""
Given the names and grades for each student in a class of

students, store them in a nested list and print the name(s) of any student(s) having the second lowest grade.

Note: If there are multiple students with the second lowest grade, order their names alphabetically and print each name on a new line.

Example
The ordered list of scores is , so the second lowest score is . There are two students with that score:

. Ordered alphabetically, the names are printed as:

alpha
beta

Input Format

The first line contains an integer,
, the number of students.
The subsequent lines describe each student over

lines.
- The first line contains a student's name.
- The second line contains their grade.

Constraints

    There will always be one or more students having the second lowest grade.

Output Format

Print the name(s) of any student(s) having the second lowest grade in. If there are multiple students, order their names alphabetically and print each one on a new line.

Sample Input 0

5
Harry
37.21
Berry
37.21
Tina
37.2
Akriti
41
Harsh
39

Sample Output 0

Berry
Harry

Explanation 0

There are

students in this class whose names and grades are assembled to build the following list:

python students = [['Harry', 37.21], ['Berry', 37.21], ['Tina', 37.2], ['Akriti', 41], ['Harsh', 39]]

The lowest grade of
belongs to Tina. The second lowest grade of belongs to both Harry and Berry, so we order their names alphabetically and print each name on a new line.
"""
n = int(input())
if 2<= n <=5:
    b = []
    c = []
    mini = 9999.00
    d = 9999.00
    i = 0
    final = []
    for x in range(n):
        a = []
        a.append(input())
        a.append(float(input()))
        b.append(a[1])
        c.append(a[0])
    #find mimimum
    for j in range (n):
        if mini > b[j]:
            mini = b[j]
    #transfer to final
    for k in range (n):
        if d >= b[k] and b[k] != mini:

            if d == b[k]:
                final.append(c[k])
            else:
                final = []
                final.append(c[k])
            d = b[k]
    f = sorted(final)
    print(*f, sep = "\n")
