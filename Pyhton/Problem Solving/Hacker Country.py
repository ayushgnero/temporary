"""
There are N cities in Hacker Country. Each pair of cities are directly connected by a unique directed road, and each road has its own toll that must be paid every time it is used. You're planning a road trip in Hacker Country, and its itinerary must satisfy the following conditions:

    You can start in any city.
    You must use

or more different roads (meaning you will visit

    or more cities).
    At the end of your trip, you should be back in your city of origin.
    The average cost (sum of tolls paid per road traveled) should be minimum.

Can you calculate the minimum average cost of a trip in Hacker Country?

Time Limits
Time limits for this challenge are provided here.

Input Format

The first line is an integer,
(number of cities).
The subsequent lines of space-separated integers each describe the respective tolls or traveling from city to city ; in other words, the integer of the line denotes the toll for traveling from city to city

.

Note: As there are no roads connecting a city to itself, the
integer of line will always be

.

Constraints


Output Format

Print the minimum cost as a rational number
(tolls paid over roads traveled). The greatest common divisor of and should be

.

Sample Input

2
0 1
2 0

Sample Output

3/2

Explanation

The toll from city
to city is . The toll from to is . Your travel cost . Your number of roads traveled is . Thus, we print 3/2 as our answer.
"""
#!/bin/python3

import os
import sys

#
# Complete the hackerCountry function below.
#
def hackerCountry(tolls):
    # print(tolls)

    # def min_exl_zero(l: list):
    #     smallest = 200
    #     zeros_found = 0
    #     for i in l:
    #         if i != 0 and i < smallest and zeros_found <= 1:
    #             smallest = i
    #         if i == 0:
    #             zeros_found += 1
    #     return smallest

    #cities = []

    # for i, d in enumerate(tolls):
    #     for ii, dd in enumerate(d):
    #         if i not in cities:
    #             cities[i] = {ii: dd}
    #             cities[i]["min"]=[min_exl_zero(d),ii]
    #         else:
    #             cities[i][ii] = dd

    # for i, d in enumerate(tolls):
    #     for ii, dd in enumerate(d):
    #         if i not in cities:
    #             cities[i] = {ii: dd}
    #             # cities[i]["min"]=[min_exl_zero(d),ii]
    #         else:
    #             cities[i][ii] = dd

    #cities = [c for c in tolls]
    lc = len(tolls)

    #
    # sorted_tolls = []
    # toll_costs = {}
    # for _ in cities.items():
    #     # print(":",_)
    #     for __ in cities[_[0]].items():
    #         # print(__)
    #         if _[0] != __[0]:
    #             sorted_tolls.append((__[1], (_[0], __[0])))
    #             if __[1] not in toll_costs:
    #                 toll_costs[__[1]] = [(_[0], __[0])]
    #             else:
    #                 toll_costs[__[1]].append((_[0], __[0]))

    # k = list(cities.keys())
    k = [i for i in range(lc)]

    # class TollRoad:
    #     def __init__(self,start_city:int):
    #         self.toll = 0
    #         self.road = 0
    #         self.path = [start_city]
    #         self.is_over = False
    #     def increase(self,toll,city):
    #         self.road +=1
    #         self.toll +=toll
    #         self.path.append(city)
    #     def decrease(self,toll):
    #         self.road -=1
    #         self.toll -=toll
    #         self.path=self.path[:-1]
    #

    class Hiker:
        def __init__(self, tolls: list):
            self.cities = tolls
            self.min_p = [200,1]
            self.min_ratio = self.min_p[0]/self.min_p[1]
            self.valid_paths = {}
            self.time_in_copy_1 = 0
            self.time_in_copy_2 = 0
            self.time_in_copy_3 = 0
            self.paths_added = 0
            self.best_path = []
            self.it = range(lc)
            self.it2 = [x for x in ["road","toll","is_over"]]
            self.iterations = 0
            self.possible_paths = []
            # self.get_path = itemgetter("path")
            # self.get_toll = itemgetter("toll")
            # self.get_road = itemgetter("road")
            # self.get_last = itemgetter(-1)

        # print(cities)
        def find_start_path_for_city(self, city: int):
            self.valid_paths[city] = []
            for i, c in enumerate(self.cities[city]):
                # print(i,c,city)
                path = {"toll": 0, "road": 0, "path": [city], "is_over": False, "lookup":{}}
                if i != city:
                    # path.increase(self.cities[city][c],c)
                    path["path"].append(i)
                    path["road"] += 1
                    path["toll"] += c
                    path["lookup"][i]=""
                    # print(path["toll"],path["road"])
                    # if path["toll"] / path["road"] < self.min_p[0] / self.min_p[
                    #     1]:
                    # print("ratio is" ,path["toll"], path["road"])
                    self.valid_paths[city].append(path)
                    # find return path
                    if (path["toll"] + self.cities[i][city]) / (
                            path["road"] + 1) < self.min_ratio:
                        self.min_p[0] = path["toll"] + self.cities[i][city]
                        self.min_p[1] = path["road"] + 1
                        self.min_ratio = self.min_p[0] / self.min_p[1]
                        #self.best_path = path["path"] + [i]


        def expand_path(self, path: dict,city:int,):
            #it = range(lc)
            #print(f"expanding {path['path']} {path['toll']}/{path['road']} < {self.min_p[0]/self.min_p[1]} {path['toll'] / path['road'] <self.min_p[0] / self.min_p[1]}")
            if path["toll"] / path["road"] > self.min_ratio:
                path["is_over"] = True
            #s = int(time.time() * 1000)
            while not path["is_over"]:
                #pp = self.get_path(path)
                pp = path["path"]
                start_len = len(pp)
                for c in self.it:
                    #self.iterations+=1
                    #self.possible_paths.append(pp.copy())
                    #s = int(time.time() * 1000)
                    if c not in path["lookup"]:
                        #self.time_in_copy_1 += int(time.time() * 1000) - s
                        # print(f"debug0 {c} {path["path"]} {path["toll"]} {path["road"]}")
                        # print(f"request to increase by {self.cities[path["path"][-1]][c]}")

                        #old_path = {k: path[k] for k in path}
                        #s = int(time.time() * 1000)
                        path["road"] += 1
                        t= self.cities[pp[-1]][c]
                        path["toll"] += t
                        pp.append(c)
                        #self.possible_paths.append(pp.copy())
                        #path["lookup"][c]=""
                        #self.time_in_copy_1 += int(time.time() * 1000) - s
                        # path.increase(self.cities[path["path"][-1]][c], c)
                        # print(f"debug {c} {path["path"]} {path["toll"]} {path["road"]}")
                        #print(f"checking if {path['path']} {path['toll']}/{path['road']} < {self.min_p[0] / self.min_p[1]} {path['toll'] / path['road'] <self.min_p[0] / self.min_p[1]}")
                        # if self.cities[c]["min"][1] == c and (path["toll"]+self.cities[c]["min"][0]) / (path["road"]+1) < self.min_p[0] / self.min_p[1]:
                        if path["toll"] / path["road"] < self.min_ratio:
                            cities_left = list(set(pp[1:] + k))
                            tolls_left = [self.cities[c][_] for _ in
                                          cities_left]
                            # print(
                            #     f"cities left {cities_left} tolls_left {tolls_left} city {c} k {k}")
                            if (min(tolls_left) + path["toll"]) / (path["road"] + 1) < self.min_ratio:
                                #print(f"ADDING {path['path']} {path['toll']}/{path['road']} < {self.min_p[0] / self.min_p[1]}")

                                #s = int(time.time() * 1000)
                                path_new = {x: path[x] for x in self.it2}
                                path_new["path"]=pp.copy()
                                path_new["lookup"]=path["lookup"].copy()
                                path_new["lookup"][c]=""
                                #path_new["lookup"]=path["lookup"].copy()

                                # self.valid_paths[path["path"][0]].append(deepcopy(path))
                                #print(f"p_new_appending {path_new}")
                                self.valid_paths[city].append(path_new)
                                #print(f"curr valid paths length {len(self.valid_paths[city])}")
                                # self.paths_added +=1
                                # self.time_in_copy += int(time.time()*1000)-s
                                # find return path
                                if (path["toll"] + self.cities[c][city]) / (
                                        path["road"] + 1) < self.min_ratio:
                                    self.min_p[0] = path["toll"] + \
                                                    self.cities[c][
                                                        city]
                                    self.min_p[1] = path["road"] + 1
                                    self.min_ratio = self.min_p[0]/self.min_p[1]
                                    # self.best_path = path["path"] + [path["path"][0]]

                            # self.time_in_copy_2 += int(time.time() * 1000) - s
                        #     def decrease(self,toll):
                        #         self.road -=1
                        #         self.toll -=toll
                        #         self.path=self.path[:-1]
                        #path = {k: old_path[k] for k in old_path}
                        #s = int(time.time() * 1000)
                        path["toll"]-=t
                        path["road"]-=1
                        # #path_old = path["path"]
                        #print("before",path["path"])
                        pp.pop(-1)
                        #path["lookup"][c]=""
                        #self.time_in_copy_1 += int(time.time() * 1000) - s
                        #path["path"]=path["path"][:-1]
                        # print("after",path["path"])
                        # print("new",path_new)
                        #print(path["path"])
                        # self.time_in_copy_3 += int(time.time() * 1000) - ss
                        # print(f"{len(path['path'])},{start_len}")
                        # print(path_old)
                        # print(path_new)
                        # raise
                        # path.decrease(self.cities[path["path"][-2]][c])
                        # break
                if len(pp) == start_len:  # when it passes from all cities and doesn't change size it's done
                    path["is_over"] = True
            #self.time_in_copy_1 += int(time.time() * 1000) - s


        def check_paths_for_city(self, city: int):
            finalized_paths = 0
            while finalized_paths < len(self.valid_paths[city]):
                finalized_paths = 0
                for i, p in enumerate(self.valid_paths[city]):
                    if p["is_over"]:
                        finalized_paths += 1
                    else:
                        self.expand_path(p,city)

        # def check_paths_for_city(self, city: int):
        #     if len(self.valid_paths[city])==0:
        #         return True
        #     for i, p in enumerate(self.valid_paths[city]):
        #         if not p["is_over"]:
        #             self.expand_path(p,city)

        # def check_paths_for_cities(self):
        #     to_check = len(tolls)
        #     while to_check>0:
        #         for c in self.it:
        #             while True:
        #                 _ = self.check_paths_for_city(c)
        #                 if _:
        #                     break
        #             # if _ :
        #                     to_check -=1
        #                     break
        #                     if to_check == 0:
        #                         break
        #                 print(c,"8888888888888888888888888",to_check)

        def find_best_ratio(self, a: int, b: int):
            # print(f"original res {a}/{b}")
            y = 10
            while True:
                to_break = True
                for i in range(y, 1, -1):
                    if a % i == 0 and b % i == 0:
                        a = a // i
                        b = b // i
                        y = i
                        to_break = False
                if to_break:
                    break
            # print((f"{a}/{b}"))
            # print(
            #     f" time1 {self.time_in_copy_1} time2 {self.time_in_copy_2} time3 {self.time_in_copy_3} iterations {self.iterations} ")
            return (f"{a}/{b}")

        def main(self):
            #ss = int(time.time() * 100)
            for c in self.it:
                self.find_start_path_for_city(c)
            #tt = int(time.time() * 100) - ss
            #self.time_for_first = tt
            #print(f"strart baseline is {self.min_p} took {tt} ms")
            for c in self.it:
                self.check_paths_for_city(c)
            # print(self.best_path, self.min_p)
            #self.check_paths_for_cities()
            return self.find_best_ratio(self.min_p[0], self.min_p[1])

    h = Hiker(tolls)
    # h.main()
    return h.main()


if __name__ == '__main__':
    fptr = open(os.environ['OUTPUT_PATH'], 'w')

    n = int(input())

    tolls = []

    for _ in range(n):
        tolls.append(list(map(int, input().rstrip().split())))

    result = hackerCountry(tolls)

    fptr.write(result + '\n')

    fptr.close()
