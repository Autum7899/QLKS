/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
package dto;

import java.math.BigDecimal;
import java.util.Date;

public class Booking {
    private int bookingId;
    private int roomId;
    private int customerId;
    private Date checkInDate;
    private Date checkOutDate;
    private Date actualCheckInDate;
    private Date actualCheckOutDate;
    private Date bookingDate;
    private String status;
    private String paymentStatus;
    private String notes;

    // Constructors, Getters and Setters
    public Booking() {}
    
    // ... (Tự động tạo bằng IDE)

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Date getActualCheckInDate() {
        return actualCheckInDate;
    }

    public void setActualCheckInDate(Date actualCheckInDate) {
        this.actualCheckInDate = actualCheckInDate;
    }

    public Date getActualCheckOutDate() {
        return actualCheckOutDate;
    }

    public void setActualCheckOutDate(Date actualCheckOutDate) {
        this.actualCheckOutDate = actualCheckOutDate;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}