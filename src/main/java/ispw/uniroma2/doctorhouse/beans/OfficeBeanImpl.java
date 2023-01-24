package ispw.uniroma2.doctorhouse.beans;

public class OfficeBeanImpl implements OfficeBean {
    private int id;
    private DoctorBean doctor;
    private String country;
    private String province;
    private String city;
    private String address;

    @Override
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctorBean) {
        this.doctor = doctorBean;
    }
}
