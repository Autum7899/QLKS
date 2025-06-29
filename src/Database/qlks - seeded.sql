-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2025 at 12:15 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+07:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlks`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookedservices`
--

CREATE TABLE `bookedservices` (
  `BookedServiceId` int(11) NOT NULL,
  `BookingId` int(11) NOT NULL,
  `ServiceId` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL DEFAULT 1,
  `PriceAtBooking` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `BookingId` int(11) NOT NULL,
  `RoomId` int(11) NOT NULL,
  `CustomerId` int(11) NOT NULL,
  `CheckInDate` date NOT NULL,
  `CheckOutDate` date NOT NULL,
  `ActualCheckInDate` datetime DEFAULT NULL,
  `ActualCheckOutDate` datetime DEFAULT NULL,
  `BookingDate` datetime DEFAULT current_timestamp(),
  `Status` varchar(20) NOT NULL,
  `PaymentStatus` varchar(20) NOT NULL DEFAULT 'Pending',
  `Notes` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`BookingId`, `RoomId`, `CustomerId`, `CheckInDate`, `CheckOutDate`, `ActualCheckInDate`, `ActualCheckOutDate`, `BookingDate`, `Status`, `PaymentStatus`, `Notes`) VALUES
(1, 1, 1, '2025-01-10', '2025-01-13', '2025-01-10 14:10:00', '2025-01-13 11:30:00', '2025-01-05 09:00:00', 'CheckedOut', 'Paid', 'Khách hàng yêu cầu phòng yên tĩnh'),
(2, 7, 2, '2025-01-20', '2025-01-25', '2025-01-20 15:00:00', '2025-01-25 12:00:00', '2025-01-15 11:20:00', 'CheckedOut', 'Paid', NULL),
(3, 13, 3, '2025-02-05', '2025-02-10', '2025-02-05 18:00:00', '2025-02-10 10:00:00', '2025-01-28 16:45:00', 'CheckedOut', 'Paid', 'Check-in muộn'),
(4, 4, 1, '2025-02-20', '2025-02-22', '2025-02-20 14:05:00', '2025-02-22 11:15:00', '2025-02-15 10:00:00', 'CheckedOut', 'Paid', 'Kỷ niệm ngày cưới'),
(5, 8, 4, '2025-03-10', '2025-03-14', '2025-03-10 14:00:00', '2025-03-14 12:10:00', '2025-03-01 17:30:00', 'CheckedOut', 'Paid', NULL),
(6, 2, 2, '2025-03-25', '2025-03-27', '2025-03-25 16:20:00', '2025-03-27 11:40:00', '2025-03-20 08:00:00', 'CheckedOut', 'Paid', NULL),
(7, 11, 5, '2025-04-02', '2025-04-05', '2025-04-02 14:00:00', '2025-04-05 12:00:00', '2025-03-28 10:00:00', 'CheckedOut', 'Paid', 'Khách hàng đi công tác'),
(8, 3, 1, '2025-04-18', '2025-04-20', '2025-04-18 19:00:00', '2025-04-20 11:00:00', '2025-04-10 15:30:00', 'CheckedOut', 'Paid', NULL),
(9, 14, 3, '2025-05-15', '2025-05-20', '2025-05-15 14:20:00', '2025-05-20 11:50:00', '2025-05-05 18:00:00', 'CheckedOut', 'Paid', 'Yêu cầu trang trí sinh nhật'),
(10, 5, 2, '2025-05-28', '2025-05-30', '2025-05-28 16:00:00', '2025-05-30 12:00:00', '2025-05-22 09:45:00', 'CheckedOut', 'Paid', NULL),
(11, 6, 4, '2025-06-10', '2025-06-14', '2025-06-10 15:15:00', '2025-06-14 10:30:00', '2025-06-01 11:00:00', 'CheckedOut', 'Paid', NULL),
(12, 15, 5, '2025-06-20', '2025-06-23', '2025-06-20 14:00:00', '2025-06-23 12:00:00', '2025-06-15 13:00:00', 'CheckedOut', 'Paid', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `CustomerId` int(11) NOT NULL,
  `FullName` varchar(100) NOT NULL,
  `IDCardNumber` varchar(20) DEFAULT NULL,
  `PhoneNumber` varchar(20) DEFAULT NULL,
  `Address` varchar(200) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`CustomerId`, `FullName`, `IDCardNumber`, `PhoneNumber`, `Address`, `Email`) VALUES
(1, 'Nguyễn Thị An', '038123456789', '0912345678', 'Số 1, Đường Nguyễn Trãi, Thanh Xuân, Hà Nội', 'an.nguyen@email.com'),
(2, 'Trần Văn Bình', '045098765432', '0987654321', 'Số 10, Đường Lê Lợi, Hoàn Kiếm, Hà Nội', 'binh.tran@email.com'),
(3, 'Lê Thị Châu', '052123450987', '0905558888', '25/5, Đường 3/2, Quận 10, TP.HCM', 'chau.le@email.com'),
(4, 'Phạm Minh Dũng', '079876543210', '0934123789', '100, Đường Hùng Vương, Hải Châu, Đà Nẵng', 'dung.pham@email.com'),
(5, 'Hoàng Văn Giang', '091234876512', '0969112233', 'Số 55, Đường Cầu Giấy, Cầu Giấy, Hà Nội', 'giang.hoang@email.com');

-- --------------------------------------------------------

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `InvoiceId` int(11) NOT NULL,
  `BookingId` int(11) NOT NULL,
  `CustomerId` int(11) NOT NULL,
  `IssueDate` datetime DEFAULT current_timestamp(),
  `TotalRoomCharge` decimal(10,2) NOT NULL,
  `TotalServiceCharge` decimal(10,2) NOT NULL,
  `GrandTotal` decimal(10,2) NOT NULL,
  `PaymentMethod` varchar(50) DEFAULT NULL,
  `PaymentDate` datetime DEFAULT NULL,
  `IssuedByUserId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `invoices`
--

INSERT INTO `invoices` (`InvoiceId`, `BookingId`, `CustomerId`, `IssueDate`, `TotalRoomCharge`, `TotalServiceCharge`, `GrandTotal`, `PaymentMethod`, `PaymentDate`, `IssuedByUserId`) VALUES
(1, 1, 1, '2025-01-13 11:35:00', 150.00, 85.00, 235.00, 'Tiền mặt', '2025-01-13 11:35:00', 1),
(2, 2, 2, '2025-01-25 12:05:00', 400.00, 95.00, 495.00, 'Thẻ tín dụng', '2025-01-25 12:05:00', 2),
(3, 3, 3, '2025-02-10 10:05:00', 750.00, 90.00, 840.00, 'Chuyển khoản', '2025-02-10 10:05:00', 1),
(4, 1, 1, '2025-02-22 11:20:00', 104.00, 30.00, 134.00, 'Thẻ tín dụng', '2025-02-22 11:20:00', 2),
(5, 4, 4, '2025-03-14 12:15:00', 340.00, 76.00, 416.00, 'Tiền mặt', '2025-03-14 12:15:00', 1),
(6, 2, 2, '2025-03-27 11:45:00', 100.00, 0.00, 100.00, 'Chuyển khoản', '2025-03-27 11:45:00', 2),
(7, 5, 5, '2025-04-05 12:05:00', 249.00, 45.00, 294.00, 'Thẻ tín dụng', '2025-04-05 12:05:00', 1),
(8, 1, 1, '2025-04-20 11:05:00', 110.00, 0.00, 110.00, 'Tiền mặt', '2025-04-20 11:05:00', 2),
(9, 3, 3, '2025-05-20 12:00:00', 800.00, 100.00, 900.00, 'Chuyển khoản', '2025-05-20 12:00:00', 1),
(10, 2, 2, '2025-05-30 12:05:00', 100.00, 30.00, 130.00, 'Tiền mặt', '2025-05-30 12:05:00', 1),
(11, 4, 4, '2025-06-14 10:35:00', 216.00, 0.00, 216.00, 'Thẻ tín dụng', '2025-06-14 10:35:00', 2),
(12, 5, 5, '2025-06-23 12:10:00', 465.00, 85.00, 550.00, 'Chuyển khoản', '2025-06-23 12:10:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `RoomId` int(11) NOT NULL,
  `RoomNumber` varchar(10) NOT NULL,
  `RoomType` varchar(50) NOT NULL,
  `PricePerNight` decimal(10,2) NOT NULL,
  `Status` varchar(20) NOT NULL,
  `RoomDescription` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`RoomId`, `RoomNumber`, `RoomType`, `PricePerNight`, `Status`, `RoomDescription`) VALUES
(1, '101', 'Standard', 50.00, 'Available', 'Phòng tiêu chuẩn 1 giường đôi'),
(2, '102', 'Standard', 50.00, 'Available', 'Phòng tiêu chuẩn 2 giường đơn'),
(3, '103', 'Standard', 55.00, 'Available', 'Phòng tiêu chuẩn view vườn'),
(4, '201', 'Standard', 52.00, 'Available', 'Phòng tiêu chuẩn có ban công'),
(5, '202', 'Standard', 50.00, 'Available', 'Phòng tiêu chuẩn cạnh thang máy'),
(6, '203', 'Standard', 54.00, 'Available', 'Phòng tiêu chuẩn tầng 2 yên tĩnh'),
(7, '301', 'Deluxe', 80.00, 'Available', 'Phòng hạng sang có view biển'),
(8, '302', 'Deluxe', 85.00, 'Available', 'Phòng hạng sang giường lớn'),
(9, '303', 'Deluxe', 82.00, 'Available', 'Phòng Deluxe với bàn làm việc'),
(10, '401', 'Deluxe', 80.00, 'Available', 'Phòng Deluxe ban công rộng'),
(11, '402', 'Deluxe', 83.00, 'Available', 'Phòng hạng sang gần thang máy'),
(12, '403', 'Deluxe', 81.00, 'Available', 'Phòng Deluxe cho gia đình'),
(13, '501', 'Suite', 150.00, 'Available', 'Phòng Suite rộng rãi với phòng khách riêng'),
(14, '502', 'Suite', 160.00, 'Available', 'Phòng Suite cao cấp có bồn tắm'),
(15, '503', 'Suite', 155.00, 'Available', 'Phòng Suite tầng cao nhất, view toàn cảnh');

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `ServiceId` int(11) NOT NULL,
  `ServiceName` varchar(100) NOT NULL,
  `Price` decimal(10,2) NOT NULL,
  `Description` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`ServiceId`, `ServiceName`, `Price`, `Description`) VALUES
(1, 'Laundry', 20.00, 'Dịch vụ giặt là và ủi quần áo'),
(2, 'Breakfast Buffet', 15.00, 'Buffet sáng tự chọn'),
(3, 'Mini Bar - Water', 2.00, 'Nước suối đóng chai'),
(4, 'Extra Bed', 30.00, 'Giường phụ');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserId` int(11) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `PasswordHash` varchar(256) NOT NULL,
  `FullName` varchar(100) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `PhoneNumber` varchar(20) DEFAULT NULL,
  `Role` varchar(20) NOT NULL DEFAULT 'Staff'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserId`, `Username`, `PasswordHash`, `FullName`, `Email`, `PhoneNumber`, `Role`) VALUES
(1, 'admin', '123456', 'Nguyễn Văn A', 'admin@hotel.com', '0901234567', 'Admin'),
(2, 'staff1', 'password', 'Trần Thị B', 'staff1@hotel.com', '0912345678', 'Staff');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookedservices`
--
ALTER TABLE `bookedservices`
  ADD PRIMARY KEY (`BookedServiceId`),
  ADD KEY `BookingId` (`BookingId`),
  ADD KEY `ServiceId` (`ServiceId`);

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`BookingId`),
  ADD KEY `RoomId` (`RoomId`),
  ADD KEY `CustomerId` (`CustomerId`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`CustomerId`),
  ADD UNIQUE KEY `IDCardNumber` (`IDCardNumber`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`InvoiceId`),
  ADD UNIQUE KEY `BookingId` (`BookingId`),
  ADD KEY `CustomerId` (`CustomerId`),
  ADD KEY `IssuedByUserId` (`IssuedByUserId`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`RoomId`),
  ADD UNIQUE KEY `RoomNumber` (`RoomNumber`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`ServiceId`),
  ADD UNIQUE KEY `ServiceName` (`ServiceName`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserId`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookedservices`
--
ALTER TABLE `bookedservices`
  MODIFY `BookedServiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `BookingId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `CustomerId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `invoices`
--
ALTER TABLE `invoices`
  MODIFY `InvoiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `RoomId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `ServiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookedservices`
--
ALTER TABLE `bookedservices`
  ADD CONSTRAINT `bookedservices_ibfk_1` FOREIGN KEY (`BookingId`) REFERENCES `bookings` (`BookingId`),
  ADD CONSTRAINT `bookedservices_ibfk_2` FOREIGN KEY (`ServiceId`) REFERENCES `services` (`ServiceId`);

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`RoomId`) REFERENCES `rooms` (`RoomId`),
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`CustomerId`) REFERENCES `customers` (`CustomerId`);

--
-- Constraints for table `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`BookingId`) REFERENCES `bookings` (`BookingId`),
  ADD CONSTRAINT `invoices_ibfk_2` FOREIGN KEY (`CustomerId`) REFERENCES `customers` (`CustomerId`),
  ADD CONSTRAINT `invoices_ibfk_3` FOREIGN KEY (`IssuedByUserId`) REFERENCES `users` (`UserId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;