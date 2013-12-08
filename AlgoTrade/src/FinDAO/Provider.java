package FinDAO;

/**
 * Created with IntelliJ IDEA.
 * User: Elier
 * Date: 12/7/13
 * Time: 12:27 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Provider {
    YAHOO("http://finance.yahoo.com/q?s=%5##VALUE");
    String urlPattern;
    Provider(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
