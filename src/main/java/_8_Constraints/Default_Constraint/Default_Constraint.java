package _8_Constraints.Default_Constraint;

//

/*                                                     Default Constraint

    It helps us assign a particular default value to every row of a column.

    A value different from the default can be stored in a field where the default constraint has been applied only if
       specifically indicated.


                                                   change column to default

    alter table customers
    change column number_of_complaints number_of_complaints int default 0;


                                                       Drop default

    alter table customers
    alter column number_of_complaints drop default;


 */

public class Default_Constraint {
}
