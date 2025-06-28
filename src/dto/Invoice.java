/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author Admin
 */

import java.math.BigDecimal;
import java.util.Date;

public class Invoice {
    private int invoiceId;
    private int bookingId;
    private int customerId;
    private Date issueDate;
    private BigDecimal totalRoomCharge;
    private BigDecimal totalServiceCharge;
    private BigDecimal discount;
    private BigDecimal vat;
    private BigDecimal grandTotal;
    private String paymentMethod;
    private Date paymentDate;
    private int issuedByUserId;
    
    // Constructors, Getters and Setters
    public Invoice() {}
    
    // ... (Tự động tạo bằng IDE)

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getTotalRoomCharge() {
        return totalRoomCharge;
    }

    public void setTotalRoomCharge(BigDecimal totalRoomCharge) {
        this.totalRoomCharge = totalRoomCharge;
    }

    public BigDecimal getTotalServiceCharge() {
        return totalServiceCharge;
    }

    public void setTotalServiceCharge(BigDecimal totalServiceCharge) {
        this.totalServiceCharge = totalServiceCharge;
    }
    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getIssuedByUserId() {
        return issuedByUserId;
    }
    public void setIssuedByUserId(int issuedByUserId) {
        this.issuedByUserId = issuedByUserId;
    }
}