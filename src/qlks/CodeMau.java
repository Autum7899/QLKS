//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//// =================================================================================
//// LỚP KẾT NỐI CƠ SỞ DỮ LIỆU
//// =================================================================================
//
///**
// * Lớp tiện ích để quản lý kết nối đến cơ sở dữ liệu MySQL.
// */
//class DatabaseConnection {
//    private static final String URL = "jdbc:mysql://localhost:3306/qlks_db"; // Thay đổi qlks_db nếu cần
//    private static final String USER = "root";
//    private static final String PASSWORD = "your_password_here"; // THAY ĐỔI MẬT KHẨU CỦA BẠN
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//}
//
//// =================================================================================
//// CÁC LỚP MODEL (DATA TRANSFER OBJECTS - DTO)
//// =================================================================================
//
//class User {
//    private int userId;
//    private String username;
//    private String fullName;
//    private String email;
//    private String phoneNumber;
//    private String role;
//    
//    // Getters and Setters
//    public int getUserId() { return userId; }
//    public void setUserId(int userId) { this.userId = userId; }
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//    public String getFullName() { return fullName; }
//    public void setFullName(String fullName) { this.fullName = fullName; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getPhoneNumber() { return phoneNumber; }
//    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
//    public String getRole() { return role; }
//    public void setRole(String role) { this.role = role; }
//}
//
//class Customer {
//    private int customerId;
//    private String fullName;
//    private String idCardNumber;
//    private String phoneNumber;
//    private String address;
//    private String email;
//
//    // Getters and Setters
//    public int getCustomerId() { return customerId; }
//    public void setCustomerId(int customerId) { this.customerId = customerId; }
//    public String getFullName() { return fullName; }
//    public void setFullName(String fullName) { this.fullName = fullName; }
//    public String getIdCardNumber() { return idCardNumber; }
//    public void setIdCardNumber(String idCardNumber) { this.idCardNumber = idCardNumber; }
//    public String getPhoneNumber() { return phoneNumber; }
//    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
//    public String getAddress() { return address; }
//    public void setAddress(String address) { this.address = address; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//}
//
//class Room {
//    private int roomId;
//    private String roomNumber;
//    private String roomType;
//    private double pricePerNight;
//    private String status;
//    private String description;
//
//    // Getters and Setters
//    public int getRoomId() { return roomId; }
//    public void setRoomId(int roomId) { this.roomId = roomId; }
//    public String getRoomNumber() { return roomNumber; }
//    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
//    public String getRoomType() { return roomType; }
//    public void setRoomType(String roomType) { this.roomType = roomType; }
//    public double getPricePerNight() { return pricePerNight; }
//    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//}
//
//class Service {
//    private int serviceId;
//    private String serviceName;
//    private double price;
//    private String description;
//
//    // Getters and Setters
//    public int getServiceId() { return serviceId; }
//    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
//    public String getServiceName() { return serviceName; }
//    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
//    public double getPrice() { return price; }
//    public void setPrice(double price) { this.price = price; }
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//}
//
///**
// * Lớp model để chứa dữ liệu báo cáo doanh thu có cấu trúc.
// */
//class RevenueRecord {
//    private String period; // e.g., "2025-07-12", "Tháng 7/2025", "Năm 2025"
//    private double totalRevenue;
//
//    public RevenueRecord(String period, double totalRevenue) {
//        this.period = period;
//        this.totalRevenue = totalRevenue;
//    }
//
//    // Getters
//    public String getPeriod() { return period; }
//    public double getTotalRevenue() { return totalRevenue; }
//
//    @Override
//    public String toString() {
//        return String.format("Kỳ: %s - Doanh thu: %.2f", period, totalRevenue);
//    }
//}
//
//
//// =================================================================================
//// GENERIC CRUD INTERFACE
//// =================================================================================
//
///**
// * Một interface chung cho các thao tác CRUD (Create, Read, Update, Delete).
// * @param <T> Kiểu đối tượng model (User, Room, etc.)
// */
//interface CrudService<T> {
//    T getById(int id);
//    List<T> getAll();
//    boolean add(T item);
//    boolean update(T item);
//    boolean delete(int id);
//}
//
//// =================================================================================
//// CÁC LỚP DỊCH VỤ (SERVICES) - CHỨA LOGIC NGHIỆP VỤ
//// =================================================================================
//
///**
// * Lớp chứa các chức năng liên quan đến người dùng và phân quyền.
// */
//class UserService {
//
//    public User login(String username, String plainPassword) {
//        String sql = "SELECT UserId, PasswordHash, FullName, Role FROM users WHERE Username = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, username);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                String hashedPasswordFromDB = rs.getString("PasswordHash");
//                if (plainPassword.equals(hashedPasswordFromDB)) {
//                    User user = new User();
//                    user.setUserId(rs.getInt("UserId"));
//                    user.setUsername(username);
//                    user.setFullName(rs.getString("FullName"));
//                    user.setRole(rs.getString("Role"));
//                    return user;
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi đăng nhập: " + e.getMessage());
//        }
//        return null;
//    }
//    
//    // Thao tác CRUD cho User
//    public boolean addUser(User user, String hashedPassword) {
//        String sql = "INSERT INTO users (Username, PasswordHash, FullName, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, user.getUsername());
//            pstmt.setString(2, hashedPassword);
//            pstmt.setString(3, user.getFullName());
//            pstmt.setString(4, user.getEmail());
//            pstmt.setString(5, user.getPhoneNumber());
//            pstmt.setString(6, user.getRole());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm người dùng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean updateUser(User user) {
//        String sql = "UPDATE users SET FullName = ?, Email = ?, PhoneNumber = ?, Role = ? WHERE UserId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, user.getFullName());
//            pstmt.setString(2, user.getEmail());
//            pstmt.setString(3, user.getPhoneNumber());
//            pstmt.setString(4, user.getRole());
//            pstmt.setInt(5, user.getUserId());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi cập nhật người dùng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean deleteUser(int userId) {
//        String sql = "DELETE FROM users WHERE UserId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, userId);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi xóa người dùng: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    public List<User> getAllUsers() {
//        List<User> userList = new ArrayList<>();
//        String sql = "SELECT UserId, Username, FullName, Email, PhoneNumber, Role FROM users";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                User user = new User();
//                user.setUserId(rs.getInt("UserId"));
//                user.setUsername(rs.getString("Username"));
//                user.setFullName(rs.getString("FullName"));
//                user.setEmail(rs.getString("Email"));
//                user.setPhoneNumber(rs.getString("PhoneNumber"));
//                user.setRole(rs.getString("Role"));
//                userList.add(user);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách người dùng: " + e.getMessage());
//        }
//        return userList;
//    }
//}
//
///**
// * Lớp quản lý các thao tác CRUD cho Customer.
// */
//class CustomerService implements CrudService<Customer> {
//    @Override
//    public boolean add(Customer customer) {
//        String sql = "INSERT INTO customers (FullName, IDCardNumber, PhoneNumber, Address, Email) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, customer.getFullName());
//            pstmt.setString(2, customer.getIdCardNumber());
//            pstmt.setString(3, customer.getPhoneNumber());
//            pstmt.setString(4, customer.getAddress());
//            pstmt.setString(5, customer.getEmail());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public boolean update(Customer customer) {
//        String sql = "UPDATE customers SET FullName = ?, IDCardNumber = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE CustomerId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, customer.getFullName());
//            pstmt.setString(2, customer.getIdCardNumber());
//            pstmt.setString(3, customer.getPhoneNumber());
//            pstmt.setString(4, customer.getAddress());
//            pstmt.setString(5, customer.getEmail());
//            pstmt.setInt(6, customer.getCustomerId());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public boolean delete(int id) {
//        String sql = "DELETE FROM customers WHERE CustomerId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, id);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi xóa khách hàng: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    @Override
//    public Customer getById(int id) {
//        // Implementation for getting a single customer
//        return null;
//    }
//
//    @Override
//    public List<Customer> getAll() {
//        List<Customer> customerList = new ArrayList<>();
//        String sql = "SELECT * FROM customers";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                Customer customer = new Customer();
//                customer.setCustomerId(rs.getInt("CustomerId"));
//                customer.setFullName(rs.getString("FullName"));
//                customer.setIdCardNumber(rs.getString("IDCardNumber"));
//                customer.setPhoneNumber(rs.getString("PhoneNumber"));
//                customer.setAddress(rs.getString("Address"));
//                customer.setEmail(rs.getString("Email"));
//                customerList.add(customer);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
//        }
//        return customerList;
//    }
//}
//
///**
// * Lớp quản lý các thao tác CRUD cho Room.
// */
//class RoomService implements CrudService<Room> {
//    @Override
//    public boolean add(Room room) {
//        String sql = "INSERT INTO rooms (RoomNumber, RoomType, PricePerNight, Status, RoomDescription) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, room.getRoomNumber());
//            pstmt.setString(2, room.getRoomType());
//            pstmt.setDouble(3, room.getPricePerNight());
//            pstmt.setString(4, room.getStatus());
//            pstmt.setString(5, room.getDescription());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm phòng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public boolean update(Room room) {
//        String sql = "UPDATE rooms SET RoomNumber = ?, RoomType = ?, PricePerNight = ?, Status = ?, RoomDescription = ? WHERE RoomId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, room.getRoomNumber());
//            pstmt.setString(2, room.getRoomType());
//            pstmt.setDouble(3, room.getPricePerNight());
//            pstmt.setString(4, room.getStatus());
//            pstmt.setString(5, room.getDescription());
//            pstmt.setInt(6, room.getRoomId());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi cập nhật phòng: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    @Override
//    public boolean delete(int id) {
//        String sql = "DELETE FROM rooms WHERE RoomId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, id);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi xóa phòng: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    @Override
//    public Room getById(int id) {
//        // Implementation for getting a single room
//        return null;
//    }
//
//    @Override
//    public List<Room> getAll() {
//        List<Room> roomList = new ArrayList<>();
//        String sql = "SELECT * FROM rooms";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                Room room = new Room();
//                room.setRoomId(rs.getInt("RoomId"));
//                room.setRoomNumber(rs.getString("RoomNumber"));
//                room.setRoomType(rs.getString("RoomType"));
//                room.setPricePerNight(rs.getDouble("PricePerNight"));
//                room.setStatus(rs.getString("Status"));
//                room.setDescription(rs.getString("RoomDescription"));
//                roomList.add(room);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách phòng: " + e.getMessage());
//        }
//        return roomList;
//    }
//}
//
///**
// * Lớp quản lý các thao tác CRUD cho Service.
// */
//class ServiceService implements CrudService<Service> {
//    @Override
//    public boolean add(Service service) {
//        String sql = "INSERT INTO services (ServiceName, Price, Description) VALUES (?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, service.getServiceName());
//            pstmt.setDouble(2, service.getPrice());
//            pstmt.setString(3, service.getDescription());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm dịch vụ: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public boolean update(Service service) {
//        String sql = "UPDATE services SET ServiceName = ?, Price = ?, Description = ? WHERE ServiceId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, service.getServiceName());
//            pstmt.setDouble(2, service.getPrice());
//            pstmt.setString(3, service.getDescription());
//            pstmt.setInt(4, service.getServiceId());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi cập nhật dịch vụ: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public boolean delete(int id) {
//        String sql = "DELETE FROM services WHERE ServiceId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, id);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi xóa dịch vụ: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public Service getById(int id) {
//        // Implementation for getting a single service
//        return null;
//    }
//
//    @Override
//    public List<Service> getAll() {
//        List<Service> serviceList = new ArrayList<>();
//        String sql = "SELECT * FROM services";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                Service service = new Service();
//                service.setServiceId(rs.getInt("ServiceId"));
//                service.setServiceName(rs.getString("ServiceName"));
//                service.setPrice(rs.getDouble("Price"));
//                service.setDescription(rs.getString("Description"));
//                serviceList.add(service);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
//        }
//        return serviceList;
//    }
//}
//
//
///**
// * Lớp chứa các nghiệp vụ phức tạp liên quan đến đặt phòng.
// */
//class BookingService {
//
//    public int createBooking(int roomId, int customerId, LocalDate checkInDate, LocalDate checkOutDate) {
//        String insertBookingSQL = "INSERT INTO bookings "
//                + "(RoomId, CustomerId, CheckInDate, CheckOutDate, BookingDate, Status, PaymentStatus) "
//                + "VALUES (?, ?, ?, ?, NOW(), 'Confirmed', 'Chưa thanh toán')";
//        String updateRoomSQL = "UPDATE rooms "
//                + "SET Status = 'Booked' "
//                + "WHERE RoomId = ?";
//        Connection conn = null;
//        int bookingId = -1;
//        try {
//            conn = DatabaseConnection.getConnection();
//            conn.setAutoCommit(false); // Bắt đầu transaction
//            try (PreparedStatement pstmtBooking = conn.prepareStatement
//            (insertBookingSQL, Statement.RETURN_GENERATED_KEYS)) {
//                pstmtBooking.setInt(1, roomId);
//                pstmtBooking.setInt(2, customerId);
//                pstmtBooking.setDate(3, java.sql.Date.valueOf(checkInDate));
//                pstmtBooking.setDate(4, java.sql.Date.valueOf(checkOutDate));
//                pstmtBooking.executeUpdate();
//                try (ResultSet generatedKeys = pstmtBooking.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        bookingId = generatedKeys.getInt(1);
//                    } else {
//                        throw new SQLException("Tạo booking thất bại, không lấy được ID.");}}}
//            try (PreparedStatement pstmtRoom = conn.prepareStatement(updateRoomSQL)) {
//                pstmtRoom.setInt(1, roomId);
//                pstmtRoom.executeUpdate();}
//            conn.commit();
//            return bookingId;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi tạo đặt phòng (Transaction): " + e.getMessage());
//            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { }
//            return -1;
//        } finally {
//            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { }}}
//
//    public boolean cancelBooking(int bookingId) {
//        String updateBookingSQL = "UPDATE bookings "
//        + "SET Status = 'Cancelled' "
//        + "WHERE BookingId = ? AND Status = 'Confirmed'";
//        String updateRoomSQL = "UPDATE rooms "
//        + "SET Status = 'Available' "
//        + "WHERE RoomId = (SELECT RoomId FROM bookings WHERE BookingId = ?)";
//        Connection conn = null;
//        try {
//            conn = DatabaseConnection.getConnection();
//            conn.setAutoCommit(false); 
//            int affectedRows;
//            // Bước 1: Cập nhật bảng bookings
//            try (PreparedStatement pstmtBooking = conn.prepareStatement(updateBookingSQL)) {
//                 pstmtBooking.setInt(1, bookingId);
//                 affectedRows = pstmtBooking.executeUpdate();
//                 if(affectedRows == 0) {
//                     throw new SQLException
//                     ("Không thể hủy đặt phòng. Đơn có thể đã check-in hoặc không tồn tại.");}}
//            // Bước 2: Cập nhật bảng rooms
//            try (PreparedStatement pstmtRoom = conn.prepareStatement(updateRoomSQL)) {
//                 pstmtRoom.setInt(1, bookingId);
//                 pstmtRoom.executeUpdate();
//            }conn.commit();return true;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi hủy đặt phòng (Transaction): " + e.getMessage());
//            if(conn != null) {try { conn.rollback();} catch (SQLException ex) {
//                    System.err.println("Lỗi khi rollback: " + ex.getMessage());
//                }}return false;} finally {if (conn != null) {
//                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}}}}} 
//
//public boolean updateBooking(int bookingId, LocalDate newCheckInDate, LocalDate newCheckOutDate) {
//        String sql = "UPDATE bookings "
//        + "SET CheckInDate = ?, CheckOutDate = ? "
//        + "WHERE BookingId = ? AND Status = 'Confirmed'";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setDate(1, java.sql.Date.valueOf(newCheckInDate));
//            pstmt.setDate(2, java.sql.Date.valueOf(newCheckOutDate));
//            pstmt.setInt(3, bookingId);
//            int affectedRows = pstmt.executeUpdate();
//            if (affectedRows > 0) {
//                return true;} else {
//                System.err.println("Không thể sửa. Đơn đặt phòng không tồn tại hoặc đã check-in.");
//                return false;}
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi sửa đặt phòng: " + e.getMessage());
//            return false;}}
//
//
//    public boolean checkIn(int bookingId) {
//        String updateBookingSQL = "UPDATE bookings "
//                + "SET ActualCheckInDate = NOW(), Status = 'CheckedIn' "
//                + "WHERE BookingId = ?";
//        String updateRoomSQL = "UPDATE rooms "
//                + "SET Status = 'Đang ở' "
//                + "WHERE RoomId = (SELECT RoomId FROM bookings WHERE BookingId = ?)";
//        Connection conn = null;
//        try {conn = DatabaseConnection.getConnection();
//            conn.setAutoCommit(false);
//            try (PreparedStatement pstmtBooking = conn.prepareStatement(updateBookingSQL)) {
//                 pstmtBooking.setInt(1, bookingId);
//                 if(pstmtBooking.executeUpdate() == 0) throw new SQLException("Booking ID không tồn tại.");}
//            try (PreparedStatement pstmtRoom = conn.prepareStatement(updateRoomSQL)) {
//                 pstmtRoom.setInt(1, bookingId);
//                 pstmtRoom.executeUpdate();}
//            conn.commit();
//            return true;} catch (SQLException e) {
//            System.err.println("Lỗi khi check-in (Transaction): " + e.getMessage());
//            if(conn != null) try { conn.rollback(); } catch (SQLException ex) {}
//            return false;} finally {
//            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}}}
//
//    public boolean checkOut(int bookingId) {
//        String updateBookingSQL = "UPDATE bookings "
//                + "SET ActualCheckOutDate = NOW(), Status = 'CheckedOut' "
//                + "WHERE BookingId = ?";
//        String updateRoomSQL = "UPDATE rooms "
//                + "SET Status = 'Cần dọn dẹp' "
//                + "WHERE RoomId = (SELECT RoomId FROM bookings WHERE BookingId = ?)";
//        Connection conn = null;
//        try {conn = DatabaseConnection.getConnection();conn.setAutoCommit(false);
//            try (PreparedStatement pstmtBooking = conn.prepareStatement(updateBookingSQL)) {
//                 pstmtBooking.setInt(1, bookingId);
//                 if(pstmtBooking.executeUpdate() == 0) {
//                     throw new SQLException
//                     ("Booking ID " + bookingId + " không tồn tại hoặc đã được check-out.");}}
//            try (PreparedStatement pstmtRoom = conn.prepareStatement(updateRoomSQL)) {
//                 pstmtRoom.setInt(1, bookingId);
//                 pstmtRoom.executeUpdate();
//            } conn.commit();return true;} catch (SQLException e) {
//            System.err.println("Lỗi khi check-out (Transaction): " + e.getMessage());
//            if(conn != null) try { conn.rollback(); } catch (SQLException ex) { }
//            return false;} finally {
//            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { 
//}}}
//
//
//
///**
// * Lớp chứa các nghiệp vụ liên quan đến dịch vụ và thanh toán.
// */
//class PaymentService {
//
//    public boolean addServiceToBooking(int bookingId, int serviceId, int quantity, double priceAtBooking) {
//        String sql = "INSERT INTO bookedservices "
//        + "(BookingId, ServiceId, Quantity, PriceAtBooking) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, bookingId);
//            pstmt.setInt(2, serviceId);
//            pstmt.setInt(3, quantity);
//            pstmt.setDouble(4, priceAtBooking);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm dịch vụ vào đơn: " + e.getMessage());
//            return false;
//        }
//    }
//    public InvoicePreview getInvoicePreview(int bookingId) {
//        InvoicePreview preview = new InvoicePreview();
//
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            // Bước 1: Lấy thông tin đặt phòng để tính tiền phòng
//            String bookingInfoSql = "SELECT b.ActualCheckInDate, r.PricePerNight, r.RoomNumber " +
//                                    "FROM bookings b " +
//                                    "JOIN rooms r ON b.RoomId = r.RoomId " +
//                                    "WHERE b.BookingId = ?";
//            try (PreparedStatement pstmt = conn.prepareStatement(bookingInfoSql)) {
//                pstmt.setInt(1, bookingId);
//                ResultSet rs = pstmt.executeQuery();if (rs.next()) {
//                    LocalDate checkInDate = rs.getTimestamp("ActualCheckInDate").toLocalDateTime().toLocalDate();
//                    LocalDate today = LocalDate.now();
//                    long nights = ChronoUnit.DAYS.between(checkInDate, today);
//                    if (nights == 0) nights = 1; // Tính tối thiểu 1 đêm
//                    double pricePerNight = rs.getDouble("PricePerNight");
//                    String roomNumber = rs.getString("RoomNumber");
//                    preview.addLineItem(new BookedServiceDetail(
//                        "Tiền phòng " + roomNumber,(int) nights,pricePerNight));}}
//            // Bước 2: Lấy danh sách các dịch vụ đã sử dụng
//            String servicesSql = "SELECT s.ServiceName, bs.Quantity, bs.PriceAtBooking " +
//                                 "FROM bookedservices bs " +
//                                 "JOIN services s ON bs.ServiceId = s.ServiceId " +
//                                 "WHERE bs.BookingId = ?";
//            try (PreparedStatement pstmt = conn.prepareStatement(servicesSql)) {
//                pstmt.setInt(1, bookingId);
//                ResultSet rs = pstmt.executeQuery();while (rs.next()) {
//                    preview.addLineItem(new BookedServiceDetail(rs.getString("ServiceName"),
//                        rs.getInt("Quantity"),rs.getDouble("PriceAtBooking")));}}} catch (SQLException e) {
//            System.err.println("Lỗi khi lấy thông tin hóa đơn tạm tính: " + e.getMessage());
//        }return preview;}
//
//    public int createInvoice(int bookingId, int customerId, double totalRoomCharge, double totalServiceCharge, String paymentMethod, int issuedByUserId) {
//        String sql = "INSERT INTO invoices "
//        + "(BookingId, CustomerId, IssueDate, TotalRoomCharge, TotalServiceCharge, GrandTotal, PaymentMethod, IssuedByUserId) "
//        + "VALUES (?, ?, NOW(), ?, ?, ?, ?, ?)";
//        double grandTotal = totalRoomCharge + totalServiceCharge;
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            pstmt.setInt(1, bookingId);
//            pstmt.setInt(2, customerId);
//            pstmt.setDouble(3, totalRoomCharge);
//            pstmt.setDouble(4, totalServiceCharge);
//            pstmt.setDouble(5, grandTotal);
//            pstmt.setString(6, paymentMethod);
//            pstmt.setInt(7, issuedByUserId);
//            int affectedRows = pstmt.executeUpdate();
//            if (affectedRows > 0) {
//                try (ResultSet rs = pstmt.getGeneratedKeys()) {if (rs.next()) {return rs.getInt(1);}}}
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi tạo hóa đơn: " + e.getMessage());}return -1;}}
//
///**
// * Lớp chứa các chức năng báo cáo, thống kê.
// */
//class ReportService {
//
//    public double getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
//        String sql = "SELECT SUM(GrandTotal) AS TongDoanhThu FROM invoices WHERE DATE(IssueDate) BETWEEN ? AND ?";
//        double totalRevenue = 0.0;
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
//            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                totalRevenue = rs.getDouble("TongDoanhThu");
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi báo cáo doanh thu: " + e.getMessage());
//        }
//        return totalRevenue;
//    }
//
//    /**
//     * Thống kê doanh thu theo từng ngày trong một khoảng thời gian.
//     * @param startDate Ngày bắt đầu.
//     * @param endDate Ngày kết thúc.
//     * @return Danh sách các bản ghi doanh thu theo ngày.
//     */
//    public List<RevenueRecord> getDailyRevenueReport(LocalDate startDate, LocalDate endDate) {
//        List<RevenueRecord> report = new ArrayList<>();
//        String sql = "SELECT DATE(IssueDate) AS Ngay, SUM(GrandTotal) AS DoanhThu " +
//                     "FROM invoices " +
//                     "WHERE DATE(IssueDate) BETWEEN ? AND ? " +
//                     "GROUP BY DATE(IssueDate) " +
//                     "ORDER BY Ngay ASC";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
//            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                report.add(new RevenueRecord(
//                    rs.getDate("Ngay").toLocalDate().toString(),
//                    rs.getDouble("DoanhThu")
//                ));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi báo cáo doanh thu theo ngày: " + e.getMessage());
//        }
//        return report;
//    }
//
//    /**
//     * Thống kê doanh thu theo từng tháng trong một năm.
//     * @param year Năm cần thống kê.
//     * @return Danh sách các bản ghi doanh thu theo tháng.
//     */
//    public List<RevenueRecord> getMonthlyRevenueReport(int year) {
//        List<RevenueRecord> report = new ArrayList<>();
//        String sql = "SELECT MONTH(IssueDate) AS Thang, SUM(GrandTotal) AS DoanhThu " +
//                     "FROM invoices " +
//                     "WHERE YEAR(IssueDate) = ? " +
//                     "GROUP BY MONTH(IssueDate) " +
//                     "ORDER BY Thang ASC";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, year);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                String period = "Tháng " + rs.getInt("Thang") + "/" + year;
//                report.add(new RevenueRecord(
//                    period,
//                    rs.getDouble("DoanhThu")
//                ));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi báo cáo doanh thu theo tháng: " + e.getMessage());
//        }
//        return report;
//    }
//
//    /**
//     * Thống kê doanh thu theo từng năm.
//     * @return Danh sách các bản ghi doanh thu theo năm.
//     */
//    public List<InvoiceDetail> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
//    List<InvoiceDetail> invoiceList = new ArrayList<>();
//    String sql = "SELECT i.InvoiceId, i.BookingId, c.FullName AS CustomerName, i.IssueDate, " +
//                 "i.TotalRoomCharge, i.TotalServiceCharge, i.GrandTotal, i.PaymentMethod, "+ 
//                 "u.FullName AS StaffName " +
//                 "FROM invoices i " +
//                 "JOIN customers c ON i.CustomerId = c.CustomerId " +
//                 "JOIN users u ON i.IssuedByUserId = u.UserId " +
//                 "WHERE DATE(i.IssueDate) BETWEEN ? AND ?";
//    try (Connection conn = DatabaseConnection.getConnection();
//         PreparedStatement pstmt = conn.prepareStatement(sql)) {
//        
//        pstmt.setDate(1, java.sql.Date.valueOf(startDate));
//        pstmt.setDate(2, java.sql.Date.valueOf(endDate));
//        ResultSet rs = pstmt.executeQuery();
//        while (rs.next()) {
//            InvoiceDetail detail = new InvoiceDetail();
//            // Gán dữ liệu từ ResultSet vào đối tượng InvoiceDetail
//            detail.setInvoiceId(rs.getInt("InvoiceId"));
//            detail.setBookingId(rs.getInt("BookingId"));
//            detail.setCustomerName(rs.getString("CustomerName"));
//            detail.setIssueDate(rs.getDate("IssueDate").toLocalDate());
//            detail.setTotalRoomCharge(rs.getDouble("TotalRoomCharge"));
//            detail.setTotalServiceCharge(rs.getDouble("TotalServiceCharge"));
//            detail.setGrandTotal(rs.getDouble("GrandTotal"));
//            detail.setPaymentMethod(rs.getString("PaymentMethod"));
//            detail.setStaffName(rs.getString("StaffName"));
//            invoiceList.add(detail);
//        }
//    } catch (SQLException e) {
//        System.err.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
//    }
//    return invoiceList;
//}
//
//
//    public List<String> getTopUsedServices() {
//        String sql = "SELECT s.ServiceName, SUM(bs.Quantity) AS SoLuongSuDung, SUM(bs.Quantity * bs.PriceAtBooking) AS TongTien " +
//                     "FROM bookedservices bs " +
//                     "JOIN services s ON bs.ServiceId = s.ServiceId " +
//                     "GROUP BY s.ServiceName " +
//                     "ORDER BY SoLuongSuDung DESC";
//        List<String> results = new ArrayList<>();
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                String serviceName = rs.getString("ServiceName");
//                int quantity = rs.getInt("SoLuongSuDung");
//                double total = rs.getDouble("TongTien");
//                results.add(String.format("Dịch vụ: %s - Số lượng: %d - Tổng tiền: %.2f", serviceName, quantity, total));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thống kê dịch vụ: " + e.getMessage());
//        }
//        return results;
//    }
//  /**
//     * Thêm một khách hàng mới vào bảng 'customers'.
//     */
//interface CrudService<T> {
//    List<T> getAll();       // Lấy tất cả đối tượng
//    boolean add(T item);    // Thêm một đối tượng mới
//    boolean update(T item); // Cập nhật một đối tượng
//    boolean delete(int id); // Xóa một đối tượng theo ID
//}
//    public boolean add(Customer customer) {
//        String sql = "INSERT INTO customers (FullName, IDCardNumber, PhoneNumber, Address, Email) "
//        + "VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, customer.getFullName());
//            pstmt.setString(2, customer.getIdCardNumber());
//            pstmt.setString(3, customer.getPhoneNumber());
//            pstmt.setString(4, customer.getAddress());
//            pstmt.setString(5, customer.getEmail());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Cập nhật thông tin của một khách hàng đã có.
//     */
//    public boolean update(Customer customer) {
//        String sql = "UPDATE customers "
//        + "SET FullName = ?, IDCardNumber = ?, PhoneNumber = ?, Address = ?, Email = ? "
//        + "WHERE CustomerId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, customer.getFullName());
//            pstmt.setString(2, customer.getIdCardNumber());
//            pstmt.setString(3, customer.getPhoneNumber());
//            pstmt.setString(4, customer.getAddress());
//            pstmt.setString(5, customer.getEmail());
//            pstmt.setInt(6, customer.getCustomerId());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Xóa một khách hàng khỏi cơ sở dữ liệu theo ID.
//     */
//    public boolean delete(int id) {
//        String sql = "DELETE FROM customers WHERE CustomerId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, id);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi xóa khách hàng: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    /**
//     * Lấy danh sách tất cả khách hàng để hiển thị lên bảng.
//     */
//    public List<Customer> getAll() {
//        List<Customer> customerList = new ArrayList<>();
//        String sql = "SELECT * FROM customers";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                Customer customer = new Customer();
//                customer.setCustomerId(rs.getInt("CustomerId"));
//                customer.setFullName(rs.getString("FullName"));
//                // ... gán các thuộc tính khác
//                customerList.add(customer);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
//        }
//        return customerList;
//    }
//public String generatePrintableInvoiceText(InvoiceDetail invoiceInfo, List<BookedServiceDetail> services) {
//    StringBuilder sb = new StringBuilder();
//    sb.append("========================================\n");
//    sb.append("           HÓA ĐƠN THANH TOÁN\n");
//    sb.append("========================================\n\n");
//    sb.append(String.format("Mã hóa đơn: %d\n", invoiceInfo.getInvoiceId()));
//    sb.append(String.format("Ngày lập: %s\n", invoiceInfo.getIssueDate().toString()));
//    sb.append(String.format("Khách hàng: %s\n", invoiceInfo.getCustomerName()));
//    sb.append(String.format("Nhân viên: %s\n\n", invoiceInfo.getStaffName()));
//    sb.append("----------------------------------------\n");
//    sb.append(String.format("%-20s %5s %10s\n", "Dịch vụ", "SL", "Thành tiền"));
//    sb.append("----------------------------------------\n");
//    // Thêm các dòng chi tiết dịch vụ và tiền phòng
//    for (BookedServiceDetail item : services) {
//        sb.append(String.format("%-20s %5d %10.2f\n", 
//            item.getServiceName(), item.getQuantity(), item.getTotal()));}
//    sb.append("----------------------------------------\n");
//    sb.append(String.format("Tổng cộng: %,.2f VND\n", invoiceInfo.getGrandTotal()));
//    sb.append(String.format("Phương thức TT: %s\n\n", invoiceInfo.getPaymentMethod()));
//    sb.append("========================================\n");
//    sb.append("      Cảm ơn quý khách và hẹn gặp lại!\n");
//    sb.append("========================================\n");
//    return sb.toString();
//}
//}
