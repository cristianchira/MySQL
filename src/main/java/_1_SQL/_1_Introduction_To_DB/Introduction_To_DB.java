package _1_SQL._1_Introduction_To_DB;

//Martin G.  Introduction to databases
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8337994#questions


/*                                  Introduction to databases


    ○ Data value

        *  A data value is the specific piece of information stored in a particular cell

                    - at the intersection of a row and a column.

        * They are the actual content that is organized and managed by the

                    - database management system (DBMS).

    Data values can be of various types, such as

         * numbers,

         * text,

         * dates,

         * or even multimedia content like images and audio files.

    ○ A record

                 * is each entry that exists in a table.

                 * It corresponds to a row of the table.

                 * all data value forms a record

    ○ A column

                 * represents a specific attribute or field of that record(row)

                 * containing specific information about every record in the table.

    ○ Relational database

                 * a few related tables

    ○ Entity

                 * the smallest unit that can contain a meaningful set of data

         rows represent

                 * the horizontal entity in the table,

          columns

                 * its vertical entity.

          A single row of a table, being a single occurrence of that entity, can be also called

                 * an entity instance.

    ○ Table

                * is a big data entity on its own


 */


/*
TOPIC:
    O valoare de date este conținutul unei celule, la întâlnirea unui rând cu o coloană. INSERT o lasă în tabelă. Un rând de rezultat nu este celula.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Să rămână o celulă în entity_row. După INSERT, data_value stă în rând. SELECT 1 nu o scrie.
MECHANISM:
    Serverul execută INSERT INTO entity_row (data_value) VALUES (1). Celula se vede după ce propoziția se termină, în tabelă. Fără entity_row, serverul refuză și nu apare nicio celulă.
LOOKALIKES:
    SELECT 1 AS data_value, din _1_SQL, întoarce un rând de rezultat și nu scrie celula. Limbajul declarativ spune ce vrei citit. Aici întrebarea este ce rămâne stocat.
BOOK THEORY:
    Manualul 9.7, secțiunea 1.2.1, deschisă în tur: o bază de date este o colecție structurată. Serverul o adaugă, o citește și o prelucrează. O bază relațională ține datele în tabele separate. Modelul logic are baze, tabele, view-uri, rânduri și coloane. Folderul nu are PDF: open_books.py a raportat NO_LOCAL_PDF. Nu citez un capitol de curs pentru acest folder.
BOOK EXAMPLE:
    storedDataValue
REPO DEMO:
    src/main/java/_1_SQL/_1_Introduction_To_DB/Introduction_To_DB.java
BREAK LAB:
    resultIsNotACell ține SELECT 1 AS data_value. Clientul poate vedea 1 și tabela nu primește celula.
TRADE-OFFS:
    Rămâi la celulă când întrebarea este ce stă în rând. Treci la _2_SQL_Declarative_Language când întrebarea este cine alege pașii de citire. Treci la _2_SQL_Statements când clasifici CREATE, INSERT sau SELECT.
ANTI-PATTERNS:
    Să numești data_value un alias de rezultat și să spui că ai un record.
STAFF QUESTIONS:
    Întrebările 1–50 din IntroductionToDb_Staff_Interview. Cheia este scrisă. Nu există main de recall.
NOVEL TRANSFER:
    Un raft de depozit ține cantitatea unui produs într-o celulă. Afișajul „1” de pe scanner, fără un rând scris, nu este stocul.
SPACED REVIEW:
    O zi: 21 octombrie 2026, celula este în rând. Șapte zile: 27 octombrie 2026, SELECT 1 nu scrie celula. Treizeci de zile: 19 noiembrie 2026, fără tabelă INSERT nu lasă nimic.

1. Enunț: Ce problemă rezolvă celula față de un rând de rezultat?
   Capcană: Dacă clientul vede 1, data_value este deja stocat.
   Cheie: Celula este conținutul de la întâlnirea rândului cu coloana. storedDataValue o lasă prin INSERT INTO entity_row (data_value) VALUES (1). resultIsNotACell doar întoarce 1.
   Demonstrație: storedDataValue
2. Enunț: Ce rămâne în tabelă după INSERT, și ce rămâne după SELECT 1 AS data_value?
   Capcană: Ambele lasă un rând, pentru că ambele au data_value.
   Cheie: INSERT, dacă entity_row există și propoziția se termină, lasă un record. SELECT 1 nu numește tabela și nu lasă celulă. În tur niciuna nu a fost trimisă.
   Demonstrație: storedDataValue
3. Enunț: De ce celula nu este propoziția declarativă?
   Capcană: Descrierea coloanei în comentariu este deja interogarea.
   Cheie: Comentariul nu ajunge la server. Celula există în tabelă și fără să ceri citirea. SELECT data_value FROM entity_row este cererea. Conținutul stocat este acest folder.
   Demonstrație: resultIsNotACell
4. Enunț: Cum eșuează celula dacă entity_row lipsește?
   Capcană: INSERT creează tabela și pune 1.
   Cheie: INSERT nu creează tabela. Serverul refuză propoziția. Nu apare rând și nu apare coloană. CREATE TABLE este alt folder.
   Demonstrație: storedDataValue
5. Enunț: Când rămâi la celulă și când trimiți doar un SELECT de constantă?
   Capcană: Orice 1 trebuie stocat, altfel nu este o valoare de date.
   Cheie: Stochezi când alt client trebuie să citească aceeași celulă mai târziu. SELECT 1 ajunge când vrei un rezultat și raftul trebuie să rămână gol.
   Demonstrație: storedDataValue

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei o celulă care rămâne -> storedDataValue -> INSERT INTO entity_row (data_value) VALUES (1) -> nu garantează rândul: în tur propoziția nu a plecat
    2. Eșecul cel mai mic
           -> DECIDE 1 afișat este deja stocul -> resultIsNotACell -> SELECT 1 AS data_value -> nu garantează o celulă în entity_row
    3. Granița
           -> Celula este în tabelă, pe server. Comentariul de curs și grila din Workbench nu sunt celula.
    4. Alternativa
           -> Declarativ când întrebi ce să citești. _2_SQL_Statements când trebuie să deosebești CREATE de INSERT.
    5. Anti-patternul
           -> Aliasul data_value tratat ca record. Tabela rămâne goală.
    6. Dovada
           -> storedDataValue ține INSERT-ul. resultIsNotACell ține constanta. Planul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> Data value este celula. INSERT o lasă în rând. SELECT 1 nu o scrie. Fără tabelă, INSERT nu inventează schema.
*/

public class Introduction_To_DB         {

    /*
    🟢 Începător
        Ce este            propoziția care pune o valoare într-o celulă
        De ce e nevoie     fără ea, comentariul despre rând și coloană nu lasă nimic în tabelă
    🟡 Producție
        Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
    🔴 Staff
        Unde               celula se vede după ce propoziția se termină, în rândul nou. Fără entity_row nu există celulă. SELECT 1 nu este acest rând.
    Pași
        1. Ține INSERT-ul gata de trimis.
        2. Nu îl confunda cu SELECT 1 AS data_value.
        3. Nu îl trimite în tur: root@localhost a fost refuzat.
    */
    // Începător: metoda ține propoziția care scrie o celulă, nu un desen pe ecran.
    // Producție: public static String storedDataValue(). Dacă dispare, fast nu mai are propoziția celulei.
    // Expert: Metoda nu deschide JDBC. Nu ține lacătul după ce a întors șirul, pentru că șirul nu a fost trimis.
    public static String storedDataValue() {
        // Începător: în tabelă rămâne un 1, în coloana data_value, dacă entity_row există și serverul termină propoziția.
        // Producție: INSERT INTO entity_row (data_value) VALUES (1). Fără șir, rămâne doar comentariul de curs.
        // Expert: Nu este SELECT 1 AS data_value și nu este CREATE TABLE. Coloana fără acest INSERT nu are încă o valoare de date.
        return "INSERT INTO entity_row (data_value) VALUES (1)";
        // Începător: metoda a dat înapoi propoziția, nu un rând deja scris.
        // Producție: acolada închide storedDataValue. Șirul nu a plecat către 3306.
        // Expert: Refuzul de autentificare lasă celula NERULAT. Acolada nu este COMMIT.
    }

    /*
    🟢 Începător
        Ce este            rezultatul care arată 1 și nu umple o celulă
        De ce e nevoie     fără el, pare că orice 1 văzut de client este un record
    🟡 Producție
        Cum se scrie       SELECT 1 AS data_value
    🔴 Staff
        Unde               rândul de rezultat pleacă la client. entity_row nu primește un rând. Vecinul este INSERT-ul de mai sus.
    Pași
        1. Ține constanta separat de INSERT.
        2. Nu o trimite ca dovadă de stoc.
    */
    // Începător: metoda ține textul care nu lasă stoc în urmă.
    // Producție: public static String resultIsNotACell(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Nu este un index și nu este un lacăt. Introduction_To_DB rămâne despre celulă, iar acest șir nu o creează.
    public static String resultIsNotACell() {
        // Începător: clientul poate vedea 1, iar rândurile din entity_row rămân cum erau.
        // Producție: SELECT 1 AS data_value. Dacă linia dispare, nu mai ai perechea greșită lângă INSERT.
        // Expert: Aliasul nu este coloana stocată. Limbajul declarativ poate cere o citire; acest text nici nu numește tabela.
        return "SELECT 1 AS data_value";
        // Începător: metoda s-a oprit la rezultatul care nu este un record.
        // Producție: acolada închide resultIsNotACell.
        // Expert: Nu compara acest text cu un WHERE din _2_SQL_Statements. Aici lipsește tabela, deci nu există celulă de filtrat.
    }
}
