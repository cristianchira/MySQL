package _2_SQL_Statements._2_DML._1_SELECT;

//  https://dev.mysql.com/doc/refman/8.0/en/select.html

/*                                       SELECT Statement


    ▬▬▐ Allows you to extract a fraction of the entire data set
                   * used to retrieve data from database objects, like tables
                   * used to query data from a database


                                    Basic select statement


    1. SELECT column_1, column_2, … column_n
       FROM table_name;

    2. SELECT * FROM table_name


                                       COMPLETE SELECT STATEMENT


                      SELECT
    [ALL | DISTINCT | DISTINCTROW ]
    [HIGH_PRIORITY]
    [STRAIGHT_JOIN]
    [SQL_SMALL_RESULT] [SQL_BIG_RESULT] [SQL_BUFFER_RESULT]
    [SQL_NO_CACHE] [SQL_CALC_FOUND_ROWS]
    select_expr [, select_expr] ...
    [into_option]

    [FROM table_references
      [PARTITION partition_list]]

    [WHERE where_condition]

    [GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]]

    [HAVING where_condition]

    [WINDOW window_name AS (window_spec)
        [, window_name AS (window_spec)] ...]

    [ORDER BY {col_name | expr | position}
      [ASC | DESC], ... [WITH ROLLUP]]

    [LIMIT {[offset,] row_count | row_count OFFSET offset}]

    [into_option]
    [FOR {UPDATE | SHARE}
        [OF tbl_name [, tbl_name] ...]
        [NOWAIT | SKIP LOCKED]
      | LOCK IN SHARE MODE]
    [into_option]

into_option: {
    INTO OUTFILE 'file_name'
        [CHARACTER SET charset_name]
        export_options
  | INTO DUMPFILE 'file_name'
  | INTO var_name [, var_name] ...
}





*/

/*
TOPIC:
    SELECT purchase_number FROM sales citește coloana; tabela rămâne, iar * necalificat lângă o altă expresie poate fi respins.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să ceri coloanele, nu să scrii tu parcurgerea. Rezultatul se vede la sfârșitul propoziției.
MECHANISM:
    Serverul execută SELECT cum este în 15.2.13. Trebuie cel puțin un select_expr. * singur înseamnă toate coloanele vizibile; id, * poate da eroare de parse.
LOOKALIKES:
    SELECT 1 nu citește sales. INSERT scrie un rând.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul SELECT_FROM a fost deschis. Manualul: un * necalificat împreună cu alte elemente poate produce eroare; folosești sales.*.
BOOK EXAMPLE:
    selectPurchaseFromSales
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/DB_Select.java
BREAK LAB:
    starBesideAColumn ține SELECT purchase_number, * FROM sales. Manualul 9.7: SELECT id, * FROM t1 poate da eroare. Forma este purchase_number, sales.*.
TRADE-OFFS:
    SELECT 1 când nu ai tabelă. INSERT când vrei să scrii un rând.
ANTI-PATTERNS:
    SELECT purchase_number, * FROM sales. Poate eșua înainte de orice rând.
STAFF QUESTIONS:
    selectPurchaseFromSales și starBesideAColumn. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O etichetă de lot cere lot_no, nu toate coloanele lipite de o steluță. SELECT lot_no FROM lot.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT purchase_number FROM sales?
   Capcană: SELECT purchase_number, * FROM sales este forma completă recomandată.
   Cheie: selectPurchaseFromSales: rezultatul are purchase_number și rândurile din sales rămân. fără select_expr, propoziția nu are ce coloană să întoarcă
   Demonstrație: selectPurchaseFromSales
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT purchase_number FROM sales?
   Capcană: SELECT scrie rândul înapoi în sales.
   Cheie: Efectul este: rezultatul are purchase_number și rândurile din sales rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: selectPurchaseFromSales
3. Enunț: De ce SELECT purchase_number FROM sales nu este vecinul său?
   Capcană: SELECT 1 citește purchase_number.
   Cheie: SELECT 1 nu citește sales. INSERT scrie un rând.
   Demonstrație: selectPurchaseFromSales
4. Enunț: Cum eșuează SELECT purchase_number, * FROM sales?
   Capcană: * include și coloanele invizibile.
   Cheie: starBesideAColumn: Manualul 9.7: SELECT id, * FROM t1 poate da eroare. Forma este purchase_number, sales.*.
   Demonstrație: starBesideAColumn
5. Enunț: Când nu mai trimiți SELECT purchase_number FROM sales?
   Capcană: Fără FROM, purchase_number se găsește singur.
   Cheie: SELECT 1 când nu ai tabelă. INSERT când vrei să scrii un rând.
   Demonstrație: selectPurchaseFromSales

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei o coloană numită din sales -> selectPurchaseFromSales -> SELECT purchase_number FROM sales -> nu garantează coloanele invizibile și nu scrie un rând
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> starBesideAColumn -> SELECT purchase_number, * FROM sales -> Manualul 9.7: SELECT id, * FROM t1 poate da eroare. Forma este purchase_number, sales.*.
    3. Granița
           -> SELECT este propoziția. Un preview din Workbench nu este rezultatul până nu o trimite.
    4. Alternativa
           -> SELECT 1 nu citește sales. INSERT scrie un rând.
    5. Anti-patternul
           -> SELECT purchase_number, * FROM sales. Poate eșua înainte de orice rând.
    6. Dovada
           -> selectPurchaseFromSales ține propoziția. starBesideAColumn ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> SELECT citește. Trebuie un select_expr. * lângă altă coloană poate eșua. Coloana invizibilă nu intră în *.
*/

public class DB_Select {
    /*
    🟢 Începător
        Ce este            rezultatul are purchase_number și rândurile din sales rămân
        De ce e nevoie     fără select_expr, propoziția nu are ce coloană să întoarcă
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT id, * FROM sales, care poate eșua la parse
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT id, * FROM sales, care poate eșua la parse.
    */
    // Începător: selectPurchaseFromSales păstrează propoziția pentru rezultatul are purchase_number și rândurile din sales rămân
    // Producție: public static String selectPurchaseFromSales(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. * nu include coloanele invizibile. Ca să le vezi, le numești.
    public static String selectPurchaseFromSales() {
        // Începător: rezultatul are purchase_number și rândurile din sales rămân
        // Producție: SELECT purchase_number FROM sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: * nu include coloanele invizibile. Ca să le vezi, le numești. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT purchase_number FROM sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide selectPurchaseFromSales. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul poate respinge * necalificat lângă altă expresie și nu întoarce lista
        De ce e nevoie     fără textul greșit, * lângă o coloană pare doar mai mult
    🟡 Producție
        Cum se scrie       SELECT purchase_number, * FROM sales
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul poate respinge * necalificat lângă altă expresie și nu întoarce lista
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT id, * FROM sales, care poate eșua la parse.
    */
    // Începător: starBesideAColumn păstrează textul greșit pentru serverul poate respinge * necalificat lângă altă expresie și nu întoarce lista
    // Producție: public static String starBesideAColumn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul 9.7: SELECT id, * FROM t1 poate da eroare. Forma este purchase_number, sales.*.
    public static String starBesideAColumn() {
        // Începător: serverul poate respinge * necalificat lângă altă expresie și nu întoarce lista
        // Producție: SELECT purchase_number, * FROM sales. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul 9.7: SELECT id, * FROM t1 poate da eroare. Forma este purchase_number, sales.*. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT purchase_number, * FROM sales";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide starBesideAColumn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
