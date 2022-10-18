package Relational_Schema.Foreign_Key;


// https://dev.mysql.com/doc/refman/5.7/en/create-table-foreign-keys.html


/*                                            Foreign Key


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


    CREATE TABLE `sales` (
  `purchase_number` int NOT NULL AUTO_INCREMENT,
  `date_of_purchase` date DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `item_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`purchase_number`),

  KEY `customer_id` (`customer_id`),

  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE


) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

 */
public class Foreign_Key {
}
