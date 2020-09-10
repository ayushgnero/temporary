"""
You have a rooted tree with vertices numbered from through where the root is vertex

.

You are given
triplets, the triplet is denoted by three integers . The triplet represents a simple path in the tree with endpoints in and such that is ancestor of . The cost of the path is

.

You have to select a subset of the paths such that the sum of path costs is maximum and the
edge of the tree belongs to at most

paths from the subset. Print the sum as the output.

Input Format

The first line contains a single integer,

, denoting the number of testcases. Each testcase is defined as follows:

    The first line contains two space-separated integers,

(the number of vertices) and
(the number of paths), respectively.
Each line
of the subsequent lines contains three space-separated integers describing the respective values of , , and where (, ) is an edge in the tree and
is maximum number of paths which can include this edge.
Each line of the
subsequent lines contains three space-separated integers describing the respective values of , , and () that define the

    path and its cost.

Constraints

    Let

be the sum of
over all the trees.
Let
be the sum of
over all the trees.

Output Format

You must print

lines, where each line contains a single integer denoting the answer for the corresponding testcase.

Sample Input

1
8 8
1 2 3
1 3 1
2 4 5
2 5 1
2 6 1
3 7 2
4 8 1
1 2 3
2 8 5
1 8 7
1 5 8
1 6 10
3 7 5
1 7 6
1 7 6

Sample Output

37

Explanation

[vertical(1).png]

One of the possible subsets contains paths
. Its total cost is .
"""
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.io.OutputStreamWriter;
import java.util.NoSuchElementException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.InputStream;


public class VerticalPaths {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        Solution solver = new Solution();
        int testCount = Integer.parseInt(in.next());
        for (int i = 1; i <= testCount; i++) {
            solver.solve(i, in, out);
        }
        out.close();
    }

    static class Solution {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int n = in.readInt();
            int m = in.readInt();
            Graph graph = new Graph(n + 2, 2 * n - 2 + m * 3);
            for (int i = 0; i < n - 1; i++) {
                int a = in.readInt();
                int b = in.readInt();
                int d = in.readInt();
                graph.addFlowWeightedEdge(a - 1, b - 1, 0, d);
                graph.addFlowWeightedEdge(b - 1, a - 1, 0, d);
            }
            long total = 0;
            for (int i = 0; i < m; i++) {
                int u = in.readInt() - 1;
                int v = in.readInt() - 1;
                int c = in.readInt();
                total += c;
                graph.addFlowWeightedEdge(n, u, 0, 1);
                graph.addFlowWeightedEdge(v, n + 1, 0, 1);
                graph.addFlowWeightedEdge(u, v, c, 1);
            }
            MinCostFlow flow = new MinCostFlow(graph, n, n + 1, false);
            total -= flow.minCostMaxFlow().first;
            out.printLine(total);
        }

    }

    static interface IntIterator {
        public int value() throws NoSuchElementException;

        public boolean advance();

        public boolean isValid();

    }

    static interface IntComparator {
        public static final IntComparator DEFAULT = (first, second) -> {
            if (first < second) {
                return -1;
            }
            if (first > second) {
                return 1;
            }
            return 0;
        };

        public int compare(int first, int second);

    }

    static interface Edge {
    }

    static class MinCostFlow {
        private final Graph graph;
        private final int source;
        private final int destination;
        private final long[] phi;
        private final long[] dijkstraResult;
        private final int[] lastEdge;
        private final Heap heap;
        private final int vertexCount;
        private final int[] visited;
        private int visitIndex;

        public MinCostFlow(Graph graph, int source, int destination, boolean hasNegativeEdges) {
            this.graph = graph;
            this.source = source;
            this.destination = destination;
            vertexCount = graph.vertexCount();
            phi = new long[vertexCount];
            if (hasNegativeEdges) {
                fordBellman();
            }
            dijkstraResult = new long[vertexCount];
            lastEdge = new int[vertexCount];
            if (graph.isSparse()) {
                heap = new Heap(vertexCount, new IntComparator() {
                    public int compare(int first, int second) {
                        return IntegerUtils.longCompare(dijkstraResult[first], dijkstraResult[second]);
                    }
                }, vertexCount);
                visited = null;
            } else {
                heap = null;
                visited = new int[vertexCount];
            }
        }

        private void fordBellman() {
            Arrays.fill(phi, Long.MAX_VALUE);
            phi[source] = 0;
            boolean[] inQueue = new boolean[vertexCount];
            int[] queue = new int[vertexCount + 1];
            queue[0] = source;
            inQueue[source] = true;
            int stepCount = 0;
            int head = 0;
            int end = 1;
            int maxSteps = 2 * vertexCount * vertexCount;
            while (head != end) {
                int vertex = queue[head++];
                if (head == queue.length) {
                    head = 0;
                }
                inQueue[vertex] = false;
                int edgeID = graph.firstOutbound(vertex);
                while (edgeID != -1) {
                    long total = phi[vertex] + graph.weight(edgeID);
                    int destination = graph.destination(edgeID);
                    if (graph.capacity(edgeID) != 0 && phi[destination] > total) {
                        phi[destination] = total;
                        if (!inQueue[destination]) {
                            queue[end++] = destination;
                            inQueue[destination] = true;
                            if (end == queue.length) {
                                end = 0;
                            }
                        }
                    }
                    edgeID = graph.nextOutbound(edgeID);
                }
                if (++stepCount > maxSteps) {
                    throw new IllegalArgumentException("Graph contains negative cycle");
                }
            }
        }

        public Pair<Long, Long> minCostMaxFlow() {
            return minCostMaxFlow(Long.MAX_VALUE);
        }

        public Pair<Long, Long> minCostMaxFlow(long maxFlow) {
            long cost = 0;
            long flow = 0;
            while (maxFlow != 0) {
                if (graph.isSparse()) {
                    dijkstraAlgorithm();
                } else {
                    dijkstraAlgorithmFull();
                }
                if (lastEdge[destination] == -1) {
                    return Pair.makePair(cost, flow);
                }
                for (int i = 0; i < dijkstraResult.length; i++) {
                    if (dijkstraResult[i] != Long.MAX_VALUE) {
                        phi[i] += dijkstraResult[i];
                    }
                }
                int vertex = destination;
                long currentFlow = maxFlow;
                long currentCost = 0;
                while (vertex != source) {
                    int edgeID = lastEdge[vertex];
                    currentFlow = Math.min(currentFlow, graph.capacity(edgeID));
                    currentCost += graph.weight(edgeID);
                    vertex = graph.source(edgeID);
                }
                maxFlow -= currentFlow;
                cost += currentCost * currentFlow;
                flow += currentFlow;
                vertex = destination;
                while (vertex != source) {
                    int edgeID = lastEdge[vertex];
                    graph.pushFlow(edgeID, currentFlow);
                    vertex = graph.source(edgeID);
                }
            }
            return Pair.makePair(cost, flow);
        }

        private void dijkstraAlgorithm() {
            Arrays.fill(dijkstraResult, Long.MAX_VALUE);
            Arrays.fill(lastEdge, -1);
            dijkstraResult[source] = 0;
            heap.add(source);
            while (!heap.isEmpty()) {
                int current = heap.poll();
                int edgeID = graph.firstOutbound(current);
                while (edgeID != -1) {
                    if (graph.capacity(edgeID) != 0) {
                        int next = graph.destination(edgeID);
                        long total = graph.weight(edgeID) - phi[next] + phi[current] + dijkstraResult[current];
                        if (dijkstraResult[next] > total) {
                            dijkstraResult[next] = total;
                            if (heap.getIndex(next) == -1) {
                                heap.add(next);
                            } else {
                                heap.shiftUp(heap.getIndex(next));
                            }
                            lastEdge[next] = edgeID;
                        }
                    }
                    edgeID = graph.nextOutbound(edgeID);
                }
            }
        }

        private void dijkstraAlgorithmFull() {
            visitIndex++;
            Arrays.fill(dijkstraResult, Long.MAX_VALUE);
            lastEdge[destination] = -1;
            dijkstraResult[source] = 0;
            for (int i = 0; i < vertexCount; i++) {
                int index = -1;
                long length = Long.MAX_VALUE;
                for (int j = 0; j < vertexCount; j++) {
                    if (visited[j] != visitIndex && dijkstraResult[j] < length) {
                        length = dijkstraResult[j];
                        index = j;
                    }
                }
                if (index == -1) {
                    return;
                }
                visited[index] = visitIndex;
                int edgeID = graph.firstOutbound(index);
                while (edgeID != -1) {
                    if (graph.capacity(edgeID) != 0) {
                        int next = graph.destination(edgeID);
                        if (visited[next] != visitIndex) {
                            long total = graph.weight(edgeID) - phi[next] + phi[index] + length;
                            if (dijkstraResult[next] > total) {
                                dijkstraResult[next] = total;
                                lastEdge[next] = edgeID;
                            }
                        }
                    }
                    edgeID = graph.nextOutbound(edgeID);
                }
            }
        }

    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void close() {
            writer.close();
        }

        public void printLine(long i) {
            writer.println(i);
        }

    }

    static class IntegerUtils {
        public static int longCompare(long a, long b) {
            if (a < b) {
                return -1;
            }
            if (a > b) {
                return 1;
            }
            return 0;
        }

    }

    static interface IntCollection extends IntStream {
    }

    static class Graph {
        public static final int REMOVED_BIT = 0;
        protected int vertexCount;
        protected int edgeCount;
        private int[] firstOutbound;
        private int[] firstInbound;
        private Edge[] edges;
        private int[] nextInbound;
        private int[] nextOutbound;
        private int[] from;
        private int[] to;
        private long[] weight;
        public long[] capacity;
        private int[] reverseEdge;
        private int[] flags;

        public Graph(int vertexCount) {
            this(vertexCount, vertexCount);
        }

        public Graph(int vertexCount, int edgeCapacity) {
            this.vertexCount = vertexCount;
            firstOutbound = new int[vertexCount];
            Arrays.fill(firstOutbound, -1);

            from = new int[edgeCapacity];
            to = new int[edgeCapacity];
            nextOutbound = new int[edgeCapacity];
            flags = new int[edgeCapacity];
        }

        public int addEdge(int fromID, int toID, long weight, long capacity, int reverseEdge) {
            ensureEdgeCapacity(edgeCount + 1);
            if (firstOutbound[fromID] != -1) {
                nextOutbound[edgeCount] = firstOutbound[fromID];
            } else {
                nextOutbound[edgeCount] = -1;
            }
            firstOutbound[fromID] = edgeCount;
            if (firstInbound != null) {
                if (firstInbound[toID] != -1) {
                    nextInbound[edgeCount] = firstInbound[toID];
                } else {
                    nextInbound[edgeCount] = -1;
                }
                firstInbound[toID] = edgeCount;
            }
            this.from[edgeCount] = fromID;
            this.to[edgeCount] = toID;
            if (capacity != 0) {
                if (this.capacity == null) {
                    this.capacity = new long[from.length];
                }
                this.capacity[edgeCount] = capacity;
            }
            if (weight != 0) {
                if (this.weight == null) {
                    this.weight = new long[from.length];
                }
                this.weight[edgeCount] = weight;
            }
            if (reverseEdge != -1) {
                if (this.reverseEdge == null) {
                    this.reverseEdge = new int[from.length];
                    Arrays.fill(this.reverseEdge, 0, edgeCount, -1);
                }
                this.reverseEdge[edgeCount] = reverseEdge;
            }
            if (edges != null) {
                edges[edgeCount] = createEdge(edgeCount);
            }
            return edgeCount++;
        }

        protected final GraphEdge createEdge(int id) {
            return new GraphEdge(id);
        }

        public final int addFlowWeightedEdge(int from, int to, long weight, long capacity) {
            if (capacity == 0) {
                return addEdge(from, to, weight, 0, -1);
            } else {
                int lastEdgeCount = edgeCount;
                addEdge(to, from, -weight, 0, lastEdgeCount + entriesPerEdge());
                return addEdge(from, to, weight, capacity, lastEdgeCount);
            }
        }

        protected int entriesPerEdge() {
            return 1;
        }

        public final int vertexCount() {
            return vertexCount;
        }

        public final int firstOutbound(int vertex) {
            int id = firstOutbound[vertex];
            while (id != -1 && isRemoved(id)) {
                id = nextOutbound[id];
            }
            return id;
        }

        public final int nextOutbound(int id) {
            id = nextOutbound[id];
            while (id != -1 && isRemoved(id)) {
                id = nextOutbound[id];
            }
            return id;
        }

        public final int source(int id) {
            return from[id];
        }

        public final int destination(int id) {
            return to[id];
        }

        public final long weight(int id) {
            if (weight == null) {
                return 0;
            }
            return weight[id];
        }

        public final long capacity(int id) {
            if (capacity == null) {
                return 0;
            }
            return capacity[id];
        }

        public final long flow(int id) {
            if (reverseEdge == null) {
                return 0;
            }
            return capacity[reverseEdge[id]];
        }

        public final void pushFlow(int id, long flow) {
            if (flow == 0) {
                return;
            }
            if (flow > 0) {
                if (capacity(id) < flow) {
                    throw new IllegalArgumentException("Not enough capacity");
                }
            } else {
                if (flow(id) < -flow) {
                    throw new IllegalArgumentException("Not enough capacity");
                }
            }
            capacity[id] -= flow;
            capacity[reverseEdge[id]] += flow;
        }

        public final boolean flag(int id, int bit) {
            return (flags[id] >> bit & 1) != 0;
        }

        public final boolean isRemoved(int id) {
            return flag(id, REMOVED_BIT);
        }

        protected void ensureEdgeCapacity(int size) {
            if (from.length < size) {
                int newSize = Math.max(size, 2 * from.length);
                if (edges != null) {
                    edges = resize(edges, newSize);
                }
                from = resize(from, newSize);
                to = resize(to, newSize);
                nextOutbound = resize(nextOutbound, newSize);
                if (nextInbound != null) {
                    nextInbound = resize(nextInbound, newSize);
                }
                if (weight != null) {
                    weight = resize(weight, newSize);
                }
                if (capacity != null) {
                    capacity = resize(capacity, newSize);
                }
                if (reverseEdge != null) {
                    reverseEdge = resize(reverseEdge, newSize);
                }
                flags = resize(flags, newSize);
            }
        }

        protected final int[] resize(int[] array, int size) {
            int[] newArray = new int[size];
            System.arraycopy(array, 0, newArray, 0, array.length);
            return newArray;
        }

        private long[] resize(long[] array, int size) {
            long[] newArray = new long[size];
            System.arraycopy(array, 0, newArray, 0, array.length);
            return newArray;
        }

        private Edge[] resize(Edge[] array, int size) {
            Edge[] newArray = new Edge[size];
            System.arraycopy(array, 0, newArray, 0, array.length);
            return newArray;
        }

        public final boolean isSparse() {
            return vertexCount == 0 || edgeCount * 20 / vertexCount <= vertexCount;
        }

        protected class GraphEdge implements Edge {
            protected int id;

            protected GraphEdge(int id) {
                this.id = id;
            }

        }

    }

    static interface IntQueue extends IntCollection {
    }

    static class Pair<U, V> implements Comparable<Pair<U, V>> {
        public final U first;
        public final V second;

        public static <U, V> Pair<U, V> makePair(U first, V second) {
            return new Pair<U, V>(first, second);
        }

        private Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }


        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pair pair = (Pair) o;

            return !(first != null ? !first.equals(pair.first) : pair.first != null) &&
                    !(second != null ? !second.equals(pair.second) : pair.second != null);
        }


        public int hashCode() {
            int result = first != null ? first.hashCode() : 0;
            result = 31 * result + (second != null ? second.hashCode() : 0);
            return result;
        }


        public String toString() {
            return "(" + first + "," + second + ")";
        }

        @SuppressWarnings({"unchecked"})
        public int compareTo(Pair<U, V> o) {
            int value = ((Comparable<U>) first).compareTo(o.first);
            if (value != 0) {
                return value;
            }
            return ((Comparable<V>) second).compareTo(o.second);
        }

    }

    static interface IntStream extends Iterable<Integer>, Comparable<IntStream> {
        public IntIterator intIterator();

        default public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                private IntIterator it = intIterator();

                public boolean hasNext() {
                    return it.isValid();
                }

                public Integer next() {
                    int result = it.value();
                    it.advance();
                    return result;
                }
            };
        }

        default public int compareTo(IntStream c) {
            IntIterator it = intIterator();
            IntIterator jt = c.intIterator();
            while (it.isValid() && jt.isValid()) {
                int i = it.value();
                int j = jt.value();
                if (i < j) {
                    return -1;
                } else if (i > j) {
                    return 1;
                }
                it.advance();
                jt.advance();
            }
            if (it.isValid()) {
                return 1;
            }
            if (jt.isValid()) {
                return -1;
            }
            return 0;
        }

    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInt() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String readString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next() {
            return readString();
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }

    static class Heap implements IntQueue {
        public IntComparator comparator;
        private int size = 0;
        public int[] elements;
        private int[] at;

        public Heap(int maxElement) {
            this(10, maxElement);
        }

        public Heap(IntComparator comparator, int maxElement) {
            this(10, comparator, maxElement);
        }

        public Heap(int capacity, int maxElement) {
            this(capacity, IntComparator.DEFAULT, maxElement);
        }

        public Heap(int capacity, IntComparator comparator, int maxElement) {
            this.comparator = comparator;
            elements = new int[capacity];
            at = new int[maxElement];
            Arrays.fill(at, -1);
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void add(int element) {
            ensureCapacity(size + 1);
            elements[size] = element;
            at[element] = size;
            shiftUp(size++);
        }

        public void shiftUp(int index) {
            //        if (index < 0 || index >= size)
            //            throw new IllegalArgumentException();
            int value = elements[index];
            while (index != 0) {
                int parent = (index - 1) >>> 1;
                int parentValue = elements[parent];
                if (comparator.compare(parentValue, value) <= 0) {
                    elements[index] = value;
                    at[value] = index;
                    return;
                }
                elements[index] = parentValue;
                at[parentValue] = index;
                index = parent;
            }
            elements[0] = value;
            at[value] = 0;
        }

        public void shiftDown(int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException();
            }
            while (true) {
                int child = (index << 1) + 1;
                if (child >= size) {
                    return;
                }
                if (child + 1 < size && comparator.compare(elements[child], elements[child + 1]) > 0) {
                    child++;
                }
                if (comparator.compare(elements[index], elements[child]) <= 0) {
                    return;
                }
                swap(index, child);
                index = child;
            }
        }

        public int getIndex(int element) {
            return at[element];
        }

        private void swap(int first, int second) {
            int temp = elements[first];
            elements[first] = elements[second];
            elements[second] = temp;
            at[elements[first]] = first;
            at[elements[second]] = second;
        }

        private void ensureCapacity(int size) {
            if (elements.length < size) {
                int[] oldElements = elements;
                elements = new int[Math.max(2 * elements.length, size)];
                System.arraycopy(oldElements, 0, elements, 0, this.size);
            }
        }

        public int poll() {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException();
            }
            int result = elements[0];
            at[result] = -1;
            if (size == 1) {
                size = 0;
                return result;
            }
            elements[0] = elements[--size];
            at[elements[0]] = 0;
            shiftDown(0);
            return result;
        }


        public IntIterator intIterator() {
            return new IntIterator() {
                private int at;


                public int value() throws NoSuchElementException {
                    return elements[at];
                }


                public boolean advance() throws NoSuchElementException {
                    return ++at < size;
                }


                public boolean isValid() {
                    return at < size;
                }


                public void remove() throws NoSuchElementException {
                    throw new UnsupportedOperationException();
                }
            };
        }

    }
}
