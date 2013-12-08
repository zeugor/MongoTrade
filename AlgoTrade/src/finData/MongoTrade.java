package finData;

import java.util.Calendar;

public class MongoTrade {
	public static void main(String[] args) {
	    //new UpdateFinDB("Finviz");

        // input example
        String value = "IBEX";
        String provider = "yahoo";
        Calendar initDate = Calendar.getInstance();
        initDate.set(2011, 11, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2013, 5, 1);
        String period = "daily";

        Downloader.remove(value, provider, initDate, endDate, period);
	}
}
