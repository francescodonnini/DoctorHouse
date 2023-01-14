package ispw.uniroma2.doctorhouse.model;

public class Location {
    private final String country;
    private final String province;
    private final String city;
    private final String firstLine;
    private final String secondLine;
    private final String thirdLine;

    public Location(String country, String province, String city, String firstLine, String secondLine, String thirdLine) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.thirdLine = thirdLine;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public String getThirdLine() {
        return thirdLine;
    }
}
