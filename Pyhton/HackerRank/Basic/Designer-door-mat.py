"""
Mr. Vincent works in a door mat manufacturing company. One day, he designed a new door mat with the following specifications:

    Mat size must be

X. ( is an odd natural number, and is times

    .)
    The design should have 'WELCOME' written in the center.
    The design pattern should only use |, . and - characters.

Sample Designs

    Size: 7 x 21
    ---------.|.---------
    ------.|..|..|.------
    ---.|..|..|..|..|.---
    -------WELCOME-------
    ---.|..|..|..|..|.---
    ------.|..|..|.------
    ---------.|.---------

    Size: 11 x 33
    ---------------.|.---------------
    ------------.|..|..|.------------
    ---------.|..|..|..|..|.---------
    ------.|..|..|..|..|..|..|.------
    ---.|..|..|..|..|..|..|..|..|.---
    -------------WELCOME-------------
    ---.|..|..|..|..|..|..|..|..|.---
    ------.|..|..|..|..|..|..|.------
    ---------.|..|..|..|..|.---------
    ------------.|..|..|.------------
    ---------------.|.---------------

Input Format

A single line containing the space separated values of
and

.

Constraints

Output Format

Output the design pattern.

Sample Input

9 27

Sample Output

------------.|.------------
---------.|..|..|.---------
------.|..|..|..|..|.------
---.|..|..|..|..|..|..|.---
----------WELCOME----------
---.|..|..|..|..|..|..|.---
------.|..|..|..|..|.------
---------.|..|..|.---------
------------.|.------------

"""

x = list(map(int, input().split()))
n = x[0]
m = x[1]
a=int((m-3)/2)
z=1
l=int((m-6)/2)
for x in range (n//2+1):
    if a >= 3:
        d="-"
        t=".|."
        c = d*a
        p=t*z
        print (c,end="")
        print (p,end="")
        print (c)
        a=int(a-3)
        z=z+2


    else:
        print(d*l,end="")
        print("WELCOME",end="")
        print(d*l)

        for x in range (n//2):
            if z < 2:
                break
            else:

                z=z-2
                a=int(a+3)
                d="-"
                t=".|."
                c = d*a
                p=t*z
                print (c,end="")
                print (p,end="")
                print (c)
