package Metamatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metamatcher {
    private String pattern;
    private TreeMap<Integer,Integer> groupsIndices;

    Metamatcher(String pattern){
        this.pattern = pattern;
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }

    Metamatcher(Pattern pattern){
        this.pattern = pattern.toString();
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }
    /**
     * @param group ordinal number of group
     * @return starting index of a fragment of pattern, which contain group capturing
     */
    public int start(int group){
        List<Integer> indices = new ArrayList<Integer>(groupsIndices.keySet());
        indices.add(0,0);
        return indices.get(group);
    }

    /**
     * @param group ordinal number of group
     * @return ending index of a fragment of pattern, which contain group capturing
     */
    public int end(int group){
        List<Integer> indices = new ArrayList<Integer>(groupsIndices.values());
        indices.add(0,pattern.length());
        return indices.get(group);
    }

    /**
     * @param group ordinal number of group
     * @return String object containing fragment of regular expression which capture given group
     */
    public String group(int group){
        return pattern.substring(start(group), end(group));
    }

    /**
     * @return number of capturing groups within given regular expression
     */
    public int groupCount(){
        return groupsIndices.size();
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("Groups count: ")
                .append(groupCount())
                .append("\n");
        for(int i = 0; i <= groupCount(); i++){
            result.append("group(")
                    .append(i).append(") ")
                    .append(start(i))
                    .append("-")
                    .append(end(i))
                    .append("\t")
                    .append(group(i));
        }
        return result.toString();
    }

    public void usePattern(String pattern){
        this.pattern = pattern;
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }

    public void usePattern(Pattern pattern){
        this.pattern = pattern.toString();
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }



    /**It extracts fragments of regular expression enclosed by parentheses, checks if these are capturing type,
     * and put start(key) and end(value) indices into Map object
     * @return Map contains fragments of regular expression which capture groups
     */
    Map<Integer,Integer> getGroups(){
        String copy = pattern;
        Pattern pattern = Pattern.compile("\\([^\\(\\)]+\\)");
        Matcher matcher = pattern.matcher(copy);
        Map<Integer,Integer> temp = new TreeMap<Integer,Integer>();

        while(matcher.find()){
            if(isCapturingGroup(matcher.group(0))){
                temp.put(matcher.start(), matcher.end());
            }
            copy = copy.substring(0,matcher.start()) + replaceWithSpaces(matcher.group(0)) + copy.substring(matcher.end());
            matcher.reset(copy);
        }

        return temp;
    }

    /**
     * @param fragment of regular expression, enclosed by brackets
     * @return true if given String consist regular expression which capture groups
     */
    boolean isCapturingGroup(String fragment){
        return fragment.matches("((?<!\\\\)\\((?!\\?<?[:=!])[^\\(\\)]+\\))");
    }

    /**
     * Provide a filler String composed of spaces, to replace part enclosed by brackets
     * @param part String containing capturing group of regex, starting and ending with brackets,
     * @return String composed of spaces (' '), with length of part object,
     */
    String replaceWithSpaces(String part){
        String filler = "";
        for(int i = 0; i < part.length(); i++){
            filler += " ";
        }
        return filler;
    }

    public static void main(String[] args){
        String[] patterns = {"(a(b(c))d)(e(fg(h)ij))",
                "^([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+\n(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
                "\\((\\d+\\.\\d+)\\s(\\d+\\.\\d+)",
                "[a-z]+\\d"};
        for(String pattern : patterns) {
            Metamatcher matcher = new Metamatcher(pattern);
            System.out.println(matcher.toString());
            System.out.println(matcher.groupCount());
            System.out.println();
        }
    }
}
