
package codeztalk.elbasha.delegate.aPIS.requests;

 import com.google.gson.annotations.SerializedName;


public class Report1Request extends ReportRequest{

    @SerializedName("ClientId")
    private String clientId;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }



}
