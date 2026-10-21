package _2_SQL_Statements._2_DML._2_INSERT._1_VALUES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8346046#overview



/*                                                  INSERT INTO table name(attributes...)  ...  VALUES


    ○


                                                      INSERT ... VALUES


    1. With column name     --don't have to add all columns in default order


                    * INSERT INTO table_name (column_1, column_2, …, column_n)

                       VALUES (value_1, value_2, …, value_n);

    2. Without column name


                   * INSERT INTO table_name

                     VALUES (value_1, value_2, …, value_n);

                                 - have to add all columns in default order
 */


/*
TOPIC:
    INSERT INTO sales (purchase_number) VALUES (1) adaugă un rând; fără lista de coloane, trebuie o valoare pentru fiecare coloană, în ordinea tabelei.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să apară cumpărarea 1. După propoziție, sales are un rând nou.
MECHANISM:
    Serverul execută INSERT ... VALUES la sfârșitul propoziției. Coloanele nenomite iau DEFAULT doar dacă modul și definiția permit.
LOOKALIKES:
    INSERT INTO sales SELECT ... ia rânduri din altă tabelă. UPDATE schimbă un rând care există.
BOOK THEORY:
    Manualul 9.7, 15.2.7 INSERT, deschis în tur: INSERT adaugă rânduri într-o tabelă care există. VALUES dă valorile, INSERT SELECT le ia din altă tabelă. DELAYED este acceptat și ignorat. PDF-ul de INSERT din folderul părinte a fost deschis la parcurgerea PDF-urilor.
BOOK EXAMPLE:
    insertOnePurchase
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_2_INSERT/_1_VALUES/VALUES_.java
BREAK LAB:
    valuesWiderThanTheList ține INSERT INTO sales (purchase_number) VALUES (1, 2). Manualul: fiecare listă VALUES are exact atâtea valori cât coloanele numite. Al doilea rând este VALUES (1), (2).
TRADE-OFFS:
    UPDATE când rândul există și vrei să schimbi valoarea.
ANTI-PATTERNS:
    VALUES (1, 2) pe o singură coloană. Rândul nu apare.
STAFF QUESTIONS:
    insertOnePurchase și valuesWiderThanTheList. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un flacon nou: INSERT INTO vial (vial_no) VALUES (18). Nu este UPDATE pe flaconul 18 dacă acela nu există.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă INSERT INTO sales (purchase_number) VALUES (1)?
   Capcană: VALUES (1, 2) inserează două rânduri într-o coloană.
   Cheie: insertOnePurchase: sales are un rând nou cu purchase_number 1. fără lista de coloane, VALUES trebuie să acopere fiecare coloană în ordinea din tabelă
   Demonstrație: insertOnePurchase
2. Enunț: Ce rămâne în tabelă sau în sesiune după INSERT INTO sales (purchase_number) VALUES (1)?
   Capcană: INSERT schimbă purchase_number pe rândul vechi.
   Cheie: Efectul este: sales are un rând nou cu purchase_number 1. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: insertOnePurchase
3. Enunț: De ce INSERT INTO sales (purchase_number) VALUES (1) nu este vecinul său?
   Capcană: UPDATE sales SET purchase_number = 1 adaugă rândul dacă lipsește.
   Cheie: INSERT INTO sales SELECT ... ia rânduri din altă tabelă. UPDATE schimbă un rând care există.
   Demonstrație: insertOnePurchase
4. Enunț: Cum eșuează INSERT INTO sales (purchase_number) VALUES (1, 2)?
   Capcană: Fără lista de coloane, serverul ignoră ordinea.
   Cheie: valuesWiderThanTheList: Manualul: fiecare listă VALUES are exact atâtea valori cât coloanele numite. Al doilea rând este VALUES (1), (2).
   Demonstrație: valuesWiderThanTheList
5. Enunț: Când nu mai trimiți INSERT INTO sales (purchase_number) VALUES (1)?
   Capcană: DELAYED face inserarea mai sigură pe 9.7.
   Cheie: UPDATE când rândul există și vrei să schimbi valoarea.
   Demonstrație: insertOnePurchase

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE adaugi un rând și numești coloana -> insertOnePurchase -> INSERT INTO sales (purchase_number) VALUES (1) -> nu garantează că o coloană NOT NULL fără default, lăsată pe dinafară, primește un rând
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> valuesWiderThanTheList -> INSERT INTO sales (purchase_number) VALUES (1, 2) -> Manualul: fiecare listă VALUES are exact atâtea valori cât coloanele numite. Al doilea rând este VALUES (1), (2).
    3. Granița
           -> VALUES este în propoziție. Un rând desenat în grid nu este inserat până nu pleacă INSERT.
    4. Alternativa
           -> INSERT INTO sales SELECT ... ia rânduri din altă tabelă. UPDATE schimbă un rând care există.
    5. Anti-patternul
           -> VALUES (1, 2) pe o singură coloană. Rândul nu apare.
    6. Dovada
           -> insertOnePurchase ține propoziția. valuesWiderThanTheList ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> INSERT VALUES adaugă rânduri. Lista de valori trebuie să se potrivească coloanelor. UPDATE nu adaugă rândul.
*/

public class VALUES_ {
    /*
    🟢 Începător
        Ce este            sales are un rând nou cu purchase_number 1
        De ce e nevoie     fără lista de coloane, VALUES trebuie să acopere fiecare coloană în ordinea din tabelă
    🟡 Producție
        Cum se scrie       INSERT INTO sales (purchase_number) VALUES (1)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este UPDATE sales SET purchase_number = 1, care schimbă rânduri existente și nu adaugă unul
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE sales SET purchase_number = 1, care schimbă rânduri existente și nu adaugă unul.
    */
    // Începător: insertOnePurchase păstrează propoziția pentru sales are un rând nou cu purchase_number 1
    // Producție: public static String insertOnePurchase(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Un singur VALUES cu mai multe numere decât coloanele din listă este respins.
    public static String insertOnePurchase() {
        // Începător: sales are un rând nou cu purchase_number 1
        // Producție: INSERT INTO sales (purchase_number) VALUES (1). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Un singur VALUES cu mai multe numere decât coloanele din listă este respins. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO sales (purchase_number) VALUES (1)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide insertOnePurchase. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul respinge lista și rândul nu apare
        De ce e nevoie     fără textul greșit, valorile în plus par un al doilea rând
    🟡 Producție
        Cum se scrie       INSERT INTO sales (purchase_number) VALUES (1, 2)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul respinge lista și rândul nu apare
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE sales SET purchase_number = 1, care schimbă rânduri existente și nu adaugă unul.
    */
    // Începător: valuesWiderThanTheList păstrează textul greșit pentru serverul respinge lista și rândul nu apare
    // Producție: public static String valuesWiderThanTheList(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul: fiecare listă VALUES are exact atâtea valori cât coloanele numite. Al doilea rând este VALUES (1), (2).
    public static String valuesWiderThanTheList() {
        // Începător: serverul respinge lista și rândul nu apare
        // Producție: INSERT INTO sales (purchase_number) VALUES (1, 2). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul: fiecare listă VALUES are exact atâtea valori cât coloanele numite. Al doilea rând este VALUES (1), (2). Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO sales (purchase_number) VALUES (1, 2)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide valuesWiderThanTheList. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
