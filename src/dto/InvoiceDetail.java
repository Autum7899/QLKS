package dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object cho bảng 'invoicedetails'.
 * Đại diện cho một dòng chi tiết trong hóa đơn (ví dụ: tiền phòng, tiền giặt ủi...).
 */
public class InvoiceDetail {
    private int invoiceDetailId;
    private int invoiceId;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;

    public InvoiceDetail() {
    }

    // Getters and Setters
    public int getInvoiceDetailId() { return invoiceDetailId; }
    public void setInvoiceDetailId(int invoiceDetailId) { this.invoiceDetailId = invoiceDetailId; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}