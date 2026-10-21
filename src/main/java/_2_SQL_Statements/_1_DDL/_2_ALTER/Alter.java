package _2_SQL_Statements._1_DDL._2_ALTER;

// https://dev.mysql.com/doc/refman/8.0/en/alter-database.html


/*                                               ALTER statement


    1. ADD COLUMN

               * ALTER TABLE table_name
                  ADD COLUMN new_column_name data_type
                  AFTER existing_column_name;

               * ALTER TABLE table_name
                 ADD COLUMN new_column_name data_type
                 FIRST;


    2. DROP

               * ALTER TABLE table_name
                 DROP COLUMN column_name;


    3. RENAME COLUMN

                * ALTER TABLE table_name
                  CHANGE COLUMN old_column_name new_column_name column_definition;

                * Example

                            - ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255);



   4. MODIFY

                * ALTER TABLE table_name MODIFY COLUMN column_name new_definition;

                * Example

                            - ALTER TABLE employees
                              MODIFY COLUMN salary DECIMAL(10,2)
                              CHECK (salary >= 0 and salary <= 100000);

                 * This will modify the salary column to

                             - change its data type to DECIMAL(10,2)

                             - and add a CHECK constraint to ensure that

                             - the salary value is between 0 and 100000.

                     Note that any indexes or constraints that reference the column

                             - may also need to be modified accordingly.

 */


/*
TOPIC:
    ALTER TABLE schimbă definiția; CHANGE COLUMN email user_email VARCHAR(255) redenumește coloana și păstrează rândurile, nu le șterge.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să schimbi coloana email fără să pierzi clienții. După propoziție, coloana se numește user_email.
MECHANISM:
    Serverul execută ALTER TABLE la sfârșitul propoziției și schimbă schema. Rândurile rămân, cu valorile mutate pe coloana nouă.
LOOKALIKES:
    UPDATE customers SET email = 'a@b.c' schimbă valori, nu numele coloanei. DROP COLUMN scoate coloana și valorile ei.
BOOK THEORY:
    Pagina ALTER TABLE nu a fost adusă integral în tur: NERULAT AICI pentru acel URL. PDF-ul de curs din comentariu descrie CHANGE COLUMN. Efectul de redenumire este propoziția din comentariul existent, păstrată.
    HISTORICAL: pagina 15.1.11 a fost adusă după aceea. CHANGE COLUMN cere numele vechi, numele nou și definiția. RENAME COLUMN schimbă doar numele. Rândurile rămân. UPDATE schimbă valori, nu numele coloanei.
BOOK EXAMPLE:
    changeEmailKeepsRows
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_2_ALTER/Alter.java
BREAK LAB:
    updateRenamesNothing ține UPDATE customers SET email = user_email. UPDATE scrie valori. Numele coloanei rămâne email dacă propoziția nici nu pornește.
TRADE-OFFS:
    UPDATE când valoarea e greșită și numele coloanei este bun. DROP COLUMN când coloana trebuie să dispară împreună cu valorile.
ANTI-PATTERNS:
    UPDATE customers SET email = user_email ca să redenumești. Coloana rămâne email.
STAFF QUESTIONS:
    changeEmailKeepsRows și updateRenamesNothing. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O fișă de pacient are coloana mail și trebuie să se numească contact_mail. ALTER TABLE patient CHANGE COLUMN mail contact_mail VARCHAR(255) păstrează adresele.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255)?
   Capcană: CHANGE COLUMN șterge clienții și face o coloană goală.
   Cheie: changeEmailKeepsRows: coloana se numește user_email și rândurile de clienți rămân. fără CHANGE, un SELECT user_email nu găsește coloana email
   Demonstrație: changeEmailKeepsRows
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255)?
   Capcană: Rândurile dispar până la COMMIT, pentru că ALTER este DML.
   Cheie: Efectul este: coloana se numește user_email și rândurile de clienți rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: changeEmailKeepsRows
3. Enunț: De ce ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255) nu este vecinul său?
   Capcană: UPDATE SET email = user_email este redenumirea.
   Cheie: UPDATE customers SET email = 'a@b.c' schimbă valori, nu numele coloanei. DROP COLUMN scoate coloana și valorile ei.
   Demonstrație: changeEmailKeepsRows
4. Enunț: Cum eșuează UPDATE customers SET email = user_email?
   Capcană: Dacă user_email nu există, UPDATE o creează și copiază email.
   Cheie: updateRenamesNothing: UPDATE scrie valori. Numele coloanei rămâne email dacă propoziția nici nu pornește.
   Demonstrație: updateRenamesNothing
5. Enunț: Când nu mai trimiți ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255)?
   Capcană: Orice schimbare de coloană se face cu UPDATE, ca să o poți da înapoi.
   Cheie: UPDATE când valoarea e greșită și numele coloanei este bun. DROP COLUMN când coloana trebuie să dispară împreună cu valorile.
   Demonstrație: changeEmailKeepsRows

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei numele user_email, nu o valoare nouă în email -> changeEmailKeepsRows -> ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255) -> nu garantează că un index sau o constrângere care citea email a fost și ea redenumită
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> updateRenamesNothing -> UPDATE customers SET email = user_email -> UPDATE scrie valori. Numele coloanei rămâne email dacă propoziția nici nu pornește.
    3. Granița
           -> ALTER TABLE este propoziția. Designerul din Workbench nu redenumește coloana până nu trimite ALTER.
    4. Alternativa
           -> UPDATE customers SET email = 'a@b.c' schimbă valori, nu numele coloanei. DROP COLUMN scoate coloana și valorile ei.
    5. Anti-patternul
           -> UPDATE customers SET email = user_email ca să redenumești. Coloana rămâne email.
    6. Dovada
           -> changeEmailKeepsRows ține propoziția. updateRenamesNothing ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CHANGE COLUMN redenumește și păstrează rândurile. UPDATE schimbă valori. ALTER face commit implicit și nu se dă înapoi ca un UPDATE.
*/

public class Alter {
    /*
    🟢 Începător
        Ce este            coloana se numește user_email și rândurile de clienți rămân
        De ce e nevoie     fără CHANGE, un SELECT user_email nu găsește coloana email
    🟡 Producție
        Cum se scrie       ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este UPDATE, care schimbă valoarea din email și lasă numele coloanei
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE, care schimbă valoarea din email și lasă numele coloanei.
    */
    // Începător: changeEmailKeepsRows păstrează propoziția pentru coloana se numește user_email și rândurile de clienți rămân
    // Producție: public static String changeEmailKeepsRows(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ALTER TABLE este DDL și face commit implicit. Nu îl pui în mijlocul unei tranzacții pe care vrei să o dai înapoi.
    public static String changeEmailKeepsRows() {
        // Începător: coloana se numește user_email și rândurile de clienți rămân
        // Producție: ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: ALTER TABLE este DDL și face commit implicit. Nu îl pui în mijlocul unei tranzacții pe care vrei să o dai înapoi. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide changeEmailKeepsRows. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul nu redenumește coloana; dacă user_email nu există, propoziția eșuează și email rămâne
        De ce e nevoie     fără textul greșit, pare că SET mută numele coloanei
    🟡 Producție
        Cum se scrie       UPDATE customers SET email = user_email
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul nu redenumește coloana; dacă user_email nu există, propoziția eșuează și email rămâne
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE, care schimbă valoarea din email și lasă numele coloanei.
    */
    // Începător: updateRenamesNothing păstrează textul greșit pentru serverul nu redenumește coloana; dacă user_email nu există, propoziția eșuează și email rămâne
    // Producție: public static String updateRenamesNothing(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. UPDATE scrie valori. Numele coloanei rămâne email dacă propoziția nici nu pornește.
    public static String updateRenamesNothing() {
        // Începător: serverul nu redenumește coloana; dacă user_email nu există, propoziția eșuează și email rămâne
        // Producție: UPDATE customers SET email = user_email. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: UPDATE scrie valori. Numele coloanei rămâne email dacă propoziția nici nu pornește. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "UPDATE customers SET email = user_email";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide updateRenamesNothing. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
