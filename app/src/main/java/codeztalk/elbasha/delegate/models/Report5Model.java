
package codeztalk.elbasha.delegate.models;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Report5Model {

    @Expose
    private String $id;
    @SerializedName("Balance")
    private Double balance;
    @SerializedName("ClientName")
    private String clientName;
    @SerializedName("EmpName")
    private String empName;
    @SerializedName("InvoiceTaxNumber")
    private String invoiceTaxNumber;
    @SerializedName("OfficialUnitPrice")
    private Double officialUnitPrice;
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("ProductName_AR")
    private String productNameAR;
    @SerializedName("ProductName_EN")
    private String productNameEN;
    @SerializedName("UnitPrice")
    private Double unitPrice;
    @SerializedName("UnitQTY")
    private int unitQTY;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getInvoiceTaxNumber() {
        return invoiceTaxNumber;
    }

    public void setInvoiceTaxNumber(String invoiceTaxNumber) {
        this.invoiceTaxNumber = invoiceTaxNumber;
    }

    public Double getOfficialUnitPrice() {
        return officialUnitPrice;
    }

    public void setOfficialUnitPrice(Double officialUnitPrice) {
        this.officialUnitPrice = officialUnitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductNameAR() {
        return productNameAR;
    }

    public void setProductNameAR(String productNameAR) {
        this.productNameAR = productNameAR;
    }

    public String getProductNameEN() {
        return productNameEN;
    }

    public void setProductNameEN(String productNameEN) {
        this.productNameEN = productNameEN;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitQTY() {
        return unitQTY;
    }

    public void setUnitQTY(int unitQTY) {
        this.unitQTY = unitQTY;
    }

}
