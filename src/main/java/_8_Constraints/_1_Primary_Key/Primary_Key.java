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
                    HISTORICAL: propoziția de deasupra nu este regula din manualul 9.7 deschis în tur. 17.6.1.6: pe InnoDB, coloana AUTO_INCREMENT trebuie să fie prima sau singura dintr-un index, nu neapărat PRIMARY KEY. 5.6.9: pe MyISAM, AUTO_INCREMENT poate fi a doua coloană din PRIMARY KEY (grp, id). Cheia compusă poate purta AUTO_INCREMENT. Ce cade pe InnoDB este coloana care nu este prima în niciun index.

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


/*
TOPIC:
    PRIMARY KEY (studentId, courseId) identifică înregistrarea și nu acceptă NULL; UNIQUE pe o coloană nu este această cheie.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să existe o singură înscriere pentru același student și același curs. A doua pereche identică este refuzată.
MECHANISM:
    În InnoDB, PRIMARY KEY este indexul grupat. Este una pe tabelă și nu acceptă NULL. O cheie unică secundară poate avea mai multe NULL.
LOOKALIKES:
    UNIQUE (email) refuză emailul duplicat și acceptă NULL. Nu identifică înscrierea.
BOOK THEORY:
    PDF-ul hibernate-primary-keys.pdf, deschis în tur: cheia primară identifică rândul, este unică și nu acceptă NULL. În InnoDB este indexul grupat.  Comentariul are deja HISTORICAL pe ideea că AUTO_INCREMENT nu poate sta într-o cheie compusă. Nu o repet aici ca regulă.
BOOK EXAMPLE:
    compositePrimaryKey
REPO DEMO:
    src/main/java/_8_Constraints/_1_Primary_Key/Primary_Key.java
BREAK LAB:
    uniqueOnStudentOnly ține CREATE TABLE CourseRegistration (studentId INT, courseId INT, UNIQUE (studentId)). UNIQUE (studentId) nu este perechea. PRIMARY KEY pe ambele coloane este identitatea și nu acceptă NULL.
TRADE-OFFS:
    UNIQUE când coloana nu este identitatea rândului și NULL trebuie permis.
ANTI-PATTERNS:
    UNIQUE (studentId) ca înscriere. A doua materie este refuzată.
STAFF QUESTIONS:
    compositePrimaryKey și uniqueOnStudentOnly. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O programare este (pacient, zi, oră) ca PRIMARY KEY. UNIQUE doar pe pacient ar interzice a doua programare.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId))?
   Capcană: UNIQUE (studentId) este aceeași înscriere.
   Cheie: compositePrimaryKey: perechea student-curs identifică rândul și a doua pereche identică este refuzată. fără PRIMARY KEY, două înscrieri identice pot intra
   Demonstrație: compositePrimaryKey
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId))?
   Capcană: PRIMARY KEY acceptă NULL.
   Cheie: Efectul este: perechea student-curs identifică rândul și a doua pereche identică este refuzată. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: compositePrimaryKey
3. Enunț: De ce CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId)) nu este vecinul său?
   Capcană: Pot fi două PRIMARY KEY pe tabelă.
   Cheie: UNIQUE (email) refuză emailul duplicat și acceptă NULL. Nu identifică înscrierea.
   Demonstrație: compositePrimaryKey
4. Enunț: Cum eșuează CREATE TABLE CourseRegistration (studentId INT, courseId INT, UNIQUE (studentId))?
   Capcană: Cheia compusă interzice a doua materie a studentului.
   Cheie: uniqueOnStudentOnly: UNIQUE (studentId) nu este perechea. PRIMARY KEY pe ambele coloane este identitatea și nu acceptă NULL.
   Demonstrație: uniqueOnStudentOnly
5. Enunț: Când nu mai trimiți CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId))?
   Capcană: Indexul grupat este un index secundar oarecare.
   Cheie: UNIQUE când coloana nu este identitatea rândului și NULL trebuie permis.
   Demonstrație: compositePrimaryKey

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE identitatea este perechea student și curs -> compositePrimaryKey -> CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId)) -> nu generează valori și nu permite NULL în cheie
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> uniqueOnStudentOnly -> CREATE TABLE CourseRegistration (studentId INT, courseId INT, UNIQUE (studentId)) -> UNIQUE (studentId) nu este perechea. PRIMARY KEY pe ambele coloane este identitatea și nu acceptă NULL.
    3. Granița
           -> PRIMARY KEY este constrângerea. Un id afișat în ecran nu este cheia dacă nu este în definiție.
    4. Alternativa
           -> UNIQUE (email) refuză emailul duplicat și acceptă NULL. Nu identifică înscrierea.
    5. Anti-patternul
           -> UNIQUE (studentId) ca înscriere. A doua materie este refuzată.
    6. Dovada
           -> compositePrimaryKey ține propoziția. uniqueOnStudentOnly ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> PRIMARY KEY este una, nu acceptă NULL și în InnoDB este indexul grupat. UNIQUE poate avea mai multe NULL. Cheia compusă este perechea, nu o singură coloană.
*/

public class Primary_Key {
    /*
    🟢 Începător
        Ce este            perechea student-curs identifică rândul și a doua pereche identică este refuzată
        De ce e nevoie     fără PRIMARY KEY, două înscrieri identice pot intra
    🟡 Producție
        Cum se scrie       CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este UNIQUE (studentId), care ar interzice a doua materie a aceluiași student
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UNIQUE (studentId), care ar interzice a doua materie a aceluiași student.
    */
    // Începător: compositePrimaryKey păstrează propoziția pentru perechea student-curs identifică rândul și a doua pereche identică este refuzată
    // Producție: public static String compositePrimaryKey(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. NULL în studentId nu este identitate. Cheia primară nu îl acceptă.
    public static String compositePrimaryKey() {
        // Începător: perechea student-curs identifică rândul și a doua pereche identică este refuzată
        // Producție: CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId)). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: NULL în studentId nu este identitate. Cheia primară nu îl acceptă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE CourseRegistration (studentId INT NOT NULL, courseId INT NOT NULL, PRIMARY KEY (studentId, courseId))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide compositePrimaryKey. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            studentul nu se poate înscrie la două cursuri, iar NULL pe studentId poate apărea de mai multe ori
        De ce e nevoie     fără textul greșit, UNIQUE pe o coloană pare cheia compusă
    🟡 Producție
        Cum se scrie       CREATE TABLE CourseRegistration (studentId INT, courseId INT, UNIQUE (studentId))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care studentul nu se poate înscrie la două cursuri, iar NULL pe studentId poate apărea de mai multe ori
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UNIQUE (studentId), care ar interzice a doua materie a aceluiași student.
    */
    // Începător: uniqueOnStudentOnly păstrează textul greșit pentru studentul nu se poate înscrie la două cursuri, iar NULL pe studentId poate apărea de mai multe ori
    // Producție: public static String uniqueOnStudentOnly(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. UNIQUE (studentId) nu este perechea. PRIMARY KEY pe ambele coloane este identitatea și nu acceptă NULL.
    public static String uniqueOnStudentOnly() {
        // Începător: studentul nu se poate înscrie la două cursuri, iar NULL pe studentId poate apărea de mai multe ori
        // Producție: CREATE TABLE CourseRegistration (studentId INT, courseId INT, UNIQUE (studentId)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: UNIQUE (studentId) nu este perechea. PRIMARY KEY pe ambele coloane este identitatea și nu acceptă NULL. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE CourseRegistration (studentId INT, courseId INT, UNIQUE (studentId))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide uniqueOnStudentOnly. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
