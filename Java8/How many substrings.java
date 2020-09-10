"""
Consider a string of characters, , of where each character is indexed from to

.

You are given
queries in the form of two integer indices: and . For each query, count and print the number of different substrings of in the inclusive range between and

.

Note: Two substrings are different if their sequence of characters differs by at least one. For example, given the string
aab, substrings a and a are the same but substrings aa and

ab are different.

Input Format

The first line contains two space-separated integers describing the respective values of
and .
The second line contains a single string denoting .
Each of the subsequent lines contains two space-separated integers describing the respective values of and

for a query.

Constraints

String

    consists of lowercase English alphabetic letters (i.e., a to z) only.

Subtasks

    For

of the test cases, For of the test cases, For of the test cases,

Output Format

For each query, print the number of different substrings in the inclusive range between index
and index

on a new line.

Sample Input 0

5 5
aabaa
1 1
1 4
1 1
1 4
0 2

Sample Output 0

1
8
1
8
5

Explanation 0

Given
aabaa, we perform the following

queries:

    1 1: The only substring of a is itself, so we print

on a new line.
1 4: The substrings of abaa are a, b, ab, ba, aa, aba, baa, and abaa, so we print
on a new line.
1 1: The only substring of a is itself, so we print
on a new line.
1 4: The substrings of abaa are a, b, ab, ba, aa, aba, baa, and abaa, so we print
on a new line.
0 2: The substrings of aab are a, b, aa, ab, and aab, so we print
on a new line.
"""
Node[] sorted = new Node[gen];
   sorted[0] = t0;
   int p = 1;
   for(int i = 0;i < gen;i++){
       Node cur = sorted[i];
       for(int j = 0;j < cur.np;j++){
           if(--indeg[cur.next[j].id] == 0){
               sorted[p++] = cur.next[j];
           }
       }
   }

   for(int i = 0;i < gen;i++)sorted[i].id = i;
   nodes = sorted;
   sortedTopologically = true;
   return this;
}

// visualizer

public String toString()
{
   StringBuilder sb = new StringBuilder();
   for(Node n : nodes){
       if(n != null){
           sb.append(String.format("{id:%d, len:%d, link:%d, cloned:%b, ",
                   n.id,
                   n.len,
                   n.link != null ? n.link.id : null,
                   n.original.id));
           sb.append("next:{");
           for(int i = 0;i < n.np;i++){
               sb.append(n.next[i].key + ":" + n.next[i].id + ",");
           }
           sb.append("}");
           sb.append("}");
           sb.append("\n");
       }
   }
   return sb.toString();
}

public String toGraphviz(boolean next, boolean suffixLink)
{
   StringBuilder sb = new StringBuilder("http://chart.apis.google.com/chart?cht=gv:dot&chl=");
   sb.append("digraph{");
   for(Node n : nodes){
       if(n != null){
           if(suffixLink && n.link != null){
               sb.append(n.id)
               .append("->")
               .append(n.link.id)
               .append("[style=dashed],");
           }

           if(next && n.next != null){
               for(int i = 0;i < n.np;i++){
                   sb.append(n.id)
                   .append("->")
                   .append(n.next[i].id)
                   .append("[label=")
                   .append(n.next[i].key)
                   .append("],");
               }
           }
       }
   }
   sb.append("}");
   return sb.toString();
}

public String label(Node n)
{
   if(n.original != null){
       return n.id + "C";
   }else{
       return n.id + "";
   }
}

public String toDot(boolean next, boolean suffixLink)
{
   StringBuilder sb = new StringBuilder("digraph{\n");
   sb.append("graph[rankdir=LR];\n");
   sb.append("node[shape=circle];\n");
   for(Node n : nodes){
       if(n != null){
           if(suffixLink && n.link != null){
               sb.append("\"" + label(n) + "\"")
               .append("->")
               .append("\"" + label(n.link) + "\"")
               .append("[style=dashed];\n");
           }

           if(next && n.next != null){
               for(int i = 0;i < n.np;i++){
                   sb.append("\"" + label(n) + "\"")
                   .append("->")
                   .append("\"" + label(n.next[i]) + "\"")
                   .append("[label=\"")
                   .append(n.next[i].key)
                   .append("\"];\n");
               }
           }
       }
   }
   sb.append("}\n");
   return sb.toString();
}
}

void run() throws Exception
{
is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
out = new PrintWriter(System.out);

long s = System.currentTimeMillis();
solve();
out.flush();
if(!INPUT.isEmpty())tr(System.currentTimeMillis()-s+"ms");
}

public static void main(String[] args) throws Exception {
new How_Many_Substrings().run();
}

private byte[] inbuf = new byte[1024];
public int lenbuf = 0, ptrbuf = 0;

private int readByte()
{
if(lenbuf == -1)throw new InputMismatchException();
if(ptrbuf >= lenbuf){
   ptrbuf = 0;
   try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
   if(lenbuf <= 0)return -1;
}
return inbuf[ptrbuf++];
}

private boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
private int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }

private double nd() { return Double.parseDouble(ns()); }
private char nc() { return (char)skip(); }

private String ns()
{
int b = skip();
StringBuilder sb = new StringBuilder();
while(!(isSpaceChar(b))){ // when nextLine, (isSpaceChar(b) && b != ' ')
   sb.appendCodePoint(b);
   b = readByte();
}
return sb.toString();
}

private char[] ns(int n)
{
char[] buf = new char[n];
int b = skip(), p = 0;
while(p < n && !(isSpaceChar(b))){
   buf[p++] = (char)b;
   b = readByte();
}
return n == p ? buf : Arrays.copyOf(buf, p);
}

private char[][] nm(int n, int m)
{
char[][] map = new char[n][];
for(int i = 0;i < n;i++)map[i] = ns(m);
return map;
}

private int[] na(int n)
{
int[] a = new int[n];
for(int i = 0;i < n;i++)a[i] = ni();
return a;
}

private int ni()
{
int num = 0, b;
boolean minus = false;
while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
if(b == '-'){
   minus = true;
   b = readByte();
}

while(true){
   if(b >= '0' && b <= '9'){
       num = num * 10 + (b - '0');
   }else{
       return minus ? -num : num;
   }
   b = readByte();
}
}

private long nl()
{
long num = 0;
int b;
boolean minus = false;
while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
if(b == '-'){
   minus = true;
   b = readByte();
}

while(true){
   if(b >= '0' && b <= '9'){
       num = num * 10 + (b - '0');
   }else{
       return minus ? -num : num;
   }
   b = readByte();
}
}

private static void tr(Object... o) { System.out.println(Arrays.deepToString(o)); }
}
