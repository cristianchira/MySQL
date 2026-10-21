package _8_Constraints._0_Autoincrement;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339104#notes   5:23


/*                         Autoincrement constraint MySQL


    ○ Autoincrement constraint can be applied to:
                  * PRIMARY KEY
                  * UNIQUE KEY
                  * INDEX

   ○ Cannot apply to all in the same time

   ○ Example
               * create table student
                 (
                     id int unsigned auto_increment primary key,
                     first_name varchar(45) null,
                     last_name  varchar(45) null,
                     email      varchar(45) null
                  )


                                ▬▬ Reset counter ▬▬


    1. TRUNCATE TABLE table_name
                * automatically resets the AUTO_INCREMENT counter

    2. ALTER TABLE student AUTO_INCREMENT = 1;
                * This command sets the AUTO_INCREMENT counter to 1,or whatever value you replace 1 with.
        This might not behave as expected
                * if there are existing rows with IDs higher than the number you set,
                * since AUTO_INCREMENT ensures new rows get a higher ID than any existing row.
        If you want to start AUTO_INCREMENT from 1,
                * you would need to delete the rows in the table
                * or ensure that the AUTO_INCREMENT value is greater than the maximum existing id.

    ○ However, always be careful with TRUNCATE and ALTER commands on your production database as
                * these changes are permanent and can't be undone.


 */


/*
TOPIC:
    AUTO_INCREMENT generează id-ul la INSERT când nu îl dai; PRIMARY KEY este constrângerea, nu generatorul.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să apară un id nou fără să îl scrii. TRUNCATE îl aduce la început; DELETE nu face același reset.
MECHANISM:
    Serverul generează valoarea la inserare. TRUNCATE resetează contorul. DELETE pe rândul maxim, în InnoDB, nu reutilizează valoarea ca un reset de TRUNCATE.
LOOKALIKES:
    PRIMARY KEY fără AUTO_INCREMENT cere id-ul în INSERT. TRUNCATE și DELETE nu sunt același reset.
BOOK THEORY:
    Manualul 9.7, 15.1.44: TRUNCATE resetează AUTO_INCREMENT.  Comentariul de curs spune că TRUNCATE resetează contorul și că ALTER TABLE ... AUTO_INCREMENT nu coboară sub maximul existent. Rămâne.
BOOK EXAMPLE:
    autoIncrementOnThePrimaryKey
REPO DEMO:
    src/main/java/_8_Constraints/_0_Autoincrement/Autoincrement.java
BREAK LAB:
    deleteDoesNotResetTheCounter ține DELETE FROM student. Manualul 9.7: TRUNCATE resetează AUTO_INCREMENT. DELETE nu este acel reset.
TRADE-OFFS:
    Id trimis de aplicație când numărul trebuie să vină din afară, fără AUTO_INCREMENT.
ANTI-PATTERNS:
    DELETE FROM student ca să începi de la 1. Contorul nu se întoarce ca la TRUNCATE.
STAFF QUESTIONS:
    autoIncrementOnThePrimaryKey și deleteDoesNotResetTheCounter. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numărul de fișă se generează la internare. TRUNCATE pe tabela de probe îl aduce la început. DELETE nu.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45))?
   Capcană: DELETE FROM student resetează AUTO_INCREMENT la 1.
   Cheie: autoIncrementOnThePrimaryKey: un INSERT care nu dă id primește următorul număr, iar id este și cheie. fără AUTO_INCREMENT, INSERT trebuie să trimită id
   Demonstrație: autoIncrementOnThePrimaryKey
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45))?
   Capcană: PRIMARY KEY generează numărul și fără AUTO_INCREMENT.
   Cheie: Efectul este: un INSERT care nu dă id primește următorul număr, iar id este și cheie. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: autoIncrementOnThePrimaryKey
3. Enunț: De ce CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45)) nu este vecinul său?
   Capcană: AUTO_INCREMENT poate sta pe o coloană care nu este index.
   Cheie: PRIMARY KEY fără AUTO_INCREMENT cere id-ul în INSERT. TRUNCATE și DELETE nu sunt același reset.
   Demonstrație: autoIncrementOnThePrimaryKey
4. Enunț: Cum eșuează DELETE FROM student?
   Capcană: ALTER TABLE ... AUTO_INCREMENT = 1 coboară sub id-urile existente.
   Cheie: deleteDoesNotResetTheCounter: Manualul 9.7: TRUNCATE resetează AUTO_INCREMENT. DELETE nu este acel reset.
   Demonstrație: deleteDoesNotResetTheCounter
5. Enunț: Când nu mai trimiți CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45))?
   Capcană: Aplicația și AUTO_INCREMENT scriu același număr fără conflict.
   Cheie: Id trimis de aplicație când numărul trebuie să vină din afară, fără AUTO_INCREMENT.
   Demonstrație: autoIncrementOnThePrimaryKey

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei numărul generat și cheia pe aceeași coloană -> autoIncrementOnThePrimaryKey -> CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45)) -> nu reutilizează id-ul șters prin DELETE și nu stă pe o coloană fără index
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> deleteDoesNotResetTheCounter -> DELETE FROM student -> Manualul 9.7: TRUNCATE resetează AUTO_INCREMENT. DELETE nu este acel reset.
    3. Granița
           -> AUTO_INCREMENT este în definiție. Un număr calculat în aplicație nu este contorul serverului.
    4. Alternativa
           -> PRIMARY KEY fără AUTO_INCREMENT cere id-ul în INSERT. TRUNCATE și DELETE nu sunt același reset.
    5. Anti-patternul
           -> DELETE FROM student ca să începi de la 1. Contorul nu se întoarce ca la TRUNCATE.
    6. Dovada
           -> autoIncrementOnThePrimaryKey ține propoziția. deleteDoesNotResetTheCounter ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> AUTO_INCREMENT generează numărul. PRIMARY KEY este constrângerea. TRUNCATE resetează contorul. DELETE nu face același reset. Coloana trebuie să fie index.
*/

public class Autoincrement {
    /*
    🟢 Începător
        Ce este            un INSERT care nu dă id primește următorul număr, iar id este și cheie
        De ce e nevoie     fără AUTO_INCREMENT, INSERT trebuie să trimită id
    🟡 Producție
        Cum se scrie       CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE TABLE student (id INT UNSIGNED PRIMARY KEY, first_name VARCHAR(45))
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE student (id INT UNSIGNED PRIMARY KEY, first_name VARCHAR(45)).
    */
    // Începător: autoIncrementOnThePrimaryKey păstrează propoziția pentru un INSERT care nu dă id primește următorul număr, iar id este și cheie
    // Producție: public static String autoIncrementOnThePrimaryKey(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu pui AUTO_INCREMENT pe o coloană care nu este index. Și nu conta pe DELETE ca să îl aduci la 1.
    public static String autoIncrementOnThePrimaryKey() {
        // Începător: un INSERT care nu dă id primește următorul număr, iar id este și cheie
        // Producție: CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45)). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu pui AUTO_INCREMENT pe o coloană care nu este index. Și nu conta pe DELETE ca să îl aduci la 1. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE student (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide autoIncrementOnThePrimaryKey. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rândurile dispar, iar contorul InnoDB nu pornește din nou ca după TRUNCATE
        De ce e nevoie     fără textul greșit, golirea prin DELETE pare resetul
    🟡 Producție
        Cum se scrie       DELETE FROM student
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rândurile dispar, iar contorul InnoDB nu pornește din nou ca după TRUNCATE
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE student (id INT UNSIGNED PRIMARY KEY, first_name VARCHAR(45)).
    */
    // Începător: deleteDoesNotResetTheCounter păstrează textul greșit pentru rândurile dispar, iar contorul InnoDB nu pornește din nou ca după TRUNCATE
    // Producție: public static String deleteDoesNotResetTheCounter(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul 9.7: TRUNCATE resetează AUTO_INCREMENT. DELETE nu este acel reset.
    public static String deleteDoesNotResetTheCounter() {
        // Începător: rândurile dispar, iar contorul InnoDB nu pornește din nou ca după TRUNCATE
        // Producție: DELETE FROM student. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul 9.7: TRUNCATE resetează AUTO_INCREMENT. DELETE nu este acel reset. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM student";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide deleteDoesNotResetTheCounter. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
