package _0_DB_ADMINISTRATION.MySQL_Commands;

/*                                MySql commands   - usefully from command line


    ○ connect to MySql using command:
                * mysql -u root -p
                * add root password

    ○ switch on database you want
                 * USE database_name;

    ○ check current database you use
                 * SELECT DATABASE();

    ○ See all tables from your current database(schema)
                 * SHOW tables;


                                 MYSQL  -- administrative command


    ○ See all databases

                * SHOW DATABASES;

    ○ See all tables from a specified database

                * SHOW TABLES IN your_database;

    ○ The command which retrieves the names of all tables across all schemas (databases) in a MySQL server.
          It queries the information_schema.tables table, which contains metadata about the tables in
          the MySQL server

               * SELECT table_name FROM information_schema.tables;


    ○ To show all tables within the information_schema database,
           which is a built-in database containing metadata about the MySQL server and its databases,
           use the following command:

                * SELECT table_name FROM information_schema.tables WHERE table_schema = 'information_schema';

    ○ See all users from MySql(Database Server)

                  * SELECT User, Host FROM mysql.user;

 */



public class My_Sql_Commands {
}
