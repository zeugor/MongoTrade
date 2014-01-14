package finData;

<<<<<<< HEAD
import FinDAO.MongoDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * @author      Elier Rodriguez <address @ example.com>
 * @version     1.0
 * @since       2013-07-12
 */
public class Downloader {
    //
    /**
     * Ticker downloader
     * <p>
     * right now works only with yahoo provider, it should work with others
     * <p>
     *
     * @param value id ticker.
     * @param provider
     * @param initDate
     * @param endDate
     * @param period
     *
     * @return should return true or false.
     */
    public static void remove(String value, String provider, Calendar initDate, Calendar endDate, String period) {
        if (provider.equals("yahoo")) {
            // Maybe delete period param and download daily always
            QueryDetail qd = new QueryDetail(
                    new String[] {"value", "initDate", "endDate", "period"}, value, initDate, endDate, period);
            YahooServiceQuery yahoo = new YahooServiceQuery(qd);
            InputStream urlReader = yahoo.getURLStream();
    //        System.out.println(yahoo.connstr);
    //        System.out.println(urlReader);

            try {
                List<String> rec = GetRec(urlReader);
                MongoDB DBConn = new MongoDB();
                DBConn.insertYahooData(rec, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(String value, String provide) {

    }

    // I dont' know yet how this work
    public static List<String> GetRec (InputStream URLStreamReader) throws IOException {
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

=======
/**
 * Author: Elier
 * Date: 12/16/13
 */
public class Downloader {
    public static boolean retrieve(Provider provider, MarketData md) {
        return provider.retrieve(md);
    }
    public static boolean update(Provider provider, MarketData md) {
        return provider.update(md);
    }
}

interface Provider {
    boolean retrieve(MarketData hd);
    boolean update(MarketData hd);
}

class Yahoo implements Provider {

}
class Finviz implements Provider {
}


abstract class MarketData {
    @Id private String name;
}

class Historical extends MarketData {
    @Id private Date date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private long volume;
}

class Quote {
    private Date date;
    private Dobule price;
}

class RealTime extends MarketData {
    ArrayList<Quote> prices;
    // Is it necesary store all these attributes?
    /*
        RealTime Schema
            name,
            date,
            price

            o

            name
            array[<date,price>]
     */
    /*
     "Average Volume": 3.49,
    "Change": "0.55%",
    "Company": "SPDR Barclays Cap Issuer Scd Corp Bond",
    "Country": "USA",
    "Current Ratio": "",
    "Dividend Yield": "3.45%",
    "EPS growth next 5 years": "",
    "EPS growth next year": "",
    "EPS growth past 5 years": "",
    "EPS growth this year": "",
    "Earnings Date": "",
    "Float Short": "",
    "Forward P/E": "",
    "Gross Margin": "",
    "Industry": "Exchange Traded Fund",
    "Insider Ownership": "",
    "Insider Transactions": "",
    "Institutional Ownership": "",
    "Institutional Transactions": "",
    "LT Debt/Equity": "",
    "Market Cap": "",
    "Operating Margin": "",
    "P/B": "",
    "P/Cash": "",
    "P/E": "",
    "P/Free Cash Flow": "",
    "P/S": "",
    "PEG": "",
    "Price": 31.03,
    "Profit Margin": "",
    "Quick Ratio": "",
    "Return on Assets": "",
    "Return on Equity": "",
    "Return on Investment": "",
    "Sales growth past 5 years": "",
    "Sector": "Financial",
    "Shares Float": "",
    "Shares Outstanding": "",
    "Short Ratio": "",
    "Total Debt/Equity": "",
    "Volume": 1075,
    "_id": "CBND"
     */
>>>>>>> d22cb319dbcb80edcdd4ac2af5bb520f5b11b72d
}
