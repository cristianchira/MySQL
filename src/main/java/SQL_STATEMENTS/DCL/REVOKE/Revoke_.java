package SQL_STATEMENTS.DCL.REVOKE;

// https://dev.mysql.com/doc/refman/8.0/en/revoke.html


/*                                                   REVOKE Statement


    ○ The REVOKE statement enables system administrators

                   * to revoke

                                 - privileges and roles,

                         which can be revoked from user accounts and roles.

                   * the exact opposite of GRANT


    ○ REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost';

      REVOKE 'role1', 'role2' FROM 'user1'@'localhost', 'user2'@'localhost';

      REVOKE SELECT ON world.* FROM 'role3';


 */

public class Revoke_ {
}
