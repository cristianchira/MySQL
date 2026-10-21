package _8_Constraints._4_Unique_Key;

// https://dev.mysql.com/doc/refman/8.0/en/partitioning-limitations-partitioning-keys-unique-keys.html


/*                                           Unique key


    ○ Used whenever you would like to specify you

                  * don't want to see duplicate data in a given field.

   ○ Ensured that all values in a column(or a set of columns) are different

                  * unique keys are implemented in sql through a constraint:


   ○  UNIQUE KEY (col1, col3),
      UNIQUE KEY (col2, col4)


                                          ADD Unique Key


    1. ALTER TABLE departments_dup
        ADD UNIQUE(dept_name);

    2. ALTER TABLE departments_dup
       ADD CONSTRAINT UC UNIQUE(dept_name);



                                          DROP Unique key


     1. ALTER TABLE departments_dup
        DROP INDEX [CONSTRAINT] dept_name;
        HISTORICAL: rândul cu [CONSTRAINT] nu este sintaxa pe care serverul o execută. UNIQUE se scoate ca index: ALTER TABLE departments_dup DROP INDEX dept_name.


     2.ALTER TABLE departments_dup
       DROP INDEX [CONSTRAINT] UC;
       HISTORICAL: [CONSTRAINT] nu face parte din DROP INDEX. Propoziția este ALTER TABLE departments_dup DROP INDEX UC.




 */

/*
TOPIC:
    ALTER TABLE departments_dup ADD UNIQUE (dept_name) refuză al doilea același nume; DROP INDEX dept_name scoate constrângerea, nu rândurile.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu existe două departamente cu același nume. Rândurile diferite rămân.
MECHANISM:
    Serverul respinge duplicatul la scriere. UNIQUE se scoate ca index: DROP INDEX. [CONSTRAINT] din curs nu face parte din DROP INDEX.
LOOKALIKES:
    PRIMARY KEY este una și nu acceptă NULL. UNIQUE pe dept_name poate sta lângă o cheie primară.
BOOK THEORY:
    Comentariul are deja HISTORICAL pe [CONSTRAINT] în DROP INDEX. Propoziția care scoate constrângerea este ALTER TABLE departments_dup DROP INDEX dept_name.
BOOK EXAMPLE:
    uniqueDeptName
REPO DEMO:
    src/main/java/_8_Constraints/_4_Unique_Key/Unique_Key.java
BREAK LAB:
    dropIndexBracketIsNotSql ține ALTER TABLE departments_dup DROP INDEX [CONSTRAINT] dept_name. DROP INDEX primește numele indexului, nu [CONSTRAINT]. Rândurile nu se șterg odată cu indexul.
TRADE-OFFS:
    DROP INDEX dept_name când duplicatul trebuie să fie din nou permis.
ANTI-PATTERNS:
    DROP INDEX [CONSTRAINT] dept_name. Constrângerea rămâne, pentru că textul nu este propoziția.
STAFF QUESTIONS:
    uniqueDeptName și dropIndexBracketIsNotSql. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Două depozite nu pot avea același cod: ADD UNIQUE (code). Scoți regula cu DROP INDEX code, iar depozitele rămân.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ALTER TABLE departments_dup ADD UNIQUE (dept_name)?
   Capcană: DROP INDEX șterge departamentele.
   Cheie: uniqueDeptName: al doilea rând cu același dept_name este refuzat. fără UNIQUE, două departamente pot avea același nume
   Demonstrație: uniqueDeptName
2. Enunț: Ce rămâne în tabelă sau în sesiune după ALTER TABLE departments_dup ADD UNIQUE (dept_name)?
   Capcană: [CONSTRAINT] face parte din DROP INDEX.
   Cheie: Efectul este: al doilea rând cu același dept_name este refuzat. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: uniqueDeptName
3. Enunț: De ce ALTER TABLE departments_dup ADD UNIQUE (dept_name) nu este vecinul său?
   Capcană: UNIQUE este a doua cheie primară și nu acceptă NULL.
   Cheie: PRIMARY KEY este una și nu acceptă NULL. UNIQUE pe dept_name poate sta lângă o cheie primară.
   Demonstrație: uniqueDeptName
4. Enunț: Cum eșuează ALTER TABLE departments_dup DROP INDEX [CONSTRAINT] dept_name?
   Capcană: ADD UNIQUE șterge duplicatul vechi în loc să refuze scrierea nouă.
   Cheie: dropIndexBracketIsNotSql: DROP INDEX primește numele indexului, nu [CONSTRAINT]. Rândurile nu se șterg odată cu indexul.
   Demonstrație: dropIndexBracketIsNotSql
5. Enunț: Când nu mai trimiți ALTER TABLE departments_dup ADD UNIQUE (dept_name)?
   Capcană: Fără UNIQUE, serverul tot refuză același dept_name.
   Cheie: DROP INDEX dept_name când duplicatul trebuie să fie din nou permis.
   Demonstrație: uniqueDeptName

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE numele departamentului nu are voie să se repete -> uniqueDeptName -> ALTER TABLE departments_dup ADD UNIQUE (dept_name) -> nu șterge departamentele când scoți indexul și nu este PRIMARY KEY
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> dropIndexBracketIsNotSql -> ALTER TABLE departments_dup DROP INDEX [CONSTRAINT] dept_name -> DROP INDEX primește numele indexului, nu [CONSTRAINT]. Rândurile nu se șterg odată cu indexul.
    3. Granița
           -> UNIQUE este constrângerea. Parantezele drepte din notiță nu sunt SQL.
    4. Alternativa
           -> PRIMARY KEY este una și nu acceptă NULL. UNIQUE pe dept_name poate sta lângă o cheie primară.
    5. Anti-patternul
           -> DROP INDEX [CONSTRAINT] dept_name. Constrângerea rămâne, pentru că textul nu este propoziția.
    6. Dovada
           -> uniqueDeptName ține propoziția. dropIndexBracketIsNotSql ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> UNIQUE refuză al doilea dept_name. Se scoate cu DROP INDEX, fără [CONSTRAINT]. Rândurile rămân. Nu este PRIMARY KEY.
*/

public class Unique_Key {
    /*
    🟢 Începător
        Ce este            al doilea rând cu același dept_name este refuzat
        De ce e nevoie     fără UNIQUE, două departamente pot avea același nume
    🟡 Producție
        Cum se scrie       ALTER TABLE departments_dup ADD UNIQUE (dept_name)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ALTER TABLE departments_dup DROP INDEX dept_name, care scoate constrângerea și lasă rândurile
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE departments_dup DROP INDEX dept_name, care scoate constrângerea și lasă rândurile.
    */
    // Începător: uniqueDeptName păstrează propoziția pentru al doilea rând cu același dept_name este refuzat
    // Producție: public static String uniqueDeptName(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DROP INDEX [CONSTRAINT] dept_name nu este sintaxa. Serverul nu primește parantezele drepte.
    public static String uniqueDeptName() {
        // Începător: al doilea rând cu același dept_name este refuzat
        // Producție: ALTER TABLE departments_dup ADD UNIQUE (dept_name). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DROP INDEX [CONSTRAINT] dept_name nu este sintaxa. Serverul nu primește parantezele drepte. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE departments_dup ADD UNIQUE (dept_name)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide uniqueDeptName. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul respinge textul și constrângerea rămâne
        De ce e nevoie     fără textul greșit, parantezele din curs par opționale
    🟡 Producție
        Cum se scrie       ALTER TABLE departments_dup DROP INDEX [CONSTRAINT] dept_name
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul respinge textul și constrângerea rămâne
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE departments_dup DROP INDEX dept_name, care scoate constrângerea și lasă rândurile.
    */
    // Începător: dropIndexBracketIsNotSql păstrează textul greșit pentru serverul respinge textul și constrângerea rămâne
    // Producție: public static String dropIndexBracketIsNotSql(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DROP INDEX primește numele indexului, nu [CONSTRAINT]. Rândurile nu se șterg odată cu indexul.
    public static String dropIndexBracketIsNotSql() {
        // Începător: serverul respinge textul și constrângerea rămâne
        // Producție: ALTER TABLE departments_dup DROP INDEX [CONSTRAINT] dept_name. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DROP INDEX primește numele indexului, nu [CONSTRAINT]. Rândurile nu se șterg odată cu indexul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE departments_dup DROP INDEX [CONSTRAINT] dept_name";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide dropIndexBracketIsNotSql. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
