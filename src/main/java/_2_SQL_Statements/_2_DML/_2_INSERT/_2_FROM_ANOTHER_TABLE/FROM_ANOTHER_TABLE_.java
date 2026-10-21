package _2_SQL_Statements._2_DML._2_INSERT._2_FROM_ANOTHER_TABLE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8346176#questions


/*                                                            FROM ANOTHER TABLE


    ○



                                                                 SQL

    ○ INSERT INTO SELECT

      INSERT INTO table_2 (column_1, column_2, …, column_n)

       SELECT column_1, column_2, …, column_n

       FROM table_1

       WHERE condition;


    ○ Example

      INSERT INTO department_dup (dept_no, dept_name )

      SELECT * FROM departments;


 */

/*
TOPIC:
    INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments copiază rânduri; SELECT * nu se potrivește listei de două coloane.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să umpli department_dup din departments. După propoziție, destinația are rândurile citite, sursa rămâne.
MECHANISM:
    Serverul execută INSERT ... SELECT. Lista de coloane a destinației trebuie acoperită de select_expr, nu de un * care aduce toate coloanele sursei.
LOOKALIKES:
    INSERT VALUES scrie constante. UPDATE nu copiază o tabelă în alta.
BOOK THEORY:
    Manualul 9.7, 15.2.7 INSERT, deschis în tur: INSERT adaugă rânduri într-o tabelă care există. VALUES dă valorile, INSERT SELECT le ia din altă tabelă. DELAYED este acceptat și ignorat. Comentariul de curs are SELECT * pe o listă de două coloane. Manualul: dacă numești coloanele, fiecare trebuie să primească o valoare.
BOOK EXAMPLE:
    insertSelectTwoColumns
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_2_INSERT/_2_FROM_ANOTHER_TABLE/FROM_ANOTHER_TABLE_.java
BREAK LAB:
    selectStarMissesTheList ține INSERT INTO department_dup (dept_no, dept_name) SELECT * FROM departments. Lista (dept_no, dept_name) cere două valori pe rând. SELECT * întoarce toate coloanele sursei.
TRADE-OFFS:
    VALUES când rândul nu vine dintr-o tabelă.
ANTI-PATTERNS:
    SELECT * pe o listă de două coloane. Inserarea poate eșua și destinația rămâne goală.
STAFF QUESTIONS:
    insertSelectTwoColumns și selectStarMissesTheList. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Copiezi codul și numele depozitului, nu toată fișa. INSERT INTO depot_dup (code, name) SELECT code, name FROM depot.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments?
   Capcană: SELECT * se potrivește mereu pe (dept_no, dept_name).
   Cheie: insertSelectTwoColumns: department_dup primește rânduri, iar departments rămâne. fără cele două expresii, SELECT * poate aduce mai multe coloane decât lista
   Demonstrație: insertSelectTwoColumns
2. Enunț: Ce rămâne în tabelă sau în sesiune după INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments?
   Capcană: INSERT SELECT șterge departments.
   Cheie: Efectul este: department_dup primește rânduri, iar departments rămâne. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: insertSelectTwoColumns
3. Enunț: De ce INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments nu este vecinul său?
   Capcană: VALUES este copia din altă tabelă.
   Cheie: INSERT VALUES scrie constante. UPDATE nu copiază o tabelă în alta.
   Demonstrație: insertSelectTwoColumns
4. Enunț: Cum eșuează INSERT INTO department_dup (dept_no, dept_name) SELECT * FROM departments?
   Capcană: Dacă numărul de coloane nu se potrivește, serverul taie de la dreapta și inserează.
   Cheie: selectStarMissesTheList: Lista (dept_no, dept_name) cere două valori pe rând. SELECT * întoarce toate coloanele sursei.
   Demonstrație: selectStarMissesTheList
5. Enunț: Când nu mai trimiți INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments?
   Capcană: Ca să copiezi, faci UPDATE.
   Cheie: VALUES când rândul nu vine dintr-o tabelă.
   Demonstrație: insertSelectTwoColumns

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE copiezi două coloane dintr-o tabelă care există -> insertSelectTwoColumns -> INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments -> nu garantează că un duplicat de cheie este ignorat
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> selectStarMissesTheList -> INSERT INTO department_dup (dept_no, dept_name) SELECT * FROM departments -> Lista (dept_no, dept_name) cere două valori pe rând. SELECT * întoarce toate coloanele sursei.
    3. Granița
           -> INSERT SELECT este propoziția. Un export-import din client este alt drum.
    4. Alternativa
           -> INSERT VALUES scrie constante. UPDATE nu copiază o tabelă în alta.
    5. Anti-patternul
           -> SELECT * pe o listă de două coloane. Inserarea poate eșua și destinația rămâne goală.
    6. Dovada
           -> insertSelectTwoColumns ține propoziția. selectStarMissesTheList ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> INSERT SELECT copiază rânduri și lasă sursa. Numărul de expresii trebuie să acopere coloanele numite. SELECT * nu promite asta.
*/

public class FROM_ANOTHER_TABLE_ {
    /*
    🟢 Începător
        Ce este            department_dup primește rânduri, iar departments rămâne
        De ce e nevoie     fără cele două expresii, SELECT * poate aduce mai multe coloane decât lista
    🟡 Producție
        Cum se scrie       INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este INSERT INTO department_dup (dept_no, dept_name) VALUES ('d009', 'Customer Service'), care nu citește departments
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT INTO department_dup (dept_no, dept_name) VALUES ('d009', 'Customer Service'), care nu citește departments.
    */
    // Începător: insertSelectTwoColumns păstrează propoziția pentru department_dup primește rânduri, iar departments rămâne
    // Producție: public static String insertSelectTwoColumns(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SELECT * nu este o prescurtare sigură când destinația numește două coloane.
    public static String insertSelectTwoColumns() {
        // Începător: department_dup primește rânduri, iar departments rămâne
        // Producție: INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: SELECT * nu este o prescurtare sigură când destinația numește două coloane. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO department_dup (dept_no, dept_name) SELECT dept_no, dept_name FROM departments";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide insertSelectTwoColumns. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă departments are și alte coloane, serverul respinge inserarea și department_dup nu primește rândurile
        De ce e nevoie     fără textul greșit, * din curs pare potrivit pe două coloane
    🟡 Producție
        Cum se scrie       INSERT INTO department_dup (dept_no, dept_name) SELECT * FROM departments
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă departments are și alte coloane, serverul respinge inserarea și department_dup nu primește rândurile
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT INTO department_dup (dept_no, dept_name) VALUES ('d009', 'Customer Service'), care nu citește departments.
    */
    // Începător: selectStarMissesTheList păstrează textul greșit pentru dacă departments are și alte coloane, serverul respinge inserarea și department_dup nu primește rândurile
    // Producție: public static String selectStarMissesTheList(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Lista (dept_no, dept_name) cere două valori pe rând. SELECT * întoarce toate coloanele sursei.
    public static String selectStarMissesTheList() {
        // Începător: dacă departments are și alte coloane, serverul respinge inserarea și department_dup nu primește rândurile
        // Producție: INSERT INTO department_dup (dept_no, dept_name) SELECT * FROM departments. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Lista (dept_no, dept_name) cere două valori pe rând. SELECT * întoarce toate coloanele sursei. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO department_dup (dept_no, dept_name) SELECT * FROM departments";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide selectStarMissesTheList. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
