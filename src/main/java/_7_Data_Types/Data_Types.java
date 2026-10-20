package _7_Data_Types;

//
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339026#questions

// Data Types
// https://dev.mysql.com/doc/refman/9.7/en/data-types.html
// HISTORICAL: https://dev.mysql.com/doc/refman/8.0/en/data-types.html — mysql.version din pom, citit în tur, este 9.7. În 13.1.6 lățimea de afișare a întregilor și ZEROFILL sunt deprecate, nu scoase, și nu limitează intervalul stocat.


/*                                               Data Types


    1. Numeric Data Types

                  * https://dev.mysql.com/doc/refman/8.0/en/numeric-types.html

    2. Date and Time Data Types

                  * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-types.html

    3. String Data Types

                 * https://dev.mysql.com/doc/refman/8.0/en/string-types.html

    4. Spatial Data Types

                 * https://dev.mysql.com/doc/refman/8.0/en/spatial-types.html

    5. The JSON Data Type

                 * https://dev.mysql.com/doc/refman/8.0/en/json.html

    6. Data Type Default Values

                 * https://dev.mysql.com/doc/refman/8.0/en/data-type-defaults.html

    7. Data Type Storage Requirements

                 * https://dev.mysql.com/doc/refman/8.0/en/storage-requirements.html

    8. Choosing the Right Type for a Column

                 * https://dev.mysql.com/doc/refman/8.0/en/choosing-types.html

    9. Using Data Types from Other Database Engines

                 *      https://dev.mysql.com/doc/refman/8.0/en/other-vendor-data-types.html


 */
/*
TOPIC:
    Tipul coloanei decide ce rămâne în celulă la sfârșitul lui INSERT: TINYINT UNSIGNED păstrează întregul din interval, DECIMAL(5, 2) taie scara, DOUBLE păstrează o aproximare.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Prețul și cantitatea mică trebuie să rămână în example așa cum le cere coloana. Cifra care nu încape în scară sau în interval nu rămâne ca atare.
MECHANISM:
    Serverul execută propoziția din DataTypes_Fast.chooseType. Conversia se face la atribuire, la sfârșitul lui INSERT, nu la COMMIT și nu într-un EXPLAIN. Tipul nu este un lacăt și nu creează un index.
LOOKALIKES:
    DOUBLE este aproximarea: o alegi când viteza bate exactitatea, nu când price trebuie să țină scara. INT(4) nu este un interval: lățimea de afișare, deprecată în 9.7, nu respinge 10000. CHECK refuză un predicat și nu taie scara unui DECIMAL.
BOOK THEORY:
    Manualul 9.7, capitolul 13, deschis în tur: serverul are tipuri numerice, de dată, șir, spațiale și JSON. 13.8: alegi tipul cel mai precis care acoperă valorile; 1..99999 stă într-un MEDIUMINT UNSIGNED; calculele cu DECIMAL au 65 de cifre; DOUBLE ajunge când exactitatea nu e critică sau viteza e prioritară. 13.1.3: DECIMAL și NUMERIC sunt exacte, NUMERIC este implementat ca DECIMAL, DECIMAL(5, 2) ține de la -999.99 la 999.99, iar cifrele peste scară se convertesc la scară, în general prin trunchiere, cu comportament dependent de sistem. 13.1.4: FLOAT și DOUBLE sunt aproximative, patru și opt octeți; FLOAT(M,D) și DOUBLE(M,D) sunt deprecate. 13.1.6: INT(M) nu limitează intervalul stocat; ZEROFILL și lățimea la întregi sunt deprecate, nu scoase; alternativa de umplere este LPAD sau un CHAR. UNSIGNED pe DECIMAL, FLOAT și DOUBLE este deprecat. 13.1.7: cu mod strict, 256 într-un TINYINT dă ERROR 1264 (22003) și INSERT-ul eșuează; fără mod restrictiv, valoarea se taie la capăt, cu Warning 1264, iar TINYINT UNSIGNED păstrează 255. PDF-ul Data_Types din acest folder spune doar că fiecare coloană trebuie să aibă un tip. Integers.pdf: întregul este număr fără parte fracționară, INT sau INTEGER. Fixed_and_Floating_Point.pdf, în ambele copii: precizia este numărul de cifre, exemplul 10.523 are precizia 5. PDF-ul de DATE, copiat și în folderul BLOB, descrie DATE, nu BLOB. String_Data_Types.pdf: șirul este text, exemplul 'James'. Niciun PDF deschis nu spune ce se pierde la scară sau la 1264.
BOOK EXAMPLE:
    DataTypes_Fast.chooseType
REPO DEMO:
    src/main/java/_7_Data_Types/fast/DataTypes_Fast.java
BREAK LAB:
    DataTypes_Fast.strictRejects256 ține SET sql_mode = 'TRADITIONAL' și INSERT INTO example (id, price) VALUES (256, 10.23). Manualul arată ERROR 1264 și niciun rând pentru un TINYINT care primește 256. În tur nu a fost trimis.
TRADE-OFFS:
    DECIMAL(6, 3) când a treia zecimală trebuie să rămână stocată. DOUBLE când viteza bate exactitatea. LPAD în rezultat când îți trebuie zerouri afișate, nu în celulă. MEDIUMINT UNSIGNED când valorile sunt 1..99999 și vrei tipul precis cu stocarea cea mai mică dintre cele care le țin.
ANTI-PATTERNS:
    SET sql_mode = '' urmat de INSERT cu 256 într-un TINYINT UNSIGNED: rămâne 255, nu 256. id INT(4) nu respinge 10000.
STAFF QUESTIONS:
    DataTypes_Staff_Interview.q01DecimalScale până la DataTypes_Staff_Interview.q50ExplainIsNotTheStoredValue. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Eticheta unei farmacii: numărul de cutii este un întreg mic, prețul de pe etichetă trebuie să rămână exact la două zecimale, iar doza 10.235 mg nu intră într-o scară de două cifre. Nu este tabela example.
SPACED REVIEW:
    O zi: 21 octombrie 2026, ce rămâne din 10.235 într-un DECIMAL(5, 2). Șapte zile: 27 octombrie 2026, ce rând lasă 256 într-un TINYINT UNSIGNED cu TRADITIONAL și cu sql_mode gol. Treizeci de zile: 19 noiembrie 2026, când rămâi la DOUBLE și când treci la DECIMAL(6, 3).
1. Enunț: Ce problemă rezolvă tipul coloanei când example trebuie să țină o cantitate mică și un preț exact la două zecimale?
   Capcană: Comentariul din Integers.java a declarat deja intervalul, deci INSERT nu mai are ce converti.
   Cheie: Serverul convertește la atribuire. chooseType declară id TINYINT UNSIGNED și price DECIMAL(5, 2), apoi inserează (1, 10.235). 1 încape. A treia zecimală nu rămâne în scară.
   Demonstrație: DataTypes_Fast.chooseType
2. Enunț: Ce rămâne în rând după INSERT INTO example (id, price) VALUES (1, 10.235)?
   Capcană: 10.235 rămâne intact, iar aliasul din client creează o a doua coloană.
   Cheie: id 1 este în 0..255 și rămâne. price este convertit la scara 2. Manualul spune că, în general, cifrele peste scară se trunchiază. Efectul se vede la sfârșitul lui INSERT. Nu se creează un index și nu se ține un lacăt de tip.
   Demonstrație: DataTypes_Fast.chooseType
3. Enunț: De ce price DOUBLE nu este price DECIMAL(5, 2)?
   Capcană: Ambele țin numere, deci 10.235 rămâne la fel.
   Cheie: DECIMAL este exact și are scară. DOUBLE este aproximativ. Îl alegi doar când viteza bate exactitatea, cum spune 13.8. Nu taie scara unui preț.
   Demonstrație: DataTypes_Fast.notDouble
4. Enunț: Ce rând rămâne dacă id TINYINT UNSIGNED primește 256?
   Capcană: 256 se salvează, iar paranteza din INT(4) l-ar fi oprit.
   Cheie: Cu TRADITIONAL, manualul arată ERROR 1264 (22003) și INSERT-ul eșuează, deci rândul nu apare. Cu sql_mode gol, același 256 se taie la 255 și rândul rămâne cu valoarea tăiată. INT(4) nu face niciuna dintre ele: lățimea nu este intervalul.
   Demonstrație: DataTypes_Fast.strictRejects256
5. Enunț: Când nu mai declarezi DECIMAL(5, 2)?
   Capcană: Orice preț trebuie să aibă două zecimale, iar zerourile din față se pun cu ZEROFILL.
   Cheie: DECIMAL(6, 3) când 10.235 trebuie să rămână stocat. DOUBLE când accepți aproximarea. LPAD(id, 4, '0') când zerourile trebuie să apară în rezultat, nu în celulă. ZEROFILL este deprecat și nu ține loc de scară.
   Demonstrație: DataTypes_Fast.keepThirdDigit
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE cantitatea încape într-un întreg mic și prețul trebuie să fie exact la două zecimale -> DataTypes_Fast.chooseType -> CREATE TABLE example (id TINYINT UNSIGNED, price DECIMAL(5, 2)) și INSERT (1, 10.235) -> nu garantează cifra stocată după trunchiere: serverul nu a primit propoziția în tur
    2. Eșecul cel mai mic
           -> DECIDE 256 încape într-un TINYINT UNSIGNED -> DataTypes_Fast.strictRejects256 -> SET sql_mode = 'TRADITIONAL' și INSERT (256, 10.23) -> nu garantează textul ERROR 1264 pe acest server: autentificarea a fost refuzată
    3. Granița
           -> Tipul este mecanismul executat de server la atribuire. setObject din JDBC nu alege scara coloanei. Click-ul din Workbench nu convertește valoarea.
    4. Alternativa
           -> DOUBLE când viteza bate exactitatea. DECIMAL(6, 3) când a treia zecimală trebuie să rămână. LPAD când zerourile sunt doar în rezultat.
    5. Anti-patternul
           -> SET sql_mode = '' și INSERT (256, 10.23) lasă 255 în TINYINT UNSIGNED. id INT(4) nu respinge 10000.
    6. Dovada
           -> DataTypes_Fast.chooseType ține propozițiile. Planul și rândul stocat nu au fost citite: root@localhost a fost refuzat.
    7. Interviu
           -> DECIMAL(5, 2) ține scara exactă și taie ce e peste ea. TINYINT UNSIGNED cu TRADITIONAL respinge 256. Cu sql_mode gol păstrează 255. DOUBLE nu ține loc de preț exact.
*/

public class Data_Types {
}
