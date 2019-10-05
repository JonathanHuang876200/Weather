package com.eland.weather;

import com.eland.JsonMethods.*;
import com.eland.JsonMethods.WeatherData;
import com.eland.JsonMethods.Time;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.sql.PreparedStatement;

public class GetJsonString {


    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, ClassNotFoundException, SQLException {

        CloseableHttpClient chc = HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                        .build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        /*取得欲連線的網址*/
        HttpGet httpGet = new HttpGet("https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=rdec-key-123-45678-011121314");
        HttpResponse response = null;
        try {
            response = chc.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            String result = EntityUtils.toString(resEntity);

            Gson gson = new Gson();
            Weather weather = gson.fromJson(result, Weather.class);     /*讀取JSON*/

            Connection con = null;
            String url = "jdbc:sqlserver://localhost:1433;databaseName=WeatherDB";
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String username = "localhost";
            Class.forName(driver);
            con = java.sql.DriverManager.getConnection(url, username, "jh876200");
            Statement st = con.createStatement();
            /*foreach loop 印出結果*/
//            Records rd = weather.getRecords();
//            for (Location l : rd.getLocation()) {
//                System.out.println(l.getLocationName() + ":");
//                for (WeatherElement we : l.getWeatherElement()) {
//                    if (!(we.getElementName().equals("CI"))) {
//                        System.out.println("[" + we.getElementName() + "]");
//                        for (Time t : we.getTime()) {
//                            System.out.println(t.getStartTime() + "(StartTime)");
//                            System.out.println(t.getEndTime() + "(EndTime)");
//                            System.out.println(t.getParameter().getParameterName() + "(Parameter)");
//                        }
//                    }
//                }
//                System.out.println("=============================================");
//            }

            /*foreach loop 放入DB*/
            String locationName = null;
            Map<String, WeatherData> weatherMap = new HashMap<>();
            Records rd = weather.getRecords();
            for (Location l : rd.getLocation()) {

                locationName =  l.getLocationName();
                for (WeatherElement we : l.getWeatherElement()) {
                    if ((we.getElementName().equals("Wx"))) {
                        for (Time t : we.getTime()) {
                            if (weatherMap.containsKey(t.getStartTime())) {
                                weatherMap.get(t.getStartTime()).setWx(t.getParameter().getParameterValue());
                            } else {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setWx(t.getParameter().getParameterValue());
                                weatherData.setStartTime(t.getStartTime());
                                weatherData.setEndTime(t.getEndTime());
                                weatherMap.put(t.getStartTime(), weatherData);
                            }
                        }
                    }
                    if ((we.getElementName().equals("PoP"))) {
                        for (Time t : we.getTime()) {
                            if (weatherMap.containsKey(t.getStartTime())) {
                                weatherMap.get(t.getStartTime()).setPop(t.getParameter().getParameterName());
                            } else {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setPop(t.getParameter().getParameterName());
                                weatherData.setStartTime(t.getStartTime());
                                weatherData.setEndTime(t.getEndTime());
                                weatherMap.put(t.getStartTime(), weatherData);
                            }
                        }
                    }
                    if ((we.getElementName().equals("MinT"))) {
                        for (Time t : we.getTime()) {
                            if (weatherMap.containsKey(t.getStartTime())) {
                                weatherMap.get(t.getStartTime()).setMinT(t.getParameter().getParameterName());
                            } else {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setMinT(t.getParameter().getParameterName());
                                weatherData.setStartTime(t.getStartTime());
                                weatherData.setEndTime(t.getEndTime());
                                weatherMap.put(t.getStartTime(), weatherData);
                            }
                        }
                    }
                    if ((we.getElementName().equals("MaxT"))) {
                        for (Time t : we.getTime()) {
                            if (weatherMap.containsKey(t.getStartTime())) {
                                weatherMap.get(t.getStartTime()).setMaxT(t.getParameter().getParameterName());
                            } else {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setMaxT(t.getParameter().getParameterName());
                                weatherData.setStartTime(t.getStartTime());
                                weatherData.setEndTime(t.getEndTime());
                                weatherMap.put(t.getStartTime(), weatherData);
                            }
                        }
                    }
                }
                for (String key:weatherMap.keySet()){
                    System.out.println(key);
                    PreparedStatement ps = con.prepareStatement("INSERT INTO weather_records (locationName,startTime,endTime,Wx,PoP,MinT,MaxT) VALUES(?, ?, ?, ?, ?, ?, ?);");
                    ps.setString(1,locationName);
                    ps.setString(2,weatherMap.get(key).getStartTime());
                    ps.setString(3,weatherMap.get(key).getEndTime());
                    ps.setString(4,weatherMap.get(key).getWx());
                    ps.setString(5,weatherMap.get(key).getPop());
                    ps.setString(6,weatherMap.get(key).getMinT());
                    ps.setString(7,weatherMap.get(key).getMaxT());
                    ps.execute();
                }
            }
//                String s1 = weather.getRecords().getLocation().get(0).getWeatherElement().get(1).getTime().get(0).getStartTime();
//                String s2 = weather.getRecords().getLocation().get(0).getWeatherElement().get(1).getTime().get(1).getStartTime();
//                String s3 = weather.getRecords().getLocation().get(0).getWeatherElement().get(1).getTime().get(2).getStartTime();
//                String e1 = "'" + weather.getRecords().getLocation().get(0).getWeatherElement().get(1).getTime().get(0).getEndTime() + "'";
//                String e2 = "'" + weather.getRecords().getLocation().get(0).getWeatherElement().get(1).getTime().get(1).getEndTime() + "'";
//                String e3 = "'" + weather.getRecords().getLocation().get(0).getWeatherElement().get(1).getTime().get(2).getEndTime() + "'";
//
//                String query = "insert into weather_records " + "(locationName,startTime,endTime,Wx,PoP,MinT,MaxT)" + " values(" + locationName + "," + "'" + s1 + "'" + "," + e1 + "," + weatherMap.get(s1).getWx() + "," + weatherMap.get(s1).getPop() + "," + weatherMap.get(s1).getMinT() + "," + weatherMap.get(s1).getMaxT() + ")";
//                st.execute(query);
//                String query1 = "insert into weather_records " + "(locationName,startTime,endTime,Wx,PoP,MinT,MaxT)" + " values(" + locationName + "," + "'" + s2 + "'" + "," + e2 + "," + weatherMap.get(s2).getWx() + "," + weatherMap.get(s2).getPop() + "," + weatherMap.get(s2).getMinT() + "," + weatherMap.get(s2).getMaxT() + ")";
//                st.execute(query1);
//                String query2 = "insert into weather_records " + "(locationName,startTime,endTime,Wx,PoP,MinT,MaxT)" + " values(" + locationName + "," + "'" + s3 + "'" + "," + e3 + "," + weatherMap.get(s3).getWx() + "," + weatherMap.get(s3).getPop() + "," + weatherMap.get(s3).getMinT() + "," + weatherMap.get(s3).getMaxT() + ")";
//                st.execute(query2);

//                2019-08-14 18:00:00
//                2019-08-15 06:00:00
//                2019-08-15 18:00:00
//            }
            /*用很爛的方法爆開塞到DB*/
//            for (int i = 0; i <= 21; i = i + 1) {
//                for (int j = 0; j <= 2; j++) {
//                    String locationName = " ' " + rd.getLocation().get(i).getLocationName() + " ' ";
//                    String startTime = " ' " + rd.getLocation().get(i).getWeatherElement()    .get(0).getTime().get(j).getStartTime() + " ' ";
//                    String endTime = " ' " + rd.getLocation().get(i).getWeatherElement().get(0).getTime().get(j).getEndTime() + " ' ";
//                    String Wx = " ' " + rd.getLocation().get(i).getWeatherElement().get(0).getTime().get(j).getParameter().getParameterName() + " ' ";
//                    String PoP = " ' " + rd.getLocation().get(i).getWeatherElement().get(1).getTime().get(j).getParameter().getParameterName() + "%" + " ' ";
//                    String MinT = " ' " + rd.getLocation().get(i).getWeatherElement().get(2).getTime().get(j).getParameter().getParameterName() + "*C" + " ' ";
//                    String MaxT = " ' " + rd.getLocation().get(i).getWeatherElement().get(4).getTime().get(j).getParameter().getParameterName() + "*C" + " ' ";
//                    String query = "insert into weather_records " + "(locationName,startTime,endTime,Wx,PoP,MinT,MaxT)" + " values(" + locationName + "," + startTime + "," + endTime + "," + Wx + "," + PoP + "," + MinT + "," + MaxT + ")";
//                    st.execute(query);
//                }
//            }
            /*用很爛的方法印出*/
//            for (int i = 0; i <= 21; i = i + 1) {
//                System.out.println("[地名]" + "      " + "[開始時間]" + "          " + "[結束時間]" + "            " + "[降雨機率]" + "  " + "[最低溫度]" + "  " + "[最高溫度]" + "  " + "[天氣現象]");
//                for (int j = 0; j <= 2; j++) {
//                    System.out.println(weather.getRecords().getLocation().get(i).getLocationName() + "時段" + (j + 1) + " "
//                            + weather.getRecords().getLocation().get(i).getWeatherElement().get(0).getTime().get(j).getStartTime() + " "/*+ " (各地三個startTime)"*/
//                            + weather.getRecords().getLocation().get(i).getWeatherElement().get(0).getTime().get(j).getEndTime() + "   "/*+ " (各地三個endTime)")*/
//                            + weather.getRecords().getLocation().get(i).getWeatherElement().get(1).getTime().get(j).getParameter().getParameterName() + "% " + "        "/*+ " (各地三個parameterName:降雨機率)"*/
//                            + weather.getRecords().getLocation().get(i).getWeatherElement().get(2).getTime().get(j).getParameter().getParameterName() + "*C" + "        "/*+ " (各地三個parameterName:最低溫度)"*/
//                            + weather.getRecords().getLocation().get(i).getWeatherElement().get(4).getTime().get(j).getParameter().getParameterName() + "*C" + "        "/*+ " (各地三個parameterName:最高溫度)"*/
//                            + weather.getRecords().getLocation().get(i).getWeatherElement().get(0).getTime().get(j).getParameter().getParameterName());/*+ " (各地三個parameterName:天氣現象)" */
//                }
//                System.out.println();
//                System.out.println("==============================================================================================================");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
