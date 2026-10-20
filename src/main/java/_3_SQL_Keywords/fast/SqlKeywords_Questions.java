// Începător: pachetul nu schimbă schema. Ține întrebările scurte despre cuvântul rezervat.
// Producție: package _3_SQL_Keywords.fast. Fără linie, întrebările nu mai stau lângă lecție.
// Expert: pachetul nu este lista RESERVED și nu răspunde el la 1064.
package _3_SQL_Keywords.fast;

/*
TOPIC:
    Cuvântul rezervat necotat este sintaxă: ERROR 1064, niciun obiect. Cuvântul nerezervat poate fi coloană.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Întrebările cer efectul pe schemă, nu definiția copiată a unui keyword.
MECHANISM:
    Parserul, înainte de InnoDB. Cheia stă în SQL_Keywords, nu în corpul acestei clase.
LOOKALIKES:
    BEGIN nerezervat față de INTERVAL rezervat. ALTER ca nume față de ALTER ... ADD.
BOOK THEORY:
    Manualul 9.7 deosebește rezervat de nerezervat, citarea, punctul și eticheta. PDF-ul de curs echivalează orice keyword cu un nume interzis.
BOOK EXAMPLE:
    SQL_Keywords.rejectedInterval
REPO DEMO:
    src/main/java/_3_SQL_Keywords/fast/SqlKeywords_Questions.java
BREAK LAB:
    CREATE TABLE interval (begin INT, end INT)
TRADE-OFFS:
    Citare când numele trebuie păstrat. Redenumire în aplicație când domeniul lasă.
ANTI-PATTERNS:
    CREATE TABLE alter (purchase_number INT)
STAFF QUESTIONS:
    SQL_Keywords.rejectedInterval, quotedInterval, beginColumnOnSales, alterAsTableName, qualifiedInterval
NOVEL TRANSFER:
    Tabela de comenzi numită order, necotat.
SPACED REVIEW:
    o zi: prima întrebare. șapte zile: a doua. treizeci de zile: a cincea, pe un nume nou.
*/

/*
Definiția unui cuvânt cheie rezervat (reserved keyword):
       Cuvântul rezervat este citit ca sintaxă. Necotat, în locul unui identificator, propoziția cade cu ERROR 1064 și obiectul nu apare.

Exemplu:
       SqlKeywords_Terms.reservedKeyword()
       CREATE TABLE interval (begin INT, end INT) nu lasă o tabelă.

Definiția unui identificator (identifier):
       Identificatorul este numele obiectului. Un cuvânt rezervat devine identificator între backtick-uri, sau după punct într-un nume calificat.

Exemplu:
       SqlKeywords_Terms.identifier()
       CREATE TABLE `interval` (begin INT, end INT) lasă o tabelă cu 0 rânduri.

Definiția unui cuvânt cheie nerezervat (nonreserved keyword):
       Cuvântul cheie nerezervat are rol în sintaxă și poate fi identificator fără citare. BEGIN și END sunt în acest grup în MySQL 9.7.

Exemplu:
       SqlKeywords_Terms.nonreservedKeyword()
       Coloana begin nu cere backtick.

Definiția unui nume calificat (qualified name):
       Cuvântul de după punct este identificator, chiar dacă este rezervat, și nu se citează.

Exemplu:
       SqlKeywords_Terms.qualifiedName()
       mydb.interval trece fără backtick.

Definiția unui cuvânt cheie restricționat (restricted keyword):
       BEGIN se citează ca etichetă de program stocat, nu ca coloană. SUPER se citează ca nume de rol.

Exemplu:
       SqlKeywords_Terms.restrictedKeyword()
       RESERVED 0 nu scutește eticheta.

Definiția unei tabele (table):
       Tabela apare doar dacă numele trece de parser.

Exemplu:
       SqlKeywords_Terms.tableName()
       `interval` lasă 0 rânduri.

Definiția unei coloane (column):
       begin poate fi coloană necotată. rank necotat nu poate.

Exemplu:
       SqlKeywords_Terms.columnName()
       ADD COLUMN rank cade cu 1064.
*/

/*
1. Ce rămâne în schemă după CREATE TABLE interval (begin INT, end INT), și de ce nu este un rând refuzat de InnoDB?
2. Ce lasă CREATE TABLE `interval` (begin INT, end INT) în număr de rânduri, și ce propoziție următoare mai are nevoie de backtick?
3. De ce begin poate fi coloană în CREATE TABLE sales (purchase_number INT, begin INT), iar interval nu poate fi numele tabelei?
4. Cum diferă CREATE TABLE alter (purchase_number INT) de ALTER TABLE sales ADD COLUMN date_of_purchase DATE, deși ambele folosesc cuvinte rezervate?
5. Când alegi mydb.interval, când alegi `interval`, și când nu trimiți deloc cuvântul pentru că RESERVED este 1?

Întrebări adăugate:
*/

// Începător: clasa nu execută SQL. Întrebările stau deasupra, corpul rămâne gol.
// Producție: public class SqlKeywords_Questions. Fără clasă, secțiunea Întrebări adăugate nu are unde să crească.
// Expert: corpul gol nu este un răspuns. Cheia pentru INTERVAL și BEGIN este în SQL_Keywords, nu aici.
/*
🟢 Începător
    Ce este            lista scurtă de întrebări, fără cheie executată în corp
    De ce e nevoie     fără ea, nu există locul în care adaugi o întrebare nouă
🟡 Producție
    Cum se scrie       clasa rămâne fără metode
🔴 Staff
    Unde               comentariul, nu parserul
Pași                   1. Citești întrebarea.
                       2. Nu găsești SQL în corp.
                       3. Propoziția stă în SQL_Keywords.
*/
public class SqlKeywords_Questions {
    // Începător: acolada nu creează și nu șterge o tabelă.
    // Producție: închide SqlKeywords_Questions. Fără ea, fișierul nu compilează și întrebările nu rămân lângă o clasă.
    // Expert: acolada goală nu este ERROR 1064 și nu înlocuiește INFORMATION_SCHEMA.KEYWORDS.
}
