package helpers;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class CSVwork {
    private static final String PATH = "input.csv";

    public static ArrayList<Info> parseCSV() {
        ArrayList<Info> arrayList = new ArrayList();
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH));
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                arrayList.add(new Info(line));
            }
        } catch (IOException ex) {
            System.out.println("??? ");
        }
        return arrayList;
    }

    public static void downloadCSV(String from, String to) throws IOException {
        String[] fromArr = from.split("-");
        String[] toArr = to.split("-");


        String urlString = "http://export.finam.ru/GDAX.BTC-USD_191114_191214.csv?market=520&em=484429&code=GDAX.BTC-USD&" +
                "apply=0&df=" + Integer.parseInt(fromArr[2]) + "&mf=" + (Integer.parseInt(fromArr[1]) - 1) + "&yf=" + fromArr[0] + "&from=" + fromArr[2] + "." + fromArr[1] +
                "." + fromArr[0] + "&dt=" + Integer.parseInt(toArr[2]) +
                "&mt=" + (Integer.parseInt(toArr[1]) - 1) + "&yt=" + toArr[0] +
                "&to=" + toArr[2] + "." + toArr[1] + "." + toArr[0] + "&p=8&f=GDAX.BTC-USD_" +
                (Integer.parseInt(fromArr[0]) - 2000) + fromArr[1] + fromArr[2] + "_" + (Integer.parseInt(toArr[0]) - 2000) + toArr[1] + toArr[2] +
                "&e=.csv&" +
                "cn=GDAX.BTC-USD&dtf=3&tmf=1&MSOR=1&mstime=on&mstimever=1&sep=3&sep2=1&datf=5&at=1";
        System.out.println(urlString);
        URLConnection conn = new URL(urlString).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        conn.connect();
        URL url = new URL(urlString);

        InputStream inStream = conn.getInputStream();
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileOutputStream fos = new FileOutputStream("input.csv");

        long filePosition = 0;
        long transferedBytes = fos.getChannel().transferFrom(rbc, filePosition, Long.MAX_VALUE);

        while (transferedBytes == Long.MAX_VALUE) {
            filePosition += transferedBytes;
            transferedBytes = fos.getChannel().transferFrom(rbc, filePosition, Long.MAX_VALUE);
        }
        rbc.close();
        fos.close();
    }
}
