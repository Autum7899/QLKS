-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 28, 2025 at 11:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


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
(30, 1, 1, '2025-06-28', '2025-06-30', '2025-06-28 16:06:23', '2025-06-28 16:22:57', '2025-06-28 14:20:23', 'CheckedOut', 'Pending', NULL),
(31, 4, 1, '2025-06-28', '2025-06-30', '2025-06-28 16:26:29', NULL, '2025-06-28 16:23:19', 'CheckedIn', 'Pending', NULL),
(32, 5, 1, '2025-06-02', '2025-06-19', '2025-06-28 16:26:21', NULL, '2025-06-28 16:23:27', 'CheckedIn', 'Pending', NULL),
(33, 11, 1, '2025-06-10', '2025-06-30', '2025-06-28 16:26:23', NULL, '2025-06-28 16:23:57', 'CheckedIn', 'Pending', NULL),
(34, 12, 1, '2025-06-10', '2025-06-02', '2025-06-28 16:26:26', NULL, '2025-06-28 16:24:06', 'CheckedIn', 'Pending', NULL);

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
(1, 'Phạm Quang C', '123456789012', '0987654321', '123 Đường ABC, Quận 1, TP.HCM', 'phamc@example.com'),
(2, 'Lê Thị D', '234567890123', '0978123456', '456 Đường XYZ, Quận 3, TP.HCM', 'led@example.com');

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
(4, '201', 'Standard', 52.00, 'Booked', 'Phòng tiêu chuẩn có ban công'),
(5, '202', 'Standard', 50.00, 'Booked', 'Phòng tiêu chuẩn cạnh thang máy'),
(6, '203', 'Standard', 54.00, 'Available', 'Phòng tiêu chuẩn tầng 2 yên tĩnh'),
(7, '301', 'Deluxe', 80.00, 'Available', 'Phòng hạng sang có view biển'),
(8, '302', 'Deluxe', 85.00, 'Available', 'Phòng hạng sang giường lớn'),
(9, '303', 'Deluxe', 82.00, 'Available', 'Phòng Deluxe với bàn làm việc'),
(10, '401', 'Deluxe', 80.00, 'Available', 'Phòng Deluxe ban công rộng'),
(11, '402', 'Deluxe', 83.00, 'Booked', 'Phòng hạng sang gần thang máy'),
(12, '403', 'Deluxe', 81.00, 'Booked', 'Phòng Deluxe cho gia đình'),
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
  MODIFY `BookedServiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `BookingId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `CustomerId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `invoices`
--
ALTER TABLE `invoices`
  MODIFY `InvoiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

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
