-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: localhost    Database: QR
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `qr_session_id` bigint NOT NULL,
  `marked_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PRESENT','LATE','ABSENT') DEFAULT 'PRESENT',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_attendance` (`student_id`,`qr_session_id`),
  KEY `course_id` (`course_id`),
  KEY `idx_student_course` (`student_id`,`course_id`),
  KEY `idx_session` (`qr_session_id`),
  KEY `idx_marked_at` (`marked_at`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attendance_ibfk_3` FOREIGN KEY (`qr_session_id`) REFERENCES `qr_session` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (1,2,2,4,'2026-02-11 08:47:14','PRESENT'),(2,2,2,5,'2026-02-11 08:54:56','PRESENT');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_code` varchar(20) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `description` text,
  `credits` int DEFAULT '3',
  `semester` varchar(50) DEFAULT NULL,
  `teacher_id` bigint NOT NULL,
  `department_id` bigint NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `course_code` (`course_code`),
  KEY `department_id` (`department_id`),
  KEY `idx_course_code` (`course_code`),
  KEY `idx_teacher` (`teacher_id`),
  KEY `idx_semester` (`semester`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (2,'CS101','Introduction to Programming',NULL,3,'1',1,1,1,'2026-02-10 18:51:20'),(3,'ACMP446','Human Computer Interaction','',3,'2',1,1,1,'2026-02-17 17:27:05');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `department_id` bigint NOT NULL,
  `department_name` varchar(255) NOT NULL,
  `head_of_department_id` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `head_of_dapartment` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `department_id` (`department_id`),
  KEY `idx_dept_id` (`department_id`),
  KEY `head_of_department_id` (`head_of_department_id`),
  KEY `FK3bi75wn012whtijcesir60wns` (`head_of_dapartment`),
  CONSTRAINT `department_ibfk_1` FOREIGN KEY (`head_of_department_id`) REFERENCES `teacher` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FK3bi75wn012whtijcesir60wns` FOREIGN KEY (`head_of_dapartment`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,1,'Computer Science',NULL,'2026-02-05 11:31:43',NULL,NULL),(2,2,'Mathematics',NULL,'2026-02-05 11:31:43',NULL,NULL),(3,3,'Physics',NULL,'2026-02-05 11:31:43',NULL,NULL);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qr_session`
--

DROP TABLE IF EXISTS `qr_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qr_session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `session_token` varchar(255) NOT NULL,
  `course_id` bigint NOT NULL,
  `teacher_id` bigint NOT NULL,
  `session_name` varchar(100) DEFAULT NULL,
  `start_time` timestamp NOT NULL,
  `expiry_time` timestamp NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `total_scans` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_token` (`session_token`),
  KEY `teacher_id` (`teacher_id`),
  KEY `idx_session_token` (`session_token`),
  KEY `idx_course` (`course_id`),
  KEY `idx_expiry` (`expiry_time`),
  CONSTRAINT `qr_session_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE,
  CONSTRAINT `qr_session_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qr_session`
--

LOCK TABLES `qr_session` WRITE;
/*!40000 ALTER TABLE `qr_session` DISABLE KEYS */;
INSERT INTO `qr_session` VALUES (1,'ed2d8e00-7cdf-43bb-a981-ffe045231ea3',2,1,'Week 1 Lecture','2026-02-10 19:19:55','2026-02-10 19:24:55',0,0),(2,'0a7b3397-16b2-4300-acaa-9cb513e2ad8f',2,1,'Week 1 Lecture','2026-02-10 19:37:02','2026-02-10 19:42:02',0,0),(3,'6754d169-e7a8-46f7-a068-40c7b5fbed01',2,1,'Week 1 Lecture','2026-02-11 08:38:24','2026-02-11 08:43:24',0,0),(4,'5253fef4-0b7b-4e2b-bff6-481c70f57478',2,1,'Week 1 Lecture','2026-02-11 08:46:44','2026-02-11 08:51:44',0,1),(5,'31753666-52fb-4e85-bcd9-35e9da1da86b',2,1,'Week 1 Lecture','2026-02-11 08:53:58','2026-02-11 08:58:58',0,1),(6,'b31c080d-6ee6-4204-8fc8-07d95c494126',2,1,'Week ! Lecture','2026-02-16 16:45:21','2026-02-16 16:50:21',0,0),(7,'54be24d3-7b39-4d40-98b5-1eec1c3ca7e3',2,1,'Test Lecture 1','2026-02-16 16:51:19','2026-02-16 16:56:19',0,0),(8,'d56c1536-5f97-4746-8f2e-365167f60d4f',2,1,'Test 2','2026-02-16 17:09:10','2026-02-16 17:14:10',1,0);
/*!40000 ALTER TABLE `qr_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `student_id` varchar(255) NOT NULL,
  `department_id` bigint NOT NULL,
  `year_of_study` int DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `student_id` (`student_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_department` (`department_id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,3,'SC13/10505/19',1,2,'+124567893'),(2,7,'SC13/07777/21',1,3,'+123456879');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_course_enrollment`
--

DROP TABLE IF EXISTS `student_course_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course_enrollment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `enrollment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('ENROLLED','DROPPED','COMPLETED') DEFAULT 'ENROLLED',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_enrollment` (`student_id`,`course_id`),
  KEY `idx_student` (`student_id`),
  KEY `idx_course` (`course_id`),
  CONSTRAINT `student_course_enrollment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_course_enrollment_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_course_enrollment`
--

LOCK TABLES `student_course_enrollment` WRITE;
/*!40000 ALTER TABLE `student_course_enrollment` DISABLE KEYS */;
INSERT INTO `student_course_enrollment` VALUES (2,2,2,'2026-02-11 08:32:54','ENROLLED');
/*!40000 ALTER TABLE `student_course_enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `employee_id` varchar(255) NOT NULL,
  `department_id` bigint NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `employee_id` (`employee_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_department` (`department_id`),
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `teacher_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,2,'EMP001',1,'+1234567890');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `role` enum('STUDENT','TEACHER','ADMIN') NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_email` (`email`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test@student.com','$2a$10$N9qo8uLOickgx2ZMRZoMye1ZD7VEUNr5VVqPYQbFhO5aElwHqXPm2','Test Student','STUDENT',1,'2026-02-05 09:15:47','2026-02-05 09:15:47'),(2,'teacher1@university.edu','$2a$10$sF1dbjLNs90Cesawx4BPSejizWDKvf4/B/lhFHShu2E7Mr.E6bSty','John Doe','TEACHER',1,'2026-02-05 11:34:02','2026-02-05 11:34:02'),(3,'student2@gmail.com','$2a$10$qhfNg0KEuUwOAsm8hXkAq.O3gRIJmimU86Jwhigxpd1dmjwJXomRa','Desmond Kinuthia','STUDENT',1,'2026-02-05 20:19:53','2026-02-05 20:19:53'),(4,'teacher@test.com','$2a$10$N9qo8uLOickgx2ZMRZoMye1ZD7VEUNr5VVqPYQbFhO5aElwHqXPm2','Dr. Smith','TEACHER',1,'2026-02-10 18:26:39','2026-02-10 18:26:39'),(5,'student1@test.com','$2a$10$N9qo8uLOickgx2ZMRZoMye1ZD7VEUNr5VVqPYQbFhO5aElwHqXPm2','John Doe','STUDENT',1,'2026-02-10 18:26:39','2026-02-10 18:26:39'),(6,'student2@test.com','$2a$10$N9qo8uLOickgx2ZMRZoMye1ZD7VEUNr5VVqPYQbFhO5aElwHqXPm2','Jane Smith','STUDENT',1,'2026-02-10 18:26:39','2026-02-10 18:26:39'),(7,'student12@student.edu','$2a$10$a1EiaFamRpTCgUQF1xxTeuYuExi8w0qUns5ws09VPddSyVoQOEAB6','Firefox Mozilla','STUDENT',1,'2026-02-10 19:26:11','2026-02-10 19:26:11'),(9,'admin@university.edu','$2a$10$sF1dbjLNs90Cesawx4BPSejizWDKvf4/B/lhFHShu2E7Mr.E6bSty','System Admin','ADMIN',1,'2026-02-17 17:11:46','2026-02-17 17:17:23');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-18 15:17:40
