DROP TABLE IF EXISTS note_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS notebook;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id           INT AUTO_INCREMENT PRIMARY KEY,
                       username     VARCHAR(50) NOT NULL UNIQUE,
                       password     VARCHAR(255) NOT NULL,
                       email        VARCHAR(100) NOT NULL UNIQUE,
                       created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE notebook (
                          id INT NOT NULL AUTO_INCREMENT,
                          user_id INT NOT NULL,
                          title VARCHAR(255) NOT NULL,
                          content TEXT NOT NULL,
                          create_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (id),
                          KEY user_id (user_id),
                          CONSTRAINT notebook_ibfk_1 FOREIGN KEY (user_id)
                              REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE tags (
                      id INT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE note_tags (
                           note_id INT NOT NULL,
                           tag_id INT NOT NULL,
                           PRIMARY KEY (note_id, tag_id),
                           CONSTRAINT note_tags_ibfk_1 FOREIGN KEY (note_id) REFERENCES notebook(id) ON DELETE CASCADE,
                           CONSTRAINT note_tags_ibfk_2 FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



