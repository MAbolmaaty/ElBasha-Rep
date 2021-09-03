package codeztalk.elbasha.delegate.aPIS.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddClientRequest extends ClientRequest
{
    @SerializedName("Client_Days")
    private List<ClientDay> mClientDays;


    public List<ClientDay> getClientDays() {
        return mClientDays;
    }

    public void setClientDays(List<ClientDay> mClientDays) {
        this.mClientDays = mClientDays;
    }

    public static class ClientDay {

        @SerializedName("DayIndex")
        private String mDayIndex;


        public ClientDay(String mDayIndex) {
            this.mDayIndex = mDayIndex;
        }

        public String getDayIndex() {
            return mDayIndex;
        }

        public void setDayIndex(String dayIndex) {
            mDayIndex = dayIndex;
        }

    }

}
