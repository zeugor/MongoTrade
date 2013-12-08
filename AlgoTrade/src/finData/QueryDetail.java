package finData;

import java.util.*;

public class QueryDetail {
	public static Map<String,String> urlStrMap =
            new HashMap<String,String>() {
                {
                    put("view","v");
                    put("overview","111");
                    put("valuation","121");
                    put("financial","161");
                    put("ownership","131");
                    put("performance","141");
                    put("technical","171");
                }
            };
    LinkedList<AttrNode> attrList = new LinkedList<AttrNode>();
    public String provider;

/*	public QueryDetail() { //default query item
		provider = "finviz";   // Sure??
		attrList.add(new AttrNode(urlStrMap.get("view"), urlStrMap.get("overview")));
	}*/

	public QueryDetail(String provider, String... query){
		this.provider = provider;

		if (query.length % 2 == 1){
			System.out.println("Please input all pairs");
			System.exit(1);
		}
        
		String aName = "";
		String urlValue = "";
        int count = 1;
        
        for (String str : query) {
			urlValue = urlStrMap.get(str);
			if (urlValue == null){
				urlValue = str;
			}
			if (count % 2 == 1) {
				aName = urlValue;
			} else {
				attrList.add(new AttrNode(aName, urlValue));
			}
			count ++;
		}
        System.out.println(attrList.toString());
	}

    public QueryDetail(String[] tag, Object... obj) {
        LinkedList<AttrNode> param = new LinkedList<AttrNode>();

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] instanceof String) {
                param.add(new AttrNode(tag[i], (String)obj[i]));
            }
            else if (obj[i] instanceof Calendar) {
                Calendar cal = (Calendar)obj[i];
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                param.add(new AttrNode(tag[i] + "_day", new Integer(day).toString()));
                param.add(new AttrNode(tag[i] + "_month", new Integer(month).toString()));
                param.add(new AttrNode(tag[i] + "_year", new Integer(year).toString()));
            }

        }

        attrList = param;
    }
//	public enum ViewType
//	{
//	    OVERVIEW(111),
//	    VALUATION(121),
//	    FINANCIAL(161),
//	    OWNERSHIP(131),
//	    PERFORMANCE(141),
//	    TECHNICAL(171);
//
//	    private Integer value;
//	    private ViewType(Integer value) {
//	       this.value = value;
//	    }
//	    public Integer getValue() {
//	       return value;
//	    }
//	}

}