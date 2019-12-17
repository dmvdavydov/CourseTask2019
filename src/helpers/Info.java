package helpers;

public class Info {
    private int day;
    private int month;
    private int year;
    private String date;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Override
    public String toString() {
        return "Info{" +
                "date='" + date + '\'' +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", closePrice=" + closePrice +
                '}';
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    private static final String SEPARATOR_1 = ";";

    public Info(String string) {
        String[] strArray = string.split(SEPARATOR_1);
        date = strArray[0];
        openPrice = Double.parseDouble(strArray[2]);
        highPrice = Double.parseDouble(strArray[3]);
        lowPrice = Double.parseDouble(strArray[4]);
        closePrice = Double.parseDouble(strArray[5]);

        day = Integer.parseInt(date.substring(0, 2));
        month = Integer.parseInt(date.substring(2, 4));
        year = Integer.parseInt("20" + date.substring(4, 6));
    }

    public Info() {
    }

    ;
}
