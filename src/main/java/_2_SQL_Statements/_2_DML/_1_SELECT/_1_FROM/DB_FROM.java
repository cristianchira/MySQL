package _2_SQL_Statements._2_DML._1_SELECT._1_FROM;

//Martin G SELECT ... FROM
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8344762#questions


/*                                       SELECT... FROM

    ○ SELECT statement
                * allows you to extract a fraction of the entire data set
                * used to retrieve data from database objects, like tables
                * used to “query data from a database”
                * is one of the most important statements in my SQL and SQL.

    ○ SELECT basic syntax
                * SELECT column_1, column_2,… column_n
                  FROM table_name;

    ○ SELECT ...FROM
                 * FROM is mandatory


 */

/*
TOPIC:
    FROM sales spune de unde se citesc rândurile; fără FROM, SELECT purchase_number nu are tabelă.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să numești sursa. După propoziție, rezultatul vine din sales și tabela rămâne.
MECHANISM:
    Serverul citește table_references din SELECT la sfârșitul propoziției. Manualul 9.7 permite SELECT fără tabelă doar când nu există coloană de tabelă, ca SELECT 1.
LOOKALIKES:
    WHERE filtrează rândurile deja alese din FROM. SELECT 1 nu numește sales.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul.
BOOK EXAMPLE:
    fromSalesNamesTheSource
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_1_FROM/DB_FROM.java
BREAK LAB:
    selectColumnWithoutFrom ține SELECT purchase_number. Lipsește sursa. SELECT 1 ar fi mers tocmai pentru că nu numește o coloană de tabelă.
TRADE-OFFS:
    SELECT 1 când nu ai tabelă și vrei o valoare calculată.
ANTI-PATTERNS:
    SELECT purchase_number fără FROM. Serverul nu citește sales.
STAFF QUESTIONS:
    fromSalesNamesTheSource și selectColumnWithoutFrom. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un raport de loturi trebuie să spună FROM lot, altfel lot_no nu are sursă.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT purchase_number FROM sales?
   Capcană: SELECT purchase_number găsește singur tabela sales.
   Cheie: fromSalesNamesTheSource: rezultatul ia purchase_number din sales și rândurile stocate rămân. fără FROM, purchase_number nu are tabelă și propoziția nu pornește
   Demonstrație: fromSalesNamesTheSource
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT purchase_number FROM sales?
   Capcană: FROM copiază sales într-o tabelă temporară stocată.
   Cheie: Efectul este: rezultatul ia purchase_number din sales și rândurile stocate rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: fromSalesNamesTheSource
3. Enunț: De ce SELECT purchase_number FROM sales nu este vecinul său?
   Capcană: SELECT 1 este același lucru, pentru că și el întoarce un rezultat.
   Cheie: WHERE filtrează rândurile deja alese din FROM. SELECT 1 nu numește sales.
   Demonstrație: fromSalesNamesTheSource
4. Enunț: Cum eșuează SELECT purchase_number?
   Capcană: Fără FROM, serverul presupune ultima tabelă din USE.
   Cheie: selectColumnWithoutFrom: Lipsește sursa. SELECT 1 ar fi mers tocmai pentru că nu numește o coloană de tabelă.
   Demonstrație: selectColumnWithoutFrom
5. Enunț: Când nu mai trimiți SELECT purchase_number FROM sales?
   Capcană: Ca să citești, e de ajuns numele coloanei.
   Cheie: SELECT 1 când nu ai tabelă și vrei o valoare calculată.
   Demonstrație: fromSalesNamesTheSource

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei coloana din sales -> fromSalesNamesTheSource -> SELECT purchase_number FROM sales -> nu garantează că sales există și nu filtrează rândurile
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> selectColumnWithoutFrom -> SELECT purchase_number -> Lipsește sursa. SELECT 1 ar fi mers tocmai pentru că nu numește o coloană de tabelă.
    3. Granița
           -> FROM este clauza din propoziție. Comentariul care spune că FROM este mandatory rămâne; el nu este execuția.
    4. Alternativa
           -> WHERE filtrează rândurile deja alese din FROM. SELECT 1 nu numește sales.
    5. Anti-patternul
           -> SELECT purchase_number fără FROM. Serverul nu citește sales.
    6. Dovada
           -> fromSalesNamesTheSource ține propoziția. selectColumnWithoutFrom ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> FROM sales numește sursa. SELECT 1 nu citește tabela. Fără FROM, coloana de tabelă eșuează.
*/

public class DB_FROM {
    /*
    🟢 Începător
        Ce este            rezultatul ia purchase_number din sales și rândurile stocate rămân
        De ce e nevoie     fără FROM, purchase_number nu are tabelă și propoziția nu pornește
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT 1, care nu citește sales
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT 1, care nu citește sales.
    */
    // Începător: fromSalesNamesTheSource păstrează propoziția pentru rezultatul ia purchase_number din sales și rândurile stocate rămân
    // Producție: public static String fromSalesNamesTheSource(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. FROM nu filtrează și nu este un index.
    public static String fromSalesNamesTheSource() {
        // Începător: rezultatul ia purchase_number din sales și rândurile stocate rămân
        // Producție: SELECT purchase_number FROM sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: FROM nu filtrează și nu este un index. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT purchase_number FROM sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide fromSalesNamesTheSource. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul nu găsește coloana și nu întoarce rânduri din sales
        De ce e nevoie     fără textul greșit, pare că numele coloanei este de ajuns
    🟡 Producție
        Cum se scrie       SELECT purchase_number
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul nu găsește coloana și nu întoarce rânduri din sales
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT 1, care nu citește sales.
    */
    // Începător: selectColumnWithoutFrom păstrează textul greșit pentru serverul nu găsește coloana și nu întoarce rânduri din sales
    // Producție: public static String selectColumnWithoutFrom(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Lipsește sursa. SELECT 1 ar fi mers tocmai pentru că nu numește o coloană de tabelă.
    public static String selectColumnWithoutFrom() {
        // Începător: serverul nu găsește coloana și nu întoarce rânduri din sales
        // Producție: SELECT purchase_number. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Lipsește sursa. SELECT 1 ar fi mers tocmai pentru că nu numește o coloană de tabelă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT purchase_number";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide selectColumnWithoutFrom. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
