package _7_Data_Types._3_String_Data_Type._6_SET_TYPE;

// The SET Type
//https://dev.mysql.com/doc/refman/8.0/en/set.html


/*                                            The SET Type


    ○ A SET is a string object that can have zero or more values,

                * each of which must be chosen from a list of permitted values specified when the table is created.

    ○ Example:

                * CREATE TABLE example_table_set (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  name VARCHAR(50),
                  skills SET('Programming', 'Web Design', 'Database Management', 'Networking', 'Security')
                  ) ENGINE = InnoDB;

                  INSERT INTO example_table_set (name, skills)
                  VALUES ('John Doe', 'Programming,Database Management');


);


 */

public class SET_Type {
}
