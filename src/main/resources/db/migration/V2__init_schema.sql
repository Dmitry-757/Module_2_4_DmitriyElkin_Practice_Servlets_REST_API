CREATE TABLE `event_tbl` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `file_id` int DEFAULT NULL,
                         `user_id` int DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_5y8r3joev3w5e34a2d3q4yimr` (`file_id`),
                         KEY `FK844jssbw5suwqemtrl8bq17ot` (`user_id`)
#                          ,CONSTRAINT `FK50qah7vdkftk2mc2xcee0pgn1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `file_tbl` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `filePath` varchar(255) DEFAULT NULL,
                        `name` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_tbl` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_event` (
                              `User_id` int NOT NULL,
                              `events_id` int NOT NULL,
                              UNIQUE KEY `UK_8ykfbpujv0y0uob473knmb826` (`events_id`),
                              KEY `FKbup8ae7piwnd86uhpd2dsl294` (`User_id`)
#                               ,CONSTRAINT `FKbup8ae7piwnd86uhpd2dsl294` FOREIGN KEY (`User_id`) REFERENCES `user_tbl` (`id`),
#                               CONSTRAINT `FKkkbk94vesij2nqd2rh10m3r08` FOREIGN KEY (`events_id`) REFERENCES `event_tbl` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

alter table `event_tbl` add CONSTRAINT `FK844jssbw5suwqemtrl8bq17ot` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`id`);
alter table `event_tbl` add CONSTRAINT `FKl2sbwm7pj8n6pm4x300le65mg` FOREIGN KEY (`file_id`) REFERENCES `file_tbl` (`id`);

alter table `user_event` add constraint `FKbup8ae7piwnd86uhpd2dsl294` FOREIGN KEY (`User_id`) REFERENCES `user_tbl` (`id`);
alter table `user_event` add constraint `FKkkbk94vesij2nqd2rh10m3r08` FOREIGN KEY (`events_id`) REFERENCES `event_tbl` (`id`)