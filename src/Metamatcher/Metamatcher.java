package Metamatcher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Tool for interpretation of the literal content of given regular expression. It performs analysis of capturing
 * abilities of this regex
 */
public class Metamatcher {
    private String pattern;
    private TreeMap<Integer,Integer> groupsIndices;
    private HashMap<String, Integer> namedGroupsIndex;

    public Metamatcher(){
        pattern = "";
        namedGroupsIndex = new HashMap<String, Integer>();
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }

    public Metamatcher(String pattern){
        this.pattern = pattern;
        namedGroupsIndex = new HashMap<String, Integer>();
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }

    public Metamatcher(Pattern pattern){
        this.pattern = pattern.toString();
        namedGroupsIndex = new HashMap<String, Integer>();
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
     * @param name name of group
     * @return starting index of a fragment of pattern, which contain named group capturing, or -1 if there is
     * no such named group
     */
    public int start(String name){
        return namedGroupsIndex.containsKey(name) ? start(namedGroupsIndex.get(name)) : -1;
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
     * @param name name of group
     * @return endig index of a fragment of pattern, which contain named group capturing, or -1 if there is no such
     * named group
     */
    public int end(String name){
        return namedGroupsIndex.containsKey(name) ? end(namedGroupsIndex.get(name)) : -1;
    }

    /**
     * @param group ordinal number of group
     * @return String object containing fragment of regular expression which capture given group
     */
    public String group(int group){
        return pattern.substring(start(group), end(group));
    }

    public String group(String name){
        return (namedGroupsIndex.containsKey(name) ? group(namedGroupsIndex.get(name)) : null);
    }

    /**
     * @return number of capturing groups within given regular expression
     */
    public int groupCount(){
        return groupsIndices.size();
    }

    /**
     * @return information about Metamatcher object: group count and groups,
     */
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
                    .append(group(i))
                    .append("\n");
        }
        return result.toString();
    }

    /**
     * Changes the regular expression that this Metamatcher uses to a given String object
     * @param pattern String consisting regular expression
     * @return same object Metamatcher
     */
    public Metamatcher usePattern(String pattern){
        this.pattern = pattern;
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
        return this;
    }

    /**
     * Changes the regular expression that this Metamatcher uses to a regular expression used by given Pattern object
     * @param pattern Pattern class object
     */
    public void usePattern(Pattern pattern){
        this.pattern = pattern.toString();
        groupsIndices = (TreeMap<Integer,Integer>)getGroups();
    }

    /**
     * @return the regular expression from which this Metamatcher use.
     */
    public String getPattern(){
        return pattern;
    }

    /**Returns start(key) and end(value) indices within Map object
     * @return Map beginning and ending indices of captured groups of given regular expression
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
            if(isNamedGroup(matcher.group(0))){
                namedGroupsIndex.put(getNamedGroup(matcher.group(0)),matcher.start());
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
        return fragment.matches("((?<!\\\\)\\((?!\\?[<]?[:=!>])[^\\(\\)]+\\))");
    }

    /**
     * @param fragment of regular expression, enclosed by brackets
     * @return true if given String consist regular expression with capturing named groups
     */
    boolean isNamedGroup(String fragment){
        return fragment.matches("\\(\\?<[A-Za-z0-9]+>[^)]+\\)");
    }

    /**
     * Extracts a name of named capturing group
     * @param fragment of regular expression
     * @return name of capturing group from within of given fragment
     */
    String getNamedGroup(String fragment){
        Matcher matcher = Pattern.compile("(?<=<)[a-zA-Z0-9]+?(?=>)").matcher(fragment);
        matcher.find();
        return matcher.group(0);
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
        Metamatcher matcher = new Metamatcher("");
        String[] patterns = {"(a(b(c))d)(e(fg(h)ij))",
                "^([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+\n(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
                "\\((\\d+\\.\\d+)\\s(\\d+\\.\\d+)",
                "(?<name>[a-z])+"};
        for(String pattern : patterns) {
            matcher = new Metamatcher(pattern);
            System.out.println(matcher.toString());
            System.out.println(matcher.groupCount());
            System.out.println();
        }
        System.out.println(matcher.group("name"));
    }
}
