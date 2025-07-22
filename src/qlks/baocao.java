//
//package qlks;
//
//import java.sql.*;
//import java.math.BigDecimal;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//// import org.mindrot.jbcrypt.BCrypt; // Cần thư viện jbcrypt
//
///**
// * Báo cáo mã nguồn tối giản và dễ hiểu của các chức năng chính.
// * File này mô tả logic nghiệp vụ cốt lõi, đã lược bỏ các thành phần giao diện.
// */
//public class baocao {
//
//    //================================================================================
//    // I. CHỨC NĂNG ĐẶT PHÒNG (BOOKING)
//    //================================================================================
//    class BookingFunctions {
//
//        /**
//         * Thêm một đặt phòng mới. Đây là một giao dịch (transaction) gồm 3 bước:
//         * 1. Kiểm tra phòng có sẵn sàng ("Available") không.
//         * 2. Thêm bản ghi mới vào bảng `bookings`.
//         * 3. Cập nhật trạng thái của phòng thành `Booked`.
//         */
//        public void addBooking(int roomId, int customerId, java.sql.Date checkInDate, java.sql.Date checkOutDate) {
//            Connection conn = null;
//            try {
//                conn = DatabaseConnection.getConnection();
//                conn.setAutoCommit(false); // Bắt đầu transaction
//
//                // Bước 1: Kiểm tra trạng thái phòng
//                String checkSql = "SELECT Status FROM rooms WHERE RoomId = ? FOR UPDATE";
//                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
//                    stmt.setInt(1, roomId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (!rs.next() || !"Available".equalsIgnoreCase(rs.getString("Status"))) {
//                        System.out.println("Lỗi: Phòng không tồn tại hoặc không khả dụng.");
//                        conn.rollback();
//                        return;
//                    }
//                }
//
//                // Bước 2: Thêm đặt phòng mới
//                String insertSql = "INSERT INTO bookings (RoomId, CustomerId, CheckInDate, CheckOutDate, Status) VALUES (?, ?, ?, ?, 'Confirmed')";
//                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
//                    stmt.setInt(1, roomId);
//                    stmt.setInt(2, customerId);
//                    stmt.setDate(3, checkInDate);
//                    stmt.setDate(4, checkOutDate);
//                    stmt.executeUpdate();
//                }
//
//                // Bước 3: Cập nhật trạng thái phòng
//                String updateRoomSql = "UPDATE rooms SET Status = 'Booked' WHERE RoomId = ?";
//                try (PreparedStatement stmt = conn.prepareStatement(updateRoomSql)) {
//                    stmt.setInt(1, roomId);
//                    stmt.executeUpdate();
//                }
//
//                conn.commit(); // Hoàn tất transaction
//                System.out.println("Tạo đặt phòng thành công.");
//
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi tạo đặt phòng: " + e.getMessage());
//                if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
//            } finally {
//                if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
//            }
//        }
//
//        /**
//         * Sửa thông tin một đặt phòng đã có.
//         */
//        public void editBooking(int bookingId, int newRoomId, int newCustomerId, java.sql.Date newCheckIn, java.sql.Date newCheckOut) {
//            String sql = "UPDATE bookings SET RoomId=?, CustomerId=?, CheckInDate=?, CheckOutDate=? WHERE BookingId=?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, newRoomId);
//                stmt.setInt(2, newCustomerId);
//                stmt.setDate(3, newCheckIn);
//                stmt.setDate(4, newCheckOut);
//                stmt.setInt(5, bookingId);
//                stmt.executeUpdate();
//                System.out.println("Cập nhật đặt phòng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi cập nhật đặt phòng: " + e.getMessage());
//            }
//        }
//
//        /**
//         * Hủy một đặt phòng. Đây là một giao dịch (transaction) gồm 2 bước:
//         * 1. Cập nhật trạng thái booking thành 'Cancelled'.
//         * 2. Cập nhật trạng thái phòng tương ứng thành 'Available'.
//         */
//        public void cancelBooking(int bookingId) {
//            Connection conn = null;
//            try {
//                conn = DatabaseConnection.getConnection();
//                conn.setAutoCommit(false); // Bắt đầu transaction
//
//                // Lấy RoomId để cập nhật lại trạng thái phòng
//                int roomId = -1;
//                String getRoomIdSql = "SELECT RoomId FROM bookings WHERE BookingId = ?";
//                try(PreparedStatement stmt = conn.prepareStatement(getRoomIdSql)) {
//                    stmt.setInt(1, bookingId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (rs.next()) roomId = rs.getInt("RoomId");
//                    else throw new SQLException("Không tìm thấy booking ID: " + bookingId);
//                }
//
//                // Bước 1: Cập nhật trạng thái booking
//                String cancelBookingSql = "UPDATE bookings SET Status = 'Cancelled' WHERE BookingId = ?";
//                try(PreparedStatement stmt = conn.prepareStatement(cancelBookingSql)) {
//                    stmt.setInt(1, bookingId);
//                    stmt.executeUpdate();
//                }
//
//                // Bước 2: Cập nhật trạng thái phòng
//                String updateRoomSql = "UPDATE rooms SET Status = 'Available' WHERE RoomId = ?";
//                try(PreparedStatement stmt = conn.prepareStatement(updateRoomSql)) {
//                    stmt.setInt(1, roomId);
//                    stmt.executeUpdate();
//                }
//
//                conn.commit(); // Hoàn tất transaction
//                System.out.println("Hủy đặt phòng thành công.");
//
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi hủy đặt phòng: " + e.getMessage());
//                if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
//            } finally {
//                if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
//            }
//        }
//
//        /**
//         * Ghi nhận khách hàng nhận phòng (check-in).
//         * Cập nhật trạng thái booking từ 'Confirmed' thành 'CheckedIn' và ghi nhận thời gian.
//         */
//        public void checkIn(int bookingId) {
//            String sql = "UPDATE bookings SET ActualCheckInDate = NOW(), Status = 'CheckedIn' WHERE BookingId = ? AND Status = 'Confirmed'";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, bookingId);
//                int affectedRows = stmt.executeUpdate();
//                if (affectedRows > 0) {
//                    System.out.println("Check-in thành công cho booking ID: " + bookingId);
//                } else {
//                    System.out.println("Check-in thất bại: Booking không tồn tại hoặc không ở trạng thái 'Confirmed'.");
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi thực hiện check-in: " + e.getMessage());
//            }
//        }
//
//        /**
//         * Ghi nhận khách hàng trả phòng (check-out). Đây là một giao dịch (transaction) gồm 2 bước:
//         * 1. Cập nhật trạng thái booking thành 'CheckedOut'.
//         * 2. Cập nhật trạng thái phòng tương ứng thành 'Available'.
//         */
//        public void checkOut(int bookingId) {
//            Connection conn = null;
//            try {
//                conn = DatabaseConnection.getConnection();
//                conn.setAutoCommit(false); // Bắt đầu transaction
//
//                // Lấy RoomId và kiểm tra trạng thái 'CheckedIn'
//                int roomId = -1;
//                String getRoomSql = "SELECT RoomId, Status FROM bookings WHERE BookingId = ?";
//                try(PreparedStatement stmt = conn.prepareStatement(getRoomSql)) {
//                    stmt.setInt(1, bookingId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (rs.next()) {
//                        if (!"CheckedIn".equalsIgnoreCase(rs.getString("Status"))) {
//                            throw new SQLException("Booking không ở trạng thái 'CheckedIn'.");
//                        }
//                        roomId = rs.getInt("RoomId");
//                    } else {
//                        throw new SQLException("Không tìm thấy booking ID: " + bookingId);
//                    }
//                }
//
//                // Bước 1: Cập nhật booking
//                String updateBookingSql = "UPDATE bookings SET ActualCheckOutDate = NOW(), Status = 'CheckedOut' WHERE BookingId = ?";
//                try(PreparedStatement stmt = conn.prepareStatement(updateBookingSql)) {
//                    stmt.setInt(1, bookingId);
//                    stmt.executeUpdate();
//                }
//
//                // Bước 2: Cập nhật phòng
//                String updateRoomSql = "UPDATE rooms SET Status = 'Available' WHERE RoomId = ?";
//                try(PreparedStatement stmt = conn.prepareStatement(updateRoomSql)) {
//                    stmt.setInt(1, roomId);
//                    stmt.executeUpdate();
//                }
//
//                conn.commit(); // Hoàn tất transaction
//                System.out.println("Check-out thành công.");
//
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi check-out: " + e.getMessage());
//                if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
//            } finally {
//                if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
//            }
//        }
//    }
//
//    //================================================================================
//    // II. CHỨC NĂNG HÓA ĐƠN (INVOICE)
//    //================================================================================
//    class InvoiceFunctions {
//
//        /**
//         * Thêm một dịch vụ vào cho một đặt phòng đang hoạt động.
//         */
//        public void addServiceToBooking(int bookingId, int serviceId, int quantity) {
//            String getPriceSql = "SELECT Price FROM services WHERE ServiceId = ?";
//            String insertSql = "INSERT INTO bookedservices (BookingId, ServiceId, Quantity, PriceAtBooking) VALUES (?, ?, ?, ?)";
//            
//            try (Connection conn = DatabaseConnection.getConnection()) {
//                // Lấy giá dịch vụ tại thời điểm hiện tại để lưu vào hóa đơn
//                double priceAtBooking = 0;
//                try (PreparedStatement stmt = conn.prepareStatement(getPriceSql)) {
//                    stmt.setInt(1, serviceId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (rs.next()) {
//                        priceAtBooking = rs.getDouble("Price");
//                    } else {
//                        throw new SQLException("Dịch vụ không tồn tại.");
//                    }
//                }
//
//                // Thêm dịch vụ đã đặt vào bảng
//                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
//                    stmt.setInt(1, bookingId);
//                    stmt.setInt(2, serviceId);
//                    stmt.setInt(3, quantity);
//                    stmt.setDouble(4, priceAtBooking);
//                    stmt.executeUpdate();
//                    System.out.println("Thêm dịch vụ vào booking thành công.");
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi thêm dịch vụ vào booking: " + e.getMessage());
//            }
//        }
//
//        /**
//         * Xóa một dịch vụ đã được thêm vào booking (dựa vào ID của bảng bookedservices).
//         */
//        public void removeServiceFromBooking(int bookedServiceId) {
//            String sql = "DELETE FROM bookedservices WHERE BookedServiceId = ?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, bookedServiceId);
//                stmt.executeUpdate();
//                System.out.println("Xóa dịch vụ khỏi booking thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi xóa dịch vụ khỏi booking: " + e.getMessage());
//            }
//        }
//
//        /**
//         * Tính toán và trả về chi tiết hóa đơn, bao gồm tiền phòng và các dịch vụ.
//         */
//        public BigDecimal getInvoiceDetails(int bookingId) {
//            BigDecimal grandTotal = BigDecimal.ZERO;
//            
//            try (Connection conn = DatabaseConnection.getConnection()) {
//                // 1. Tính tiền phòng
//                String roomSql = "SELECT DATEDIFF(IFNULL(ActualCheckOutDate, CheckOutDate), IFNULL(ActualCheckInDate, CheckInDate)) as nights, r.PricePerNight " +
//                               "FROM bookings b JOIN rooms r ON b.RoomId = r.RoomId WHERE b.BookingId = ?";
//                try (PreparedStatement stmt = conn.prepareStatement(roomSql)) {
//                    stmt.setInt(1, bookingId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (rs.next()) {
//                        long nights = Math.max(1, rs.getLong("nights")); // Tối thiểu 1 đêm
//                        BigDecimal pricePerNight = rs.getBigDecimal("PricePerNight");
//                        grandTotal = grandTotal.add(pricePerNight.multiply(BigDecimal.valueOf(nights)));
//                    }
//                }
//
//                // 2. Cộng dồn tiền dịch vụ
//                String serviceSql = "SELECT SUM(bs.Quantity * bs.PriceAtBooking) AS total FROM bookedservices bs WHERE bs.BookingId = ?";
//                try (PreparedStatement stmt = conn.prepareStatement(serviceSql)) {
//                    stmt.setInt(1, bookingId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (rs.next()) {
//                        BigDecimal servicesTotal = rs.getBigDecimal("total");
//                        if (servicesTotal != null) grandTotal = grandTotal.add(servicesTotal);
//                    }
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi tính toán hóa đơn: " + e.getMessage());
//            }
//            return grandTotal;
//        }
//
//        /**
//         * Xử lý thanh toán: Tạo hóa đơn và thực hiện Check-out cho booking.
//         */
//        public void processPaymentAndCheckout(int bookingId, int userId, String paymentMethod) {
//            BigDecimal grandTotal = getInvoiceDetails(bookingId);
//
//            String sql = "INSERT INTO invoices (BookingId, IssuedByUserId, GrandTotal, PaymentMethod, PaymentDate) VALUES (?, ?, ?, ?, NOW())";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                
//                stmt.setInt(1, bookingId);
//                stmt.setInt(2, userId);
//                stmt.setBigDecimal(3, grandTotal);
//                stmt.setString(4, paymentMethod);
//                stmt.executeUpdate();
//                System.out.println("Tạo hóa đơn thành công.");
//
//                // Sau khi thanh toán thành công, tự động check-out
//                new BookingFunctions().checkOut(bookingId);
//
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi xử lý thanh toán: " + e.getMessage());
//            }
//        }
//        
//        /**
//         * In hóa đơn chi tiết cho một booking cụ thể ra file text.
//         * Hàm này tập hợp thông tin từ nhiều bảng để tạo một file hóa đơn hoàn chỉnh.
//         */
//        public void printInvoiceToFile(int bookingId, String staffUsername) {
//            String filePath = System.getProperty("user.home") + "/Desktop/HoaDon/HoaDon_Booking_" + bookingId + ".txt";
//            
//            try (PrintWriter writer = new PrintWriter(filePath)) {
//                // 1. In thông tin chung của hóa đơn (mã booking, nhân viên, ngày giờ)
//                writer.println("===== HÓA ĐƠN THANH TOÁN =====");
//                
//                // 2. Lấy và in thông tin khách hàng từ CSDL
//                // SQL: SELECT FullName, PhoneNumber FROM customers c JOIN bookings b ON ... WHERE b.BookingId = ?
//                writer.println("Khách hàng: [Tên khách hàng]");
//
//                // 3. Lấy và in chi tiết tiền phòng
//                // SQL: SELECT DATEDIFF(...) as nights, r.PricePerNight, ... FROM bookings b JOIN rooms r ON ...
//                writer.println("1. Tiền phòng: [Tiền phòng]");
//
//                // 4. Lấy và in chi tiết các dịch vụ đã sử dụng
//                // SQL: SELECT s.ServiceName, bs.Quantity, ... FROM bookedservices bs JOIN services s ON ...
//                writer.println("2. Dịch vụ đã sử dụng: [Tổng tiền dịch vụ]");
//
//                // 5. Tính tổng cộng cuối cùng và in ra file
//                writer.println("TỔNG CỘNG: [Tổng tiền]");
//                
//                System.out.println("Đã in hóa đơn thành công ra file: " + filePath);
//
//            } catch (Exception e) {
//                System.out.println("Lỗi khi in hóa đơn: " + e.getMessage());
//            }
//        }
//    }
//
//    //================================================================================
//    // III. QUẢN LÝ PHÒNG (ROOM)
//    //================================================================================
//    class RoomFunctions {
//
//        public void addRoom(String roomNumber, String roomType, BigDecimal price, String description) {
//            String checkSql = "SELECT RoomId FROM rooms WHERE RoomNumber = ?";
//            String insertSql = "INSERT INTO rooms (RoomNumber, RoomType, PricePerNight, RoomDescription, Status) VALUES (?, ?, ?, ?, 'Available')";
//            try (Connection conn = DatabaseConnection.getConnection()) {
//                // Kiểm tra số phòng đã tồn tại chưa
//                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
//                    stmt.setString(1, roomNumber);
//                    if (stmt.executeQuery().next()) {
//                        System.out.println("Lỗi: Số phòng đã tồn tại.");
//                        return;
//                    }
//                }
//                // Thêm phòng mới
//                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
//                    stmt.setString(1, roomNumber);
//                    stmt.setString(2, roomType);
//                    stmt.setBigDecimal(3, price);
//                    stmt.setString(4, description);
//                    stmt.executeUpdate();
//                    System.out.println("Thêm phòng thành công.");
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi thêm phòng: " + e.getMessage());
//            }
//        }
//
//        public void updateRoom(int roomId, String roomNumber, String roomType, BigDecimal price, String description, String status) {
//            String sql = "UPDATE rooms SET RoomNumber = ?, RoomType = ?, PricePerNight = ?, RoomDescription = ?, Status = ? WHERE RoomId = ?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, roomNumber);
//                stmt.setString(2, roomType);
//                stmt.setBigDecimal(3, price);
//                stmt.setString(4, description);
//                stmt.setString(5, status);
//                stmt.setInt(6, roomId);
//                stmt.executeUpdate();
//                System.out.println("Cập nhật phòng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi cập nhật phòng: " + e.getMessage());
//            }
//        }
//
//        public void deleteRoom(int roomId) {
//            // Kiểm tra xem phòng có đang được đặt không
//            String checkSql = "SELECT Status FROM rooms WHERE RoomId = ?";
//            String deleteSql = "DELETE FROM rooms WHERE RoomId = ?";
//            try (Connection conn = DatabaseConnection.getConnection()) {
//                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
//                    stmt.setInt(1, roomId);
//                    ResultSet rs = stmt.executeQuery();
//                    if (rs.next() && "Booked".equalsIgnoreCase(rs.getString("Status"))) {
//                        System.out.println("Lỗi: Không thể xóa phòng đang được đặt.");
//                        return;
//                    }
//                }
//                // Xóa phòng
//                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
//                    stmt.setInt(1, roomId);
//                    stmt.executeUpdate();
//                    System.out.println("Xóa phòng thành công.");
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi xóa phòng: " + e.getMessage());
//            }
//        }
//    }
//
//    //================================================================================
//    // IV. QUẢN LÝ KHÁCH HÀNG (CUSTOMER)
//    //================================================================================
//    class CustomerFunctions {
//
//        public void addCustomer(String fullName, String idCardNumber, String phone, String address, String email) {
//            String sql = "INSERT INTO customers (FullName, IDCardNumber, PhoneNumber, Address, Email) VALUES (?, ?, ?, ?, ?)";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, fullName);
//                stmt.setString(2, idCardNumber);
//                stmt.setString(3, phone);
//                stmt.setString(4, address);
//                stmt.setString(5, email);
//                stmt.executeUpdate();
//                System.out.println("Thêm khách hàng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi thêm khách hàng: " + e.getMessage());
//            }
//        }
//
//        public void updateCustomer(int customerId, String fullName, String idCardNumber, String phone, String address, String email) {
//            String sql = "UPDATE customers SET FullName = ?, IDCardNumber = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE CustomerId = ?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, fullName);
//                stmt.setString(2, idCardNumber);
//                stmt.setString(3, phone);
//                stmt.setString(4, address);
//                stmt.setString(5, email);
//                stmt.setInt(6, customerId);
//                stmt.executeUpdate();
//                System.out.println("Cập nhật khách hàng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
//            }
//        }
//
//        public void deleteCustomer(int customerId) {
//            // Kiểm tra khách hàng có booking nào không
//            String checkSql = "SELECT BookingId FROM bookings WHERE CustomerId = ?";
//            String deleteSql = "DELETE FROM customers WHERE CustomerId = ?";
//            try (Connection conn = DatabaseConnection.getConnection()) {
//                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
//                    stmt.setInt(1, customerId);
//                    if (stmt.executeQuery().next()) {
//                        System.out.println("Lỗi: Không thể xóa khách hàng đang có đặt phòng.");
//                        return;
//                    }
//                }
//                // Xóa khách hàng
//                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
//                    stmt.setInt(1, customerId);
//                    stmt.executeUpdate();
//                    System.out.println("Xóa khách hàng thành công.");
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi xóa khách hàng: " + e.getMessage());
//            }
//        }
//    }
//
//    //================================================================================
//    // V. QUẢN LÝ DỊCH VỤ (SERVICE)
//    //================================================================================
//    class ServiceFunctions {
//
//        public void addService(String name, BigDecimal price, String description) {
//            String sql = "INSERT INTO services (ServiceName, Price, Description) VALUES (?, ?, ?)";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, name);
//                stmt.setBigDecimal(2, price);
//                stmt.setString(3, description);
//                stmt.executeUpdate();
//                System.out.println("Thêm dịch vụ thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi thêm dịch vụ: " + e.getMessage());
//            }
//        }
//
//        public void updateService(int serviceId, String name, BigDecimal price, String description) {
//            String sql = "UPDATE services SET ServiceName = ?, Price = ?, Description = ? WHERE ServiceId = ?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, name);
//                stmt.setBigDecimal(2, price);
//                stmt.setString(3, description);
//                stmt.setInt(4, serviceId);
//                stmt.executeUpdate();
//                System.out.println("Cập nhật dịch vụ thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi cập nhật dịch vụ: " + e.getMessage());
//            }
//        }
//
//        public void deleteService(int serviceId) {
//            String sql = "DELETE FROM services WHERE ServiceId = ?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, serviceId);
//                stmt.executeUpdate();
//                System.out.println("Xóa dịch vụ thành công.");
//            } catch (SQLIntegrityConstraintViolationException e) {
//                System.out.println("Lỗi: Không thể xóa dịch vụ đã được sử dụng trong một booking.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi xóa dịch vụ: " + e.getMessage());
//            }
//        }
//    }
//
//    //================================================================================
//    // VI. QUẢN LÝ NGƯỜI DÙNG (USER)
//    //================================================================================
//    class UserFunctions {
//
//        private String hashPassword(String password) {
//            // return BCrypt.hashpw(password, BCrypt.gensalt());
//            return password; // Giả định cho báo cáo
//        }
//
//        public void addUser(String username, String password, String fullName, String email, String phone, String role) {
//            String sql = "INSERT INTO users (Username, PasswordHash, FullName, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?)";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, username);
//                stmt.setString(2, hashPassword(password));
//                stmt.setString(3, fullName);
//                stmt.setString(4, email);
//                stmt.setString(5, phone);
//                stmt.setString(6, role);
//                stmt.executeUpdate();
//                System.out.println("Thêm người dùng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi thêm người dùng: " + e.getMessage());
//            }
//        }
//
//        public void updateUser(int userId, String username, String password, String fullName, String email, String phone, String role) {
//            String sql = "UPDATE users SET Username=?, PasswordHash=?, FullName=?, Email=?, PhoneNumber=?, Role=? WHERE UserId=?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, username);
//                stmt.setString(2, hashPassword(password)); // Luôn hash lại mật khẩu mới
//                stmt.setString(3, fullName);
//                stmt.setString(4, email);
//                stmt.setString(5, phone);
//                stmt.setString(6, role);
//                stmt.setInt(7, userId);
//                stmt.executeUpdate();
//                System.out.println("Cập nhật người dùng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi cập nhật người dùng: " + e.getMessage());
//            }
//        }
//
//        public void deleteUser(int userId, int currentUserId) {
//            if (userId == currentUserId) {
//                System.out.println("Lỗi: Không thể tự xóa chính mình.");
//                return;
//            }
//            String sql = "DELETE FROM users WHERE UserId = ?";
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, userId);
//                stmt.executeUpdate();
//                System.out.println("Xóa người dùng thành công.");
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi xóa người dùng: " + e.getMessage());
//            }
//        }
//    }
//    
//    //================================================================================
//    // VII. THỐNG KÊ DOANH THU
//    //================================================================================
//    class StatisticsFunctions {
//
//        /**
//         * Lọc và truy vấn danh sách hóa đơn dựa trên các tiêu chí.
//         */
//        public List<InvoiceDTO> filterInvoices(java.sql.Date fromDate, java.sql.Date toDate, String paymentMethod) {
//            List<InvoiceDTO> results = new ArrayList<>();
//            StringBuilder sql = new StringBuilder("SELECT * FROM invoices WHERE 1=1");
//            List<Object> params = new ArrayList<>();
//
//            if (fromDate != null) {
//                sql.append(" AND PaymentDate >= ?");
//                params.add(fromDate);
//            }
//            if (toDate != null) {
//                sql.append(" AND PaymentDate <= ?");
//                params.add(toDate);
//            }
//            if (paymentMethod != null && !paymentMethod.equals("Tất cả")) {
//                sql.append(" AND PaymentMethod = ?");
//                params.add(paymentMethod);
//            }
//            sql.append(" ORDER BY PaymentDate DESC");
//
//            try (Connection conn = DatabaseConnection.getConnection();
//                 PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//                for (int i = 0; i < params.size(); i++) {
//                    ps.setObject(i + 1, params.get(i));
//                }
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    results.add(new InvoiceDTO(rs));
//                }
//            } catch (SQLException e) {
//                System.out.println("Lỗi khi lọc hóa đơn: " + e.getMessage());
//            }
//            return results;
//        }
//
//        /**
//         * Xuất dữ liệu thống kê ra một file text trên Desktop.
//         */
//        public void exportStatisticsToTextFile(List<InvoiceDTO> invoiceData) {
//            if (invoiceData == null || invoiceData.isEmpty()) {
//                System.out.println("Không có dữ liệu để xuất.");
//                return;
//            }
//            
//            String desktopPath = System.getProperty("user.home") + "/Desktop/ThongKe";
//            new File(desktopPath).mkdirs();
//
//            String fileName = "ThongKeDoanhThu_" + java.time.LocalDate.now() + ".txt";
//            File file = new File(desktopPath, fileName);
//
//            try (PrintWriter writer = new PrintWriter(file)) {
//                writer.println("===== BÁO CÁO THỐNG KÊ DOANH THU =====");
//                writer.println("Ngày xuất: " + java.time.LocalDate.now());
//                writer.println("Tổng số hóa đơn: " + invoiceData.size());
//                writer.println("-----------------------------------------");
//
//                double totalRevenue = 0;
//                for (InvoiceDTO invoice : invoiceData) {
//                    writer.println(invoice.toString());
//                    totalRevenue += invoice.grandTotal;
//                }
//
//                writer.println("-----------------------------------------");
//                writer.printf("TỔNG DOANH THU: %,.0f VNĐ%n", totalRevenue);
//
//                System.out.println("Xuất thống kê thành công: " + file.getAbsolutePath());
//
//            } catch (IOException e) {
//                System.out.println("Lỗi khi ghi file thống kê: " + e.getMessage());
//            }
//        }
//    }
//}
