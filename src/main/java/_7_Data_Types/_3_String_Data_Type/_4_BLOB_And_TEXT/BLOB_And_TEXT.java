package _7_Data_Types._3_String_Data_Type._4_BLOB_And_TEXT;

// The BLOB and TEXT Types
// https://dev.mysql.com/doc/refman/8.0/en/blob.html

// Martin G. DATE, DATETIME, TIMESTAMP BLOB  5: 08
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339036#questions


/*                    The BLOB and TEXT Types

    ○ refers to a file of binary data

                * involves saving files .jpg,   . doc


    ○ CREATE TABLE mytable (
       id INT NOT NULL AUTO_INCREMENT,
       blob_column BLOB,
       PRIMARY KEY (id)
       );

       INSERT INTO mytable (blob_column) VALUES ('binary data');


                         The four BLOB types are


    1. TINYBLOB,

    2. BLOB,

    3. MEDIUMBLOB,

    4.LONGBLOB


    ○ Insert a photo from your computer into a table in MySQL

                * using the LOAD_FILE() function

                * along with an INSERT statement.

    ○  sql script

                * -- Create table
                  CREATE TABLE photos (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255),
                      image BLOB
                    );

                    -- Set file path and load file
                    SET @photo = LOAD_FILE('/path/to/photo.jpg');

                    -- Insert photo into table
                    INSERT INTO photos (name, image) VALUES ('My Photo', @photo);

*/


public class BLOB_And_TEXT {
}
