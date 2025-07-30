package _7_Data_Types._3_String_Data_Type._5_ENUM;

//ENUM Type
//https://dev.mysql.com/doc/refman/8.0/en/enum.html


/*                                            Enum Type


    ○ CREATE TABLE shirts (
       name VARCHAR(40),
       size ENUM('x-small', 'small', 'medium', 'large', 'x-large')
       );

       INSERT INTO shirts (name, size) VALUES ('dress shirt','large'), ('t-shirt','medium'),
       ('polo shirt','small');

        SELECT name, size FROM shirts WHERE size = 'medium';
        +---------+--------+
        | name    | size   |
        +---------+--------+
        | t-shirt | medium |
        +---------+--------+
        UPDATE shirts SET size = 'small' WHERE size = 'large';


 */

public class ENUM_Data_Type {
}
