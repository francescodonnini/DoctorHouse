package ispw.uniroma2.doctorhouse.model;

public class Location {
    private final String country;
    private final String province;
    private final String city;
    private final String address;

    public Location(String country, String province, String city, String address) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

}
