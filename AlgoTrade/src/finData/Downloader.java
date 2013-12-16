package finData;

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
}
