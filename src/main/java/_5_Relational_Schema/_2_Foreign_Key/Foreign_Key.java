package _5_Relational_Schema._2_Foreign_Key;

//Martin G. Foreign Key
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338066#overview

// FOREIGN KEY Constraints
// https://dev.mysql.com/doc/refman/8.0/en/create-table-foreign-keys.html


/*                                            Foreign Key

    ○ Add FOREIGN KEY on existing table
                * ALTER TABLE orders
                  ADD CONSTRAINT fk_orders_customer_id FOREIGN KEY (customer_id)
                  REFERENCES customers(id);

    ○ identifies the relationships between tables,
                * not the tables themselves

    ○ Example
                * foreign key (customer_id) references customer(customer_id) on delete cascade
                            - customer_id is column from child (references) table  -- sales table
                            - customer is customer table
                            - customer(customer_id) table and column from parent table who was references


                                                on delete cascade


    ○ If a specific value
                  * from the parent's table primary key has been deleted
                  * all the records from child table referring to this value will be removed as well


                                                   Full example


    ○ CREATE TABLE `sales` (
      `purchase_number` int NOT NULL AUTO_INCREMENT,
      `date_of_purchase` date DEFAULT NULL,
      `customer_id` int DEFAULT NULL,
      `item_code` varchar(10) DEFAULT NULL,
      PRIMARY KEY (`purchase_number`),

      KEY `customer_id` (`customer_id`),

      CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
      ON DELETE CASCADE

       ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

 */
public class Foreign_Key {
}
