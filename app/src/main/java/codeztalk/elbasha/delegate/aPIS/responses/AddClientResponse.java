package codeztalk.elbasha.delegate.aPIS.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddClientResponse
{
    @SerializedName("$id")
    private String m$id;
    @SerializedName("Address")
    private Object mAddress;
    @SerializedName("Client_Days")
    private List<ClientDay> mClientDays;
    @SerializedName("ClientName")
    private String mClientName;
    @SerializedName("ClientRecipts")
    private List<Object> mClientRecipts;
    @SerializedName("ClientsDebits")
    private List<Object> mClientsDebits;
    @SerializedName("Created_Date")
    private String mCreatedDate;
    @SerializedName("DelgateMan")
    private String mDelgateMan;
    @SerializedName("Employee")
    private Object mEmployee;
    @SerializedName("EmployeeId")
    private Long mEmployeeId;
    @SerializedName("GPSLocation")
    private String mGPSLocation;
    @SerializedName("Id")
    private Long mId;
    @SerializedName("IsMaster")
    private Boolean mIsMaster;
    @SerializedName("Mobile1")
    private String mMobile1;
    @SerializedName("Mobile2")
    private String mMobile2;
    @SerializedName("Transactions")
    private List<Object> mTransactions;

    public String get$id() {
        return m$id;
    }

    public void set$id(String $id) {
        m$id = $id;
    }

    public Object getAddress() {
        return mAddress;
    }

    public void setAddress(Object address) {
        mAddress = address;
    }

    public List<ClientDay> getClientDays() {
        return mClientDays;
    }

    public void setClientDays(List<ClientDay> clientDays) {
        mClientDays = clientDays;
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String clientName) {
        mClientName = clientName;
    }

    public List<Object> getClientRecipts() {
        return mClientRecipts;
    }

    public void setClientRecipts(List<Object> clientRecipts) {
        mClientRecipts = clientRecipts;
    }

    public List<Object> getClientsDebits() {
        return mClientsDebits;
    }

    public void setClientsDebits(List<Object> clientsDebits) {
        mClientsDebits = clientsDebits;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getDelgateMan() {
        return mDelgateMan;
    }

    public void setDelgateMan(String delgateMan) {
        mDelgateMan = delgateMan;
    }

    public Object getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Object employee) {
        mEmployee = employee;
    }

    public Long getEmployeeId() {
        return mEmployeeId;
    }

    public void setEmployeeId(Long employeeId) {
        mEmployeeId = employeeId;
    }

    public String getGPSLocation() {
        return mGPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        mGPSLocation = gPSLocation;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Boolean getIsMaster() {
        return mIsMaster;
    }

    public void setIsMaster(Boolean isMaster) {
        mIsMaster = isMaster;
    }

    public String getMobile1() {
        return mMobile1;
    }

    public void setMobile1(String mobile1) {
        mMobile1 = mobile1;
    }

    public String getMobile2() {
        return mMobile2;
    }

    public void setMobile2(String mobile2) {
        mMobile2 = mobile2;
    }

    public List<Object> getTransactions() {
        return mTransactions;
    }

    public void setTransactions(List<Object> transactions) {
        mTransactions = transactions;
    }


    public static class ClientDay {

        @SerializedName("$id")
        private String m$id;
        @SerializedName("Client")
        private Client mClient;
        @SerializedName("Client_Id")
        private Long mClientId;
        @SerializedName("DayIndex")
        private Long mDayIndex;
        @SerializedName("Id")
        private Long mId;

        public String get$id() {
            return m$id;
        }

        public void set$id(String $id) {
            m$id = $id;
        }

        public Client getClient() {
            return mClient;
        }

        public void setClient(Client client) {
            mClient = client;
        }

        public Long getClientId() {
            return mClientId;
        }

        public void setClientId(Long clientId) {
            mClientId = clientId;
        }

        public Long getDayIndex() {
            return mDayIndex;
        }

        public void setDayIndex(Long dayIndex) {
            mDayIndex = dayIndex;
        }

        public Long getId() {
            return mId;
        }

        public void setId(Long id) {
            mId = id;
        }

        public class Client {

            @SerializedName("$ref")
            private String m$ref;

            public String get$ref() {
                return m$ref;
            }

            public void set$ref(String $ref) {
                m$ref = $ref;
            }

        }

    }


}
