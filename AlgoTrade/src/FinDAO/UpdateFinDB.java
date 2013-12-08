package FinDAO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import finData.FinvizServiceQuery;
import finData.QueryDetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Xiliang
 * Date: 24/10/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateFinDB {
    public UpdateFinDB(String mode) throws IOException {
        if (mode.equals("Finviz")) {
            UpdateFinvizDB();
        } /*else if (mode.equals("Yahoo")) {
            UpdateYahooDB();
        } else {
            UpdateFinvizDB();
            UpdateYahooDB();
        }       */
    }

    public void UpdateFinvizDB() throws IOException {
        String[] viewList = new String[]{"overview", "valuation", "financial", "ownership", "performance", "technical"};
        for (String view : viewList) {
            QueryDetail qd = new QueryDetail("finviz", "view", view);
            InputStream FinvizURLReader = new FinvizServiceQuery(qd).getURLStream();
            System.out.println(FinvizURLReader);
            System.exit(1);
            List<String> FinvizRec = GetRec(FinvizURLReader);
            MongoDB DBConn = new MongoDB();
            DBConn.InsertFinvizData(FinvizRec);
        }
    }

    public void UpdateYahooDB() {
        //TODO: loop through all stocks, Query from finviz database
        MongoDB findb = null;
        try {
            findb = new MongoDB();
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        DBCollection FinvizRealtime = findb.GetDBConn().getCollection("FinvizRealtime");
        DBCursor cursor = FinvizRealtime.find(new BasicDBObject(/*"_id",new BasicDBObject("$lt","BF-B")*/),new BasicDBObject("_id",true))
                .sort(new BasicDBObject("_id",1));
        while(cursor.hasNext()){
            String Ticker = (String) cursor.next().get("_id");
            try {
                InputStream YahooURLReader = new URL("http://ichart.finance.yahoo.com/table.csv?s="+Ticker).openStream();
                List<String> YahooRec = new LinkedList<String>();
                YahooRec = GetRec(YahooURLReader);
                MongoDB DBConn = new MongoDB();
                DBConn.InsertYahooData(YahooRec,Ticker);
                System.out.println("Updating for "+Ticker);
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnknownHostException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                System.out.println("Ticker: "+Ticker +" is not available on Yahoo");
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
    public List<String> GetRec (InputStream URLStreamReader) throws IOException {
        List<String> ReturnRec = new LinkedList<String>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(URLStreamReader,"UTF-8"));
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            ReturnRec.add(inputLine);
            //System.out.println(inputLine);
        }
        in.close();
        return ReturnRec;
    }
}


