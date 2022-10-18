package SQL_STATEMENTS.DCL.GRANT;

// https://dev.mysql.com/doc/refman/8.0/en/grant.html


/*                                             GRANT Statement  General Overview


    ○  Example

                   * GRANT ALL
                     ON db1.* TO 'jeffrey'@'localhost';

                   * GRANT 'role1', 'role2' TO 'user1'@'localhost', 'user2'@'localhost';

                   * GRANT SELECT ON world.* TO 'role3';


    ○ The GRANT statement enables system administrators

                   * to grant

                                 - privileges

                                 - and roles,

                         which can be granted to user accounts and roles.

    ○ These syntax restrictions apply:

                   * GRANT

                                 - cannot mix granting both privileges and roles in the same statement.

                          A given GRANT statement must grant either privileges or roles.


                   * The ON clause distinguishes whether the statement grants privileges or roles:

                                  - With ON, the statement grants privileges.


                                   - Without ON, the statement grants roles.

                                   - It is permitted to assign both privileges and roles to an account,

                                                  • but you must use separate GRANT statements,

                                                   • each with syntax appropriate to what is to be granted.
 */

public class Grant_ {
}
