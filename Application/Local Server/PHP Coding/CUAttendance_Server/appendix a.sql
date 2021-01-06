-- phpMyAdmin SQL Dump
-- version 4.5.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 21, 2018 at 03:17 PM
-- Server version: 5.7.11
-- PHP Version: 5.6.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mut_attendance`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `class_date` date NOT NULL,
  `status` int(11) NOT NULL,
  `comment` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `class`
--

CREATE TABLE `class` (
  `id` int(11) NOT NULL,
  `code` text NOT NULL,
  `room` text NOT NULL,
  `instructor_id` int(11) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `mon` tinyint(1) NOT NULL DEFAULT '0',
  `tue` tinyint(1) NOT NULL DEFAULT '0',
  `wed` tinyint(1) NOT NULL DEFAULT '0',
  `thu` tinyint(1) NOT NULL DEFAULT '0',
  `fri` tinyint(1) NOT NULL DEFAULT '0',
  `sat` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `class`
--

INSERT INTO `class` (`id`, `code`, `room`, `instructor_id`, `start_time`, `end_time`, `mon`, `tue`, `wed`, `thu`, `fri`, `sat`) VALUES
(1, 'TEST COURSE 101', 'ROOM1', 10, '13:00:00', '14:50:00', 1, 0, 1, 0, 1, 0),
(2, 'ENG 201', 'ROOM2', 10, '09:30:00', '10:45:00', 1, 0, 1, 0, 0, 0),
(3, 'MATH 202', 'ROOM3', 12, '11:00:00', '12:15:00', 1, 0, 1, 0, 0, 0),
(4, 'PHY 205', 'ROOM3', 13, '09:30:00', '10:45:00', 0, 1, 0, 1, 0, 0),
(5, 'MATH 222', 'ROOM4', 14, '14:00:00', '15:15:00', 0, 1, 0, 1, 0, 0),
(6, 'ELEC 221L', 'ROOM1', 15, '08:00:00', '10:00:00', 0, 0, 0, 0, 1, 0),
(11, 'ARB 201', 'ROOM1', 10, '08:00:00', '09:15:00', 1, 0, 1, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `class_student`
--

CREATE TABLE `class_student` (
  `id` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `class_student`
--

INSERT INTO `class_student` (`id`, `class_id`, `student_id`, `status`) VALUES
(1000, 1, 100, 1),
(1001, 2, 100, 1),
(1002, 3, 100, 1),
(1003, 4, 100, 1),
(1004, 5, 100, 1),
(1005, 6, 100, 1),
(1006, 1, 101, 1),
(1007, 2, 101, 1),
(1008, 3, 101, 1),
(1009, 4, 101, 1),
(1010, 5, 101, 1),
(1011, 6, 101, 1),
(1012, 1, 102, 1),
(1013, 2, 102, 1),
(1014, 3, 102, 1),
(1015, 4, 102, 1),
(1016, 5, 102, 1),
(1017, 6, 102, 1),
(1018, 1, 103, 1),
(1019, 2, 103, 1),
(1020, 3, 103, 1),
(1021, 4, 103, 1),
(1022, 5, 103, 1),
(1023, 6, 103, 1),
(1024, 1, 104, 1),
(1025, 2, 104, 1),
(1026, 3, 104, 1),
(1027, 4, 104, 1),
(1028, 5, 104, 1),
(1029, 6, 104, 1),
(1030, 1, 105, 1),
(1031, 2, 105, 1),
(1032, 3, 105, 1),
(1033, 4, 105, 1),
(1034, 5, 105, 1),
(1035, 6, 105, 1),
(1036, 1, 106, 1),
(1037, 2, 106, 1),
(1038, 3, 106, 1),
(1039, 4, 106, 1),
(1040, 5, 106, 1),
(1041, 6, 106, 1),
(1042, 1, 107, 1),
(1043, 2, 107, 1),
(1044, 3, 107, 1),
(1045, 4, 107, 1),
(1046, 5, 107, 1),
(1047, 6, 107, 1),
(1048, 2, 108, 1),
(1049, 4, 108, 1),
(1050, 6, 108, 1),
(1051, 1, 109, 1),
(1052, 3, 109, 1),
(1053, 5, 109, 1),
(1054, 11, 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `instructors`
--

CREATE TABLE `instructors` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `phone` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instructors`
--

INSERT INTO `instructors` (`id`, `name`, `phone`, `email`, `password`) VALUES
(10, 'Walid Kamali', '+96170139077', 'walid.kamali@gmail.com', 'e10adc3949ba59abbe56e057f20f883e'),
(11, 'Lina Nachabe', '+9613320587', 'lina_nachabe@yahoo.com', 'e10adc3949ba59abbe56e057f20f883e'),
(12, 'Bassel El-Haj', '+9613785456', 'hajbassel@live.com ', 'e10adc3949ba59abbe56e057f20f883e'),
(13, 'Aladin Hamzeh', '+96171433312', 'aladin.hamzeh@gmail.com', 'e10adc3949ba59abbe56e057f20f883e'),
(14, 'Mohammad Al-Kadiri', '+96178822998', 'mohammad.kadiri.mk@outlook.com', 'e10adc3949ba59abbe56e057f20f883e'),
(15, 'Moustafa Jalloul', '+96170382780', 'steef_jalloul@hotmail.com', 'e10adc3949ba59abbe56e057f20f883e');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `phone` text NOT NULL,
  `email` text NOT NULL,
  `tag_id` text NOT NULL,
  `image` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `name`, `phone`, `email`, `tag_id`, `image`) VALUES
(100, 'Ibrahim Maassarani', '+96176319386', 'ibrahim.maassarani@gmail.com', 'd188b26c', ''),
(101, 'Nazir Jazzar', '+96171543782', 'nazir-jazzar@hotmail.com', 'dbdf38b4', ''),
(102, 'Abed AlRahman AlZahed', '+9613123456', 'abed.zahed@gmail.com', 'd19c497c', ''),
(103, 'Ghassan Jaem', '+96170123456', 'jaem-ghassan@gmail.com', 'd187267c', ''),
(104, 'Omar Sayadi', '+96171123456', 'omar-sayadi@gmail.com', 'd190d4ec', ''),
(105, 'Wissam Dabbussy', '+9613431219', 'wissam-2000@hotmail.com', 'd186f51c', ''),
(106, 'Farouk Maassarani', '+96176123456', 'farouk-maassarani@gmail.com', 'c4a965bc', ''),
(107, 'Bilal Moubayed', '+9613098765', 'moubayed.bilal@gmail.com', 'd19ac3fc', ''),
(108, 'Ali Abbas', '+96176476192', 'ali-abbas@gmail.com', 'd18a4eac', ''),
(109, 'Alaa Rifi', '+96176876213', 'rifi-alaa@gmail.com', 'd188b36c', '');

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `phone` text NOT NULL,
  `mut_id` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `test_table`
--

CREATE TABLE `test_table` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `phone` text NOT NULL,
  `mut_id` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `class_student`
--
ALTER TABLE `class_student`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `instructors`
--
ALTER TABLE `instructors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test_table`
--
ALTER TABLE `test_table`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `class`
--
ALTER TABLE `class`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `class_student`
--
ALTER TABLE `class_student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1055;
--
-- AUTO_INCREMENT for table `instructors`
--
ALTER TABLE `instructors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;
--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `test_table`
--
ALTER TABLE `test_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
