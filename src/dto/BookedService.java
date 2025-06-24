package dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object cho bảng 'bookedservices'.
 * Đại diện cho một dịch vụ cụ thể đã được đặt trong một lần booking.
 */
public class BookedService {
    private int bookedServiceId;
    private int bookingId;
    private int serviceId;
    private int quantity;
    private Date serviceDate;
    private BigDecimal priceAtBooking;

    public BookedService() {
    }

    // Getters and Setters
    public int getBookedServiceId() { return bookedServiceId; }
    public void setBookedServiceId(int bookedServiceId) { this.bookedServiceId = bookedServiceId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getServiceDate() { return serviceDate; }
    public void setServiceDate(Date serviceDate) { this.serviceDate = serviceDate; }

    public BigDecimal getPriceAtBooking() { return priceAtBooking; }
    public void setPriceAtBooking(BigDecimal priceAtBooking) { this.priceAtBooking = priceAtBooking; }
}