/*
TOPIC:
    Întrebări despre ce rămâne în example după tipul coloanei, nu despre join și nu despre cheie.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să recunoști când tipul păstrează valoarea, când taie scara și când respinge rândul.
MECHANISM:
    Propozițiile stau în DataTypes_Fast. Această clasă nu le trimite.
LOOKALIKES:
    DOUBLE nu ține scara. INT(4) nu este intervalul. CHECK nu convertește la atribuire.
BOOK THEORY:
    Manualul 9.7, 13.1.3 și 13.1.7: DECIMAL(5, 2) convertește cifrele peste scară; TRADITIONAL respinge 256 într-un TINYINT. PDF-urile deschise nu spun asta.
BOOK EXAMPLE:
    DataTypes_Fast.chooseType
REPO DEMO:
    src/main/java/_7_Data_Types/fast/DataTypes_Fast.java
BREAK LAB:
    DataTypes_Fast.strictRejects256
TRADE-OFFS:
    DECIMAL(6, 3) când a treia zecimală rămâne. DOUBLE când viteza bate exactitatea. LPAD când zerourile sunt în rezultat.
ANTI-PATTERNS:
    sql_mode gol, care lasă 255 în loc de 256.
STAFF QUESTIONS:
    DataTypes_Staff_Interview.q01DecimalScale până la DataTypes_Staff_Interview.q50ExplainIsNotTheStoredValue. Aici sunt cinci, cu cheia scrisă.
NOVEL TRANSFER:
    Eticheta unei farmacii: prețul și doza nu împart aceeași scară.
SPACED REVIEW:
    O zi: 21 octombrie 2026. Șapte zile: 27 octombrie 2026. Treizeci de zile: 19 noiembrie 2026.

Definiția unei alegeri de tip (data type):
       Tipul coloanei decide ce valoare rămâne în celulă la sfârșitul lui INSERT și ce cifră nu rămâne.

Exemplu:
       DataTypes_Terms.explainDataType()
       id 1 rămâne. A treia zecimală din 10.235 nu rămâne într-un DECIMAL(5, 2).

1. Enunț: Ce problemă rezolvă TINYINT UNSIGNED împreună cu DECIMAL(5, 2) în example?
   Capcană: Comentariul din Integers.java a stocat deja intervalul, deci INSERT nu mai convertește nimic.
   Cheie: chooseType este propoziția. 1 rămâne în id. 10.235 nu își păstrează a treia zecimală în price.
   Demonstrație: DataTypes_Fast.chooseType
2. Enunț: Ce este în rând după INSERT INTO example (id, price) VALUES (1, 10.235)?
   Capcană: Ambele valori rămân exact cum au fost tastate, iar serverul adaugă un index pe price.
   Cheie: id 1 încape în 0..255. price este convertit la scara 2 la sfârșitul lui INSERT. Nu apare un index și nu se ține un lacăt de tip.
   Demonstrație: DataTypes_Fast.chooseType
3. Enunț: De ce price DOUBLE nu răspunde la aceeași cerere ca DECIMAL(5, 2)?
   Capcană: Sunt numere, deci 10.235 rămâne la fel.
   Cheie: DOUBLE este aproximativ și se alege când viteza bate exactitatea. DECIMAL ține scara. notDouble nu taie scara.
   Demonstrație: DataTypes_Fast.notDouble
4. Enunț: Ce rând lasă 256 într-un id TINYINT UNSIGNED?
   Capcană: 256 se salvează, sau INT(4) l-ar fi oprit înainte.
   Cheie: Cu TRADITIONAL, manualul arată ERROR 1264 și INSERT-ul eșuează. Cu sql_mode gol, rămâne 255. Lățimea nu face nici respingerea, nici tăierea.
   Demonstrație: DataTypes_Fast.strictRejects256
5. Enunț: Când urci scara în loc să rămâi la DECIMAL(5, 2)?
   Capcană: Două zecimale sunt regula pentru orice număr, iar ZEROFILL păstrează cifrele lipsă.
   Cheie: keepThirdDigit, adică DECIMAL(6, 3), când 10.235 trebuie să rămână stocat. LPAD umple doar rezultatul. ZEROFILL este deprecat și nu ține loc de scară.
   Demonstrație: DataTypes_Fast.keepThirdDigit

Întrebări adăugate:

*/
// Începător: întrebările scurte stau aici, fără o metodă care să răspundă singură.
// Producție: package _7_Data_Types.fast. Corpul clasei rămâne gol.
// Expert: Nu muta aici banca de la _10_JOINs. Subiectul este ce rămâne în celulă după tip.
package _7_Data_Types.fast;

// Începător: clasa ține întrebările în comentariu și nu trimite INSERT-ul.
// Producție: public final class DataTypes_Questions. Dacă primește o metodă, nu mai este clasa goală cerută la creare.
// Expert: Cheia este scrisă deasupra. Nu este un main de recall și nu acoperă CHAR față de VARCHAR.
public final class DataTypes_Questions {
    // Începător: nu există o propoziție în corp, fiindcă răspunsurile stau în comentariu.
    // Producție: acolada închide DataTypes_Questions. Sub ea nu se adaugă un main.
    // Expert: chooseType rămâne în DataTypes_Fast. Golul de aici nu șterge cele cinci întrebări.
}
