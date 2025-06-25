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

public class Room {
    private int roomId;
    private String roomNumber;
    private String roomType;
    private BigDecimal pricePerNight;
    private String status; // Trống, Đã đặt, Đang có khách, Đang dọn dẹp, Bảo trì 
    private String roomDescription;

    // Constructors, Getters and Setters
    public Room() {}
    
    // ... (Tự động tạo bằng IDE)

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }
    @Override
public String toString() {
    return roomNumber + " - " + roomType; // Ví dụ: 101 - Deluxe
}
}