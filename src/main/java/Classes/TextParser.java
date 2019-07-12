package Classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TextParser {
    public static void main(String[] args){
        parseForAddition("leqsebi, dzmoba, ertoba,,,", "saba sturua  ", "chati exeba veshapebs");
    }


    /**
     * Parses tags, name and description to generate tags
     *
     * @param tags String of tags
     * @param name name of chat
     * @param description description of chat
     * */

    public static ArrayList<String> parseForAddition(String tags, String name, String description){
        ArrayList<String> result = new ArrayList<String>();
        String delim = ",";
        StringTokenizer st = new StringTokenizer(tags, delim);
        while(st.hasMoreElements()){
            String tag = st.nextToken().trim();
            result.add(tag);
        }
        String nameAndDesc = name + " " + description;
        st = new StringTokenizer(nameAndDesc);
        while(st.hasMoreElements()){
            String tag = st.nextToken().trim();
            result.add(tag);
        }
        return result;
    }

    /**
     * Parses the line for search
     *
     * @param line String written in a search field
     * */
    public static ArrayList<String> parseForSearch(String line){
        ArrayList<String> result = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(line);
        while(st.hasMoreElements()){
            String tag = st.nextToken().trim();
            result.add(tag);
        }
        return result;
    }
}
