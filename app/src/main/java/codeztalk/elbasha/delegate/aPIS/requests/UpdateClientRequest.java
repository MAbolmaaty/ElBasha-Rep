package codeztalk.elbasha.delegate.aPIS.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateClientRequest extends ClientRequest
{
    @SerializedName("Client_Days")
    private List<UpdateClientDay> mClientDays;

    public List<UpdateClientDay> getmClientDays() {
        return mClientDays;
    }

    public void setmClientDays(List<UpdateClientDay> mClientDays) {
        this.mClientDays = mClientDays;
    }

    public static class UpdateClientDay {

        @SerializedName("DayIndex")
        private String mDayIndex;

        @SerializedName("Client_Id")
        private String mClientId;

        public UpdateClientDay(String mDayIndex,String mClientId) {
            this.mDayIndex = mDayIndex;
            this.mClientId = mClientId;
        }

        public String getDayIndex() {
            return mDayIndex;
        }

        public void setDayIndex(String dayIndex) {
            mDayIndex = dayIndex;
        }

        public String getClientId() {
            return mClientId;
        }

        public void setClientId(String clientId) {
            mClientId = clientId;
        }


    }


}
