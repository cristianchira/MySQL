package _7_Data_Types._2_Date_DateTime_Timestamp;

//Automatic Initialization and Updating for TIMESTAMP and DATETIME
// https://dev.mysql.com/doc/refman/8.0/en/timestamp-initialization.html

// Date and Time Data Types
// https://dev.mysql.com/doc/refman/8.0/en/date-and-time-types.html

// DATE, DATETIME, TIMESTAMP
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339036#questions


/*                                           Date and Time Data Types


            * DATE                                              'YYYY-MM-DD'       format

            * DATETIME                                          'YYYY-MM-DD hh:mm:ss'     format

            * TIMESTAMP                                          'YYYY-MM-DD hh:mm:ss[.fraction]'

    ○ TIMESTAMP Example                                            '2022-12-31 23:59:59.999999'


                          Automatic Initialization and Updating for TIMESTAMP and DATETIME


    ○ Example

                * CREATE TABLE update_datetime_timestamp (
                  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                  name VARCHAR(50) NOT NULL,
                  timestamp_column TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                  datetime_column DATETIME DEFAULT CURRENT_TIMESTAMP
                  );

                     INSERT INTO update_datetime_timestamp (name) VALUES
                     ('Alice');

                     INSERT INTO update_datetime_timestamp (name) VALUES
                     ('John');




 */
public class DATE_DATETIME_TIMESTAMP {
}
