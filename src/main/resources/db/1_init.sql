CREATE TABLE student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nim VARCHAR(10) UNIQUE,
    full_name VARCHAR(50),
    address TEXT,
    date_of_birth DATE,
    password TEXT not null,
    role TEXT not null
    );
