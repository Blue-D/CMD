-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2017-12-04 13:39:08
-- 服务器版本： 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `cms`
--

-- --------------------------------------------------------

--
-- 表的结构 `advisor`
--

CREATE TABLE IF NOT EXISTS `advisor` (
  `TNO` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `ANA` varchar(20) DEFAULT NULL,
  `Atele` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `Aemail` varchar(20) CHARACTER SET latin1 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `advisor`
--

INSERT INTO `advisor` (`TNO`, `ANA`, `Atele`, `Aemail`) VALUES
('201701001', '李招辉', '21312314', 'adasda123141');

-- --------------------------------------------------------

--
-- 表的结构 `budget`
--

CREATE TABLE IF NOT EXISTS `budget` (
  `TNO` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `Budget` double DEFAULT NULL,
  `Ispassed` int(11) DEFAULT '0',
  `Reason` varchar(100) DEFAULT NULL,
  `BFileUrl` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `competition`
--

CREATE TABLE IF NOT EXISTS `competition` (
`CNO` int(11) NOT NULL,
  `CNA` varchar(20) DEFAULT NULL,
  `SOG` varchar(2) DEFAULT NULL,
  `Level` varchar(20) DEFAULT NULL,
  `RegInpro` int(11) DEFAULT NULL,
  `MaxTeamMember` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `competition`
--

INSERT INTO `competition` (`CNO`, `CNA`, `SOG`, `Level`, `RegInpro`, `MaxTeamMember`) VALUES
(1, '中国软件杯', 'G', '一级竞赛', 1, 0);

-- --------------------------------------------------------

--
-- 表的结构 `logininf`
--

CREATE TABLE IF NOT EXISTS `logininf` (
  `UserNum` varchar(20) CHARACTER SET latin1 NOT NULL,
  `Pass` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `Jur` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `logininf`
--

INSERT INTO `logininf` (`UserNum`, `Pass`, `Jur`) VALUES
('20171001', '012100430', 1);

-- --------------------------------------------------------

--
-- 表的结构 `precompetitiondata`
--

CREATE TABLE IF NOT EXISTS `precompetitiondata` (
  `TNO` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `PhoUrl` varchar(40) DEFAULT NULL,
  `VideoUrl` varchar(40) DEFAULT NULL,
  `DocUrl` varchar(40) DEFAULT NULL,
  `CerUrl` varchar(40) DEFAULT NULL,
  `IsPassed` int(11) DEFAULT '0',
  `Reason` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `prizer`
--

CREATE TABLE IF NOT EXISTS `prizer` (
  `TNO` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `CNO` int(11) DEFAULT NULL,
  `Year` year(4) DEFAULT NULL,
  `Prize` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `reimbursement`
--

CREATE TABLE IF NOT EXISTS `reimbursement` (
  `TNO` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `SMoney` double DEFAULT NULL,
  `RMoney` double DEFAULT NULL,
  `IsPassed` int(11) DEFAULT '0',
  `RFileUrl` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `reimbursement`
--

INSERT INTO `reimbursement` (`TNO`, `SMoney`, `RMoney`, `IsPassed`, `RFileUrl`) VALUES
('201701001', NULL, NULL, 0, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `student`
--

CREATE TABLE IF NOT EXISTS `student` (
  `SNA` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `SNO` varchar(20) NOT NULL,
  `Major` varchar(20) DEFAULT NULL,
  `tele` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `email` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ID` varchar(20) CHARACTER SET latin1 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `student`
--

INSERT INTO `student` (`SNA`, `SNO`, `Major`, `tele`, `email`, `ID`) VALUES
('豆豆', '1010101010', '计科', '13062990207', '859732541@qq.com', '321202199610050612');

-- --------------------------------------------------------

--
-- 表的结构 `team`
--

CREATE TABLE IF NOT EXISTS `team` (
  `TNO` varchar(20) CHARACTER SET latin1 NOT NULL,
  `TNA` varchar(20) DEFAULT NULL,
  `CaptainSNO` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `CNO` int(20) DEFAULT NULL,
  `Year` year(4) DEFAULT NULL,
  `TIsPassed` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `team`
--

INSERT INTO `team` (`TNO`, `TNA`, `CaptainSNO`, `CNO`, `Year`, `TIsPassed`) VALUES
('201701001', '蓝蓝的天', '1562810210', 1, 2017, 0);

-- --------------------------------------------------------

--
-- 表的结构 `teamadvisor`
--

CREATE TABLE IF NOT EXISTS `teamadvisor` (
  `TNO` varchar(20) DEFAULT NULL,
  `ANO` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `teammember`
--

CREATE TABLE IF NOT EXISTS `teammember` (
  `TNO` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `SNO` varchar(20) NOT NULL,
  `IsPassed` int(11) DEFAULT '0',
  `Reason` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `teammember`
--

INSERT INTO `teammember` (`TNO`, `SNO`, `IsPassed`, `Reason`) VALUES
('201701001', '1010101010', 0, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `advisor`
--
ALTER TABLE `advisor`
 ADD PRIMARY KEY (`TNO`);

--
-- Indexes for table `competition`
--
ALTER TABLE `competition`
 ADD PRIMARY KEY (`CNO`), ADD UNIQUE KEY `CNA` (`CNA`);

--
-- Indexes for table `logininf`
--
ALTER TABLE `logininf`
 ADD PRIMARY KEY (`UserNum`);

--
-- Indexes for table `prizer`
--
ALTER TABLE `prizer`
 ADD KEY `fk_2` (`CNO`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
 ADD PRIMARY KEY (`SNO`), ADD UNIQUE KEY `SNO` (`SNO`);

--
-- Indexes for table `team`
--
ALTER TABLE `team`
 ADD PRIMARY KEY (`TNO`), ADD KEY `fk_1` (`CNO`);

--
-- Indexes for table `teammember`
--
ALTER TABLE `teammember`
 ADD KEY `fk_3` (`SNO`), ADD KEY `fk_4` (`TNO`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `competition`
--
ALTER TABLE `competition`
MODIFY `CNO` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- 限制导出的表
--

--
-- 限制表 `prizer`
--
ALTER TABLE `prizer`
ADD CONSTRAINT `fk_2` FOREIGN KEY (`CNO`) REFERENCES `competition` (`CNO`);

--
-- 限制表 `team`
--
ALTER TABLE `team`
ADD CONSTRAINT `fk_1` FOREIGN KEY (`CNO`) REFERENCES `competition` (`CNO`);

--
-- 限制表 `teammember`
--
ALTER TABLE `teammember`
ADD CONSTRAINT `fk_3` FOREIGN KEY (`SNO`) REFERENCES `student` (`SNO`) ON UPDATE CASCADE,
ADD CONSTRAINT `fk_4` FOREIGN KEY (`TNO`) REFERENCES `team` (`TNO`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
