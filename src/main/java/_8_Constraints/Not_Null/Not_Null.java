package _8_Constraints.Not_Null;

//Martin G. NOT NULL
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8400660#questions

//NULL Values
// https://dev.mysql.com/doc/refman/8.0/en/null-values.html


/*                                            not null


    ○ Null value is as a missing value

                  * 0 or NONE

                                - is assigned by user

                 * NULL

                               - is assigned by computer
                                 HISTORICAL: propoziția de deasupra nu este regula. NULL este lipsa valorii, nu un 0 pus de server. Clientul poate scrie NULL explicit. Pe MySQL 9.7, sql_mode implicit include STRICT_TRANS_TABLES: o coloană NOT NULL fără DEFAULT, omisă la INSERT pe InnoDB, oprește propoziția. Nu primește un NULL inventat.

    ○ doesn't accept null data value

    ○ the not null restriction is applied

                  * through the NOT NULL Constraint

    ○ when you insert values in the table

                  * you cannot leave the respective field empty

    ○ If yuo leave it empty

                 * MySql will signal an error


    (
    ...
    company_name varchar(255) not null,
    ...
    )

                                      ADD  not null


    ○ ALTER TABLE companies
       CHANGE COLUMN company_name company_name varchar(255) NOT NULL;


    ○ ALTER TABLE companies
       MODIFY company_name varchar(255) NOT NULL;


                                  DROP  not null


    ○ ALTER TABLE companies
       CHANGE COLUMN company_name company_name varchar(255) NULL;

  ○ ALTER TABLE companies
     MODIFY company_name varchar(255) NULL;



 */

/*
TOPIC:
    company_name VARCHAR(255) NOT NULL refuză rândul fără nume; DEFAULT umple lipsa, NOT NULL nu pune un nume.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu existe o firmă fără nume. INSERT-ul care omite company_name eșuează dacă nu există default.
MECHANISM:
    Serverul respinge NULL pe coloana NOT NULL. NULL nu este 0 și nu este șirul gol. Șirul gol este o valoare și trece de NOT NULL.
LOOKALIKES:
    company_name VARCHAR(255) DEFAULT '' umple golul și nu refuză un NULL explicit la fel, dacă defaultul acoperă omiterea.
BOOK THEORY:
    PDF-ul NOT_NULL_Constraint.pdf a fost deschis. Comentariul are deja HISTORICAL: NULL nu este 0 pus de server. Pagina INSERT 9.7 a fost deschisă despre coloana fără valoare.
BOOK EXAMPLE:
    companyNameRejectsNull
REPO DEMO:
    src/main/java/_8_Constraints/Not_Null/Not_Null.java
BREAK LAB:
    defaultEmptyIsNotNotNull ține CREATE TABLE companies (company_name VARCHAR(255) DEFAULT ''). DEFAULT umple. NOT NULL refuză NULL. Șirul gol nu este NULL și trece de NOT NULL.
TRADE-OFFS:
    DEFAULT când omiterea trebuie umplută, nu respinsă.
ANTI-PATTERNS:
    DEFAULT '' ca să obligi numele. Intră firma fără nume, cu șir gol.
STAFF QUESTIONS:
    companyNameRejectsNull și defaultEmptyIsNotNotNull. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Lotul fără cod este respins: lot_no VARCHAR(20) NOT NULL. Șirul gol tot trece. Dacă golul nu este cod, îți trebuie și o verificare de lungime.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE companies (company_name VARCHAR(255) NOT NULL)?
   Capcană: NOT NULL pune un nume gol în locul NULL.
   Cheie: companyNameRejectsNull: un INSERT fără nume, sau cu NULL, este refuzat dacă nu ai default. fără NOT NULL, company_name poate rămâne NULL
   Demonstrație: companyNameRejectsNull
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE companies (company_name VARCHAR(255) NOT NULL)?
   Capcană: '' este oprit de NOT NULL.
   Cheie: Efectul este: un INSERT fără nume, sau cu NULL, este refuzat dacă nu ai default. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: companyNameRejectsNull
3. Enunț: De ce CREATE TABLE companies (company_name VARCHAR(255) NOT NULL) nu este vecinul său?
   Capcană: DEFAULT '' este aceeași obligație.
   Cheie: company_name VARCHAR(255) DEFAULT '' umple golul și nu refuză un NULL explicit la fel, dacă defaultul acoperă omiterea.
   Demonstrație: companyNameRejectsNull
4. Enunț: Cum eșuează CREATE TABLE companies (company_name VARCHAR(255) DEFAULT '')?
   Capcană: NULL este 0.
   Cheie: defaultEmptyIsNotNotNull: DEFAULT umple. NOT NULL refuză NULL. Șirul gol nu este NULL și trece de NOT NULL.
   Demonstrație: defaultEmptyIsNotNotNull
5. Enunț: Când nu mai trimiți CREATE TABLE companies (company_name VARCHAR(255) NOT NULL)?
   Capcană: Câmpul obligatoriu din formular ține loc de NOT NULL.
   Cheie: DEFAULT când omiterea trebuie umplută, nu respinsă.
   Demonstrație: companyNameRejectsNull

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE lipsa numelui trebuie să fie eroare -> companyNameRejectsNull -> CREATE TABLE companies (company_name VARCHAR(255) NOT NULL) -> nu oprește șirul gol și nu inventează un nume
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> defaultEmptyIsNotNotNull -> CREATE TABLE companies (company_name VARCHAR(255) DEFAULT '') -> DEFAULT umple. NOT NULL refuză NULL. Șirul gol nu este NULL și trece de NOT NULL.
    3. Granița
           -> NOT NULL este constrângerea. Un câmp obligatoriu doar în formular lasă un alt client să trimită NULL.
    4. Alternativa
           -> company_name VARCHAR(255) DEFAULT '' umple golul și nu refuză un NULL explicit la fel, dacă defaultul acoperă omiterea.
    5. Anti-patternul
           -> DEFAULT '' ca să obligi numele. Intră firma fără nume, cu șir gol.
    6. Dovada
           -> companyNameRejectsNull ține propoziția. defaultEmptyIsNotNotNull ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> NOT NULL refuză NULL. Nu pune 0 și nu pune un nume. Șirul gol trece. DEFAULT umple omiterea. Formularul nu este constrângerea.
*/

public class Not_Null {
    /*
    🟢 Începător
        Ce este            un INSERT fără nume, sau cu NULL, este refuzat dacă nu ai default
        De ce e nevoie     fără NOT NULL, company_name poate rămâne NULL
    🟡 Producție
        Cum se scrie       CREATE TABLE companies (company_name VARCHAR(255) NOT NULL)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este company_name VARCHAR(255) DEFAULT ''
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu company_name VARCHAR(255) DEFAULT ''.
    */
    // Începător: companyNameRejectsNull păstrează propoziția pentru un INSERT fără nume, sau cu NULL, este refuzat dacă nu ai default
    // Producție: public static String companyNameRejectsNull(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Șirul gol trece de NOT NULL. Dacă golul nu este un nume, NOT NULL nu ajunge.
    public static String companyNameRejectsNull() {
        // Începător: un INSERT fără nume, sau cu NULL, este refuzat dacă nu ai default
        // Producție: CREATE TABLE companies (company_name VARCHAR(255) NOT NULL). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Șirul gol trece de NOT NULL. Dacă golul nu este un nume, NOT NULL nu ajunge. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE companies (company_name VARCHAR(255) NOT NULL)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide companyNameRejectsNull. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            omiterea devine șir gol și NULL nu este refuzul pe care îl voiai la un nume obligatoriu
        De ce e nevoie     fără textul greșit, DEFAULT '' pare NOT NULL
    🟡 Producție
        Cum se scrie       CREATE TABLE companies (company_name VARCHAR(255) DEFAULT '')
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care omiterea devine șir gol și NULL nu este refuzul pe care îl voiai la un nume obligatoriu
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu company_name VARCHAR(255) DEFAULT ''.
    */
    // Începător: defaultEmptyIsNotNotNull păstrează textul greșit pentru omiterea devine șir gol și NULL nu este refuzul pe care îl voiai la un nume obligatoriu
    // Producție: public static String defaultEmptyIsNotNotNull(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DEFAULT umple. NOT NULL refuză NULL. Șirul gol nu este NULL și trece de NOT NULL.
    public static String defaultEmptyIsNotNotNull() {
        // Începător: omiterea devine șir gol și NULL nu este refuzul pe care îl voiai la un nume obligatoriu
        // Producție: CREATE TABLE companies (company_name VARCHAR(255) DEFAULT ''). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DEFAULT umple. NOT NULL refuză NULL. Șirul gol nu este NULL și trece de NOT NULL. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE companies (company_name VARCHAR(255) DEFAULT '')";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide defaultEmptyIsNotNotNull. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
