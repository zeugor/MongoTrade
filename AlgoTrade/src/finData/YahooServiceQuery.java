package finData;

import java.util.HashMap;

public class YahooServiceQuery extends GenericServiceQuery {
    private static HashMap<String, String> map = new HashMap<String, String>(9) {
        {
            put("value", "s=%5E");
            put("initDate_day", "a=");
            put("initDate_month", "b=");
            put("initDate_year", "c=");
            put("endDate_day", "d=");
            put("endDate_month", "e=");
            put("endDate_year", "f=");
            put("period", "g=");
            put("daily", "d");
        }
        // Downloading Yahoo data: www.gummy-stuff.org/Yahoo-data.htm


    };           // this should be in the father class  y sobrecargado

	public YahooServiceQuery(QueryDetail query) {
		super(query);
	}

	public String connstrConstructor(QueryDetail query) {
		String cstr = "http://ichart.finance.yahoo.com/table.csv?";

        // mapear los attributs
        for (AttrNode node : query.attrList) {
            cstr += map.get(node.getName());
            if (node.getName() == "period") {
            } else {
                 cstr += node.getValue() + "&";
            }

        }
        System.out.println(cstr);
             // System.exit(1);


		return cstr.substring(0, cstr.length() - 1);
    }

}
