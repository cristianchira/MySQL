package _8_Constraints._1_Primary_Key;

// PRIMARY KEY and UNIQUE Index Constraints
// https://dev.mysql.com/doc/refman/8.0/en/constraint-primary-key.html


// Chad Darby Primary Key
// https://www.udemy.com/course/spring-hibernate-tutorial/learn/lecture/36834466#questions


/*                           Primary Key Constraint


    ○ The PRIMARY KEY constraint uniquely identifies each record in a table.

    ○ Primary keys must contain UNIQUE values,
                * and cannot contain NULL values.

    ○ A table can have only ONE primary key;
                  * and in the table,
                  * this primary key can consist of
                              - single or
                              - multiple columns (fields).
                                          • CREATE TABLE CourseRegistration (
                                            studentId unsigned INT NOT NULL ,
                                            courseId INT NOT NULL,
                                            dateOfRegistration DATE,
                                            grade CHAR(1),
                                            PRIMARY KEY (studentId, courseId)
                                            );
                  * can't use AUTO_INCREMENT with a composite primary key in MySQL.

    ○ Normally, errors occur for data-change statements (such as INSERT or UPDATE)
                  * that would violate
                                - primary-key,
                                - unique-key,
                                - or foreign-key constraints.

    ○ If you are using a
                  * transactional storage engine such as InnoDB,
                                 - MySQL automatically ROLLS BACK the statement.

                  * If you are using a non-transactional storage engine,
                                - MySQL STOPS PROCESSING the statement at the row for which the error
                                      occurred
                                - and LEAVES any remaining rows unprocessed.


                                      ▬▬ ADD Primary key ▬▬


    ○ ALTER TABLE <table_name>
      ADD PRIMARY KEY(column_name);


                                     ▬▬ DROP Primary key ▬▬


    ○ ALTER TABLE <table_name>
      DROP PRIMARY KEY;
                * if the primary key is a composite key (i.e., it's made up of more than one column),
                      the DROP PRIMARY KEY command will drop the entire composite key.
         In MySQL, there's no way to
                * drop just part of a composite key;
         You have to drop the whole thing
                * and then re-add the columns you want to keep as a new primary or unique key.


                                   ▬▬ Change AUTO_INCREMENT counter ▬▬


    ○ ALTER TABLE student_tracker.student AUTO_INCREMENT=2500;

    ○ truncate table student
                * typically reset auto_increment counter

 */


public class Primary_Key {
}
