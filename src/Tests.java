import helpers.CSVwork;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class Tests {

    public static void main(String[] args) throws IOException {
        String urlString = "http://export.finam.ru/POLY_170620_170623.txt?market=1&em=175924&code=POLY&apply=0&df=1&mf=8&yf=2019&from=1.9.2019&dt=01&" +
                "mt=11&yt=2019&to=01.12.2019&p=8&f=POLY_170620_170623&e=.txt&cn=POLY&dtf=1&tmf=1&" +
                "MSOR=1&mstime=on&mstimever=1&sep=3&sep2=1&datf=1&at=1";

        URLConnection conn = new URL(urlString).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        conn.connect();
        URL url = new URL(urlString);


// подготовили коннект

// Т.к. запрос у нас GET, то сразу принимаем входящие данные.
// Вот тут как раз (при открытии InputStream ) и происходит отправка GET запроса на сервер.
        InputStream inStream = conn.getInputStream();
// Т.к. файл у нас бинарный, открываем ReadableByteChannel и создаем файл
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileOutputStream fos = new FileOutputStream("input.csv");

// Перенаправляем данные из ReadableByteChannel прямо канал файла.
// Говорят, так быстрее, чем по одному байту вычитывать из потока и писать в файл.
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