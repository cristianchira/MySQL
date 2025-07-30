package _4_Database_Terminology.Normalized;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_normalized


/*                              Normalized database


    ○ A database design strategy where

                   * data is split into multiple tables,

                   * and duplicate values condensed into single rows represented by an ID,

                   * to avoid

                                 - storing,

                                 - querying,

                                 - and updating redundant or lengthy values.

                         It is typically used in OLTP applications.

    ○ For example, an address might be given a unique ID,

                  * so that a census database could represent the relationship lives at this address

                  *  by associating that ID with each member of a family,

                  * rather than storing multiple copies of a complex value such as 123 Main Street, Anytown, USA.

 */

public class Normalized_ {
}
