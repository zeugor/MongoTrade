package finData;

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

}
