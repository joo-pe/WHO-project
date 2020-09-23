-- phpMyAdmin SQL Dump
-- version 4.7.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jul 09, 2018 at 07:00 PM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `post_board`
--
CREATE DATABASE IF NOT EXISTS `post_board` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `post_board`;

-- --------------------------------------------------------

--
-- Table structure for table `board`
--

CREATE TABLE `board` (
  `idx` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `pw` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `date` date NOT NULL,
  `hit` int(11) NOT NULL,
  `lock_post` int(11) NOT NULL,
  `file` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `board`
--

INSERT INTO `board` (`idx`, `name`, `pw`, `title`, `content`, `date`, `hit`, `lock_post`, `file`) VALUES
(1, 'S Writer', '', '자유게시판입니다.', '자유게시판입니다.', '2016-07-02', 0, 0, ''),
(2, 'S Writer', '', '자유게시판입니다.', '자유게시판입니다.', '2016-07-02', 22, 0, ''),
(3, 'S Writer', '', '자유게시판입니다.', '자유게시판입니다.', '2016-07-02', 22, 0, ''),
(4, 'S Writer', '', '자유게시판입니다.', '자유게시판입니다.', '2016-07-02', 17, 0, ''),
(5, 'S Writer', '$2y$10$htZzrEFYowOoqrXWVYb9euRSIrIQ78Mn2st2LJ23fj7fA2TflG8Si', '자유게d', '자유게시판입니다.', '2016-07-02', 8, 1, ''),
(7, 'admin', '$2y$10$quqJ2psn9yJ39X3LQwuGs.brhUwCpWNaElEn4/3srnjCQXdoyc3P.', '댓글 테스트를 위한 글!', '댓글 테스트를 위한 글입니다!', '2018-04-02', 15, 0, ''),
(8, '관리자', '$2y$10$.sgBeP7rnSWO029moZL7IOvDeqEbqqS4Ex4xZf/F1NikawD79Dgei', '파일 업로드', '파일 업로드', '2018-05-12', 8, 0, '새 텍스트 문서.txt');

-- --------------------------------------------------------

--
-- Table structure for table `reply`
--

CREATE TABLE `reply` (
  `idx` int(11) NOT NULL,
  `con_num` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `pw` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reply`
--

INSERT INTO `reply` (`idx`, `con_num`, `name`, `pw`, `content`, `date`) VALUES
(1, 7, 'admin', '$2y$10$dBSVM444ar73VYjpnlHtKu7EftoRte2ssIFL6a6HpwvoYc/f/aJFW', '1234', '2018-04-02 19:47:19'),
(2, 7, 'admin', '$2y$10$XhZDVxia4W/xGpJ.Y8iATOSyERTS4Av7PR.uDnAoHF3q1gNyPH7dW', '댓글 테스트', '2018-04-02 19:47:26');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `board`
--
ALTER TABLE `board`
  ADD PRIMARY KEY (`idx`);

--
-- Indexes for table `reply`
--
ALTER TABLE `reply`
  ADD PRIMARY KEY (`idx`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `board`
--
ALTER TABLE `board`
  MODIFY `idx` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `reply`
--
ALTER TABLE `reply`
  MODIFY `idx` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
