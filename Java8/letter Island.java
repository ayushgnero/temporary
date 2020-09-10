"""
You are given string and number

.

Consider a substring
of string . For each position of string mark it if there is an occurence of the substring that covers the position. More formally, position will be marked if there exists such index that: and . We will tell produce islands if all the marked positions form

groups of contiguous positions.

For example, if we have a string ababaewabaq the substring aba marks the positions 1, 2, 3, 4, 5, 8, 9, 10; that is XXXXXewXXXq (X denotes marked position). We can see 2 groups of contiguous positions, that is 2 islands. Finally, substring aba produces 2 islands in the string ababaewabaq.

Calculate and print the number of different substrings of string
that produce exactly

islands.

Input Format

The first line contains string
. The string consists of lowercase letters only. The second line contains an integer

.

Output Format

Output a single integer

the answer to the problem.

Sample Input

abaab
2

Sample Output

3

Explanation

All the suitable substrings are: a, ab, b.
"""
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Letter_Islands {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        if(in.hasNext()){
            final char[] str = in.next().toCharArray();
            if(str.length>0 && in.hasNext()){
                int k = in.nextInt();
                if(k>0 && k<=str.length){
                    System.out.println((new FoundSubStrings(str, k)).count());
                }
            }
        }
    }

    static class FoundSubStrings {

        private final char[] str;
        private final int k;
        private Map<Long, SubString> curr;
        private Map<Long, SubString> next;
        private SubString previousSub=null;
        private int previousSubParentStartIndex = -1;
        private int previousSubLen = -1;

        public FoundSubStrings(char[] str, int k) {
            this.str = str;
            this.k = k;
            curr = new HashMap<>(str.length>32 ? str.length>>1 : str.length);
            next = new HashMap<>(str.length>32 ? str.length>>1 : str.length);
        }

        public long count(){
            long countResult = 0;
            int startIndex;
            char lastChar = str[0];
            int lastCharCount = 0;
            for(int i=0; i<=str.length; i++){
                if(i==str.length || lastChar!=str[i]){
                    if(lastCharCount>1){
                        for(int j=i-lastCharCount; j<i-1; j++){
                            this.add(j, lastChar, i-j);
                        }
                    }
                    this.add(i-1, lastChar);
                    if(i!=str.length){
                        lastChar = str[i];
                        lastCharCount = 1;
                    }
                } else {
                    lastCharCount++;
                }
            }
            //
            this.switchLists();
            //
            while(!curr.isEmpty()){
                for(SubString subStr : curr.values()){
                    if(subStr.islands==k){
                        countResult++;
                        if(k==1 && subStr.size==1){
                            countResult+=str.length-subStr.startIndex-subStr.len;
                            continue;
                        }
                    } else if(subStr.size<k){
                        continue;
                    }
                    for(int i=0; i<subStr.size && ((startIndex=subStr.indexes[i])<(str.length-subStr.len)); i++){
                        this.add(subStr.startIndex, startIndex, str[startIndex+subStr.len], subStr.len+1, subStr.size);
                    }
                }
                this.switchLists();
            }
            return countResult;
        }

        private void add(int parentStartIndex, int startIndex, char chr, int len, int childsLength){
            if(previousSubParentStartIndex!=parentStartIndex || previousSubLen!=len || previousSub.chr!=chr){
                long key = getKey(parentStartIndex, len, chr);
                previousSub = next.get(key);
                if(previousSub==null){
                    previousSub = new SubString(parentStartIndex, startIndex, chr, len, childsLength);
                    next.put(key, previousSub);
                }
                previousSubParentStartIndex = previousSub.parentStartIndex;
                previousSubLen = len;
            }
            previousSub.addIndex(startIndex);
        }

        private void add(int startIndex, char chr, int len){
            long key = getKey(len, chr);
            SubString sub = next.get(key);
            if(sub==null){
                sub = new SubString(startIndex, chr, len);
                next.put(key, sub);
            }
            sub.addIndex(startIndex);
        }

        private void add(int startIndex, char chr){
            if(previousSub==null || previousSubLen!=1 || previousSub.chr!=chr){
               long key = getKey(chr);
               previousSub = next.get(key);
                if(previousSub==null){
                    previousSub = new SubString(startIndex, chr, 1);
                    next.put(key, previousSub);
                }
                previousSubLen = 1;
            }
            previousSub.addIndex(startIndex);
        }

        private void switchLists(){
            previousSubParentStartIndex = -1;
            previousSub = null;
            Map<Long, SubString> tmp = curr;
            curr = next;
            next = tmp;
            tmp.clear();
        }


        public static long getKey(int parentStartIndex, int length, char chr){
            return (((long)parentStartIndex) | ((long)length<<31) | ((long)chr)<<23);
        }

        public static long getKey(int length, char chr){
            return (((long)length<<31) | (((long)chr)<<23));
        }

        public static long getKey(char chr){
            return (((long)chr)<<23);
        }

        class SubString implements Comparable<SubString> {

            private final int parentStartIndex;
            private final int len;
            private final char chr;
            private int startIndex;
            private int islands = 0;
            //
            private int[] indexes;
            private int size = 0;

            public SubString(int startIndex, char chr, int length) {
                this(-1, startIndex, chr, length, 16);
            }

            public SubString(int startIndex, char chr, int length, int childsLength) {
                this(-1, startIndex, chr, length, childsLength);
            }

            public SubString(int parentStartIndex, int startIndex, char chr, int length, int childsLength) {
                this.parentStartIndex = parentStartIndex;
                this.startIndex = startIndex;
                this.len = length;
                this.chr = chr;
                this.indexes = new int[childsLength>16? 16: childsLength+1];
            }

            public void addIndex(int index){
                if(size==0 || (indexes[size-1]+len<index)){
                    islands++;
                }
                if(indexes.length==size+1){
                    int[] tmpArr = new int[indexes.length<<1];
                    System.arraycopy(indexes, 0, tmpArr, 0, indexes.length);
                    indexes = tmpArr;
                }
                indexes[size++] = index;
            }

            @Override
            public int compareTo(SubString o) {
                return (this.parentStartIndex==o.parentStartIndex) ? chr - o.chr :
                    this.parentStartIndex - o.parentStartIndex;
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder(100);
                sb.append("SubString{startIndex=").append(startIndex).append(", length=")
                        .append(len).append(", islands=")
                        .append(islands).append(", numberOfIndexes=")
                        .append(size).append(", arr=");
                for(int i=startIndex; i<startIndex+len; i++){
                    sb.append(str[i]).append(',');
                }
                sb.setCharAt(sb.length()-1,'}');
                return sb.toString();
            }
        }
    }
}
