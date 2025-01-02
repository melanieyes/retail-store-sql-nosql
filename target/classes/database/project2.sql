CREATE DATABASE IF NOT EXISTS store;
USE store;

-- Administration
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    Role VARCHAR(50) DEFAULT 'user'
);

-- Insert Admin key info
INSERT INTO Users (Username, Password, Role) VALUES
    ('melanie', 'database', 'admin'),
    ('milan', 'access', 'manager');

CREATE TABLE ProdCateg (
    ProdCatID INT AUTO_INCREMENT PRIMARY KEY,
    CatName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Suppliers (
    SuppID INT AUTO_INCREMENT PRIMARY KEY,
    SuppName VARCHAR(100) NOT NULL,
    ContactP VARCHAR(100),
    Phone VARCHAR(20),
    Address TEXT,
    Status VARCHAR(20) DEFAULT 'Active'
);

CREATE TABLE Products (
    ProdsID INT AUTO_INCREMENT PRIMARY KEY,
    ProdsName VARCHAR(100) NOT NULL,
    ProdCatID INT,
    Price DECIMAL(12, 2) NOT NULL,
    QtyStock INT DEFAULT 0,
    SuppID INT,
    FOREIGN KEY (ProdCatID) REFERENCES ProdCateg(ProdCatID) ON DELETE SET NULL,
    FOREIGN KEY (SuppID) REFERENCES Suppliers(SuppID) ON DELETE SET NULL
);

-- Insert data into ProdCateg table
INSERT INTO ProdCateg (CatName) VALUES
    ('Smartphones & Tablets'),
    ('Laptops & Computers'),
    ('Smart Home Devices'),
    ('Audio & Accessories'),
    ('Cameras & Photography'),
    ('Gaming & VR'),
    ('Drones & Robotics');

-- Insert data into Suppliers table
INSERT INTO Suppliers (SuppName, ContactP, Phone, Address, Status) VALUES
    ('TechWorld Inc.', 'Alice Johnson', '555-1234', '123 Tech Avenue, Silicon Valley, CA', 'Active'),
    ('TechGlobal Corp.', 'Daniel Carter', '555-2345', '789 Innovation Drive, Seattle, WA', 'Active'),
    ('NextGen Tech', 'Sophia Brown', '555-6789', '456 Digital Way, Austin, TX', 'Active'),
    ('CloudSolutions Ltd.', 'Michael Green', '555-5670', '321 Cloud Park, Denver, CO', 'Active'),
    ('DataTech Partners', 'Olivia Taylor', '555-7891', '654 Data Lane, Boston, MA', 'Active'),
    ('EYEAI', 'Emma Wilson', '555-8902', '987 AI Blvd, Palo Alto, CA', 'Active');

-- Insert data into Products table
INSERT INTO Products (ProdsName, SuppID, Price, QtyStock, ProdCatID) VALUES
    ('Smartphone XYZ', 1, 699.99, 50, 1),
    ('Laptop ABC', 1, 1199.99, 30, 2),
    ('Wireless Headphones', 2, 149.99, 60, 4),
    ('Tablet Pro 10.5', 3, 499.99, 40, 1),
    ('Smartwatch Series 5', 3, 299.99, 70, 1),
    ('Gaming Laptop Ultra', 4, 1599.99, 25, 2),
    ('4K Ultra HD Smart TV', 4, 999.99, 20, 3),
    ('External Hard Drive 2TB', 5, 89.99, 100, 2),
    ('Bluetooth Speaker X', 5, 129.99, 80, 4),
    ('Wireless Gaming Mouse', 6, 79.99, 90, 4),
    ('Mechanical Keyboard Pro', 6, 119.99, 60, 4),
    ('Drone with HD Camera', 6, 499.99, 30, 7),
    ('Virtual Reality Headset', 6, 349.99, 40, 6),
    ('Smart Home Hub', 6, 199.99, 50, 3),
    ('Noise-Canceling Headphones', 6, 249.99, 60, 4),
    ('Portable Power Bank 20,000mAh', 6, 39.99, 150, 4),
    ('Digital Camera Pro', 6, 1299.99, 15, 5),
    ('3D Printer', 6, 899.99, 10, 5),
    ('Smart Thermostat', 6, 199.99, 25, 3),
    ('Action Camera 4K', 6, 199.99, 35, 5),
    ('Streaming Stick 4K', 6, 49.99, 100, 3),
    ('Electric Scooter', 6, 499.99, 20, 6),
    ('Smart Doorbell Camera', 6, 149.99, 40, 3),
    ('Home Security System', 6, 399.99, 15, 3),
    ('Wireless Charging Pad', 6, 29.99, 200, 4),
    ('Portable Bluetooth Projector', 6, 299.99, 20, 3),
    ('Smart Light Bulbs (4-Pack)', 6, 49.99, 150, 3),
    ('Fitness Tracker Band', 6, 99.99, 80, 4);
