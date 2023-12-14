DROP DATABASE IF EXISTS ziot;
CREATE DATABASE ziot;
USE ziot;
CREATE TABLE `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    phone VARCHAR(16),
    location VARCHAR(128),
    sex INT,
    address VARCHAR(128)
);
CREATE TABLE device (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    state INT NOT NULL,
    owner_id INT NOT NULL,
    description VARCHAR(256),
    type INT NOT NULL,
    activate_time TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES `user`(id)
);
CREATE TABLE message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    device_id INT NOT NULL,
    time TIMESTAMP NOT NULL,
    info VARCHAR(256) NOT NULL,
    value INT NOT NULL,
    alert INT NOT NULL DEFAULT 0,
    lat DOUBLE NOT NULL,
    lng DOUBLE NOT NULL,
    FOREIGN KEY (device_id) REFERENCES device(id)
);
-- 插入测试数据，device_id 为 1 或 2
INSERT INTO message (device_id, time, info, value, alert, lat, lng) VALUES
                                                                        (2, '2023-11-29 08:30:00', 'Info 1', 50, 0, 116.101, 39.90923),
                                                                        (2, '2023-11-29 09:00:00', 'Info 2', 75, 1, 116.102, 39.90929),
                                                                        (2, '2023-11-29 10:30:00', 'Info 3', 60, 1, 116.103, 39.90933);
INSERT INTO message (device_id, time, info, value, alert, lat, lng) VALUES
                                                                        (2, '2023-11-29 11:30:00', 'Info 1', 50, 0, 116.104, 39.90988),
                                                                        (2, '2023-11-29 12:00:00', 'Info 2', 75, 1, 116.1055, 39.92),
                                                                        (2, '2023-11-29 13:30:00', 'Info 3', 60, 1, 116.1088, 39.97);
delete from message where device_id=2;