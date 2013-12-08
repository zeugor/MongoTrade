package FinDAO;

import Util.NumericalUtil;
import com.mongodb.*;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 en Xiliang
 * Date: 23/10/13
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MongoDB {
    static private boolean MONGO_LOCAL = false;

    static final String DEFAULT_DB = "FinData";
    static final String DEFAULT_HOST = "localhost";
    static final int DEFAULT_PORT = 27017;

    private DB database;

    public MongoDB () throws UnknownHostException {
        if (MONGO_LOCAL) {
            // set the mongodb port #port = 27017 in /etc/mongodb.conf
            MongoClient client = new MongoClient(new ServerAddress(DEFAULT_HOST, DEFAULT_PORT)) ;
            database = client.getDB(DEFAULT_DB);
        } else {
            String textUri = "mongodb://admin:pass@ds057568.mongolab.com:57568/mongo_trade";
            MongoClientURI uri = new MongoClientURI(textUri);
            MongoClient client  = new MongoClient(uri);
            database = client.getDB("mongo_trade");
        }
    }

    public DB GetDBConn() {
        return database;
    }

    public void InsertYahooData (List<String> YahooReturn, String Ticker){
        List<DBObject> YahooData = new LinkedList<DBObject>();
        YahooData = ConstructYahooObj(YahooReturn, Ticker);
        DBCollection YahooHist = database.getCollection("YahooHistory");
        //separate year/month/day
        for (DBObject DataItem: YahooData){
            //DBObject IDQuery = new BasicDBObject("Ticker",Ticker);
            //YahooHist.update(IDQuery,DataItem,true,false);
            YahooHist.insert(DataItem);
        }
    }

    public void insertYahooData(List<String> yahooReturn, String ticker) {
        DateFormat DatePart = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat TimePart = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String dateStr = DatePart.format(now);
        String timeStr = TimePart.format(now);

        //List<DBObject> yahooData = ConstructFinvizObj(yahooReturn);

        DBCollection yahooHist = database.getCollection("YahooHistory");
        //DBCollection yahooRealtime = database.getCollection("YahooRealtime");

/*        for (DBObject dataItem: yahooData){
            DBObject setVal = (DBObject)dataItem.get("$set");
            DBObject IDQuery = new BasicDBObject("_id",setVal.get("_id"));
            setVal.removeField("_id");
            dataItem.put("$set", setVal);
            yahooRealtime.update(IDQuery, dataItem, true, false);
        }*/

        List<DBObject> yahooHistData =  ConstructYahooObj(yahooReturn, ticker);

        for (DBObject dataItem: yahooHistData){

            DBObject valueSet = (DBObject)dataItem.get("$set");
            // I should use a timestamp
            DBObject idObj = new BasicDBObject("date", valueSet.get("_id"));
            idObj.put("ticker", ticker);
            DBObject idQuery = new BasicDBObject("_id", idObj);
            valueSet.put("_id", idObj);

            System.out.println(valueSet);

            yahooHist.update(idQuery, valueSet, true, false);
        }

    }

    //perform ETL
    //keep price as List, update using push price to the day's finviz data
    //however if price change substantially, re-download all the data column
    public void InsertFinvizData (List<String> FinvizReturn){
        //construct map
        Date now = new Date();
        DateFormat DatePart = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat TimePart = new SimpleDateFormat("HH:mm:ss");
        String datestr = DatePart.format(now);
        String timestr = TimePart.format(now);

        List<DBObject> FinvizData = new LinkedList<DBObject>() ;
        FinvizData = ConstructFinvizObj(FinvizReturn);


        DBCollection FinvizHist = database.getCollection("FinvizHistory");
        DBCollection FinvizRealtime = database.getCollection("FinvizRealtime");

        for (DBObject DataItem: FinvizData){
            DBObject setVal = (DBObject)DataItem.get("$set");
            DBObject IDQuery = new BasicDBObject("_id",setVal.get("_id"));
            setVal.removeField("_id");
            DataItem.put("$set", setVal);
            FinvizRealtime.update(IDQuery, DataItem, true, false);
        }

        List<DBObject> FinvizHistData = new LinkedList<DBObject>();
        FinvizHistData = ConstructFinvizObj(FinvizReturn);

        //add time tag to id and ticker column for hist table
        //add subdoc of map time:time, price:price

        for (DBObject DataItem: FinvizHistData){
            DBObject ValueSet = (DBObject)DataItem.get("$set");
            DBObject IDQuery = new BasicDBObject("_id",ValueSet.get("_id")+":"+datestr);
            BasicDBObject PriceData = new BasicDBObject("time",timestr).append("Price",ValueSet.get("Price"));
            DataItem.put("$push",new BasicDBObject("Price",PriceData));
            ValueSet.put("Ticker", ValueSet.get("_id"));
            ValueSet.removeField("_id");
            ValueSet.removeField("Price");
            DataItem.put("$set",ValueSet);
            FinvizHist.update(IDQuery,DataItem,true,false);
        }
        //FinvizHist.insert(IDQuery,DataItem,true,false);
        //for (BasicDBObject DataItem : FinvizData) {

        //}
    }

    /*
    I should modify the _id object, like { date, value}
     */
    private List<DBObject> ConstructYahooObj(List<String> yahooReturn, String Ticker) {
        LinkedList<DBObject> mongoObjList = new LinkedList<DBObject>();
        int headerFlag = 0;
        String[] headers = new String[]{""};
        for (String yahooValues : yahooReturn){
            yahooValues = yahooValues.replaceAll("\"","");
            if (headerFlag == 0){
                //extract headers
                headers = yahooValues.split(",");
                headerFlag++;
                headers[0] = "_id";
            }else {
                BasicDBObject yahooEntry = new BasicDBObject();
                String[] yahooRec = yahooValues.split(",");
                int counter = 0;
                for (String header : headers){
                    if (true) {//if (counter >= 1){     //ignore finviz table id
                        if (yahooRec[counter] instanceof String) {
                            yahooEntry.put(header, yahooRec[counter]);
                        }
                        else if(NumericalUtil.isDouble(yahooRec[counter])){
                            yahooEntry.put(header,Double.parseDouble(yahooRec[counter]));
                        }
                        else{
                            yahooEntry.put(header, yahooRec[counter]);
                        }

                    }
                    counter ++;
                }
                mongoObjList.add(new BasicDBObject("$set", yahooEntry));
            }


        }

        return mongoObjList;
    }
/*
        private List<DBObject> ConstructYahooObj(List<String> finvizReturn, String Ticker) {
        LinkedList<DBObject> MongoObjList = new LinkedList<DBObject>();
        int headerFlag = 0;
        String[] headers = new String[]{""};
        for (String finvizValues : finvizReturn){
            if (headerFlag == 0){
                //extract headers
                headers = finvizValues.split(",");
                headerFlag++;
              }else {
                BasicDBObject YahooEntry = new BasicDBObject();
                String[] YahooRec = finvizValues.split(",");
                int counter = 0;
                for (String header : headers){
                    if(NumericalUtil.isDouble(YahooRec[counter])){
                        YahooEntry.put(header,Double.parseDouble(YahooRec[counter]));
                    }else{
                        YahooEntry.put(header,YahooRec[counter]);
                    }
                    counter ++;
                }
                YahooEntry.put("Ticker",Ticker);
                //MongoObjList.add(new BasicDBObject("$set",YahooEntry));
                MongoObjList.add(YahooEntry);
            }


        }

        return MongoObjList;
    }
    */

    private List<DBObject> ConstructFinvizObj(List<String> finvizReturn) {
        LinkedList<DBObject> MongoObjList = new LinkedList<DBObject>();
        int headerFlag = 0;
        String[] headers = new String[]{""};
        for (String finvizValues : finvizReturn){
            finvizValues = finvizValues.replaceAll("\"","");
            if (headerFlag == 0){
                //extract headers
                headers = finvizValues.split(",");
                headerFlag++;
                headers[1] = "_id";
            }else {
                BasicDBObject FinvizEntry = new BasicDBObject();
                String[] FinvizRec = finvizValues.split(",");
                int counter = 0;
                for (String header : headers){
                    if (counter >= 1){     //ignore finviz table id
                        if(NumericalUtil.isDouble(FinvizRec[counter])){
                            FinvizEntry.put(header,Double.parseDouble(FinvizRec[counter]));
                        }else{
                            FinvizEntry.put(header,FinvizRec[counter]);
                        }

                    }
                    counter ++;
                }
                MongoObjList.add(new BasicDBObject("$set", FinvizEntry));
            }


        }

        return MongoObjList;
    }


    private List<DBObject> ConstructMongoQuery(List<String> finvizReturn) {

        LinkedList<DBObject> MongoQueryList = new LinkedList<DBObject>();
        int headerFlag = 0;
        String[] headers = new String[]{""};
        for (String finvizValues : finvizReturn){

            if (headerFlag == 0){
                //extract headers
                headers = finvizReturn.get(0).split(",");
                headerFlag++;
                headers[0] = "_id";
            }else {
                //use stock ticker as ID
                BasicDBObject FinvizQuery = new BasicDBObject();
                String[] FinvizRec = finvizValues.split(",");
                FinvizQuery.put(headers[1],FinvizRec[1]);
                MongoQueryList.add(FinvizQuery);
            }
        }

        return MongoQueryList;
    }


}
