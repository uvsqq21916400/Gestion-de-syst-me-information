-- TABLE COMPANY
CREATE TABLE COMPANY (
    company_name VARCHAR(100) PRIMARY KEY,
    company_profile TEXT NULL,
    company_size VARCHAR(50) NULL
);

-- TABLE LOCATION
CREATE TABLE LOCATION (
    location_id INT PRIMARY KEY,
    location VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    latitude DECIMAL(9,6) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL
);

-- TABLE JOB
CREATE TABLE JOB (
    job_id INT PRIMARY KEY,
    role VARCHAR(100) NOT NULL,
    work_type VARCHAR(50) NOT NULL,
    salary_range VARCHAR(50) NULL,
    job_posting_date DATE NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    job_portal VARCHAR(100) NULL,
    job_description TEXT NULL,
    responsibilities TEXT NULL,
    benefits TEXT NULL,
    company_name VARCHAR(100) NOT NULL,
    location_id INT NOT NULL,
    FOREIGN KEY (company_name) REFERENCES COMPANY(company_name),
    FOREIGN KEY (location_id) REFERENCES LOCATION(location_id)
);

-- TABLE REQUIREMENTS
CREATE TABLE REQUIREMENTS (
    requirements_id INT PRIMARY KEY,
    experience VARCHAR(50) NULL,
    qualifications TEXT NULL,
    skills TEXT NULL,
    preference VARCHAR(50) NULL
);

-- TABLE CONTACT
CREATE TABLE CONTACT (
    contact_id INT PRIMARY KEY,
    contact_person VARCHAR(100) NOT NULL,
    contact VARCHAR(100) NOT NULL
);
