package dao;

import dto.Room;
import qlks.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho thực thể Phòng (Room).
 * Quản lý các thao tác CRUD và các nghiệp vụ liên quan đến phòng.
 */
public class RoomDAO {

    /**
     * Thêm một phòng mới vào CSDL.
     * @param room Đối tượng Room chứa thông tin cần thêm.
     * @return true nếu thêm thành công.
     * @throws SQLException
     */
    public boolean addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms(RoomNumber, RoomType, PricePerNight, Status, RoomDescription) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setBigDecimal(3, room.getPricePerNight());
            ps.setString(4, room.getStatus());
            ps.setString(5, room.getRoomDescription());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin của một phòng.
     * @param room Đối tượng Room chứa thông tin cần cập nhật.
     * @return true nếu cập nhật thành công.
     * @throws SQLException
     */
    public boolean updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET RoomNumber = ?, RoomType = ?, PricePerNight = ?, Status = ?, RoomDescription = ? WHERE RoomId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setBigDecimal(3, room.getPricePerNight());
            ps.setString(4, room.getStatus());
            ps.setString(5, room.getRoomDescription());
            ps.setInt(6, room.getRoomId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một phòng khỏi CSDL.
     * @param roomId ID của phòng cần xóa.
     * @return true nếu xóa thành công.
     * @throws SQLException
     */
    public boolean deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE RoomId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy thông tin một phòng bằng ID.
     * @param roomId ID của phòng.
     * @return Đối tượng Room hoặc null nếu không tìm thấy.
     * @throws SQLException
     */
    public Room getRoomById(int roomId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE RoomId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả các phòng.
     * @return List<Room>
     * @throws SQLException
     */
    public List<Room> getAllRooms() throws SQLException {
        List<Room> roomList = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY RoomNumber ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                roomList.add(mapResultSetToRoom(rs));
            }
        }
        return roomList;
    }

    /**
     * Cập nhật chỉ trạng thái của một phòng. 
     * @param roomId ID của phòng
     * @param newStatus Trạng thái mới (Trống, Đã đặt, Đang có khách, Đang dọn dẹp, Bảo trì)
     * @return true nếu cập nhật thành công.
     * @throws SQLException
     */
    public boolean updateRoomStatus(int roomId, String newStatus) throws SQLException {
        String sql = "UPDATE rooms SET Status = ? WHERE RoomId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus);
            ps.setInt(2, roomId);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Helper method để chuyển đổi ResultSet sang đối tượng Room.
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("RoomId"));
        room.setRoomNumber(rs.getString("RoomNumber"));
        room.setRoomType(rs.getString("RoomType"));
        room.setPricePerNight(rs.getBigDecimal("PricePerNight"));
        room.setStatus(rs.getString("Status"));
        room.setRoomDescription(rs.getString("RoomDescription"));
        return room;
    }
}