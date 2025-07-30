package _4_Database_Terminology.InnoDB;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_innodb


/*                                  InnoDB


    ○ A MySQL component that combines
                   * high performance
                   * with transactional capability
           for
                    * reliability,
                    * robustness,
                    * and concurrent access.

    ○ It embodies

                    * the ACID design philosophy.
                    * Represented as a storage engine;
                    * it handles tables created or altered with the ENGINE=INNODB clause.

    ○ See Chapter 15,

                   * The InnoDB Storage Engine for architectural details
                   * and administration procedures,


    ○ and Section 8.5,
                   * “Optimizing for InnoDB Tables” for performance advice.

    ○ In MySQL 5.5 and higher,
                   * InnoDB is the default storage engine for
                                 - new tables and the ENGINE=INNODB clause is not required.

    ○ InnoDB tables are ideally suited for hot backups.

    ○ See Section 30.2,
                   * “MySQL Enterprise Backup Overview”
           for information about
                   * the MySQL Enterprise Backup product for backing up MySQL servers
                   * without interrupting normal processing.







 */

public class InnoDB {
}
