package finData;

public class FinvizServiceQuery extends GenericServiceQuery {

	public FinvizServiceQuery(QueryDetail query) {
		super(query);
	}
	public String connstrConstructor(QueryDetail query) {
		String cstr = "http://finviz.com/export.ashx?";

        for (AttrNode node : query.attrList) {
            cstr += node.getName() + "=" + node.getValue() + "&";
        }

		return cstr.substring(0, cstr.length() - 1);
	}
}
