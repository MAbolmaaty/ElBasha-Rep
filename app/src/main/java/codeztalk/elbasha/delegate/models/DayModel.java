package codeztalk.elbasha.delegate.models;

public class DayModel
{

    int id;
    String dayEN;
    String dayAR;
    boolean isSelected;

    public DayModel(int id, String dayEN, String dayAR, boolean isSelected) {
        this.id = id;
        this.dayEN = dayEN;
        this.dayAR = dayAR;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayEN() {
        return dayEN;
    }

    public void setDayEN(String dayEN) {
        this.dayEN = dayEN;
    }

    public String getDayAR() {
        return dayAR;
    }

    public void setDayAR(String dayAR) {
        this.dayAR = dayAR;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
