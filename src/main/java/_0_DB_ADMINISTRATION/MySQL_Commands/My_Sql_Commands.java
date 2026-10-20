package _0_DB_ADMINISTRATION.MySQL_Commands;

/*                          MySql commands   - usefully from command line


    ○ connect to MySql using command:
                * mysql -u root -p
                * add root password

    ○ switch on database you want
                 * USE database_name;

    ○ check current database you use
                 * SELECT DATABASE();

    ○ See all tables from your current database(schema)
                 * SHOW tables;


    SHOW DATABASES de mai jos este o propoziție pe care o execută serverul. Nu este o comandă pe care clientul mysql o consumă singur.
                                 HISTORICAL: MYSQL  -- administrative command. Manualul 9.7: SHOW DATABASES listează directoarele din data directory pentru contul care are privilegiu; programul mysqlshow poate aduce aceeași listă. Comenzile interpretate de client sunt help, clear, delimiter și status, nu SHOW.


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
