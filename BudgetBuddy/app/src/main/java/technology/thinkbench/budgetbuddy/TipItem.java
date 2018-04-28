package technology.thinkbench.budgetbuddy;

/**
 * Created by willis on 4/27/18.
 */

public class TipItem {

    private String title;
    private String body;
    private String url;

    TipItem(){
        title = "Google Homepage";
        body = "A good place to start searches.";
        url = "http://www.google.com";
    }

    TipItem(String t, String b, String u){
        title = t;
        body = b;
        url = u;
    }

    String getTitle(){
        return title;
    }

    String getBody(){
        return body;
    }

    String getUrl(){
        return url;
    }

}
